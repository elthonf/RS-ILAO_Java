import Problem.Description.Estado;
import Problem.Description.Problem;
import Problem.Description.*;

import java.util.Map;
import java.util.Set;
import java.util.LinkedList;
import java.util.Iterator;

public class Exponencial extends Heuristica
{
	private double _riskFactor = 0.0;
	public Exponencial(Map<Integer, Estado> e, double riskFactor)
	{
		estadosFinais = e;
		this._riskFactor = riskFactor;
	}
	
	// Heuristica para o estado meta mais proximo
	public double calcula(Estado e) {
		//if(e.isGoal()) return -1.0 * Math.signum(this._riskFactor);
		if(e.isGoal()) return 1.0 * Math.signum(this._riskFactor);
		//Constante
		double ret = Double.POSITIVE_INFINITY;
		for(int i = 0; i < e.actionSize(); i++) {
			Acao a = e.getAction(i);
			ret = Math.min(ret, a.getCost()) ;
		}
		//ret = -1.0 * Math.signum(this._riskFactor) * Math.exp( this._riskFactor * ret);
		ret = 1.0 * Math.signum(this._riskFactor) * Math.exp( this._riskFactor * ret);

		//System.out.println("H para  " + e.getName() + ": " + ret);
		return ret;
	}

}