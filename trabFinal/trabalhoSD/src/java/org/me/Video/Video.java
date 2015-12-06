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
import java.io.IOException;
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
	public Video download(@WebParam(name = "id") String id) {
		//TODO write your implementation code here:
		return null;
	}
	
	/**
	 * Web service operation
	 */
	@WebMethod(operationName = "upload")
	//public Boolean upload(@WebParam(name = "descricao") String descricao, @WebParam(name = "video") byte[] video) {
	public Boolean upload(@WebParam(name = "descricao") String descricao, @WebParam(name = "video") String nomeVideo) {
		FileInputStream fileInputStream = null;
		File file = new File("/home/ricke/Documents/git/trab-SDUFSCar/trabFinal/rmi2/123.mp4");
		
		byte[] bFile = new byte[(int) file.length()];
		try{
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		}catch(IOException e){
			System.out.println(e);
		}


		OperacoesVideo vidOp = new OperacoesVideo(con);
		VideoModel vid = new VideoModel();
		vid.setDados(bFile);
		vid.setDescricao(descricao);
		
		if(vidOp.insertVideo(vid))
			return true;
		else
			return false;
	}
}