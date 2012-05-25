package agents;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import core.Stage;

public class ScriptAgent extends Lutador{
	private int indexAtual;
	private ArrayList <AgentStates.EAcoesDoLutador> actionList;

	public ScriptAgent(String name, int initLife, String scriptPath) {
		super(name, initLife);
		this.actionList = new ArrayList<AgentStates.EAcoesDoLutador>();
		try {
			Scanner sc = new Scanner(new File(scriptPath));
			while (sc.hasNext()){
				String curAction = sc.next();
				this.actionList.add( AgentStates.getStateByName( curAction ) );
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public AgentStates.EAcoesDoLutador getNextAction() {
		this.indexAtual++;
		if (this.indexAtual == this.actionList.size())
			this.indexAtual = 0;
		return this.actionList.get(this.indexAtual);
	}

	@Override
	public void run() {
		while (!isDead() && !getEnemy().isDead()) {

			// Seta meu novo estado, baseado nas açoes do script
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
