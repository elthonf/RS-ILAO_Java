package Problem;

import Problem.Description.Acao;
import Problem.Description.Problem;
import Problem.Description.Estado;

import java.util.Map;
import java.util.HashMap;

public class LeGridWorld
{
	private Arquivo arquivo;
	private Problem gW;
	private int qtdAcoes;
	
	public LeGridWorld(Arquivo a)
	{
		arquivo = a;
		qtdAcoes = 0;
	}
	
	public Problem executa()
	{
		gW = new Problem();
		
		gW.adicionaEstados(leEstados());
		leAcoes();
		leCustos();
		gW.setDiscount(Double.parseDouble(arquivo.leLinha().split(" ")[2]));
		leEstadoInicial();
		leEstadoObjetivo();
		
		return gW;
	}
	
	public Map<Integer, Estado> leEstados()
	{
		// descarta "states"	
		arquivo.leLinha();
		String texto = arquivo.leLinha();
		Map<Integer, Estado> estados = new HashMap<Integer, Estado>();		
		String[] nomeEstados = texto.split(",");
	
		for(int i = 0; i < nomeEstados.length; i++)	estados.put(nomeEstados[i].trim().hashCode(), new Estado(nomeEstados[i].trim()));
		
		// descarta "endstates"	
		arquivo.leLinha();
		
		return estados;
	}
	
	public void leAcoes()
	{
		String[] palavras = arquivo.leLinha().split(" "); 
		
		do
		{
			String nomeAcao = palavras[1];
			for(palavras = arquivo.leLinha().split(" "); !palavras[0].equals("endaction"); )
			{
				Acao a = new Acao(nomeAcao);
				String nomeEstado = palavras[0].trim();
				do
				{
					a.adicionaTransicao(gW.getEstado(palavras[1]), Double.parseDouble(palavras[2]), Double.parseDouble(palavras[3]));
					palavras = arquivo.leLinha().split(" ");
				} while(nomeEstado.equals(palavras[0].trim()));
				gW.getEstado(nomeEstado).addAcao(a);
			}
			qtdAcoes++;
		} while((palavras = arquivo.leLinha().split(" "))[0].equals("action"));
	}
	
	public void leCustos()
	{
		String[] palavras;
		int i = 0;
		System.out.println("leCustos");
		for(palavras = arquivo.leLinha().split(" "); !palavras[0].equals("endcost");  palavras = arquivo.leLinha().split(" "))
		{
			System.out.println(palavras[0].trim());
			double multiplicador = 1.0;
			if(Double.parseDouble(palavras[2]) != 1.0){
				multiplicador = 10.0;
			}
			multiplicador = 1.0;
			gW.getEstado(palavras[0].trim()).setActionCost(i, Double.parseDouble(palavras[2] ) * multiplicador);
			i = (i+1)%qtdAcoes;
		}
	}
	
	public void leEstadoInicial()
	{
		// Descarta "inicialstate"
		arquivo.leLinha();
		gW.setInitialState(arquivo.leLinha().trim());
		// Descarta "endinicialstate"
		arquivo.leLinha();
	}
	
	public void leEstadoObjetivo()
	{
		// Descarta "goalstate"
		arquivo.leLinha();
		for(String estado = arquivo.leLinha().trim(); !estado.equals("endgoalstate"); estado = arquivo.leLinha().trim()) gW.setFinalState(estado);
	}
}