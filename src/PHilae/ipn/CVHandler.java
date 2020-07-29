/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.ipn;

import PHilae.APController;
import PHilae.APMain;
import PHilae.acx.ATBox;
import static PHilae.enu.AXResult.Success;
import PHilae.enu.TXType;
import PHilae.vma.VXCaller;
import PHilae.vma.VXController;
import PHilae.vma.VXProcessor;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Objects;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.Holder;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.json.JSONObject;

/**
 *
 * @author Pecherk
 */
public class CVHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        ATBox box = new ATBox(APMain.vmaLog);
        try {
            response.setHeader("Cache-Control", "no-cache");
            response.setHeader("Content-Type", "application/json");
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            String reqMessage = box.getFile().readInputStream(request.getInputStream());

            if (box.getWorker().isJson(reqMessage)) {
                CBRequest cBRequest = new CBRequest();
                cBRequest.setCallId(APController.generateKey());
                JSONObject requestObject = new JSONObject(reqMessage);

                cBRequest.setTxnDt(new Date());
                cBRequest.setClient(request.getRemoteAddr());
                cBRequest.setAmount(requestObject.getBigDecimal("TransAmount"));

                cBRequest.setBalance(box.getWorker().convertToType(requestObject.getString("OrgAccountBalance"), BigDecimal.class, null));
                cBRequest.setBillRef(requestObject.getString("BillRefNumber"));
                cBRequest.setCurrency(APController.getCurrency().getCode());

                cBRequest.setFirstName(requestObject.getString("FirstName"));
                cBRequest.setLastName(requestObject.getString("LastName"));
                cBRequest.setMiddleName(requestObject.getString("MiddleName"));

                cBRequest.setMsisdn(requestObject.getString("MSISDN"));
                cBRequest.setShortCode(requestObject.getString("BusinessShortCode"));
                cBRequest.setTranTime(requestObject.getString("TransTime"));

                cBRequest.setTxnId(requestObject.getString("TransID"));
                cBRequest.setTxnType(requestObject.getString("TransactionType"));
                VXController.calls.put(cBRequest.getCallId() + "I", reqMessage);

                Holder<String> responseCode = new Holder<>();
                Holder<String> responseMessage = new Holder<>();
                Holder<String> errorMessage = new Holder<>();

                mpesaVerify(cBRequest, responseCode, responseMessage, errorMessage);
                String resMessage = new JSONObject("{\"ResultCode\": " + (Objects.equals(Success.getCode(), responseCode.value) ? "0" : "1") + ",\"ResultDesc\": \"" + (Objects.equals(Success.getCode(), responseCode.value) ? "Success" : "Failed") + "\",\"ThirdPartyTransID\": \"" + cBRequest.getTxnId() + "\"}").toString();
                VXController.calls.put(cBRequest.getCallId() + "O", resMessage);
                response.getWriter().print(resMessage);
            } else {
                box.getLog().logEvent("======<[ vmt <[ CV=<" + request.getRemoteAddr() + "> message=<" + reqMessage + "> ]> ping ]>======");
                response.getWriter().print(APController.application);
            }
        } catch (Exception ex) {
            box.getLog().logEvent(ex);
        }
        baseRequest.setHandled(true);
    }

    public void mpesaVerify(CBRequest cBRequest, Holder<String> responseCode, Holder<String> responseMessage, Holder<String> errorMessage) {
        Method method = new Object() {
        }.getClass().getEnclosingMethod();
        new VXProcessor(new VXCaller(cBRequest.getClient(), TXType.MpesaVerify,
                cBRequest.getTxnId(), cBRequest.getMsisdn(), cBRequest.getBillRef(),
                cBRequest.getCallId(), method.getName(), method.getParameterTypes(),
                cBRequest, responseCode, responseMessage, errorMessage))
                .execute(responseCode, responseMessage, errorMessage);
    }
}
