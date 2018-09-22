import Problem.Description.Estado;
import Problem.Description.Problem;
import Problem.Description.*;

import java.util.Map;

public class ZeroOne extends Heuristica
{
	public ZeroOne(Map<Integer, Estado> e)
	{
		estadosFinais = e;
	}
	
	public double calcula(Estado estadoCorrente)
	{	
		return 1.0;
	}
}