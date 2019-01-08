import java.math.BigDecimal;

/*
 * Funcoes matematicas utilitarias
 */
public class Util {

	/*
	 * Funcao para calcular o fatorial de um numero qualquer n
	 */
	public static BigDecimal Fatorial(BigDecimal n) {
		BigDecimal soma = BigDecimal.ONE;
		
		//calculo de 0!
		if (n.compareTo(BigDecimal.ZERO) == 0)
		{
		return soma; 
		}
		else{
		//calculo de n!
		for(BigDecimal i = BigDecimal.ONE; i.compareTo(n.add(BigDecimal.ONE)) != 0; i = i.add(BigDecimal.ONE))
		{
		soma = soma.multiply(i);
		}
		return soma;
		}
	}
	
	/*
	 * Funcao para calcular a potencia de 2^n
	 */
	public static BigDecimal Potencia2(BigDecimal n) {
		BigDecimal soma = BigDecimal.ONE;
		
		//calculo de 2^0 
		if (n.compareTo(BigDecimal.ZERO) == 0) return soma;
		
		//calculo de 2^n 
		for(BigDecimal i = BigDecimal.ONE; i.compareTo(n) != 0; i = i.add(BigDecimal.ONE))
			soma = soma.multiply(BigDecimal.valueOf(2));
		
		return soma;
	} 

}
