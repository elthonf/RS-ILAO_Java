import Problem.Description.*;
import Problem.Arquivo;

import java.util.LinkedList;
import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Iterator;

public class LAO
{
	public static final int VALUE = 0;
	public double riskFactor = 0.15; //Fator de risco
	
	private Estado estadoInicial;
	private Map<Integer, Estado> vertices;
	private Map<Integer, Boolean> setZ;
	private Map<Integer, Boolean> setT;
	private int count;
	private Heuristica h;
	private Problem p;

	public LAO(Problem p)
	{
		count = 0;
		this.p = p;
		vertices = new HashMap<Integer, Estado>();
		setZ = new HashMap<Integer, Boolean>();
		setT = new HashMap<Integer, Boolean>();
		// Definicao da Heuristica
		//h = new Manhattan(p.getFinalStates());
		//h = new Exponencial(p.getFinalStates(), this.riskFactor);
		h = new ZeroOne(p.getFinalStates());
		
		estadoInicial = p.getInitialState();
		estadoInicial.setCost(h.calcula(estadoInicial));
		vertices.put(estadoInicial.hashCode(), estadoInicial);
	}
	
	public void executa(int dynammicAlgorithm)
	{
		boolean nonTerminalTip = true;
		while(nonTerminalTip)
		{			
			do
			{
				setT.clear();
				setT.put(estadoInicial.hashCode(), true);
				nonTerminalTip = depthFirstSearch(estadoInicial);
			} while(nonTerminalTip);
			this.pprint(0, 0);
			if(dynammicAlgorithm == VALUE) nonTerminalTip = valueIteration();
			this.pprint(0, 0);
		}
		this.pprint(0, 0);
	}
	
	public void exec_IV() {
		
	}
	
	public double maxvet( double[] valores ) {
		double maior = Double.NEGATIVE_INFINITY;
		
		for(int i=0; i < valores.length; i++	 ) {
			if(valores[i] > maior)
				maior = valores[i];
		}
		return maior;
	}
	public int maxveti( double[] valores ) {
		double maior = Double.NEGATIVE_INFINITY;
		int index = 0;
		for(int i=0; i < valores.length; i++	 ) {
			if(valores[i] > maior) {
				maior = valores[i];
				index = i;
			}
		}
		return index;
	}
	
	public void print(Arquivo file)
	{
		Set<Integer> chaves = setZ.keySet();
		for(Integer chave : chaves)
		{
			Estado e = vertices.get(chave);
			if(e.isGoal()) System.out.println("Estado: " + e.getName() + " custo: " + e.getCost());
			//else if(e.wasExpanded()) System.out.println("Estado: " + e.getName() + " acao: " + p.getAction(e.getBestAction()).getName() + " custo: " + e.getCost());
			else
			{
				System.out.println("Estado: " + e.getName() + " acao: " + e.getAction(e.getBestAction()).getName() + " custo: " + e.getCost());
				file.escreveArquivo("Estado: " + e.getName() + " acao: " + e.getAction(e.getBestAction()).getName() + " custo: " + e.getCost());
			}
		}
		System.out.println("\nQuantidade de estados na politica: " + setZ.size());
		file.escreveArquivo("Quantidade de estados na politica: " + setZ.size());
		System.out.println("Quantidade de estados expandidos: " + count);
		file.escreveArquivo("Quantidade de estados expandidos: " + count);
	}
	
	public void pprint(int xMax, int yMax)
	{
		this.pprint(xMax, yMax, false); 
	}
	public void pprint(int xMax, int yMax, Boolean	force) {
		if(!force) return;
		if(xMax == 0) xMax = 5;
		if(yMax == 0) yMax = 14;
		System.out.println("");
		for (int y = 1; y <=yMax; y++) {
			for (int x = 1; x <= xMax; x++) {
				String s = "x" + x + "y" + y;
				int sHash = s.hashCode();
				System.out.print("[" + s + "|" + sHash + "] ");

				Estado e = this.p.getEstado(s);
				if(e.wasExpanded()) System.out.print("*e*");
				else System.out.print("   ");
				
				int ba = e.getBestAction();
				if(ba > -1) {
					Acao a = e.getAction(ba);
					System.out.print("(" + a.getName() + ")");
				}else {
					System.out.print("( null )");
				}
				System.out.print(String.format("%.4f", e.getCost()) );
				
				System.out.print("\t");
			}
			System.out.println("");
		}
	}
	
	// Busca em profundidade ate um no nao expandido
	private boolean depthFirstSearch(Estado estadoCorrente)
	{
		boolean expanded = false;
		if(estadoCorrente.wasExpanded())
		{
			boolean firstExpanded = false;
			Acao a = estadoCorrente.getAction(estadoCorrente.getBestAction());
			LinkedList<Transicao> t = a.getTransitions();
			for(Iterator<Transicao> it = t.iterator(); it.hasNext();)
			{
				Transicao trans = it.next();
				Estado e = trans.getState();
				
				if(!setT.containsKey(e.hashCode()) && !e.isGoal())
				{
					setT.put(e.hashCode(), true);
					firstExpanded = depthFirstSearch(e);
					if(firstExpanded) expanded = firstExpanded;
				}	
			}
		}
		else
		{
			expande(estadoCorrente);
			return true;
		}
		
		minUtility(estadoCorrente);
		this.pprint(0, 0);
		
		return expanded;
	}
	
	private void expande(Estado estadoExp)
	{
		count++;
		System.out.println("\nExpande: " + estadoExp.getName());

		int size = estadoExp.actionSize();
        for(int i = 0; i < size; i++)
		{
			Acao a = estadoExp.getAction(i);
			LinkedList<Transicao> t = a.getTransitions();
			for(Iterator<Transicao> it = t.iterator(); it.hasNext();)
			{
				Transicao trans = it.next();
				Estado e = trans.getState();
				
				if(!vertices.containsKey(e.hashCode()))
				{
					vertices.put(e.hashCode(), e);
					e.setCost(h.calcula(e));
				}
			}
		}
		estadoExp.setExpanded();
		minUtility(estadoExp);
		this.pprint(0, 0);
	}
	
	private boolean valueIteration()
	{
		double epsilon = Math.pow(10, -16);
		double error;
		LinkedList<Estado> visited = new LinkedList<Estado>();
		
		do
		{
			setZ.clear();
			visited.add(estadoInicial);
			setZ.put(estadoInicial.hashCode(), true);
			error = 0;
		
			// Busca em profundidade
			while(!visited.isEmpty())
			{
				Estado s = visited.remove();
				
				if(!s.wasExpanded()) return true;
				
				double errorI = update(s);
				error = Math.max(errorI, error);
				Acao a = s.getAction(s.getBestAction());
				List<Transicao> t = a.getTransitions();
				for(Iterator<Transicao> it = t.iterator(); it.hasNext(); )
				{
					Transicao trans = it.next();
					Estado e = trans.getState();
					
					if(!setZ.containsKey(e.hashCode()) && !e.isGoal())
					{
						setZ.put(e.hashCode(), true);
						visited.add(e);
					}
				}
			}
		} while(error > epsilon);
		
		return false;
	}
	
	private boolean policyIterationRSMDP()
	{
		double epsilon = Math.pow(10, -16);
		double error;
		LinkedList<Estado> visited = new LinkedList<Estado>();
		
		do
		{
			setZ.clear();
			visited.add(estadoInicial);
			setZ.put(estadoInicial.hashCode(), true);
			error = 0;
		
			// Busca em profundidade
			while(!visited.isEmpty())
			{
				Estado s = visited.remove();
				
				if(!s.wasExpanded()) return true;
				
				double errorI = update(s);
				error = Math.max(errorI, error);
				Acao a = s.getAction(s.getBestAction());
				List<Transicao> t = a.getTransitions();
				for(Iterator<Transicao> it = t.iterator(); it.hasNext(); )
				{
					Transicao trans = it.next();
					Estado e = trans.getState();
					
					if(!setZ.containsKey(e.hashCode()) && !e.isGoal())
					{
						setZ.put(e.hashCode(), true);
						visited.add(e);
					}
				}
			}
		} while(error > epsilon);
		
		return false;
	}
	
	
	private double update(Estado s)
	{
		double oldCost = s.getCost();
		minUtility(s);
		double newCost = s.getCost();
		double errorI = Math.abs(oldCost - newCost);		
		
		return errorI;
	}
		
	private void minUtility(Estado estadoCorrente)
	{
		double sinal = -1.0 * Math.signum(this.riskFactor);
		sinal = 1.0;
		double novoCusto = Double.POSITIVE_INFINITY * sinal;
		int bestAction = -1;
		int size = estadoCorrente.actionSize();
		for(int i = 0; i < size; i++)
		{
			Acao a = estadoCorrente.getAction(i);
			LinkedList<Transicao> t = a.getTransitions();
			double costEst = 0;
			for(Iterator<Transicao> it = t.iterator(); it.hasNext(); )
			{
				Transicao trans = it.next();
				Estado e = trans.getState();
				double prob = trans.getProbA();
			
				costEst += prob*e.getCost();
				//costEst += prob*e.getCost() * Math.exp(riskFactor);
			}
			//costEst += a.getCost();
			costEst *= Math.exp(this.riskFactor * a.getCost());
			if(costEst *sinal < novoCusto *sinal )
			{
				novoCusto = costEst;
				bestAction = i;
			}
		}
		estadoCorrente.setCost(novoCusto);
		estadoCorrente.setBestEdge(bestAction);
	}
	
	
	public void superValueIteration() {
		System.out.println( "\n\nsuperValueIteration" ); //Cost
		Map<Integer, Estado> estados = this.p.getEstados();
		
		Map<Integer, Double> V0 = new HashMap<Integer, Double> ();
		Map<Integer, Acao> V0a = new HashMap<Integer, Acao> ();
		Map<Integer, Double> Vi = new HashMap<Integer, Double> (); //Iteracao anterior
		Map<Integer, Acao> Via = new HashMap<Integer, Acao> ();
		Map<Integer, Double> Vii = new HashMap<Integer, Double> (); //Iteracao atual
		Map<Integer, Acao> Viia = new HashMap<Integer, Acao> ();
		
		//Varre os estados e monta V0
		for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
			Integer k = it.next();
			//System.out.println(k.toString()); //Key
			Estado s = estados.get(k);
			//System.out.print( s.getName() ); //State Name
			
			double[] costs = new double[s.actionSize()];

			V0.put(k, Double.POSITIVE_INFINITY);
			V0a.put(k, null);
			Vi.put(k, Double.POSITIVE_INFINITY);
			Via.put(k, null);
			Vii.put(k, Double.POSITIVE_INFINITY);
			Viia.put(k, null);
			
			for(int i = 0; i < s.actionSize(); i++)
			{
				Acao a = s.getAction(i);
				costs[i] = Math.exp(this.riskFactor * a.getCost() );
				if (costs[i]  < V0.get(k) ){
					V0.replace(k, costs[i]);
					//V0.replace(k, 1000.0);
					V0a.replace(k, a);
				}
			}
			
			if (s.actionSize() < 1)
				V0.replace(k, 1.0);
			Vi.replace(k, V0.get(k));
			Via.replace(k, V0a.get(k));
			//System.out.print(" ");
			//System.out.println( V0.get(k) ); //Cost
		}
		
		double epsilon = 1e-16; //Erro maximo
		double erro = Double.NEGATIVE_INFINITY; //Erro da iteraçao
		double erroS = Double.NEGATIVE_INFINITY; //Erro do estado S
		
		do{
			erro = Double.NEGATIVE_INFINITY;
			erroS = Double.NEGATIVE_INFINITY;
			//System.out.println( "\n\nIteração X:" ); //Cost
			for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
				Integer k = it.next();
				erroS = 0.0;
				Estado s = estados.get(k);
				//System.out.print( s.getName() ); //State Name
				
				Vii.replace(k, Double.POSITIVE_INFINITY ); //Obtem a melhor acao para o estado
				Viia.replace(k, null);
				if (s.actionSize() < 1)
					Vii.replace(k, 1.0);
				for(int i = 0; i < s.actionSize(); i++) {
					Acao a = s.getAction(i);
					List<Transicao> t = a.getTransitions();
					double costAction = 0.0;
					for(Iterator<Transicao> ita = t.iterator(); ita.hasNext(); )
					{
						Transicao trans = ita.next();
						Estado e = trans.getState();
						double myCost = Math.exp(this.riskFactor * a.getCost() );
						costAction +=  myCost * trans.getProbA() * Vi.get( e.hashCode() );
					}
					
					if(costAction < Vii.get(k)) { //Max para beta negativo e max para beta positivo
						Vii.replace(k, costAction);
						Viia.replace(k, a);
						s.setBestEdge(i);
					}
					//Math.signum(d)
					//System.out.print(" ");
					//System.out.print(a.getName());
					//System.out.print(" ");
					//System.out.println( costAction ); //Cost
				}
				//Calcula o erro do estado (e o maior erro dos estados)
				erroS = Math.abs(Vii.get(k) - Vi.get(k));
				if(!s.isGoal()) erro = Math.max(erroS, erro);
				//System.out.print( "Vi(s): " ); System.out.print( Vii.get(k) );
				//System.out.print( " Vi-1(s): " ); System.out.print( Vi.get(k) );
				//System.out.print( " Erro S: " );
				//System.out.println( erroS );
			}

			//Substitui o V anterior e 
			for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
				Integer k = it.next();
				Vi.replace(k, Vii.get(k));
				Via.replace(k, Viia.get(k));
			}
			//System.out.print("\n\t ERRO: ");
			//System.out.println( erro ); //Cost
		}while(erro > epsilon);

		
		System.out.println( "\n\nCONVERGIU!!! Segue politica resultante" ); //Cost
		double maior = Double.NEGATIVE_INFINITY;
		//Substitui o V anterior e 
		for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
			Integer k = it.next();
			Estado s = estados.get(k);
			System.out.print( s.getName() ); //State Name
			System.out.print(" - ");
			Acao a = Viia.get(k);
			if (a != null) {
				System.out.print(a.getName());
			}
			else {
				System.out.print("goal-state");
			}
			System.out.print(" -> ");
			System.out.println(Vii.get(k));	
			maior = Math.max(maior, Vii.get(k));
		}
	
		System.out.print( "MAIOR VALOR: ");
		System.out.println( maior);
		System.out.println( "FIM"); //State Name
	}

	
	public void superValueIterationLOG() {
		System.out.println( "\n\nsuperValueIterationLOG" ); //Cost
		Map<Integer, Estado> estados = this.p.getEstados();
		
		Map<Integer, Double> V0 = new HashMap<Integer, Double> ();
		Map<Integer, Acao> V0a = new HashMap<Integer, Acao> ();
		Map<Integer, Double> Vi = new HashMap<Integer, Double> (); //Iteracao anterior
		Map<Integer, Acao> Via = new HashMap<Integer, Acao> ();
		Map<Integer, Double> Vii = new HashMap<Integer, Double> (); //Iteracao atual
		Map<Integer, Acao> Viia = new HashMap<Integer, Acao> ();
		
		//Varre os estados e monta V0
		for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
			Integer k = it.next();
			//System.out.println(k.toString()); //Key
			Estado s = estados.get(k);
			//System.out.print( s.getName() ); //State Name
			
			double[] costs = new double[s.actionSize()];

			V0.put(k, Double.POSITIVE_INFINITY );
			V0a.put(k, null);
			Vi.put(k, Double.POSITIVE_INFINITY);
			Via.put(k, null);
			Vii.put(k, Double.POSITIVE_INFINITY);
			Viia.put(k, null);
			
			for(int i = 0; i < s.actionSize(); i++)
			{
				Acao a = s.getAction(i);
				//System.out.print();
				costs[i] = Math.exp(this.riskFactor * a.getCost() );
				if (costs[i]  < V0.get(k) ){
					V0.replace(k, costs[i]);
					V0a.replace(k, a);
				}
			}
			
			if (s.actionSize() < 1)
				V0.replace(k, 0.0);
			Vi.replace(k, V0.get(k));
			Via.replace(k, V0a.get(k));
			//System.out.print(" ");
			//System.out.println( V0.get(k) ); //Cost
		}

		double epsilon = 1e-15; //Erro maximo
		double erro = Double.NEGATIVE_INFINITY; //Erro da iteraçao
		double erroS = Double.NEGATIVE_INFINITY; //Erro do estado S
		
		do{
			erro = Double.NEGATIVE_INFINITY;
			erroS = Double.NEGATIVE_INFINITY;
			//System.out.println( "\n\nIteração X:" ); //Cost
			for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
				Integer k = it.next();
				erroS = 0.0;
				Estado s = estados.get(k);
				//System.out.print( s.getName() ); //State Name
				
				Vii.replace(k, Double.POSITIVE_INFINITY); //Obtem a melhor acao para o estado
				Viia.replace(k, null);
				if (s.actionSize() < 1)
					Vii.replace(k, 0.0);
				for(int ia = 0; ia < s.actionSize(); ia++) {
					Acao a = s.getAction(ia);
					List<Transicao> t = a.getTransitions();
					double[] ki = new double[t.size()];
					double costAction = 0.0;
					int i = 0;
					for(Iterator<Transicao> ita = t.iterator(); ita.hasNext(); )
					{
						Transicao trans = ita.next();
						Estado e = trans.getState();
						ki[i] = Math.log( trans.getProbA() ) +  Vi.get( e.hashCode() );
						i++;
					}
					double maxki = this.maxvet(ki);
					int maxkiI = this.maxveti(ki);
					
					double somaExpKi = 0.0;
					
					i = 0;
					for(Iterator<Transicao> ita = t.iterator(); ita.hasNext(); )
					{
						Transicao trans = ita.next();
						somaExpKi += Math.exp(ki[i] - maxki);
						i++;
					}
					costAction = this.riskFactor * a.getCost() + maxki + Math.log(somaExpKi);
					if(costAction < Vii.get(k)) { //Max para beta negativo e max para beta positivo
						Vii.replace(k, costAction);
						Viia.replace(k, a);
						s.setBestEdge(i);
					}
					//System.out.print(" ");
					//System.out.print(a.getName());
					//System.out.print(" ");
					//System.out.println( costAction ); //Cost
				}
				//Calcula o erro do estado (e o maior erro dos estados)
				erroS = Math.abs(Vii.get(k) - Vi.get(k));
				if(!s.isGoal()) erro = Math.max(erroS, erro);
				//System.out.print( "Vi(s): " ); System.out.print( Vii.get(k) );
				//System.out.print( " Vi-1(s): " ); System.out.print( Vi.get(k) );
				System.out.print( " Erro S: " );
				System.out.println( erroS );
			}

			//Substitui o V anterior e 
			for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
				Integer k = it.next();
				Vi.replace(k, Vii.get(k));
				Via.replace(k, Viia.get(k));
			}
			//System.out.print("\n\t ERRO: ");
			//System.out.println( erro ); //Cost
		}while(erro > epsilon);

		
		System.out.println( "\n\nCONVERGIU!!! Segue politica resultante" ); //Cost
		double maior = Double.NEGATIVE_INFINITY;
		//Substitui o V anterior e 
		for(Iterator<Integer> it = estados.keySet().iterator(); it.hasNext(); ) {
			Integer k = it.next();
			Estado s = estados.get(k);
			System.out.print( s.getName() ); //State Name
			System.out.print(" - ");
			Acao a = Viia.get(k);
			if (a != null) {
				System.out.print(a.getName());
			}
			else {
				System.out.print("goal-state");
			}
			System.out.print(" -> ");
			System.out.println(Vii.get(k));		
			maior = Math.max(maior, Vii.get(k));
		}
		
		System.out.print( "MAIOR VALOR: ");
		System.out.println( maior);
		System.out.println( "FIM"); //State Name
	}
	
}