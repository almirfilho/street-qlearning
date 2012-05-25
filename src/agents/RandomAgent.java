package agents;

import java.util.Random;

import agents.AgentStates.EAcoesDoLutador;

import core.Stage;

public class RandomAgent extends Lutador {

	public RandomAgent(String name, int initLife) {
		super(name, initLife);
	}

	public AgentStates.EAcoesDoLutador getNextAction() {
		AgentStates.EAcoesDoLutador lastState = getState();
		Random rand = new Random();
		int actionIndex = rand.nextInt(AgentStates.EAcoesDoLutador.TOTAL_STATES.ordinal());

		//Se meu estado anterior foi PULO, não posso executar nenhuma ação de pulo neste novo estado...
		if (lastState == EAcoesDoLutador.evPular || lastState == EAcoesDoLutador.evAtaquePulo) {
			if (actionIndex == EAcoesDoLutador.evPular.ordinal() || actionIndex == EAcoesDoLutador.evAtaquePulo.ordinal()){
				return getNextAction();
			}
		}
		return AgentStates.getStateByIndex(actionIndex);
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
				e.printStackTrace();
			}
		}
	}

}
