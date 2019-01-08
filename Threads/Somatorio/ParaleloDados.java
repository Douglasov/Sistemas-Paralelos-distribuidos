import java.util.Scanner;
import java.math.BigDecimal;
import java.math.RoundingMode;

/*
 * Algoritmo Paralelo em Dados
 * Calculo do somatorio (1! + 2! + 3! + ... + n!) + (2^1 + 2^2 + ... + 2^n)
 */
public class ParaleloDados extends Thread {
	private BigDecimal inicio; //inicio do intervalo de processamento
	private BigDecimal fim; //fim do intervalo de processamento
	private BigDecimal resultado; //resultado do processamento
	private BigDecimal menos1=new BigDecimal("-1");
	private BigDecimal dois=new BigDecimal("2");
	private BigDecimal x=new BigDecimal("9");//x
	/*
	 * Construtor padrao
	 */
	public ParaleloDados(){
		this.inicio = BigDecimal.ZERO;
		this.fim = BigDecimal.ZERO;
		resultado = BigDecimal.ZERO;
	}

	/*
	 * Define o inicio e o fim do intervalo de dados para processamento
	 */
	public void DefineIntervalo(BigDecimal inicio, BigDecimal fim){
		this.inicio = inicio;
		this.fim = fim;
		resultado = BigDecimal.ZERO;
	}

	/*
	 * Algoritmo executado pela thread
	 * @see java.lang.Thread#run()
	 */
	public void run() {				
		resultado = BigDecimal.ZERO;
		
		for(BigDecimal i = inicio; i.compareTo(fim) != 0; i = i.add(BigDecimal.ONE)) {
			resultado = resultado.add(         (x.pow(i.intValue()))           .divide(           Util.Fatorial(i),6,RoundingMode.UP));
			resultado = resultado.add(         (((menos1).pow(i.intValue())).multiply(x.pow((i.intValue()*2))) ).divide(Util.Fatorial(i.multiply(dois)),6,RoundingMode.UP)      );
			resultado = resultado.add(         (((menos1).pow(i.intValue())).multiply(x.pow(((i.intValue()*2)+1))) ).divide(Util.Fatorial((i.multiply(dois)).add(BigDecimal.ONE)),6,RoundingMode.UP)      );
	
		}
		
	}
	
	/*
	 * Retorna o resultado do processamento
	 */
	public BigDecimal Resultado() {
		return resultado;
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		Scanner teclado = new Scanner(System.in);
		int numThreads;
		BigDecimal n, resultado, passo, tamanho, posicao;
		long tempo1, tempo2; 
		
		
		n = BigDecimal.ZERO;
		passo = new BigDecimal("2000"); 
		
		System.out.println("Entre com a quantidade de threads: ");
		numThreads = teclado.nextInt();
		
		teclado.close();
		
		ParaleloDados[] threads = new ParaleloDados[numThreads]; 
		
		//Calcula o somatorio para os valores 2000, 4000, 6000, 8000, 10000
		for(int i = 0; i < 5; i++){
			n = n.add(passo);
			tamanho = n.divide( BigDecimal.valueOf(numThreads) );
			
			posicao = BigDecimal.ONE;
			resultado = BigDecimal.ZERO;

			//cria as threads
			for(int j = 0; j < numThreads; j++) 
				threads[j] = new ParaleloDados();
			
			tempo1 = System.nanoTime();
			
			//divide os dados entre as (numThreads - 1) threads
			for(int j = 0; j < numThreads - 1; j++) {
				threads[j].DefineIntervalo(posicao, posicao.add(tamanho));
				posicao = posicao.add(tamanho);
			}
			//atribui o restante dos dados para a ultima thread 
			//resolve a questao da divisao inteira
			threads[numThreads - 1].DefineIntervalo(posicao, n);
		
			//inicia as threads
			for(int j = 0; j < numThreads; j++)
				threads[j].start();
			
			//define um ponto de sincronismo (barreira) 
			//aguarda o termino do processamento de todas as threads
			for(int j = 0; j < numThreads; j++)
				threads[j].join();
			
			//junta os resultados das threads
			for(int j = 0; j < numThreads; j++)
				resultado = resultado.add(threads[j].Resultado());
			
			tempo2 = System.nanoTime();
			
			System.out.print("N = " + n.toString());
			System.out.print("\tResultado = " + resultado);
			System.out.println("\tTempo = " + String.valueOf((tempo2 - tempo1)/1000000) + " ms");
		}
	}

}
