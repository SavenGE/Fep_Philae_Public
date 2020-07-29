/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import PHilae.APController;
import PHilae.APMain;
import PHilae.ipn.CBHandler;
import PHilae.ipn.CVHandler;
import com.sun.xml.ws.spi.ProviderImpl;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.xml.ws.Endpoint;
import javax.xml.ws.soap.SOAPBinding;
import org.eclipse.jetty.http.HttpScheme;
import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.http.spi.JettyHttpServer;
import org.eclipse.jetty.security.ConstraintMapping;
import org.eclipse.jetty.security.ConstraintSecurityHandler;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.UserStore;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.InetAccessHandler;
import org.eclipse.jetty.util.preventers.AWTLeakPreventer;
import org.eclipse.jetty.util.preventers.AppContextLeakPreventer;
import org.eclipse.jetty.util.preventers.DOMLeakPreventer;
import org.eclipse.jetty.util.preventers.DriverManagerLeakPreventer;
import org.eclipse.jetty.util.preventers.GCThreadLeakPreventer;
import org.eclipse.jetty.util.preventers.Java2DLeakPreventer;
import org.eclipse.jetty.util.preventers.LDAPLeakPreventer;
import org.eclipse.jetty.util.preventers.LoginConfigurationLeakPreventer;
import org.eclipse.jetty.util.preventers.SecurityProviderLeakPreventer;
import org.eclipse.jetty.util.security.Constraint;
import org.eclipse.jetty.util.security.Credential;
import org.eclipse.jetty.util.ssl.SslContextFactory;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

/**
 *
 * @author Pecherk
 */
public class VXServer {

    private static final int OutputBufferSize = 32768;
    private static final int RequestHeaderSize = 8192;
    private static final int ResponseHeaderSize = 8192;
    private final Server server = new Server(new QueuedThreadPool(VXController.maxClientSessions));

    public VXServer() {
        initialize();
    }

    public final void initialize() {
        try {
            File homeDir = new File(VXController.workDir);
            homeDir.mkdirs();

            File baseDir = new File(homeDir + "/base");
            baseDir.mkdirs();

            System.setProperty("jetty.home", homeDir.getAbsolutePath());
            System.setProperty("jetty.base", baseDir.getAbsolutePath());
        } catch (Exception ex) {
            APMain.vmaLog.logEvent(ex);
        }
    }

    public void start() {
        new Thread(()
                -> {
            try {
                if (!server.isStarted()) {
                    startServer();
                }
            } catch (Exception ex) {
                APMain.vmaLog.logEvent(ex);
            }
        }).start();
    }

    private void setLeakPreventers() {
        server.addBean(new AppContextLeakPreventer());
        server.addBean(new AWTLeakPreventer());
        server.addBean(new DOMLeakPreventer());

        server.addBean(new DriverManagerLeakPreventer());
        server.addBean(new GCThreadLeakPreventer());
        server.addBean(new Java2DLeakPreventer());

        server.addBean(new LDAPLeakPreventer());
        server.addBean(new LoginConfigurationLeakPreventer());
        server.addBean(new SecurityProviderLeakPreventer());
    }

    private void startServer() throws Exception {
        setLeakPreventers();
        ProviderImpl provider = new com.sun.xml.ws.spi.ProviderImpl();
        JettyHttpServer httpServer = new JettyHttpServer(server, true);

        ContextHandler homeHandler = new ContextHandler(VXController.homeContext);
        homeHandler.setHandler(new VHHandler());

        ContextHandler validatorHandler = new ContextHandler(VXController.mpesaValidatorContext);
        validatorHandler.setHandler(new CVHandler());

        ContextHandler paybillHandler = new ContextHandler(VXController.mpesaDepositContext);
        paybillHandler.setHandler(new CBHandler());

        ContextHandlerCollection contextHandler = new ContextHandlerCollection();
        contextHandler.setHandlers(new Handler[]{
            homeHandler, validatorHandler, paybillHandler
        });

        server.setHandler(contextHandler);

        if (!VXController.isSuspended()) {
            Endpoint endpoint = provider.createEndpoint(SOAPBinding.SOAP11HTTP_BINDING, new VXService());
            List<javax.xml.ws.handler.Handler> handlerChain = endpoint.getBinding().getHandlerChain();
            handlerChain.add(new VLHandler());
            endpoint.getBinding().setHandlerChain(handlerChain);
            endpoint.publish(httpServer.createContext(VXController.vmaWsContext));
        }

        server.setDumpAfterStart(false);
        server.setDumpBeforeStop(false);
        server.setStopAtShutdown(true);

        if (APController.getWorker().isYes(VXController.enableHttps)) {
            SslContextFactory sslContextFactory = new SslContextFactory();
            sslContextFactory.setProtocol(VXController.secureSocketProtocol);
            sslContextFactory.setKeyManagerFactoryAlgorithm(VXController.keyFactoryAlgorithm);

            sslContextFactory.setKeyStoreType(VXController.keystoreType);
            sslContextFactory.setKeyStorePath(VXController.keystoreFile);
            sslContextFactory.setKeyStorePassword(VXController.keystorePassword);

            sslContextFactory.setKeyManagerPassword(VXController.keystorePassword);
            sslContextFactory.setTrustStorePath(VXController.keystoreFile);
            sslContextFactory.setTrustStorePassword(VXController.keystorePassword);
            sslContextFactory.setCertAlias(VXController.certificateAlias);

            sslContextFactory.setExcludeCipherSuites();//For troubleshooting only

            HttpConfiguration https_config = new HttpConfiguration();
            https_config.setSecureScheme(HttpScheme.HTTPS.asString());
            https_config.setSecurePort(VXController.serverPort);

            https_config.setOutputBufferSize(OutputBufferSize);
            https_config.setRequestHeaderSize(RequestHeaderSize);
            https_config.setResponseHeaderSize(ResponseHeaderSize);

            https_config.setSendServerVersion(false);
            https_config.setSendDateHeader(false);
            https_config.addCustomizer(new SecureRequestCustomizer());

            ServerConnector httpsConnector = new ServerConnector(server, new SslConnectionFactory(sslContextFactory, HttpVersion.HTTP_1_1.asString()), new HttpConnectionFactory(https_config));
            httpsConnector.setIdleTimeout(VXController.timeoutMinutes * 60000);
            httpsConnector.setPort(VXController.serverPort);

            httpsConnector.setReuseAddress(true);
            server.addConnector(httpsConnector);
        } else {
            HttpConfiguration http_config = new HttpConfiguration();
            http_config.setOutputBufferSize(OutputBufferSize);
            http_config.setRequestHeaderSize(RequestHeaderSize);

            http_config.setResponseHeaderSize(ResponseHeaderSize);
            http_config.setSendServerVersion(false);
            http_config.setSendDateHeader(false);

            ServerConnector httpConnector = new ServerConnector(server, new HttpConnectionFactory(http_config));
            httpConnector.setIdleTimeout(VXController.timeoutMinutes * 60000);
            httpConnector.setPort(VXController.serverPort);

            httpConnector.setReuseAddress(true);
            server.addConnector(httpConnector);
        }

        server.start();
        server.join();

    }

    public Handler lockHandler(ContextHandler handler, String login, String password) {
        Constraint constraint = new Constraint();
        constraint.setAuthenticate(true);
        constraint.setRoles(new String[]{
            handler.getContextPath()
        });

        ConstraintMapping mapping = new ConstraintMapping();
        mapping.setPathSpec(handler.getContextPath());
        mapping.setConstraint(constraint);

        ConstraintSecurityHandler securityHandler = new ConstraintSecurityHandler();
        securityHandler.setConstraintMappings(Collections.singletonList(mapping));
        securityHandler.setAuthenticator(new org.eclipse.jetty.security.authentication.BasicAuthenticator());

        HashLoginService loginService = new HashLoginService();
        loginService.setName("Login");
        UserStore userStore = new UserStore();

        userStore.addUser(login, Credential.getCredential(password), new String[]{
            handler.getContextPath()
        });
        loginService.setUserStore(userStore);

        securityHandler.setLoginService(loginService);
        securityHandler.setHandler(handler);
        return securityHandler;
    }

    public Handler secureHandler(Handler contextHandler, ArrayList<String> peerAddresses) {
        InetAccessHandler accessHandler = new InetAccessHandler();
        peerAddresses.stream().forEach((peer)
                -> {
            accessHandler.include(peer);
        });
        accessHandler.setHandler(contextHandler);
        return accessHandler;
    }

    public void stop() {
        try {
            server.stop();
        } catch (Exception ex) {
            APMain.vmaLog.logDebug(ex);
        }
    }
}
