/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package PHilae.vma;

import PHilae.APController;
import PHilae.APMain;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

/**
 *
 * @author Pecherk
 */
public class VHHandler extends AbstractHandler {

    @Override
    public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            APMain.vmaLog.logEvent("======<[ vma <[ clientAddress=<" + request.getRemoteAddr() + "> ]> ping ]>======");
            response.getWriter().print(APController.application);
        } catch (Exception ex) {
            APMain.vmaLog.logEvent(ex);
        }
        baseRequest.setHandled(true);
    }
}
