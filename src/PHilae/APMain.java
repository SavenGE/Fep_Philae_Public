/*
 * To change this license header, choose License Headers in Project Properties. 
 * To change this template file, choose Tools | Templates
 * and open the template in the editor. 
 */
package PHilae;

import static PHilae.APController.getWorker;
import PHilae.acx.AXLogger;
import PHilae.acx.ACPanel;
import PHilae.acx.AXAppender;
import PHilae.acx.AXFile;
import PHilae.acx.CSWorker;
import PHilae.est.ESController;
import PHilae.ipn.MPWorker;
import PHilae.sms.ALController;
import PHilae.sms.ALProcessor;
import PHilae.vma.VXController;
import PHilae.vma.VXServer;
import com.jgoodies.looks.plastic.PlasticLookAndFeel;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.Window;
import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import static java.util.concurrent.TimeUnit.DAYS;
import static java.util.concurrent.TimeUnit.HOURS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;
import javax.swing.JDialog;
import javax.swing.UIManager;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author Pecherk
 */
public class APMain {

    public static APFrame apFrame;
    public static AXLogger acxLog;
    public static AXLogger smsLog;

    public static AXLogger vmaLog;
    public static AXLogger estLog;
    public static VXServer server;

    public static boolean exit = false;
    public static Color infoColor = new Color(0, 0, 128);
    public static Color errorColor = new Color(192, 0, 0);

    public static final JDialog consoleDialog = new JDialog();
    public static final ScheduledThreadPoolExecutor scheduler = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        setLookAndFeel();
        new APMain().execute();
    }

    private void execute() {
        initialize();
        displayWindow();
        while (apFrame == null || !apFrame.isVisible()) {
            pauseThread(2000);
        }
        startServer();
        startTasks();
    }

    public void initialize() {
        setConsole();
        setLoggers();
        setControllers();
        setAppenders();
    }

    private static void setLookAndFeel() {
        try {
            UIManager.setLookAndFeel(new PlasticLookAndFeel());
        } catch (Exception ex) {
            ex = null;
        }
    }

    public void setConsole() {
        ACPanel panel = new ACPanel();
        consoleDialog.setTitle(APController.application);
        consoleDialog.setIconImage(Toolkit.getDefaultToolkit().createImage(getClass().getResource("/PHilae/ximg/icon.png")));

        javax.swing.GroupLayout consoleLayout = new javax.swing.GroupLayout(consoleDialog.getContentPane());
        consoleDialog.getContentPane().setLayout(consoleLayout);

        consoleLayout.setHorizontalGroup(
                consoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        consoleLayout.setVerticalGroup(
                consoleLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(panel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        consoleDialog.setSize(500, 500);
        consoleDialog.setUndecorated(true);
        consoleDialog.setLocationRelativeTo(null);

//        System.setOut(new PrintStream(new ACStream(infoColor), true));
//        System.setErr(new PrintStream(new ACStream(errorColor), true));
    }

    private void setLoggers() {
        acxLog = new AXLogger("acx", "acx" + File.separator + "logs");
        smsLog = new AXLogger("sms", "sms" + File.separator + "logs");
        vmaLog = new AXLogger("vma", "vma" + File.separator + "logs");
        estLog = new AXLogger("est", "est" + File.separator + "logs");
        acxLog.logEvent("==========<" + APController.application + ">==========");
    }

    private void displayWindow() {
        EventQueue.invokeLater(()
                -> {
            apFrame = new APFrame();
            consoleDialog.setVisible(false);
            for (Window window : APFrame.getWindows()) {
                getWorker().prepareScrollers(window);
            }
            apFrame.setLocationRelativeTo(null);
            apFrame.setVisible(true);
            consoleDialog.dispose();
        });
    }

    public static void shutdown(boolean restart) {
        String fileName = "PHilae.exe";
        if (!restart || (fileName = new AXFile().getFile(".", fileName.split("[.]")[0], fileName.split("[.]")[1])) != null) {
            APController.shutdown();
            stopServer();
            stopTasks();

            if (restart) {
                try {
                    Runtime.getRuntime().exec(fileName);
                } catch (Exception ex) {
                    acxLog.logEvent(ex);
                }
            }
            exit();
        }
    }

    private static void pauseThread(int millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception ie) {
            acxLog.logEvent(ie);
        }
    }

    private void setControllers() {
        APController.initialize();
        ALController.initialize();
        VXController.initialize();
        ESController.initialize();
    }

    public static void startServer() {
        (server == null ? server = new VXServer() : server).start();
    }

    public static void restartServer() {
        stopServer();
        startServer();
    }

    public static void startTasks() {
        scheduler.setRemoveOnCancelPolicy(true);
        /*for sms sending*/
//        scheduler.scheduleAtFixedRate(ALProcessor::process, 0, 10, SECONDS);
        /*expiring old alerts*/
//        scheduler.scheduleWithFixedDelay(ALController::expireOldAlerts, 0, 1, DAYS);

        /*for email statement sending*/
//        scheduler.scheduleAtFixedRate(ESProcessor::process, 0, 10, SECONDS);
        scheduler.scheduleWithFixedDelay(APController::recyclePool, 15, 15, SECONDS);
        scheduler.scheduleWithFixedDelay(APController::refreshPool, 10, 10, MINUTES);
        scheduler.scheduleWithFixedDelay(APController::deleteXapiHistory, 0, 1, DAYS);

        scheduler.scheduleAtFixedRate(MPWorker::registerUrl, 0, 1, HOURS);
        /*for processing loan recoveries*/
//        scheduler.scheduleAtFixedRate(LRWorker::process, 0, 12, HOURS);

        scheduler.scheduleAtFixedRate(CSWorker::process, 0, 1, DAYS);

        scheduler.scheduleAtFixedRate(VXController::saveTreeLog, 2, 2, HOURS);
        scheduler.scheduleAtFixedRate(ESController::saveTreeLog, 2, 2, HOURS);
        scheduler.scheduleAtFixedRate(ALController::saveTreeLog, 2, 2, HOURS);
        scheduler.scheduleAtFixedRate(APController::refresh, 1, 1, HOURS);
        scheduler.scheduleAtFixedRate(APMain::cleanUp, 1, 1, HOURS);
    }

    public static void cleanUp() {
        try {
            EventQueue.invokeLater(apFrame::purgeTreeItems);
        } catch (Throwable ex) {
            acxLog.logEvent(ex);
        }
        try {
            java.beans.Introspector.flushCaches();
            System.gc();
        } catch (Throwable ex) {
            acxLog.logEvent(ex);
        }
    }

    public static void stopServer() {
        if (server != null) {
            server.stop();
        }
    }

    public static void stopTasks() {
        try {
            scheduler.shutdownNow();
        } catch (Exception ex) {
            acxLog.logEvent(ex);
        }
    }

    private void setAppenders() {
        Logger.getRootLogger().addAppender(new AXAppender(APController.isDebugMode() ? Level.DEBUG : Level.INFO));
    }

    public static Image getIconImage() {
        return Toolkit.getDefaultToolkit().createImage(APMain.class.getResource("/PHilae/ximg/icon.png"));
    }

    private static void exit() {
        System.exit(0);
    }
}
