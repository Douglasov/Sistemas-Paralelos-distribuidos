import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;


public class Servidor extends UnicastRemoteObject implements IExemplo {
	private int contador;
	private File afile[];
	
	public Servidor() throws RemoteException { 
		super();
		contador = 0;
		preenchelista();
	} 

	public void preenchelista() {
        File file = new File("arquivos");
        afile = file.listFiles();
	}
	
	public Vector envialistadearquivos() {
        File file = new File("arquivos");
		afile = file.listFiles();
		Vector<String> listadearquivos= new Vector<>();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
			File arquivos = afile[i];
            listadearquivos.add(arquivos.getName());
			}
			return listadearquivos;
    }

	public void modifica(int n) {
		contador += n;
		System.out.println("Valor da alteração: " + n );
		
	}


	public int valor() {
		return contador;
	}
	
	public Mensagem enviaarquivo(int arqnum) {
		Mensagem msg=null;
		try{
        File myFile = new File(("arquivos/"+afile[arqnum].getName()));
        byte[] bFile = new byte[(int) myFile.length()];
        FileInputStream fis = new FileInputStream(myFile);
        fis.read(bFile);
        fis.close();
		msg=new Mensagem(afile[arqnum].getName(), bFile);
		return msg;
		}
		catch(Exception e)
		{
			return msg;
		}
		
	}

	public void recebearq(Mensagem msg)
	{
		try
		{
		File file = new File("arquivos/"+msg.getNomearq());
		FileOutputStream in= new FileOutputStream(file);
		in.write(msg.getarquivo());
		in.close();
		System.out.println("Arquivo "+msg.getNomearq()+" recebido");
		}
		catch(Exception e)
		{
			
		}
	}

	public static void main(String[] args) {
		try {
			//configuracao da JVM para testar em maquinas diferentes
			System.setProperty("java.rmi.server.hostname","127.0.0.1");

			Servidor server = new Servidor();
			String endereco = "//localhost/Contador";
			System.out.println("Registering " + endereco + "...");
			Naming.rebind(endereco, server);
			System.out.println("Registrado!");
		} catch (RemoteException e) {
			System.err.println("Erro durante o registro do objeto! " + e);
			e.printStackTrace();
			System.exit(1); 
		} catch (MalformedURLException e) {
			System.err.println("URL invalida! " + e);
			e.printStackTrace();
			System.exit(2);
		} 

	} 
}
