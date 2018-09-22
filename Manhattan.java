import Problem.Description.Estado;
import Problem.Description.Problem;
import Problem.Description.*;

import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.Iterator;

public class Manhattan extends Heuristica
{
	public Manhattan(Map<Integer, Estado> e)
	{
		estadosFinais = e;
	}
	
	// Heuristica de Manhattan para o estado meta mais proximo
	public double calcula(Estado e)
	{
		if(e.isGoal()) return 0;
		
		int x0 = e.getX();
		int y0 = e.getY();
		int vX = Math.abs(e.getVX());
		int vY = Math.abs(e.getVY());
		if(vX == 0) vX = 1;
		if(vY == 0) vY = 1;
		
		double answer = Double.POSITIVE_INFINITY;
		
		Set<Integer> keys = estadosFinais.keySet();
		for(Integer key : keys)
		{
			int x1 = estadosFinais.get(key).getX();
			int y1 = estadosFinais.get(key).getY();
			
			double aux = Math.abs(x0-x1)/vX + Math.abs(y0-y1)/vY;
			
			answer = Math.min(answer, aux);
		}
		
		return answer;
	}

}