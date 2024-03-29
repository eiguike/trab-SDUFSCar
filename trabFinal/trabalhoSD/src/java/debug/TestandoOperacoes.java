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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author ricke
 */
public class TestandoOperacoes {

    ConexaoBD con = new ConexaoBD();

    public static void main(String[] args) throws SQLException {

        Scanner user_input = new Scanner(System.in);

        String fileAddress;
        String descricao;

        System.out.print("Insira o caminho para o video que será carregado: ");
        fileAddress = user_input.nextLine();

        System.out.print("Insira uma descrição para o video: ");
        descricao = user_input.nextLine();

        FileInputStream fileInputStream = null;
        File file = new File(fileAddress);

        byte[] bFile = new byte[(int) file.length()];
        try {
            fileInputStream = new FileInputStream(file);
            fileInputStream.read(bFile);
            fileInputStream.close();
        } catch (IOException e) {
            System.out.println("Arquivo especificado não existe!");
            return;
        }

        String videoId;

        OperacoesVideo vidOp;
        vidOp = new OperacoesVideo();

        videoId = vidOp.geraChave();

        VideoModel vid = new VideoModel();
        vid.setDados(bFile);
        vid.setDescricao(descricao);
        vid.setId(videoId);

        vidOp.insertVideo(vid);
    }

}
