/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import PHilae.APController;
import PHilae.APMain;
import java.io.ByteArrayOutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

/**
 *
 * @author Pecherk
 */
public class VLHandler implements SOAPHandler<SOAPMessageContext> {

    private boolean writeMessage(SOAPMessageContext messageContext) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            String callId;
            Map map = (Map) messageContext.get(MessageContext.HTTP_RESPONSE_HEADERS);
            Boolean outward = (Boolean) messageContext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

            if (outward) {
                List<String> headerField = (List<String>) map.remove("callId");
                callId = (headerField != null && !headerField.isEmpty()) ? headerField.get(0) : null;
            } else {
                map.put("callId", Collections.singletonList(callId = APController.generateKey()));
            }
            if (callId != null) {
                messageContext.getMessage().writeTo(baos);
                VXController.calls.put(callId + (outward ? "O" : "I"), baos.toString());
            }
        } catch (Exception ex) {
            APMain.vmaLog.logError(ex);
        }
        return true;
    }

    @Override
    public Set<QName> getHeaders() {
        return Collections.emptySet();
    }

    @Override
    public boolean handleMessage(SOAPMessageContext messageContext) {
        return writeMessage(messageContext);
    }

    @Override
    public boolean handleFault(SOAPMessageContext messageContext) {
        return writeMessage(messageContext);
    }

    @Override
    public void close(MessageContext messageContext) {
    }
}
