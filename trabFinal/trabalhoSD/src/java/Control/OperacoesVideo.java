/*
Aqui nesta classe conterá todas as operações que devemos fazer no trabalho, sendo elas:
- Download de um vídeo
- Upload de um vídeo
 */
package Control;

import Model.VideoModel;
import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.util.IOUtils;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricke
 */
public class OperacoesVideo {

    ConexaoBD con;

    public OperacoesVideo(ConexaoBD con) {
        this.con = con;
    }

    public OperacoesVideo() {
        this.con = new ConexaoBD();
    }

    public URL downloadVideo(VideoModel video) {
        // precisamos buscar o id do video no S3
        String id = video.getId();
        int i;
        ResultSet rs = null;
        String aux = null;
        String texto_consulta;
        
        // expressão regular para detectar se é um número ou 
        // string
        if(video.getId().matches("[0-9]+")){
            texto_consulta
                = "SELECT descricao, iddownload FROM video WHERE video.id='" + id + "';";
        }else{
            texto_consulta
                = "SELECT descricao, iddownload FROM video WHERE video.iddownload='"+id+"';";
        }

        
        System.out.println(texto_consulta);

        try {
            con.st.execute(texto_consulta);
            rs = con.st.getResultSet();
            rs.next();
            video.setDescricao(rs.getString(1));
            video.setIdDownload(rs.getString(2));
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }

        System.out.println(video.getDescricao());
        System.out.println(video.getIdDownload());

        // tendo o id em mãos podemos baixa-lo
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.SA_EAST_1));

        String bucketName = "trabalhosd";

        /*
             * Download an object - When you download an object, you get all of
             * the object's metadata and a stream from which to read the contents.
             * It's important to read the contents of the stream as quickly as
             * possibly since the data is streamed directly from Amazon S3 and your
             * network connection will remain open until you read all the data or
             * close the input stream.
             *
             * GetObjectRequest also supports several other options, including
             * conditional downloading of objects based on modification times,
             * ETags, and selectively downloading a range of an object.
         */
        java.util.Date expiration = new java.util.Date();
        long msec = expiration.getTime();
        msec += 1000 * 60 * 120; // 2 hour.
        expiration.setTime(msec);

        GeneratePresignedUrlRequest generatePresignedUrlRequest
                = new GeneratePresignedUrlRequest(bucketName, video.getIdDownload());
        generatePresignedUrlRequest.setMethod(HttpMethod.GET); // Default.
        generatePresignedUrlRequest.setExpiration(expiration);

        URL url = s3.generatePresignedUrl(generatePresignedUrlRequest);

//        System.out.println("Downloading an object");
//        S3Object object = s3.getObject(new GetObjectRequest(bucketName, video.getIdDownload()));
//        try {
//            byte[] buf = IOUtils.toByteArray(object.getObjectContent());
//            video.setDados(buf);
//        } catch (IOException ex) {
//            Logger.getLogger(OperacoesVideo.class.getName()).log(Level.SEVERE, null, ex);
//            return null;
//        }
        return url;
    }

    public String insertVideo(VideoModel video) {
        try {
            // transformando bytes[] em um File
            video.setId(geraChave());
        } catch (SQLException ex) {
            Logger.getLogger(OperacoesVideo.class.getName()).log(Level.SEVERE, null, ex);
        }
        File outputFile = new File("/tmp/" + video.getId());
        try {
            FileOutputStream outputstream = new FileOutputStream(outputFile);
            outputstream.write(video.getDados());
        } catch (IOException ex) {
            Logger.getLogger(OperacoesVideo.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }

        // devemos primeiramente tentar enviar o vídeo para a cloud
        AWSCredentials credentials = null;
        try {
            credentials = new ProfileCredentialsProvider().getCredentials();
        } catch (Exception e) {
            throw new AmazonClientException(
                    "Cannot load the credentials from the credential profiles file. "
                    + "Please make sure that your credentials file is at the correct "
                    + "location (~/.aws/credentials), and is in valid format.",
                    e);
        }
        AmazonS3 s3 = new AmazonS3Client(credentials);
        s3.setRegion(Region.getRegion(Regions.SA_EAST_1));

        String bucketName = "trabalhosd";
        try {
            s3.putObject(bucketName, video.getId(), outputFile);
        } catch (AmazonServiceException ase) {
            System.out.println("Caught an AmazonServiceException, which means your request made it "
                    + "to Amazon S3, but was rejected with an error response for some reason.");
            System.out.println("Error Message:    " + ase.getMessage());
            System.out.println("HTTP Status Code: " + ase.getStatusCode());
            System.out.println("AWS Error Code:   " + ase.getErrorCode());
            System.out.println("Error Type:       " + ase.getErrorType());
            System.out.println("Request ID:       " + ase.getRequestId());
            return null;
        } catch (AmazonClientException ace) {
            System.out.println("Caught an AmazonClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with S3, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message: " + ace.getMessage());
            return null;
        }

        // logo em seguida, realizar o insert do video no banco de dados relacional
        ResultSet rs = null;
        String texto_consulta = "INSERT INTO video (descricao,iddownload) VALUES('" + video.getDescricao()
                + "','" + video.getId() + "');";

        System.out.println(texto_consulta);
        try {
            con.st.execute(texto_consulta);
        } catch (SQLException e) {
            System.out.println(e);
            return null;
        }
        return video.getId();
    }

    public String geraChave() throws SQLException {
        String videoId;
        ConexaoBD con = new ConexaoBD();
        ResultSet rs = null;

        char[] chars = "abcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        StringBuilder sb = new StringBuilder();
        while (true) {
            Random random = new Random();
            for (int i = 0; i < 8; i++) {
                char c = chars[random.nextInt(chars.length)];
                sb.append(c);
            }

            videoId = sb.toString();

            try {
                rs = con.st.executeQuery("SELECT * FROM video WHERE iddownload = \'" + videoId + "\'");
            } catch (SQLException e) {
                System.out.println(e);
            }

            if (!rs.next()) {
                break;
            }
        }

        return videoId;
    }

}
