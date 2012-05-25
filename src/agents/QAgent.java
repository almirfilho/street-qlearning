package agents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;

import agents.AgentStates.EAcoesDoLutador;

import core.Stage;

public class QAgent extends Lutador {
	/**
	 * Número de linhas da qTable, N estados do agente x N estados do oponente
	 */
	private static final int QTABLEROWS = AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal()*AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal();
	/** Número de colunas, N ações possíveis */
	private static final int QTABLECOLS = AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal();

	// Variável que diz ao agente se ele está treinando(deve explorar) ou lutado
	// (deve usufruir).
	private boolean training;
	// Tabela de ações e reforços
	private double qTable[][];
	private double rTable[][];

	/**
	 * Matriz que converte cada par de indices (i,j), sendo i o estado do agente
	 * e j o estado do oponente, para um indice na QTable
	 */
	public static int states2QTableIndex[][];

	// Inicializa��o de propriedades est�ticas
	static
	{
		// Inicia a matriz de estados-indicesQTable
		states2QTableIndex = new int[AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal()][AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal()];
		int index = 0;
		for (int i = 0; i < AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal(); i++) {
			for (int j = 0; j < AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal(); j++) {
				states2QTableIndex[i][j] = index;
				index++;
			}
		}
	}
	
	/**
	 * Construtor da classe, utiliza o construtor da classe pai Lutador e
	 * inicializa a qTable e a rTable
	 * 
	 * @param name
	 *            O nome do lutador
	 * @param initLife
	 *            O life inicial do lutador
	 * @param training
	 *            Flag indicando se o agente está treinando ou lutando
	 */
	public QAgent(String name, int initLife, boolean training) {
		super(name, initLife);
		// Inicializa a QTable e a RTable
		this.qTable = new double[QTABLEROWS][QTABLECOLS];
		this.rTable = new double[QTABLEROWS][QTABLECOLS];
		setTraining(training);
		readRTable("rtable.txt");
	}

	/**
	 * Seta o agente para treinar
	 * 
	 * @param value
	 *            TRUE se estiver treinando, FALSE caso contrário
	 */
	public void setTraining(boolean value) {
		this.training = value;
	}

	/**
	 * Verifica se o agente está treinando ou se está lutando
	 * 
	 * @return TRUE se estiver treinando, FALSE se estiver lutando
	 */
	public boolean isTraining() {
		return this.training;
	}

	/**
	 * Verifica se o agente está lutando, este método é uma negação do método
	 * isTraining()
	 * 
	 * @return TRUE se estiver lutando, FALSE caso contrário.
	 */
	public boolean isFighting() {
		return !this.training;
	}

	/**
	 * Le a rTable de um arquivo
	 * 
	 * @param fileName
	 *            O arquivo.
	 */
	public void readRTable(String fileName) {
		try {
			Scanner sc = new Scanner(new File(fileName));
			for (int i = 0; i < QTABLEROWS; i++) {
				for (int j = 0; j < QTABLECOLS; j++) {
					this.rTable[i][j] = sc.nextDouble();
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("ERRO: Arquivo não encontrado: " + fileName);
		}
	}

	/**
	 * Escolhe a próxima ação a ser tomada baseada no melhor valor de Q
	 * 
	 * @return O indice da ação a ser tomada baseada no melhor valor de Q.
	 */
	public AgentStates.EAcoesDoLutador usufruir() {
		int qTableIndex = states2QTableIndex[getState().ordinal()][getEnemy()
				.getState().ordinal()];
		int actIndex = -1;
		double qv = Double.NEGATIVE_INFINITY;
		for (int i = 0; i < QTABLECOLS; i++) {
			if (Double.compare(this.qTable[qTableIndex][i], qv) > 0) {
				actIndex = i;
				qv = this.qTable[qTableIndex][i];
			}
		}
		return AgentStates.getStateByIndex(actIndex);
	}

	/**
	 * Escolhe a próxima ação a ser tomada aleatoreamente
	 * 
	 * @return O indice da ação a ser tomada
	 */
	public AgentStates.EAcoesDoLutador explorar() {
		AgentStates.EAcoesDoLutador lastState = getState();
		Random rand = new Random();
		int actionIndex = rand.nextInt(AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal());

		//Se meu estado anterior foi PULO, não posso executar nenhuma ação de pulo neste novo estado...
		if (lastState == EAcoesDoLutador.evPular || lastState == EAcoesDoLutador.evAtaquePulo) {
			if (actionIndex == EAcoesDoLutador.evPular.ordinal() || actionIndex == EAcoesDoLutador.evAtaquePulo.ordinal()){
				return explorar();
			}
		}
		return AgentStates.getStateByIndex(actionIndex);
	}

	/**
	 * Imprime a qTable na tela
	 */
	public void printQTable() {
		for (int i = 0; i < QTABLEROWS; i++) {
			for (int j = 0; j < QTABLECOLS; j++) {
				System.out.printf( "%.2f\t", this.qTable[i][j]);
			}
			System.out.println();
		}
	}
	
	/**
	 * Imprime a qTable na tela
	 */
	public void printRTable() {
		for (int i = 0; i < QTABLEROWS; i++) {
			for (int j = 0; j < QTABLECOLS; j++) {
				System.out.print((int)this.rTable[i][j] + " ");
			}
			System.out.println();
		}
	}

	/**
	 * Pega o maior valor de Q para o par de estados (meuEstado, estadoOponente)
	 * 
	 * @param myState
	 *            O valor do meu estado
	 * @param oppState
	 *            O valor do estado do oponente
	 * @return O maior valor que Q para esse par.
	 */
	public double getMaxQ(AgentStates.EAcoesDoLutador myState, AgentStates.EAcoesDoLutador oppState) {
		double max = Double.NEGATIVE_INFINITY;
		int qTableIndex = states2QTableIndex[myState.ordinal()][oppState.ordinal()];
		for (int i = 0; i < QTABLECOLS; i++) {
			if (Double.compare(this.qTable[qTableIndex][i], max) > 0)
				max = this.qTable[qTableIndex][i];
		}
		return max;
	}

	@Override
	public void run() {
		while (!isDead() && !getEnemy().isDead()) {
			// Pega o estado anterior
			AgentStates.EAcoesDoLutador lastState = getState();
			
			
			int myInitLife = getLife();
			int oppInitLife = getEnemy().getLife();
			
			// Seta meu novo estado, baseado nos critérios de usufruir ou
			// explorar
			// Se estiver treinando, uso o explorar, senão, uso o usufruir
			if (isTraining())
				setState(explorar());
			else
				setState(usufruir());
		
				System.out.println(getName() + " -> " + getStateName());
	
				// Verifico se meu novo estado é um estado de ataque
				// Se for, verifico se devo sugar life do oponente ou não
				AgentStates.EAcoesDoLutador myState = getState();
				AgentStates.EAcoesDoLutador oppState = getEnemy().getState();
				
				
				Stage.incReadyThreads();
				try {
					synchronized (this) {
						this.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
	
				// bonus = quantidade de life perdida por mim - qtd de life perdida
				// pelo meu oponente
				//int bonus1 = getLife() - getEnemy().getLife();
				
				int bonus1 = (oppInitLife - getEnemy().getLife()) - (myInitLife - getLife());
				
				double trainingFactor = 0.0;
				
				if (isTraining())
					trainingFactor = 0.8;
				else
					trainingFactor = 0.5;
				
				//Pega o maior valor para a ação que eu tomei...
				double bonus2 = trainingFactor * getMaxQ(myState, getEnemy().getState());

				// Pega o indice correspondente da tabela Q.
				int qTableIndex = states2QTableIndex[lastState.ordinal()][oppState.ordinal()];
				
				double bonusRTable =this.rTable[qTableIndex][myState.ordinal()]/10;
				
				double alpha = 0.5;
				// Atribui a recompensa na minha qTable, onde myState representa meu
				// novo estado, ou a ação que eu tomei
				this.qTable[qTableIndex][myState.ordinal()] += ((alpha)*(bonus1 + bonus2) + (1-alpha)*bonusRTable)/100;
		}
	}
}
