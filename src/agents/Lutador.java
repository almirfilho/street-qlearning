package agents;

import agents.AgentStates.EAcoesDoLutador;

public abstract class Lutador implements Runnable {
	// Nome do lutador
	private String name;
	// Life do lutador
	private int life;
	// O estado desse lutador
	private AgentStates.EAcoesDoLutador state;
	// O inimigo deste lutador
	private Lutador enemy;

	
	/**
	 * Construtor da classe
	 * 
	 * @param name
	 *            O nome do lutador
	 * @param initLife
	 *            O life do lutador
	 */
	public Lutador(String name, int initLife) {
		setName(name);
		setLife(initLife);
		setState(EAcoesDoLutador.evParar);
		setEnemy(null);
	}

	/**
	 * Pega o nome do lutador
	 * 
	 * @return O nome do lutador
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Seta o nome do lutador
	 * 
	 * @param s
	 *            O novo nome para o lutador
	 */
	public void setName(String s) {
		this.name = s;
	}

	/**
	 * Retorna o life do lutador
	 * 
	 * @return O life do lutador
	 */
	public int getLife() {
		return this.life;
	}

	/**
	 * Seta o life do lutador
	 * 
	 * @param v
	 *            O novo valor do life do lutador
	 */
	public void setLife(int v) {
		this.life = v;
	}

	/**
	 * Decrementa o life do lutador
	 * 
	 * @param v
	 *            O valor a ser decrementado
	 */
	public void decreaseLife(int v) {
		this.life -= v;
	}

	/**
	 * Verifica se o lutador está morto
	 * 
	 * @return TRUE se estiver morto, FALSE caso contrário
	 */
	public boolean isDead() {
		return (this.life <= 0);
	}

	/**
	 * Seta o estado do lutador
	 * 
	 * @param s
	 *            O valor do estado do lutador
	 */
	public void setState(AgentStates.EAcoesDoLutador s) {
		synchronized (this) {
			this.state = s;
		}
	}

	/**
	 * Retorna o estado do lutador
	 * 
	 * @return O estado do lutador
	 */
	public AgentStates.EAcoesDoLutador getState() {
		synchronized (this) {
			return this.state;	
		}
	}

	/**
	 * Retorna o nome do estado que o lutador se encontra
	 * 
	 * @return O nome do estado que o lutador se encontra
	 */
	public String getStateName() {
		synchronized (this) {
			return AgentStates.getStateName(this.state);
		}
	}

	/**
	 * Define quem é o inimigo deste lutador
	 * 
	 * @param e
	 *            O lutador inimigo
	 */
	public void setEnemy(Lutador e) {
		this.enemy = e;
	}

	/**
	 * Retorna a referência do inimigo deste lutador
	 * 
	 * @return Uma referência ao inimigo deste lutador
	 */
	public Lutador getEnemy() {
		return this.enemy;
	}

}
