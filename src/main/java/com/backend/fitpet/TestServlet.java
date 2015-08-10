package com.backend.fitpet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by David on 6/20/2015.
 */
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        super.doGet(req, resp);

        if(req.getParameter("test") == null){
            resp.setContentType("text/plain");
            resp.getWriter().println("Hello, test param is null");
//            Properties p = System.getProperties();
//            p.list(resp.getWriter());
        }else{
            resp.setContentType("text/plain");
            resp.getWriter().println("Hello, test param is not null and the value is " + req.getParameter("test"));
//            Properties p = System.getProperties();
//            p.list(resp.getWriter());
        }
    }
}
