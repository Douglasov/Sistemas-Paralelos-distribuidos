import java.net.*;
import java.io.*;
import java.util.*;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.File;
import java.io.FileOutputStream;


public class AplicacaoCliente {

    private Socket servidor;
    private ObjectOutputStream streamSaida;
    private ObjectInputStream streamEntrada;
    private ThreadEntrada entrada; 
    private String nick;
    private File afile[];

    public AplicacaoCliente(String ip, int porta, String nick) throws UnknownHostException, IOException {
        this.nick = nick;
        servidor = new Socket(ip, porta);
        streamSaida = new ObjectOutputStream(servidor.getOutputStream());
        streamEntrada = new ObjectInputStream(servidor.getInputStream());
        entrada = new ThreadEntrada(streamEntrada);
        entrada.start();
        //get nick
        enviaMensagem("", nick);
        preenchelista();
    }

    public void enviaMensagem(Mensagem msg) throws IOException {
        streamSaida.writeObject(msg);
    }


    
    public void enviaMensagem(String msg) throws IOException {
        streamSaida.writeObject(new Mensagem(msg, nick));
    }

    public void preenchelista() {
        File file = new File("arquivos");
        afile = file.listFiles();
    }

    public void enviaarquivo(int pos) throws IOException {
        File myFile = new File("arquivos/"+afile[pos].getName());
        byte[] bFile = new byte[(int) myFile.length()];
        FileInputStream fis = new FileInputStream(myFile);
        fis.read(bFile);
        fis.close();
        Mensagem msg=new Mensagem("up4rqu1", nick,afile[pos].getName(),bFile);
        enviaMensagem(msg);
    }

    public void listadearquivoscliente() throws IOException {
        File file = new File("arquivos");
        afile = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            System.out.println(i+ " - " + arquivos.getName());
            }
    }

    
    public void enviaMensagem(String msg, String destinatario) throws IOException {
        streamSaida.writeObject(new Mensagem(msg, nick, destinatario));
    }

    public void enviaMensagem(String msg, String destinatario,int op) throws IOException {
        streamSaida.writeObject(new Mensagem(msg, nick, destinatario,op));
    }
    
    
    public String getNick() {
        return nick;
    }
    
    public void finaliza() throws IOException {
        entrada.finaliza();
        streamSaida.close();
        streamEntrada.close();
        servidor.close();
    }

    public void espera()
    {
        try{
            Thread.sleep(1000);
      }catch(Exception e){
            
      }
    }
    
    public static void main(String[] args) 
           throws UnknownHostException, IOException {

        String msg = "", nick = "";
        boolean fim = false;
        
        
        InputStreamReader streamTeclado = new InputStreamReader(System.in);
        BufferedReader teclado = new BufferedReader(streamTeclado);

        System.out.println("Digite seu nickname para conectar: ");
        nick = teclado.readLine();
            
        AplicacaoCliente cliente = new AplicacaoCliente("127.0.0.1", 12345, nick);
    
        System.out.println(nick + " se conectou ao servidor!");
        
        
        
        do {
            
            System.out.print("\033[H\033[2J");
            System.out.println("Digite: \n1-Para enviar uma mensagem em broadcast \n2-Para baixar um arquivo \n3-Solicitar lista de arquivos \n4-Solicitar lista de clientes\n5-Enviar um arquivo para o servidor\n6-Desconectar");
            msg = teclado.readLine();
            if(msg.equals("1"))
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                System.out.println("Digite a mensagem: ");
                msg = teclado.readLine();
                cliente.enviaMensagem(msg, nick);
                cliente.espera();
            }
            else if(msg.equals("2"))
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                cliente.enviaMensagem("l1st4rq", nick  );
                cliente.espera();
                System.out.println("Digite o numero do arquivo que deseja baixar: ");
                msg=teclado.readLine();
                cliente.enviaMensagem("s0liarq", nick, Integer.parseInt(msg));
                cliente.espera();

 
            }
            else if(msg.equals("3"))
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                
                cliente.enviaMensagem("l1st4rq", nick  );
                cliente.espera();
                System.out.println("\n Enter para continuar ");
                teclado.readLine();
            }
            else if(msg.equals("4"))
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                cliente.enviaMensagem("l1st4cl1e7", nick);
                cliente.espera();
                System.out.println("\n Enter para continuar ");
                teclado.readLine();
                
            }
            else if(msg.equals("5"))
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                cliente.listadearquivoscliente();
                System.out.println("Digite o numero do arquivo que deseja dazer upload no servidor: ");
                msg=teclado.readLine();
                cliente.enviaarquivo(Integer.parseInt(msg));
                cliente.espera();
            }
            else if(msg.equals("6"))
            {
                fim = true;  
            }           
                
        } while (!fim);
                
        cliente.finaliza();
    }
    
}

class ThreadEntrada extends Thread {

    private ObjectInputStream streamEntrada;
    private boolean fim;

    public ThreadEntrada(ObjectInputStream streamEntrada) {
        this.streamEntrada = streamEntrada;
        this.fim = false;
    }
    public ObjectInputStream power()
    {
       return streamEntrada;
    }
    public void finaliza() {
        fim = true;
    }

    public void run() {
        Mensagem msg;
        
        
        

        try {
            while(!fim) {

                msg = (Mensagem) streamEntrada.readObject();
                if(msg.getNomearq()==null)
                {
                if(msg.getRemetente().equals(""))
                System.out.println(msg.getRemetente() + "" + msg.getMensagem());
                else
                System.out.println(msg.getRemetente() + ": " + msg.getMensagem());
                }
                else
                {
                    
                    File file = new File("arquivos/"+msg.getNomearq());
                    FileOutputStream in= new FileOutputStream(file);
                    in.write(msg.getarquivo());
                    in.close();
                    System.out.println("Arquivo "+msg.getNomearq()+" recebido");

                }




            }
        } catch (Exception e) {
            System.err.println("Desconectado do Servidor");
        }
    }
}

