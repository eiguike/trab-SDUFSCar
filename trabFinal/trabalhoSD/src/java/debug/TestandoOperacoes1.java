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
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author ricke
 */
public class TestandoOperacoes1 {

    ConexaoBD con = new ConexaoBD();

    public static void main(String[] args) {
        OperacoesVideo vidOp;
        vidOp = new OperacoesVideo();
        VideoModel vid = new VideoModel();
        vid.setId("4");
        URL url = vidOp.downloadVideo(vid);
        if (url != null) {
            System.out.println("DEU CERTO!!!");
        } else {
            System.out.println("DEU ERRADO!!!");
            return;
        }
        System.out.println(url.toString());
    }

}
