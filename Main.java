import Problem.Description.*;
import Problem.*;

import java.util.*;

class Main
{	
	private static final int SIZE = 1;
    
	public static void main_ORIGINAL(String args[])
    {
		if(args.length != 1)
		{
			System.out.println("Um parametro eh necessario.");
			System.out.println("Ex.: Main [arquivo de entrada]");
			return;
		}
		
		//Arquivo arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
		Arquivo arquivoEntrada = null;
		Arquivo arquivoSaida = new Arquivo(args[0].substring(0, args[0].length()-3) + "txt", Arquivo.ESCRITA);
		
		Cronometro cT = new Cronometro();	
		Problem gW = null;
		
		System.out.println();
		
		LAO lao = null;
		double averageTime = 0;
		for(int i = 0; i < SIZE; i++)
		{
			arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
			LeGridWorld lGW = new LeGridWorld(arquivoEntrada);
			
			System.out.println("Lendo Arquivo... " + args[0]);
			gW = lGW.executa();
			System.out.println("Leitura Concluida!");
			lao = new LAO(gW);
			
			System.out.println("\nExecutando LAO*...");
			cT.start();
			lao.executa(LAO.VALUE);
			cT.stop();
			
			averageTime += cT.getTempo();
			
			System.out.println("\nExecucao " + (i+1) + " de " + SIZE);
			System.out.println("Tempo medio " + averageTime/(i+1));
			arquivoEntrada.fechaArquivo();
		}
		averageTime /= SIZE;
		
		System.out.println("\nPolitica:");
		lao.print(arquivoSaida);
		System.out.println("Quantidade Total de Estados: " + gW.sizeEstados());
		arquivoSaida.escreveArquivo("Quantidade Total de Estados: " + gW.sizeEstados());
		
		//System.out.println("\nTempo de execucao: " + cT.getTempo() + " ms");
		//arquivoSaida.escreveArquivo("Tempo de execucao: " + cT.getTempo() + " ms");
		System.out.println("\nTempo de execucao: " + averageTime + " ms");
		arquivoSaida.escreveArquivo("Tempo de execucao: " + averageTime + " ms");
		
		arquivoEntrada.fechaArquivo();
		arquivoSaida.fechaArquivo();
    }
	
	public static void main_IV(String args[]) {

		//Arquivo arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
		Arquivo arquivoEntrada = null;
		Arquivo arquivoSaida = new Arquivo(args[0].substring(0, args[0].length()-3) + "txt", Arquivo.ESCRITA);
		
		Cronometro cT = new Cronometro();	
		Problem gW = null;
		
		System.out.println("main_IV\n**********");
		
		arquivoEntrada = new Arquivo(args[0], Arquivo.LEITURA);
		LeGridWorld lGW = new LeGridWorld(arquivoEntrada);
		
		System.out.println("Lendo Arquivo... " + args[0]);
		gW = lGW.executa();
		System.out.println("Leitura Concluida!");
		LAO lao = null;
		lao = new LAO(gW);
		
		//System.out.println("\nExecutando LAO*...");
		//cT.start();
		//lao.executa(LAO.VALUE);
		//cT.stop();
		
		System.out.println("\nExecutando IV...");
		cT.start();
		lao.superValueIterationLOG();
		cT.stop();
		System.out.println("\nTempo de execucao: " + cT.getTempo() + " ms");
		
		
		System.out.println("\nPolitica:");
		lao.print(arquivoSaida);
		
		lao.pprint(5, 7, true);

	}
	
	public static void main(String args[]) {
		//System.out.println("ddd".hashCode());
		//main_ORIGINAL
		//main_IV
		main_IV(args);
		return;
	}
}
