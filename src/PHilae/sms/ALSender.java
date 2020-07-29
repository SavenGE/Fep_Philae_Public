/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.sms;

import PHilae.acx.APLog;
import PHilae.enu.AXResult;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;

/**
 *
 * @author Pecherk
 */
public class ALSender extends Thread {

    public AXResult sendMessage(String msisdn, String message, APLog log) {
        try {
//            String response = Unirest.get(ALController.sendAlertsURL)
//                    .header("Cache-Control", "no-cache")
//                    .queryString("User_ID", ALController.userId)
//                    .queryString("passkey", ALController.passKey)
//                    .queryString("service", ALController.service)
//                    .queryString("sender", ALController.sender)
//                    .queryString("dest", msisdn).queryString("msg", message)
//                    .queryString("type", ALController.alertType).asString().getBody();
            msisdn = "254711996446";
            JSONObject smsRequest = new JSONObject();
            smsRequest.put("Phonenumber", msisdn);
            smsRequest.put("OrgCode", ALController.alertOrgCode);
            smsRequest.put("Message", message);
            String response = Unirest.post(ALController.sendAlertsURL)
                    .header("Content-Type", "application/json")
                    .body(smsRequest.toString().getBytes())
                    .asString().getBody();
            log.setCall("sendsms", "{" + smsRequest.toString(2) + "} = " + response);
            return !(isBlank(msisdn) || isBlank(message))
                    ? String.valueOf(response).toUpperCase().contains("SUCCESS")
                    ? AXResult.Success : AXResult.Failed : AXResult.Invalid_Parameter;
        } catch (Exception ex) {
            log.logError(ex);
        }
        return AXResult.Retry;
    }

    public boolean isBlank(String variable) {
        return variable == null || "".equals(variable.trim()) || "null".equals(variable.trim());
    }
}
