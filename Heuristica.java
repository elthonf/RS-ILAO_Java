import Problem.Description.Estado;
import Problem.Description.Problem;
import Problem.Description.*;

import java.util.Map;

public abstract class Heuristica
{
	protected Map<Integer, Estado> estadosFinais;
	
	public abstract double calcula(Estado e);
}