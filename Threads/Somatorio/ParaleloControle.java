import java.math.BigDecimal;
import java.math.RoundingMode;

public class ParaleloControle extends Thread {			
	private BigDecimal n; //numero de elementos para processamento
	private BigDecimal x;
	private BigDecimal menos1;
	private BigDecimal dois;
	private BigDecimal resultado; //resultado do processamento
	private int tarefa; //tarefa realizada pela thread
	
	/*
	 * Construtor padrao 
	 */
	public ParaleloControle(BigDecimal n,BigDecimal x, int tarefa){
		this.n = n;
		this.tarefa = tarefa;
		this.x=x;
		menos1=new BigDecimal("-1");
	    dois=new BigDecimal("2");
		resultado = BigDecimal.ZERO;
	}
		
	/*
	 * Algoritmo executado pela thread
	 * @see java.lang.Thread#run()
	 */
	public void run() {				
		resultado = BigDecimal.ZERO;
		
		if (tarefa == 0) { //exponencial
		
			for(BigDecimal i = BigDecimal.ZERO; i.compareTo(n) != 0; i = i.add(BigDecimal.ONE))
				{
					resultado = resultado.add(       (x.pow(i.intValue()))           .divide(           Util.Fatorial(i),6,RoundingMode.UP));
				}
		
		}
		else if(tarefa == 1) { //cos

			for(BigDecimal i = BigDecimal.ZERO; i.compareTo(n) != 0; i = i.add(BigDecimal.ONE)) 
				resultado = resultado.add(         (((menos1).pow(i.intValue())).multiply(x.pow((i.intValue()*2))) ).divide(Util.Fatorial(i.multiply(dois)),6,RoundingMode.UP)      );		
			
		}
		else //sen
		{
			for(BigDecimal i = BigDecimal.ZERO; i.compareTo(n) != 0; i = i.add(BigDecimal.ONE))
			{
			resultado = resultado.add(         (((menos1).pow(i.intValue())).multiply(x.pow(((i.intValue()*2)+1))) ).divide(Util.Fatorial((i.multiply(dois)).add(BigDecimal.ONE)),6,RoundingMode.UP)      );
			}
		}
		
	}
	
	/*
	 * Retorna o resultado do processamento
	 */
	public BigDecimal Resultado() {
		return resultado;
	}
	
	public static void main(String[] args) throws InterruptedException {
		BigDecimal n,x, resultado, passo;
		long tempo1, tempo2; 
		
		n = BigDecimal.ZERO;
		passo = new BigDecimal("2000");
		//X
		x=new BigDecimal("9");
				
		ParaleloControle thread0, thread1,thread2;
		
		//Calcula o somatorio para os valores 2000, 4000, 6000, 8000, 10000
		for(int i = 0; i < 5; i++){
			n = n.add(passo);
			resultado = BigDecimal.ZERO;
		
			//cria as threads
			thread0 = new ParaleloControle(n , x,0);
			thread1 = new ParaleloControle(n , x,1);
			thread2 = new ParaleloControle(n , x,2);

			tempo1 = System.nanoTime();

			//inicia as threads
			thread0.start();
			thread1.start();
			thread2.start();
					
			//define um ponto de sincronismo (barreira) 
			//aguarda o termino do processamento de todas as threads
			thread0.join();
			thread1.join();
			thread2.join();
			
			//junta os resultados das threads
			resultado = resultado.add(thread0.Resultado());
			resultado = resultado.add(thread1.Resultado());
			resultado = resultado.add(thread2.Resultado());
			
			tempo2 = System.nanoTime();
			
			System.out.print("N = " + n.toString());
			System.out.println("\tTempo = " + String.valueOf((tempo2 - tempo1)/1000000) + " ms");
		}
	}

}
