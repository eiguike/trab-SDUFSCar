/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package debug;

import Control.ConexaoBD;
import Control.OperacoesVideo;
import Model.VideoModel;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 *
 * @author ricke
 */
public class TestandoOperacoes {
	ConexaoBD con = new ConexaoBD();
	
	public static void main(String[] args){
		FileInputStream fileInputStream = null;
		File file = new File("/home/ricke/Downloads/67b.jpg");
		String descricao = "meu pé";
		
		byte[] bFile = new byte[(int) file.length()];
		try{
			fileInputStream = new FileInputStream(file);
			fileInputStream.read(bFile);
			fileInputStream.close();
		}catch(IOException e){
			System.out.println(e);
		}
		
		
		OperacoesVideo vidOp;
		vidOp = new OperacoesVideo();
		VideoModel vid = new VideoModel();
		vid.setDados(bFile);
		vid.setDescricao(descricao);
		vid.setId("meupe");
		
		if(vidOp.insertVideo(vid))
			System.out.println("DEU CERTO!!!");
		else
			System.out.println("DEU ERRADO!!!");
	}
	
}
