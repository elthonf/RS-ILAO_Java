import Problem.Description.Estado;
import Problem.Description.Problem;
import Problem.Description.*;

import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.Iterator;

public class Min extends Heuristica
{
	private final int FIRST = -1;
	public Min(Map<Integer, Estado> e)
	{
		estadosFinais = e;
	}
	
	public double calcula(Estado estadoCorrente)
	{
		double custoMin = FIRST;
		int size = estadoCorrente.actionSize();
		for(int i = 0; i < size; i++)
		{
			Acao a = estadoCorrente.getAction(i);
			LinkedList<Transicao> t = a.getTransitions();
			double custoSucessorEstimado = FIRST;
			for(Iterator<Transicao> it = t.iterator(); it.hasNext(); )
			{
				Transicao trans = it.next();
				Estado e = trans.getState();
				double prob = trans.getProbA();
				
				if(prob > 0) custoSucessorEstimado = Math.min(e.getCost(), custoSucessorEstimado);
				if(custoSucessorEstimado == FIRST) custoSucessorEstimado = e.getCost();
			}
			double costEst = a.getCost() + custoSucessorEstimado;
			custoMin = Math.min(custoMin, costEst);
			if(custoMin == FIRST) custoMin = costEst;
		}
		
		return custoMin;
	}
}