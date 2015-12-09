/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.WebServiceRef;
import org.me.video.VideoModel;
import org.me.video.Video_Service;

/**
 *
 * @author floss
 */
@WebServlet(name = "Download", urlPatterns = {"/Download"})
public class Download extends HttpServlet {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/trabalhoSD/Video.wsdl")
    private Video_Service service;

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String id = request.getParameter("idVideo");
        VideoModel result = download(id);

        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {

            out.println("<div id=\"videoPlayer\" style=\"display: inline-block; float: left; box-shadow: 0px 0px 4px 0.1px rgb(200, 200, 200); width: 65%; padding-left: 5px; height: 100%;\">    <b id=\"nomeVideo\" style=\"padding-left: 20px;\">        Vídeo    </b>     <a href=\"" + result.getURL() + "\" target=\"_blank\" download=\"video.mp4\"><img src=\"./baixar.png\" style=\"padding-left: 10px; padding-top: 10px;\" title=\"Download do vídeo\" height=\"20\"></a>    <div style=\"\" id=\"video\">        <video style=\"padding-bottom: 20px; padding-top: 15px; width: 95%; padding-left: 20px;\" controls=\"controls\">            <source src=\"" + result.getURL() + "\" type=\"video/mp4\">            <source src=\"" + result + "\" type=\"video/ogg\">            Your browser does not support HTML5 video.        </video>    </div></div><div id=\"descricaoVideo\" style=\"background: white none repeat scroll 0% 0%; display: inline-block; box-shadow: 0px 0px 4px 0.1px rgb(200, 200, 200); margin-top: 2%; width: 32%; float: right; height: 100%;\">    <div style=\"margin:3%;\">        <b> Descrição do vídeo:</b>        <p>"+result.getDescricao()+"</p>    </div></div>");

        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

    private VideoModel download(java.lang.String id) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.me.video.Video port = service.getVideoPort();
        return port.download(id);
    }


}
