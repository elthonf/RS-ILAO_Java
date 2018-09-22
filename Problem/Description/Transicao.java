package Problem.Description;

public class Transicao
{
	private Estado proxEstado;
	private double probA;
	private double probB;
	
	public Transicao(Estado e, double pA, double pB)
	{
		proxEstado = e;
		probA = pA;
		probB = pB;
	}
	
	public Estado getState()
	{
		return proxEstado;
	}
	
	public double getProbA()
	{
		return probA;
	}
}