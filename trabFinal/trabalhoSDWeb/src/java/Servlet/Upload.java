/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.ws.WebServiceRef;
import org.me.video.Video_Service;

/**
 *
 * @author floss
 */
@WebServlet(name = "Upload", urlPatterns = {"/Upload"})
@MultipartConfig
public class Upload extends HttpServlet {

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
    protected void processRequest(HttpServletRequest request, HttpServletResponse response, String URL)
            throws ServletException, IOException {

        
        
        String descricao = request.getParameter("descricao");
        Part file = request.getPart("arquivo");
        System.out.println(descricao);

        String filename = file.getSubmittedFileName();
        InputStream fileContent = file.getInputStream();
//        OutputStream out1 = 
        System.out.println(file.getSize());
        byte[] bytes = new byte[(int) file.getSize()];
        int bytesRead;

        while ((bytesRead = fileContent.read(bytes)) != -1);
//            out1.write(bytes, 0, bytesRead);
//        }
        fileContent.close();
        
//        out1.close();

        String idvideo = upload(descricao, bytes);
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            out.println(idvideo);
        }
        


//
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<!DOCTYPE html>");
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet Upload</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet Upload at " + request.getContextPath() + "</h1>");
//            out.println(request.getMethod());
//            out.println("</body>");
//            out.println("</html>");
//        }
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
        System.out.println("HUEHUEUHEU");
        processRequest(request, response, null);

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
        processRequest(request, response, "meupé");
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

    private String upload(java.lang.String descricao, byte[] video) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        org.me.video.Video port = service.getVideoPort();
        return port.upload(descricao, video);
    }

}
