import java.math.BigDecimal;
import java.math.RoundingMode;


public class Sequencial {

	public void multiplica(int linha1,int coluna1,int linha2,int coluna2,int matA[][],int matB[][],int matresp[][]) {		
		

		for (int i=0; i<linha1; i++)
		{
			for (int j=0 ; j<coluna2; j++)
			{
			for(int k=0 ; k<linha2 ; k++)
			matresp[i][j] += matA[i][k]*matB[k][j];
			}   
		 }
		
	 }

	public static void main(String[] args) {
		long tempo1, tempo2;
		Sequencial mult = new Sequencial();
		int linha1=1000;
		int coluna1=1000;
		int linha2=coluna1;//LxN*NxC
		int coluna2=1000;
		int matA[][]=new int[linha1][coluna1];
		int matB[][]=new int[linha2][coluna2];
		int matresp[][]=new int[linha1][coluna2];

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


			tempo1 = System.nanoTime();


			mult.multiplica(linha1,coluna1,linha2,coluna2,matA,matB,matresp);

			tempo2 = System.nanoTime();

			//System.out.print("Matriz Resultante\n");
			for (int l = 0; l<linha1; l++) {
				for (int c = 0; c<coluna2; c++) {
					//System.out.print(matresp[l][c]+ " ");
				}
				//System.out.println();
			}

			System.out.println("\tTempo = " + String.valueOf((tempo2 - tempo1) / 1000000) + " ms");


	}

}
