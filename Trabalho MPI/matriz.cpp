#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include "mpi.h"


using namespace std;



int main(int argc, char *argv[] )
{
    //size e rank do mpi
    int size, rank;
    clock_t inicio, fim, duracao;
    //matrizes e suas ordens
    int **mat1, **mat2, **result, tammat = 5;

    //alocando matrizes
    mat1 = new int *[tammat];
    mat2 = new int *[tammat];
    result = new int *[tammat];
    for (int i = 0; i < tammat; i++)
    {
        mat1[i] = new int[tammat];
        mat2[i] = new int[tammat];
        result[i] = new int[tammat];
    }


    //preenchendo
    for (int i = 0; i < tammat; i++)
    {
        for (int j = 0; j < tammat; j++)
        {
            mat1[i][j] = 2;
            mat2[i][j] = 2;
            result[i][j] = 0;
        }
    }

    //MPI
    MPI_Status status;
    //inicnado ambiente de troca de mensagens recebendo quantos o numero de processos envolvidos
    MPI_Init(&argc, &argv);
    //atrelando a size o numero de processos envolvidos
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    //atrelando a rank o qual processo esta sendo executado
	MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    inicio=clock();


    //dividindo a tarefa da multiplicação entre os processos
    for (int i = rank; i < tammat; i = i + size)
	{
		for (int j = 0; j < tammat; j++)
		{
			for (int m = 0; m < tammat; m++)
			{
				result[i][j] =result[i][j]+ mat1[i][m]+mat1[m][j];
			}
		}
	}

    //verifica se processo é escravo
    if (rank != 0)
	{
        //envia a mensagem de cada processo escravo conforme a divisão das tarefas feita na multiplicação para o processo mestre
        for (int i = rank; i < tammat; i = i + size)
        {
                //envia uma mensagem que inicia no endereço &result[i][0] com tamanho tammat, a variavel da mensagem é do tipo MPI_INT 
                //A mensagem é enviada para o processo de rank0(processo mestre) com a tag de identificação i utilizando o canal de comunucação MPI_COMM_WORLD
            	MPI_Send(&result[i][0], tammat, MPI_INT,0,i, MPI_COMM_WORLD);
        }
    }

    //verifica se processo é mestre
    if (rank == 0)
	{
        //o processo mestre o recebe a mensagem de cada processo escravo conforme a divisão das tarefas feita na multiplicação
		for (int j = 1; j < size; j++)
		{
			for (int i = j; i < tammat; i = i + size)
			{
                //recebe uma mensagem que inicia no endereço &result[i][0] com tamanho tammat, a variavel da mensagem é do tipo MPI_INT 
                //A mensagem recebida tem como origem o processo de rank[i](processos escravos) com a tag de identificação i utilizando o canal de comunucação MPI_COMM_WORLD
				MPI_Recv(&result[i][0], tammat, MPI_INT, j,i, MPI_COMM_WORLD, &status);
			}
		}
	}

    //barreira de sincronismo bloqueia a execução da programa até que todos os processes tenham se comunicado
    MPI_Barrier(MPI_COMM_WORLD);

    //verifica se processo é mestre
    if (rank == 0)
    {
        for (int i = 0; i < tammat; i++)
    {
        for (int j = 0; j < tammat; j++)
        {
            cout<<" "<<result[i][j]<<" ";
        }
        cout<<endl;
    }
    fim=clock();
    duracao=fim-inicio;
    cout<<"\n duração:"<<duracao<<"ms\n";

    }
    MPI_Finalize();
    return 0;

}
