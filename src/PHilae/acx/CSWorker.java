/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.enu.AXResult;
import PHilae.est.ESController;
import PHilae.model.AXRequest;
import PHilae.model.AXResponse;
import PHilae.model.AXSplit;
import PHilae.sms.ALController;
import PHilae.vma.VXController;
import java.util.ArrayList;

/**
 *
 * @author Pecherk
 */
public class CSWorker extends Thread {

    public static boolean wait = false;
    private static final ATBox box = new ATBox(new AXCaller("caller"));

    public static void process() {
        try {
            if (!APMain.exit) {
                APController.setEndOfYear(getClient().isEndOfYear());
                ArrayList<AXSplit> splits = getClient().querySplits();
                for (AXSplit split : splits) {
                    try {
                        wait = true;
                        AXLogger log = APMain.acxLog;

                        getBox().setLog(new AXCaller("caller"));
                        getXapi().setResponse(new AXResponse());
                        Long startTime = System.currentTimeMillis();

                        try {
                            AXRequest aXRequest = new AXRequest();
                            aXRequest.setReference(APController.generateKey());
                            aXRequest.setAccessCode(split.getModule());

                            aXRequest.setNarration(split.getDescription());
                            aXRequest.setAmount(split.getAmount());
                            getCaller().setTxnId(aXRequest.getReference());

                            getCaller().setAccount(split.getDebitAccount());
                            getCaller().setNarration(split.getDescription());
                            switch (split.getModule()) {
                                case VXController.module:
                                    log = APMain.vmaLog;
                                    aXRequest.setModule(VXController.module);
                                    aXRequest.setChannel(VXController.getChannel());
                                    aXRequest.setRole(VXController.getRole());
                                    break;
                                case ALController.module:
                                    log = APMain.smsLog;
                                    aXRequest.setModule(ALController.module);
                                    aXRequest.setChannel(ALController.getChannel());
                                    aXRequest.setRole(ALController.getRole());
                                    break;
                                case ESController.module:
                                    log = APMain.estLog;
                                    aXRequest.setModule(ESController.module);
                                    aXRequest.setChannel(ESController.getChannel());
                                    aXRequest.setRole(ESController.getRole());
                                    break;
                            }

                            getCaller().setRequest(aXRequest);
                            AXResponse aXResponse = getXapi().postSplit(aXRequest, split);
                            getCaller().setResponse(aXResponse);

                            if (aXResponse.getResult() == AXResult.Success) {
                                split.setStatus("S");
                                getClient().updateSplit(split);
                                if (split.getTxnRef() != null && split.getField() != null) {
                                    switch (split.getField()) {
                                        case "C":
                                            getClient().updateChargeId(split.getModule(), split.getTxnRef(), aXResponse.getTxnId());
                                            break;
                                    }
                                }
                            }
                        } catch (Exception ex) {
                            getCaller().logError(ex);
                        }
                        getCaller().setCall("duration", (System.currentTimeMillis() - startTime) + " Ms");
                        log.logEvent(getCaller());
                    } catch (Exception ex) {
                        APMain.acxLog.logEvent(ex);
                    }
                    wait = false;
                    if (APMain.exit) {
                        break;
                    }
                }
                splits.clear();
            }
        } catch (Throwable ex) {
            APMain.acxLog.logEvent(ex);
        }
    }

    private static AXCaller getCaller() {
        return getBox().getLog(AXCaller.class);
    }

    /**
     * @return the xClient
     */
    public static AXClient getXapi() {
        return getBox().getXapi();
    }

    private static DBPClient getClient() {
        return getBox().getClient();
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
    }
}
