/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.acx;

import PHilae.APController;
import PHilae.APMain;
import PHilae.DBPClient;
import PHilae.enu.TXType;
import PHilae.model.CNAccount;
import PHilae.vma.VXCaller;
import PHilae.vma.VXController;
import PHilae.vma.VXProcessor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import javax.xml.ws.Holder;
import org.json.JSONObject;

/**
 *
 * @author Pecherk
 */
public class LRWorker {

    public static boolean wait = false;
    private static final ATBox box = new ATBox(new AXCaller("caller"));

    public static void process() {
        try {
            processRecoveries(VXController.loanOneProducts, VXController.loanOneRecoveryDay);
            processRecoveries(VXController.loanTwoProducts, VXController.loanTwoRecoveryDay);
        } catch (Throwable ex) {
            APMain.vmaLog.logEvent(ex);
        }
    }

    private static void processRecoveries(ArrayList<Long> productIds, Integer daysPast) {
        try {
            for (CNAccount loanAccount : getClient().queryLoanAccounts(productIds, daysPast)) {
                for (CNAccount debitAccount : getClient().queryDepositAccounts(loanAccount.getCustId(), VXController.nwdProducts)) {
                    try {
                        wait = true;
                        Holder<String> requestId = new Holder<>(APController.generateKey());
                        Holder<String> responseCode = new Holder<>();
                        Holder<String> responseMessage = new Holder<>();
                        Holder<String> errorMessage = new Holder<>();

                        VXController.calls.put(requestId.value + "I", new JSONObject().put("RequestId", requestId.value).put("LoanAccount", loanAccount.getAccountNumber()).put("DebitAccount", debitAccount.getAccountNumber()).put("Amount", loanAccount.getBalance()).toString());
                        recoverLoan(loanAccount, debitAccount, requestId, responseCode, responseMessage, errorMessage);
                        VXController.calls.put(requestId.value + "O", new JSONObject().put("RequestId", requestId.value).put("ResultCode", responseCode.value).put("ResultDesc", responseMessage.value).toString());
                    } catch (Exception ex) {
                        APMain.vmaLog.logEvent(ex);
                    }
                    wait = false;
                    if (APMain.exit) {
                        break;
                    }
                }
                wait = false;
                if (APMain.exit) {
                    break;
                }
            }
        } catch (Exception ex) {
            APMain.vmaLog.logEvent(ex);
        }
    }

    public static void recoverLoan(CNAccount loanAccount, CNAccount debitAccount, Holder<String> requestId, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(getWorker().getHostAddress(), TXType.LoanRecovery,
                requestId.value, getClient().queryMobileContact(loanAccount.getCustId()),
                loanAccount.getAccountNumber(),
                requestId.value, method.getName(), method.getParameterTypes(), loanAccount,
                debitAccount, requestId, responseCode, responseMessage, errorMessage))
                .execute(responseCode, responseMessage, errorMessage);
    }

    private static DBPClient getClient() {
        return getBox().getClient();
    }

    private static AXWorker getWorker() {
        return getBox().getWorker();
    }

    /**
     * @return the box
     */
    public static ATBox getBox() {
        return box;
    }
}
