/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.ipn;

import PHilae.APMain;
import PHilae.vma.VXController;
import com.mashape.unirest.http.Unirest;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.DatatypeConverter;
import org.json.JSONObject;

/**
 *
 * @author Pecherk
 */
public class MPWorker {

    public static void registerUrl() {
        try {
            if (VXController.getWorker().isYes(VXController.updateMpesaUrl)) {
                JSONObject response, token = Unirest.get(VXController.mpesaTokenUrl).queryString("grant_type", "client_credentials").header("Content-Type", "application/json").header("Cache-Control", "no-cache").header("authorization", "Basic " + DatatypeConverter.printBase64Binary((VXController.mpesaConsumerKey + ":" + VXController.mpesaConsumerSecret).getBytes(StandardCharsets.ISO_8859_1))).asJson().getBody().getObject();
                JSONObject request = new JSONObject("{\"ShortCode\": \"" + VXController.mpesaPaybillNumber + "\" ,\"ResponseType\": \"" + VXController.mpesaTimeoutAction + "\",\"ConfirmationURL\": \"" + VXController.mpesaConfirmationUrl + "\",\"ValidationURL\": \"" + VXController.mpesaValidationUrl + "\"}");
                APMain.vmaLog.logEvent(token.toString() + " = " + request.toString() + " = " + (response = Unirest.post(VXController.mpesaRegisterUrl).header("Content-Type", "application/json").header("Cache-Control", "no-cache").header("authorization", "Bearer " + token.getString("access_token")).body(request.toString()).asJson().getBody().getObject()).toString());
                if (String.valueOf(response.get("ResponseDescription")).toLowerCase().contains("success")) {
                    VXController.getClient().updateSetting(VXController.getSettings().get("UpdateMpesaUrl").setValue(VXController.updateMpesaUrl = "No"));
                }
            }
        } catch (Exception ex) {
            APMain.vmaLog.logEvent(ex);
        }
    }
}
