/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 *
 * @author Pecherk
 */
public class AXLogger extends APLog {

    private final Long maxSize = 10485760L;
    private final AXFile axFile = new AXFile();
    public String realm = "acx", path = "acx" + File.separator + "logs";

    public AXLogger(String realm, String path) {
        this.realm = realm;
        this.path = path;
    }

    @Override
    public void logDebug(Object event) {
        if (APController.isDebugMode()) {
            logEvent(null, null, null, event);
        }
    }

    @Override
    public void logDebug(String key, Object event) {
        if (APController.isDebugMode()) {
            logEvent(key, null, null, event);
        }
    }

    @Override
    public void logDebug(String key, Object event, String prefix) {
        if (APController.isDebugMode()) {
            setCall(key, event, prefix);
        }
    }

    @Override
    public void logEvent(Object event) {
        logEvent(null, null, null, event);
    }

    @Override
    public void logEvent(Object input, Object event) {
        logEvent(null, input, null, event);
    }

    public void logEvent(String message, Object input, Object event) {
        logEvent(null, input, message, event);
    }

    public void logEvent(String key, Object input, String message, Object event) {
        try {
            StringBuilder buffer = new StringBuilder("<event realm=\"" + (key != null ? key.toLowerCase() : realm) + "\" datetime=\"" + new Date() + "\">");
            if (event instanceof Throwable) {
                printError(buffer, (Throwable) event, input, message, "error", getIndent());
                buffer.append("\r\n").append("</event>\r\n");
            } else if (String.valueOf(event).trim().startsWith("<") && String.valueOf(event).trim().endsWith(">")) {
                buffer.append("\r\n").append(indentLines(String.valueOf(event), getIndent())).append("\r\n");
                buffer.append("</event>\r\n");
            } else {
                buffer.append("\r\n").append(getIndent()).append("<info>").append(String.valueOf(event)).append("</info>");
                buffer.append("\r\n").append("</event>\r\n");
            }
            new Thread(()
                    -> {
                writeToLog(buffer.toString(), event instanceof Throwable);
            }).start();
            if (event instanceof AXCaller) {
                ((AXCaller) event).getCalls().clear();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void archiveOldLog(String lastDate, File logs, File logFile) {
        rotateExistingLogs(lastDate, logs);
        try {
            axFile.appendToFile(logFile, "</logger>");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        File oldLog = new File(logs, "events-" + lastDate + "-0.log");
        logFile.renameTo(oldLog);
        try {
            axFile.compressFileToGzip(oldLog);
            axFile.deleteFile(oldLog);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        purgeOldLogs();
    }

    private void rotateExistingLogs(String lastDate, File logs) {
        int count = 99;
        while (count >= 0) {
            try {
                File prev = new File(logs, "events-" + lastDate + "-" + count + ".log.gz");
                if (prev.exists()) {
                    if (count >= 99) {
                        axFile.deleteFile(prev);
                    } else {
                        prev.renameTo(new File(logs, "events-" + lastDate + "-" + (count + 1) + ".log.gz"));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            count--;
        }
    }

    private File getNewLog(File logsDir, File logFile) {
        logFile = new File(logsDir, "events.log");
        try {
            logFile.createNewFile();
            axFile.appendToFile(logFile, "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            axFile.appendToFile(logFile, "<logger class=\"" + AXLogger.class.getName() + "\" datetime=\"" + new Date() + "\">");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return logFile;
    }

    private synchronized void writeToLog(String logEvent, boolean error) {
        try {
            (error ? System.err : System.out).print(logEvent);
            axFile.appendToFile(getLog(), logEvent);
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private File getLog() {
        File logs = new File(path);
        if (!logs.exists()) {
            logs.mkdirs();
        }
        File logFile = new File(path, "events.log");
        if (logFile.exists()) {
            String lastDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(logFile.lastModified()));
            if (!lastDate.equals(new SimpleDateFormat("yyyy-MM-dd").format(new Date())) || logFile.length() >= maxSize) {
                archiveOldLog(lastDate, logs, logFile);
                return getNewLog(logs, logFile);
            }
            return logFile;
        }
        return getNewLog(logs, logFile);
    }

    private void purgeOldLogs() {

        File logs = new File(path);
        for (File log : logs.listFiles()) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(new Date(log.lastModified()));
            if (Calendar.getInstance().get(Calendar.MONTH) - c1.get(Calendar.MONTH) >= 0 && Calendar.getInstance().get(Calendar.YEAR) - c1.get(Calendar.YEAR) >= APController.yearsToKeepLogs) {
                try {
                    axFile.deleteFile(log);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    public void logError(String message, Throwable ex) {
        logEvent(message, null, ex);
    }

    @Override
    public void logError(Throwable ex) {
        logEvent(ex);
    }

    @Override
    public void setCall(String callRef, Object callObject) {
        setCall(callRef, callObject, false);
    }

    @Override
    public void setCall(String callRef, Object callObject, boolean replace) {
        setCall(callRef, callObject, replace, 0, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, long position) {
        setCall(callRef, callObject, false, position, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, String prefix) {
        setCall(callRef, callObject, false, 0, prefix);
    }

    @Override
    public void setCall(String callRef, Object callObject, boolean replace, long position) {
        setCall(callRef, callObject, replace, position, null);
    }

    @Override
    public void setCall(String callRef, Object callObject, boolean replace, long position, String prefix) {
        logEvent("<" + callRef + ">" + (APController.isDebugMode() ? checkBlank(prefix, "") : "") + convertToString(callObject) + "</" + callRef + ">");
    }
}
