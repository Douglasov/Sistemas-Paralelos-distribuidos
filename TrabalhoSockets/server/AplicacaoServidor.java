import java.net.*;
import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileInputStream;
import java.net.UnknownHostException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedInputStream;

public class AplicacaoServidor extends Thread {
    private ServerSocket servidor;
    private int porta;
    private boolean corta = true;
    private File afile[];
    Vector<String> listadearquivos= new Vector<>();

    Vector<Socket> sock = new Vector<>();
    Vector<Cliente> cliente = new Vector<>();

    public AplicacaoServidor(int porta) throws IOException {
        this.porta = porta;
        servidor = new ServerSocket(porta);
        preenchelista();
        
    }

    public void esperaConexoes() throws IOException {
        Socket sock1;
        // servidor.accept();
        sock1 = servidor.accept();
        sock.add(sock1);
        // sock1 = servidor.accept();
        cliente.add(new Cliente(sock.lastElement(), this));
        cliente.lastElement().start();
        

    }
    public void preenchelista() {
        File file = new File("arquivos");
        afile = file.listFiles();
    }

    public void listadearquivos() {
        File file = new File("arquivos");
        afile = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            System.out.println(i+ " - " + arquivos.getName());
            }
    }

    public void listadearquivoscliente(ObjectOutputStream saida) throws IOException {
        File file = new File("arquivos");
        afile = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            saida.writeObject(new Mensagem(i+ " - " + arquivos.getName(),""));
            }
    }



    public void enviaarquivo(ObjectOutputStream saida,int pos) throws IOException {
        File myFile = new File("arquivos/"+afile[pos].getName());
        byte[] bFile = new byte[(int) myFile.length()];
        FileInputStream fis = new FileInputStream(myFile);
        fis.read(bFile);
        fis.close();
        Mensagem msg=new Mensagem("", "",afile[pos].getName(),bFile);
        saida.writeObject(msg);
    }

    public void recebearquivo(Mensagem msg,String nick,int porta) throws IOException 
    {

        File file = new File("arquivos/"+msg.getNomearq());
        FileOutputStream in= new FileOutputStream(file);
        in.write(msg.getarquivo());
        in.close();
        System.out.println("Arquivo "+msg.getNomearq()+" recebido enviado por Cliente "+nick+" Porta "+porta);
    }

    public void finaliza() throws IOException {

        for (int i = 0; i < cliente.size(); i++) {
            cliente.get(i).finaliza();
            sock.get(i).close();
        }
        servidor.close();
    }

    public void enviaMensagemTodos(Mensagem msg) throws IOException {
        for (int i = 0; i < cliente.size(); i++) {
            cliente.get(i).saida().writeObject(msg);
        }
    }

    public void listaclientes() {
        for (int i = 0; i < cliente.size(); i++) {
            System.out.println("Cliente: " + cliente.get(i).getNick() + " Porta: " + cliente.get(i).getendereco());
        }
    }

    public void listaclientesforclientes(ObjectOutputStream saida) throws IOException {
        if(!cliente.isEmpty())
        {
        for (int i = 0; i < cliente.size(); i++) {
            saida.writeObject(new Mensagem("Cliente: " + cliente.get(i).getNick() + " Porta: " + cliente.get(i).getendereco(),""));
        }
        }
        else
        {
            saida.writeObject(new Mensagem("Não há clientes conectados",""));
        }
    }

    public void desconectaclientes(int portdel){
        for (int i = 0; i < cliente.size(); i++) {
        if(cliente.get(i).getendereco()==portdel)
        {
            cliente.removeElementAt(i);
        }
    }
    }

    public int getPorta() {
        return porta;
    }

    public void corta() {
        corta = false;
    }

    @Override
    public void run() {
        while (corta) {
            try {
                esperaConexoes();
            } catch (IOException exception) {
            }
        }
    }

    public static void main(String[] args) throws IOException {

        boolean fim = false;
        Scanner tecl = new Scanner(System.in);
        String msg;
        System.out.println("Mensagem enviada para todos!");
        AplicacaoServidor server = new AplicacaoServidor(12345);

        System.out.println("Porta 12345 aberta!");
        
        server.start();

        do {
            System.out.println("Digite:\n1-Mostrar lista de clientes conectados\n2-Mostar lista de arquivos do servidor \n3-Fechar servidor \n");
            msg = tecl.nextLine();
            if (msg.equals("1")) 
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                server.listaclientes();
                tecl.nextLine();
            }
            else if (msg.equals("2"))
            {
                System.out.print("\033[H\033[2J");
                System.out.flush();
                server.listadearquivos();
                tecl.nextLine();
            } 
            else if (msg.equals("3")) 
            {
                fim = true;

            }
            System.out.print("\033[H\033[2J");
            System.out.flush();

        } while (!fim);

        tecl.close();
        server.finaliza();
        server.corta();

        return;

    }

}

class Cliente extends Thread {
    boolean firstmsg = true;
    private String nick;
    private Socket sock;
    private boolean fim;
    private AplicacaoServidor server;
    private ObjectInputStream streamEntrada;
    private ObjectOutputStream streamSaida;


    public Cliente(Socket sock, AplicacaoServidor server) throws IOException {
        this.sock = sock;
        this.server = server;
        fim = false;

        streamSaida = new ObjectOutputStream(sock.getOutputStream());
        streamEntrada = new ObjectInputStream(sock.getInputStream());
    }

    public void finaliza() {
        fim = true;
    }

    public ObjectInputStream entrada() {
        return streamEntrada;
    }

    public int getendereco() {
        return sock.getPort();
    }

    public void solicitaarquivo(int pos) throws IOException {
        
        server.enviaarquivo(saida(),pos);
    }

    public void solicitalistaclientes() throws IOException
    {
        server.listaclientesforclientes(saida());
    }

    public void solicitalista() throws IOException
    {
        server.listadearquivoscliente(saida());
    }
    public void solicitaupload(Mensagem msg) throws IOException
    {
        server.recebearquivo(msg,nick,getendereco());
    }

    public String getNick() {
        return nick;
    }

    public ObjectOutputStream saida() {
        return streamSaida;
    }


    public void run() {

        try {
            Mensagem msg;

            while (!fim) {
                msg = (Mensagem) streamEntrada.readObject();
                if (firstmsg) {
                    // pegando nick
                    nick = msg.getDestinatario();
                    // System.out.println(" "+nick+" ");
                    firstmsg = false;
                    System.out.println("Nova conexão com o cliente "+getNick()+" Porta " + getendereco());

                } 
                else if(msg.getRemetente().equals(nick)&&msg.getMensagem().equals("l1st4rq"))
                {
                   
                    solicitalista();

                }
                else if(msg.getRemetente().equals(nick)&&(msg.getMensagem().equals("s0liarq")))
                {
                 
                    solicitaarquivo(msg.getOpcao());


                }
                else if(msg.getRemetente().equals(nick)&&(msg.getMensagem().equals("l1st4cl1e7")))
                {
                        solicitalistaclientes();
                }
                else if(msg.getRemetente().equals(nick)&&(msg.getMensagem().equals("up4rqu1")))
                {
                        solicitaupload(msg);
                }
                else {
                    server.enviaMensagemTodos(msg);
                    System.out.println("Mensagem enviada para todos!");
                }
            }
        } catch (Exception e) {
        System.err.println("O Cliente "+nick+" Porta "+getendereco() +" se desconectou");
        server.desconectaclientes(getendereco());

        }
    }
}

class teclado extends Thread {
    boolean fim = false;
    Scanner tecl = new Scanner(System.in);
    String msg;

    public teclado() {

    }

    @Override
    public void run() {
        System.out.println("Digite fim para sair: ");
        msg = tecl.nextLine();
        if (msg.equals("fim") || msg.equals("FIM")) {
            fim = true;

        }

    }

    public boolean getfim() {
        return fim;
    }

    public void close() {
        tecl.close();
    }

}
