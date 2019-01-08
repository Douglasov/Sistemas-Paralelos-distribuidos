import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;


public class ParaleloDados extends Thread {
	private int iniciolinha;
	private int fimlinha;
	private int linha1;
	private int coluna1;
	private int linha2;
	private int coluna2;
	private int matA[][];
	private int matB[][];
	private int matresp[][];
	/*
	 * Construtor padrao
	 */
	public ParaleloDados(int linha1,int coluna1,int linha2,int coluna2,int matA[][],int matB[][],int matresp[][]){
		this.linha1=linha1;
		this.coluna1=coluna1;
		this.linha2=linha2;
		this.coluna2=coluna2;
		this.matA=matA;
		this.matB=matB;
		this.matresp=matresp;
	}

	/*
	 * Define o inicio e o fim do intervalo de dados para processamento
	 */
	public void DefineIntervalo(int iniciolinha, int fimlinha){
		this.iniciolinha = iniciolinha;
		this.fimlinha = fimlinha;
	}


	public void run() {				


			for (int i=iniciolinha; i<fimlinha; i++)
			{
    			for (int j=0 ; j<coluna2; j++)
    			{
        		for(int k=0 ; k<linha2 ; k++)
				matresp[i][j] += matA[i][k]*matB[k][j];
				}   
			 }
		
	}
	
	
	
	public static void main(String[] args) throws InterruptedException {
		Scanner teclado = new Scanner(System.in);
		long tempo1, tempo2;
		int linha1=1000;
		int coluna1=1000;
		int linha2=coluna1;//LxN*NxC
		int coluna2=1000;
		int matA[][]=new int[linha1][coluna1];
		int matB[][]=new int[linha2][coluna2];
		int matresp[][]=new int[linha1][coluna2];
		int numThreads;

		//System.out.print("matA\n");
		for (int l = 0; l<linha1; l++) {
			for (int c = 0; c<coluna1; c++) {
				matA[l][c] = (int)(Math.random()*100);
				//System.out.print(matA[l][c]+ " ");
			}
			//System.out.println();
		}
		
		//System.out.println();
		//System.out.println();

		//System.out.print("matB\n");
		for (int l = 0; l<linha2; l++) {
			for (int c = 0; c<coluna2; c++) {
				matB[l][c] = (int)(Math.random()*100);
				//System.out.print(matB[l][c]+ " ");
			}
			//System.out.println();
		}
		//System.out.println();
		//System.out.println();

		
		System.out.println("Entre com a quantidade de threads: ");
		numThreads = teclado.nextInt();
		
		teclado.close();
		
		//caso o numero de threads seja maior que o de linhas da matriz resultante
		if(numThreads>linha1)
		{
			numThreads=linha1;
		}

		//criando as threads
		ParaleloDados[] threads = new ParaleloDados[numThreads];
		

		for (int l = 0; l<numThreads; l++)
		threads[l]= new ParaleloDados(linha1,coluna1,linha2,coluna2,matA,matB,matresp);

			
			tempo1 = System.nanoTime();
			

			//definindo o intervalo em cada thread vai executar
			int divisao=linha1/numThreads,restdivisao=linha1%numThreads;

			
			for(int j = 0; j < numThreads ; j++) {
				if((restdivisao)==0)
				threads[j].DefineIntervalo((divisao)*j, (divisao)*(j+1));
				else
				{
					if(j==numThreads-1)
					threads[j].DefineIntervalo((divisao)*j, (((divisao)*(j+1))+(restdivisao)));
					else
					threads[j].DefineIntervalo((divisao)*j, (divisao)*(j+1));
				}

			}
		
			//inicia as threads
			for(int j = 0; j < numThreads; j++)
				threads[j].start();
			
			//define um ponto de sincronismo (barreira) 
			//aguarda o termino do processamento de todas as threads
			for(int j = 0; j < numThreads; j++)
				threads[j].join();
			
			
			tempo2 = System.nanoTime();

			// System.out.print("Matriz Resultante\n");
			// for (int l = 0; l<linha1; l++) {
			// 	for (int c = 0; c<coluna2; c++) {
			// 		System.out.print(matresp[l][c]+ " ");
			// 	}
			// 	System.out.println();
			// }


			System.out.println("\tTempo = " + String.valueOf((tempo2 - tempo1)/1000000) + " ms");
		
	}

}
