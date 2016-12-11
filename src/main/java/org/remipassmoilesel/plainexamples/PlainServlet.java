package org.remipassmoilesel.plainexamples;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "PlainServlet", urlPatterns = {"/plainservlet"})
public class PlainServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.setContentType("text/plain");

        PrintWriter writer = resp.getWriter();
        writer.write("Plain servlet running !");
        writer.flush();
    }
}
