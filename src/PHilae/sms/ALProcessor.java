/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.sms;

import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.acx.ATBox;
import PHilae.model.MXAlert;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Pecherk
 */
public class ALProcessor {

    private static final ATBox box = new ATBox(APMain.smsLog);
    public static final ConcurrentHashMap<String, Long> wip = new ConcurrentHashMap<>();

    public static void process() {
        try {
            if (!APMain.exit && !ALController.isSuspended()) {
                processPendingAlerts();
            }
        } catch (Throwable ex) {
            APMain.smsLog.logEvent(ex);
        }
    }

    private static void processPendingAlerts() {
        Date currentDate = getClient().getProcessingDate();
        getClient().queryAlerts().values().stream().map((mXAlert)
                -> {
            if (currentDate.after(mXAlert.getExpiryDate())) {
                mXAlert.setStatus("E");
            }
            return mXAlert;
        }).filter((mXAlert) -> (mXAlert.isLoaded()
                || (mXAlert.isActive() && !mXAlert.isTriggered() && isTimeRight(mXAlert)
                && (currentDate.after(mXAlert.getNextDate())
                || currentDate.equals(mXAlert.getNextDate())
                || mXAlert.isRealTime())))).forEach((mXAlert)
                -> {
            Long time = wip.getOrDefault(mXAlert.getCode(), 1L);
            if (time != 0L && System.currentTimeMillis() - time > 10000) {
                new ALWorker(mXAlert).start();
            }
        });
    }

    private static boolean isTimeRight(MXAlert mXAlert) {
        int time = Integer.parseInt(new SimpleDateFormat("HH").format(getClient().getSystemDate()));
        switch (mXAlert.getRunTime()) {
            case "Morning":
                return time >= 7 && time < 14;
            case "Afternoon":
                return time >= 14 && time < 19;
            case "Evening":
                return time >= 19 && time <= 23;
            case "Night":
                return time >= 0 && time < 7;
            default:
                return true;
        }
    }

    /**
     * @return the tDClient
     */
    public static DBPClient getClient() {
        return getBox().getClient();
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
    }
}
