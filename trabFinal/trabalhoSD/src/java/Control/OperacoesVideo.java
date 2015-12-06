/*
Aqui nesta classe conterá todas as operações que devemos fazer no trabalho, sendo elas:
- Download de um vídeo
- Upload de um vídeo

*/
package Control;

import Model.VideoModel;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricke
 */
public class OperacoesVideo {
	ConexaoBD con;
	
	public OperacoesVideo(ConexaoBD con){
		this.con = con;
	}

	public OperacoesVideo() {
		this.con = new ConexaoBD();
	}
	
	public boolean insertVideo(VideoModel video){
		// transformando bytes[] em um File
		File outputFile = new File("/tmp/"+video.getId());
		try{
			FileOutputStream outputstream = new FileOutputStream(outputFile);
			outputstream.write(video.getDados());
		} catch (IOException ex) {
			Logger.getLogger(OperacoesVideo.class.getName()).log(Level.SEVERE, null, ex);
			return false;
		}
		
		// devemos primeiramente tentar enviar o vídeo para a cloud
		AWSCredentials credentials = null;
		try {
			credentials = new ProfileCredentialsProvider().getCredentials();
		} catch (Exception e) {
			throw new AmazonClientException(
				"Cannot load the credentials from the credential profiles file. " +
					"Please make sure that your credentials file is at the correct " +
					"location (~/.aws/credentials), and is in valid format.",
				e);
		}
		AmazonS3 s3 = new AmazonS3Client(credentials);
		s3.setRegion(Region.getRegion(Regions.SA_EAST_1));
		
		String bucketName = "trabalhosd";
		try{
			s3.putObject(bucketName, video.getId(), outputFile);
		} catch (AmazonServiceException ase) {
			System.out.println("Caught an AmazonServiceException, which means your request made it "
				+ "to Amazon S3, but was rejected with an error response for some reason.");
			System.out.println("Error Message:    " + ase.getMessage());
			System.out.println("HTTP Status Code: " + ase.getStatusCode());
			System.out.println("AWS Error Code:   " + ase.getErrorCode());
			System.out.println("Error Type:       " + ase.getErrorType());
			System.out.println("Request ID:       " + ase.getRequestId());
			return false;
		} catch (AmazonClientException ace) {
			System.out.println("Caught an AmazonClientException, which means the client encountered "
				+ "a serious internal problem while trying to communicate with S3, "
				+ "such as not being able to access the network.");
			System.out.println("Error Message: " + ace.getMessage());
			return false;
		}
		
		// logo em seguida, realizar o insert do video no banco de dados relacional
		ResultSet rs = null;
		String texto_consulta = "INSERT INTO video (descricao,iddownload) VALUES('"+video.getDescricao()+
			"','"+video.getId()+"');";
		
		System.out.println(texto_consulta);
		try{
			con.st.execute(texto_consulta);
		}catch(SQLException e){
      System.out.println(e);
			return false;
		}
		return true;
	}
	
}
