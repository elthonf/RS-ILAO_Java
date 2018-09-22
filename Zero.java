import Problem.Description.Estado;
import Problem.Description.Problem;
import Problem.Description.*;

import java.util.Map;

public class Zero extends Heuristica
{
	public Zero(Map<Integer, Estado> e)
	{
		estadosFinais = e;
	}
	
	public double calcula(Estado estadoCorrente)
	{	
		return 0;
	}
}