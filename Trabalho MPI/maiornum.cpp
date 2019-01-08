#include <stdio.h>
#include <stdlib.h>
#include <iostream>
#include "mpi.h"

using namespace std;

int main(int argc, char *argv[])
{
    //size e rank do mpi
    int size, rank;
    clock_t inicio, fim, duracao;
    int tamvet = 100, *vet = new int[tamvet], divisao, divisaofim, sobra;

    //preenchendo os vetores onde o maior vai ser o 200 e seundo maior vai ser 196
    for (int i = 0; i < tamvet; i++)
    {
        if(i%2==0)
        vet[i] = i + 1;
        else
        vet[i] = (i + 1)*2;
        //cout<<vet[i];
    }

    //MPI
    MPI_Status status;
    //inicnado ambiente de troca de mensagens recebendo quantos o numero de processos envolvidos
    MPI_Init(&argc, &argv);
    //atrelando a size o numero de processos envolvidos
    MPI_Comm_size(MPI_COMM_WORLD, &size);
    //atrelando a rank o qual processo esta sendo executado
    MPI_Comm_rank(MPI_COMM_WORLD, &rank);
    inicio = clock();
    //vetor que irá os maiores numeros achados em cada processo
    int *maior = new int[size];
    //vetor que irá os segundo maiores numeros achados em cada processo
    int *Segmaior = new int[size];

    //encontrando o numero de posiçoes que cada processo ira verificar
    divisao = tamvet / size;
    sobra = tamvet % size;
    //caso numero de posições que cada processo ira verificar não seja igual
    if (sobra != 0 && rank == (size - 1))
    {
        divisaofim = (divisao * (rank + 1)) + sobra;
    }
    else
    {
        divisaofim = divisao * (rank + 1);
    }


    //verificação de qual numero é maior numero e o segundo maior em cada processo
    for (int i = divisao * rank; i < divisaofim; i++)
    {

        if ((i == divisao * rank) || vet[i] > maior[rank])
        {
            Segmaior[rank] = maior[rank];
            maior[rank] = vet[i];
        }
    }

    //verifica se processo é escravo
    if (rank != 0)
    {
        //envia uma mensagem que inicia no endereço &maior[i] com tamanho 1, a variavel da mensagem é do tipo MPI_INT
        //A mensagem é enviada para o processo de rank0(processo mestre) com a tag de identificação rank utilizando o canal de comunucação MPI_COMM_WORLD
        MPI_Send(&maior[rank], 1, MPI_INT, 0, rank, MPI_COMM_WORLD);
        //envia uma mensagem que inicia no endereço &Segmaior[i] com tamanho 1, a variavel da mensagem é do tipo MPI_INT
        //A mensagem é enviada para o processo de rank0(processo mestre) com a tag de identificação rank+size utilizando o canal de comunucação MPI_COMM_WORLD
        MPI_Send(&Segmaior [rank], 1, MPI_INT, 0, rank + size, MPI_COMM_WORLD);
    }

    //verifica se processo é mestre
    if (rank == 0)
    {
        for (int i = 1; i < size; i++)
        {
            //recebe uma mensagem que inicia no endereço &maior[i] com tamanho 1, a variavel da mensagem é do tipo MPI_INT
            //A mensagem recebida tem como origem o processo de i(processos escravos) com a tag de identificação i utilizando o canal de comunucação MPI_COMM_WORLD
            MPI_Recv(&maior[i], 1, MPI_INT, i, i, MPI_COMM_WORLD, &status);
            //recebe uma mensagem que inicia no endereço &2maior[i] com tamanho 1, a variavel da mensagem é do tipo MPI_INT
            //A mensagem recebida tem como origem o processo de i(processos escravos) com a tag de identificação i+size utilizando o canal de comunucação MPI_COMM_WORLD
            MPI_Recv(&Segmaior [i], 1, MPI_INT, i, i + size, MPI_COMM_WORLD, &status);
        }
    }

    //barreira de sincronismo bloqueia a execução da programa até que todos os processos tenham se comunicado
    MPI_Barrier(MPI_COMM_WORLD);

    //verifica se processo é mestre
    if (rank == 0)
    {
        int maiorT = 0;
        int maiorsegT=0;
        int SegmaiorT = 0;
        for (int j = 0; j < size; j++)
        {
            //encontra o maior e o segundo maior numero no vetor maior[]
            if (maior[j] > maiorT || j == 0)
            {
                maiorsegT=maiorT;
                maiorT = maior[j];
            }
            //encontra o maior numero no vetor Segmaior[]
            if (Segmaior[j] > SegmaiorT || j == 0)
            {
                SegmaiorT = Segmaior [j];
            }
        }

        //encontra qual é maior entre o segundo maior numero do vetor maior[] e o maior numero do vetor Segmaior
        if(SegmaiorT<maiorsegT)
        {
            SegmaiorT=maiorsegT;
        }
        cout <<"Maior:"<< maiorT<<" Segundo Maior:"<<SegmaiorT;
        fim = clock();
        duracao = fim - inicio;
        cout << "\n duração:" << duracao << "ms\n";
    }
    MPI_Finalize();
    return 0;
}