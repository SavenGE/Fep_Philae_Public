/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 *
 * @author Pecherk
 */
public class VXSecurity {

    public SSLSocketFactory createSSLSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, IOException, CertificateException, UnrecoverableKeyException, KeyManagementException {
        KeyManagerFactory managerFactory = KeyManagerFactory.getInstance(VXController.keyFactoryAlgorithm);
        KeyStore keyStore = KeyStore.getInstance(VXController.keystoreType);
        try (InputStream stream = new FileInputStream(new File(VXController.keystoreFile))) {
            keyStore.load(stream, VXController.keystorePassword.toCharArray());
        }

        managerFactory.init(keyStore, VXController.keystorePassword.toCharArray());
        SSLContext sslContext = SSLContext.getInstance(VXController.secureSocketProtocol);
        sslContext.init(managerFactory.getKeyManagers(), getTrustManager(), new SecureRandom());
        return sslContext.getSocketFactory();
    }

    private TrustManager[] getTrustManager() {
        TrustManager[] trustManagers = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                }
            }
        };
        return trustManagers;
    }

    public HostnameVerifier getHostnameVerifier() {
        return (String hostname, SSLSession session) -> true;
    }
}
