package agents;

import agents.AgentStates.EAcoesDoLutador;
import core.Stage;

public class OnlyAtackAgent extends Lutador{

	public OnlyAtackAgent(String name, int initLife) {
		super(name, initLife);
	}

	public AgentStates.EAcoesDoLutador getNextAction() {
		return EAcoesDoLutador.evAtaque;
	}

	@Override
	public void run() {
		while (!isDead() && !getEnemy().isDead()) {

			// Seta meu novo estado, baseado em uma ação aleatória
			setState(getNextAction());

			System.out.println(getName() + " -> " + getStateName());
			
			Stage.incReadyThreads();			
			try {
				synchronized (this) {
					this.wait();
				}
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
