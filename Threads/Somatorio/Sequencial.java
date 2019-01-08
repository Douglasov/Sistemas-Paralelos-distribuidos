import java.math.BigDecimal;
import java.math.RoundingMode;



public class Sequencial {
	

	public BigDecimal Somatorio(BigDecimal n,BigDecimal x) {		
		BigDecimal soma = BigDecimal.ZERO;
		BigDecimal menos1=new BigDecimal("-1");
	    BigDecimal dois=new BigDecimal("2");
		
		for(BigDecimal i = BigDecimal.ZERO; i.compareTo(n) != 0; i = i.add(BigDecimal.ONE)) 
		{
			soma = soma.add(         (x.pow(i.intValue()))           .divide(           Util.Fatorial(i),6,RoundingMode.UP));
			soma = soma.add(         (((menos1).pow(i.intValue())).multiply(x.pow((i.intValue()*2))) ).divide(Util.Fatorial(i.multiply(dois)),6,RoundingMode.UP)      );
			soma = soma.add(         (((menos1).pow(i.intValue())).multiply(x.pow(((i.intValue()*2)+1))) ).divide(Util.Fatorial((i.multiply(dois)).add(BigDecimal.ONE)),6,RoundingMode.UP)      );
		}
		
		return soma;
	}
	
	public static void main(String[] args) {
		BigDecimal n, resultado, passo;
		BigDecimal x=new BigDecimal("9");
		long tempo1, tempo2; 
		Sequencial alg = new Sequencial();
		
		n = BigDecimal.ZERO;
		passo = new BigDecimal("2000");
		
		//Calcula o somatorio para os valores 2000, 4000, 6000, 8000, 10000
		for(int i = 0; i < 5; i++){
			n = n.add(passo)
					;
			tempo1 = System.nanoTime(); 
			resultado = alg.Somatorio( n,x );
			tempo2 = System.nanoTime(); 
			
			System.out.print("N = " + n.toString());
			System.out.println("\tTempo = " + String.valueOf((tempo2 - tempo1)/1000000) + " ms");
		}
		
	}

}
