package Problem.Description;

import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

public class Acao
{
	private String nome;
	private LinkedList<Transicao> transicoes;
	private double custo;
	
	public Acao(String nome)
	{
		this.nome = nome;
		transicoes = new LinkedList<Transicao>();
	}
	
	public String getName()
	{
		return nome;
	}
	
	public void setCost(double custo)
	{
		this.custo = custo;
	}
	
	public double getCost()
	{
		//return 10.0;
		return custo;
	}
	
	public void adicionaTransicao(Estado proxEstado, double proA, double proB)
	{
		Transicao t = new Transicao(proxEstado, proA, proB);
	
		transicoes.add(t);
	}
	
	public LinkedList<Transicao> getTransitions()
	{
		return transicoes;
	}
	
	public int hashCode()
	{
		return nome.hashCode();
	}
}