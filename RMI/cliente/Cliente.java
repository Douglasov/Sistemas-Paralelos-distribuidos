import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.net.MalformedURLException;
import java.util.Random;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.util.*;
import java.io.*;


public class Cliente
{

	private static File afile[];

	public Cliente()  { 

		preenchelista();
	} 

	public void preenchelista() {
        File file = new File("arquivos");
        afile = file.listFiles();
	}

	public static void listadearquivos() {
		System.out.println("Lista de Arquivos Deste Cliente");
        File file = new File("arquivos");
		afile = file.listFiles();
        int i = 0;
        for (int j = afile.length; i < j; i++) {
			File arquivos = afile[i];
            System.out.println(i+"-"+arquivos.getName());
			}
    }

	public static void recebearq(Mensagem msg)
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



	public static Mensagem makearqmsg(int Arqnum) {
		Mensagem msg=null;
		try{
        File myFile = new File("arquivos/"+afile[Arqnum].getName());
        byte[] bFile = new byte[(int) myFile.length()];
        FileInputStream fis = new FileInputStream(myFile);
        fis.read(bFile);
        fis.close();
		msg=new Mensagem(afile[Arqnum].getName(), bFile);
		return msg;
		}
		catch(Exception e)
		{
			return msg;
		}

		
		
	}

	public static void listaarquivos(Vector<String> listaarq)
		{
			System.out.println("Lista de Arquivos no Servidor:");
			for(int i=0;i<listaarq.size();i++)
			{
				System.out.println(i+"-"+listaarq.get(i));
			}
		}

	public static void main(String[] args) {
		int val, n;
		String op="-0";
		IExemplo server = null;
		Random rand = new Random();
		Boolean fim=false;
		InputStreamReader streamTeclado = new InputStreamReader(System.in);
		BufferedReader teclado = new BufferedReader(streamTeclado);
		
		try {
			System.out.print("\033[H\033[2J");
			String endereco = "//localhost/Contador";
			System.out.println("Localizando o objeto " + endereco);
			server = (IExemplo) Naming.lookup(endereco);			
			System.out.println("Conectado");
			System.out.println("\nAperte qualquer tecla para continuar");

			//recebe arquivos
			//recebearq(server.enviaarquivo());
			//envia arquivos
			//server.recebearq(makearqmsg());
			//listaarquivos(server.envialistadearquivos());

			teclado.readLine();
			do {
				
				System.out.print("\033[H\033[2J");
				System.out.println("Digite: \n1-Solicitar lista de arquivos do servidor\n2-Solicitar lista de arquivos deste cliente \n3-Baixar um arquivo do servidor \n4-Enviar um arquivo para o servidor \n5-Desconectar");
				op = teclado.readLine();
				
				if(op.equals("1"))
				{
					System.out.print("\033[H\033[2J");
					listaarquivos(server.envialistadearquivos());
					System.out.println("\nAperte qualquer tecla para continuar");
					teclado.readLine();
				}
				else if(op.equals("2"))
				{
					System.out.print("\033[H\033[2J");
					listadearquivos();
					System.out.println("\nAperte qualquer tecla para continuar");
					teclado.readLine();
				}
				else if(op.equals("3"))
				{
					System.out.print("\033[H\033[2J");
					listaarquivos(server.envialistadearquivos());
					System.out.println("Digite o número do arquivo que deseja baixar");
					op = teclado.readLine();
					recebearq(server.enviaarquivo(Integer.parseInt(op)));
					System.out.println("\nAperte qualquer tecla para continuar");
					teclado.readLine();
				}
				else if(op.equals("4"))
				{
					System.out.print("\033[H\033[2J");
					listadearquivos();
					System.out.println("Digite o número do arquivo que deseja baixar");
					op = teclado.readLine();
					server.recebearq(makearqmsg(Integer.parseInt(op)));	
					System.out.println("\nAperte qualquer teclara para continuar");
					teclado.readLine();				
				}
				else if(op.equals("5"))
				{
					fim = true;  
				}           
					
			} while (!fim);
		} catch (NotBoundException e) {
			System.err.println("Problema ao locarlizar o objeto remoto " + e);
			e.printStackTrace();
			System.exit(1);
		} 
		catch (RemoteException e) {
			System.err.println("Falha durante a chamada do procedimento remoto! " + e);
			e.printStackTrace();
			System.exit(2);
		} catch (MalformedURLException e) {
			System.err.println("URL invalida!\n" + e);
			e.printStackTrace();
			System.exit(3);
		} 
		catch (IOException e) {
			System.err.println("Teclado deu ruim " + e);
			e.printStackTrace();
			System.exit(1);
		}
 
	} 
} 
