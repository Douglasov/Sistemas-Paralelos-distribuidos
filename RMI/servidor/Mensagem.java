import java.io.*;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Mensagem implements Serializable {
    private String msg;
    private String remetente;
    private String destinatario;
    private String nomearq=null;
    private int Opcao;
    private byte[] arquivo;
    
    public Mensagem() {
        this.msg = "";
        this.remetente = "";
        this.destinatario = "";
    }
    


    public Mensagem(String narq, byte[] arq) {
        nomearq=narq;
        arquivo=arq;
    }

    public Mensagem(String narq) {
        nomearq=narq;
    }



    

    /**
     * @return the nomearq
     */
    public String getNomearq() {
        return nomearq;
    }

    public byte[] getarquivo()
    {
        return arquivo;
    }
    
    public String getMensagem() {
        return msg;
    }
    
    public void setMensagem(String msg) {
        this.msg = msg;
    }
    
    public String getRemetente() {
        return remetente;
    }
    
    public void setRemetente(String remetente) {
        this.remetente = remetente;
    }
    
    public String getDestinatario() {
        return destinatario;
    }
    
    public void setDestinatario(String destinatario) {
        this.destinatario = destinatario;
    }
    

}
