import java.rmi.Remote;
import java.rmi.RemoteException;
import java.io.File;
import java.util.*;


public interface IExemplo extends Remote
{
	void modifica(int n) throws RemoteException;
	int valor() throws RemoteException;
	Mensagem enviaarquivo(int arqnum) throws RemoteException;
	void recebearq(Mensagem msg) throws RemoteException;
	Vector envialistadearquivos() throws RemoteException;
	
} 


