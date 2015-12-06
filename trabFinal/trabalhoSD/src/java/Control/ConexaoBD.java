
package Control;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConexaoBD {
    //Objeto que guarda informacoes da conexao com o SGBD.
    private Connection myConnection;

    //Objeto usado para enviar comandos SQL no SGBD
    Statement st;

    // Variável que definira estado da conexão
    private boolean conectado;

    public boolean isConectado() {
        return conectado;
    }

    public void setConectado(boolean conectado) {
        this.conectado = conectado;
    }

    // Construtor
    public ConexaoBD(){
        setConectado(false);
        try{
            Class.forName("org.postgresql.Driver").newInstance();
            
            myConnection = DriverManager.getConnection("jdbc:postgresql:" +
                    "//ec2-54-94-203-79.sa-east-1.compute.amazonaws.com:5432/postgres?user=postgres&password=postgres");
            st = myConnection.createStatement();
            setConectado(true);
        }
        catch (Exception e){
            setConectado(false);
            e.printStackTrace();
        }
    }
}