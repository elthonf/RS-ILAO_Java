package Problem.Description;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class Problem
{
	private Map<Integer, Estado> estados;
	private Map<Integer, Acao> acoes;
	private double discountFactor;
	private Estado estadoInicial;
	private Map<Integer, Estado> estadoFinal;
	
	public Problem()
	{
		estadoFinal = new HashMap<Integer, Estado>();
	}
	
	public void adicionaEstados(Map<Integer, Estado> e)
	{
		estados = e;
	}
	
	public Estado getEstado(String nome)
	{
		return estados.get(nome.hashCode());
	}
	
	public Map<Integer, Estado> getEstados()
	{
		return estados;
	}
	
	public int sizeEstados()
	{
		return estados.size();
	}
	
	public void adicionaAcoes(Map<Integer, Acao> a)
	{
		acoes = a;
	}
	
	public Acao getAction(String nome)
	{
		return acoes.get(nome.hashCode());
	}
	
	public Acao getAction(Integer chave)
	{
		return acoes.get(chave);
	}
	
	public Set<Integer> getSetActions()
	{
		return acoes.keySet();
	}
	
	public int sizeAcoes()
	{
		return acoes.size();
	}
	
	public void setDiscount(double d)
	{
		discountFactor = d;
	}
	
	public double getDiscount()
	{
		return discountFactor;
	}
	
	public void setInitialState(String e)
	{
		estadoInicial = getEstado(e);
	}
	
	public Estado getInitialState()
	{
		return estadoInicial;
	}
	
	public void setFinalState(String nome)
	{
		Estado estadoFinal = getEstado(nome);
		estadoFinal.setGoal();
		this.estadoFinal.put(estadoFinal.hashCode(), estadoFinal);
	}
	
	public Map<Integer, Estado> getFinalStates()
	{
		return estadoFinal;
	}
	
	public Estado getFinalState(Integer key)
	{
		return estadoFinal.get(key);
	}
}