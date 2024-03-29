/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
 */
package org.me.Video;

import Model.VideoModel;
import Control.ConexaoBD;
import Control.OperacoesVideo;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;

/**
 *
 * @author ricke
 */
@WebService(serviceName = "Video")
public class Video {

    VideoModel vid;
    ConexaoBD con = new ConexaoBD();

    /**
     * This is a sample web service operation
     */
    @WebMethod(operationName = "hello")
    public String hello(@WebParam(name = "name") String txt) {
        return "Hello " + txt + " !";
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "download")
    public VideoModel download(@WebParam(name = "id") String id) {
        VideoModel vid = new VideoModel();
        vid.setId(id);
        OperacoesVideo vidOp = new OperacoesVideo(con);
        VideoModel retorno = vidOp.downloadVideo(vid);

//        File outputFile = new File("/tmp/" + vid.getDescricao());
//       try {
//            FileOutputStream outputstream = new FileOutputStream(outputFile);
//            outputstream.write(vid.getDados());
//        } catch (IOException ex) {
//            Logger.getLogger(OperacoesVideo.class.getName()).log(Level.SEVERE, null, ex);
//        }
        if (retorno != null) {
            return retorno;
        } else {
            return null;
        }
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "upload")
    //public Boolean upload(@WebParam(name = "descricao") String descricao, @WebParam(name = "video") byte[] video) {
    public String upload(@WebParam(name = "descricao") String descricao, @WebParam(name = "video") byte[] bFile) {
//        FileInputStream fileInputStream = null;
//        File file = new File(nomeVideo);
//
//        byte[] bFile = new byte[(int) file.length()];
//        byte[] bFile = new byte;
//        try {
//            fileInputStream = new FileInputStream(file);
//            fileInputStream.read(bFile);
//            fileInputStream.close();
//        } catch (IOException e) {
//            System.out.println(e);
//        }

        OperacoesVideo vidOp = new OperacoesVideo(con);
        VideoModel vid = new VideoModel();
        vid.setDados(bFile);
        vid.setDescricao(descricao);

        return vidOp.insertVideo(vid);

    }
}
