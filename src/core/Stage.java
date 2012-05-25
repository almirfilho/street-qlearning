package core;

import gui.Animacao;
import gui.FramePrincipal;
import gui.PainelDeDesenho;
import gui.QuadroDeAnimacao;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JFrame;

import sound.MP3;

import agents.AgentStates.EAcoesDoLutador;
import agents.Lutador;
import agents.OnlyAtackAgent;
import agents.QAgent;
import agents.RandomAgent;
import agents.ScriptAgent;

public class Stage {

	/**
	 * Contador para o stage saber quantas Threads jÃ¡ escolheram seu novo estado
	 */
	private static Integer nReadyThreads = 0;

	public static final int nIterations = 50;
	/** Life inicial dos personagens */
	public static final int initialLife = 100;
	
	/** Tempo de uma animação de ação de um personagem */
	private static int aniDuration = 10;

	/** Converte um indice para o nome de uma aÃ§Ã£o/estado */
	// public static String[] index2Action;
	/** Converte o nome de uma aÃ§Ã£o para um Ã­ndice */
	// public static HashMap<String, Integer> action2Index;
	/** Agente inteligente... rsrsrs */
	public static QAgent ia1;
	public static QAgent ia2;
	/** Agente randomico */
	public static RandomAgent ra1;
	public static RandomAgent ra2;
	/** Agente que sempre ataca */
	public static OnlyAtackAgent oa1;
	/** Agente que ataca conforme um script */
	public static ScriptAgent sa1;

	/**
	 * Incrementa o valor das Threads que estÃ£o prontas
	 */
	public static void incReadyThreads() {
		synchronized (Stage.nReadyThreads) {
			Stage.nReadyThreads++;
		}
	}

	/**
	 * Reseta o numero de Threads prontas
	 */
	public static void resetReadyThreads() {
		synchronized (Stage.nReadyThreads) {
			Stage.nReadyThreads = 0;
		}
	}

	/**
	 * Retorna o nÃºmero de Threads que jÃ¡ finalizaram seu processamento
	 * 
	 * @return O nÃºmero de Threads que jÃ¡ finalizaram seu processamento
	 */
	public static int getReadyThreadsCount() {
		synchronized (Stage.nReadyThreads) {
			return Stage.nReadyThreads;
		}
	}

	private static void handleActions(Lutador a, Lutador b) {
		EAcoesDoLutador myState = a.getState();
		EAcoesDoLutador oppState = b.getState();

		switch (myState) {
		// Ataque
		case evAtaque:
			if (oppState == EAcoesDoLutador.evAtaque
					|| oppState == EAcoesDoLutador.evParar)
				b.decreaseLife(10);
			else if (oppState == EAcoesDoLutador.evDefender)
				b.decreaseLife(5);
			break;
		// Ataque_pulo
		case evAtaquePulo:
			if (oppState == EAcoesDoLutador.evAtaquePulo
					|| oppState == EAcoesDoLutador.evPular)
				b.decreaseLife(10);
			break;
		// Ataque_agachado
		case evAtaqueAgachado:
			if (oppState == EAcoesDoLutador.evAtaque
					|| oppState == EAcoesDoLutador.evParar
					|| oppState == EAcoesDoLutador.evAgachar
					|| oppState == EAcoesDoLutador.evAtaqueAgachado)
				b.decreaseLife(10);
			else if (oppState == EAcoesDoLutador.evDefender)
				b.decreaseLife(5);
			break;
		}
	}

	/**
	 * Inicia os componentes estÃ¡ticos.
	 */
	public static void initStaticComponents() {
		// Cria os agentes
		Stage.ia1 = new QAgent("Marcio", Stage.initialLife, true);
		Stage.ia2 = new QAgent("Vini", Stage.initialLife, true);
		Stage.ra1 = new RandomAgent("Emilio", Stage.initialLife);
		Stage.ra2 = new RandomAgent("Renier", Stage.initialLife);
		Stage.oa1 = new OnlyAtackAgent("Almir", Stage.initialLife);
		Stage.sa1 = new ScriptAgent("ScriptAgent", Stage.initialLife,
				"ataque_script.txt");
	}
	
	/** Inicializa as propriedades dos lutadores na interface gráfica. */
	public static void criaLutadoresGUI( PainelDeDesenho drawPanel )
	{
		// Imagens dos lutadores
		drawPanel.setPlayerImage(PainelDeDesenho.ID_FIRST_PLAYER, "/images/ryu.png");
		drawPanel.setPlayerImage(PainelDeDesenho.ID_SECOND_PLAYER, "/images/ryu_preto.png");

		
		int nFrames = 0;
		// Cria animações do lutador 1
		/** PARAR */
		Animacao a1Parar = new Animacao();
		nFrames = 5;
		a1Parar.addFrame( new QuadroDeAnimacao(7, 14, 60, 90, aniDuration/nFrames));
		a1Parar.addFrame( new QuadroDeAnimacao(94, 15, 60, 90, aniDuration/nFrames));
		a1Parar.addFrame( new QuadroDeAnimacao(183, 14, 60, 90, aniDuration/nFrames));
		a1Parar.addFrame( new QuadroDeAnimacao(279, 11, 57, 93, aniDuration/nFrames));
		a1Parar.addFrame( new QuadroDeAnimacao(367, 12, 58, 92, aniDuration/nFrames));
		
		/** DEFENDER */
		Animacao a1Defender = new Animacao();
		nFrames = 1;
//		a1Defender.addFrame( new QuadroDeAnimacao(443, 2335, 62, 92, aniDuration/nFrames));
		a1Defender.addFrame( new QuadroDeAnimacao(525, 2334, 64, 93, aniDuration/nFrames));
		
		/** AGACHAR */
		Animacao a1Agachar = new Animacao();
		nFrames = 3;
		a1Agachar.addFrame( new QuadroDeAnimacao(32, 1212, 53, 83, aniDuration/nFrames));
		a1Agachar.addFrame( new QuadroDeAnimacao(115, 1227, 57, 68, aniDuration/nFrames));
		a1Agachar.addFrame( new QuadroDeAnimacao(197, 1235, 61, 61, aniDuration/nFrames));
		
		/** PULAR */
		Animacao a1Pular = new Animacao();
		nFrames = 7;
		a1Pular.addFrame( new QuadroDeAnimacao(16, 847, 56, 85, aniDuration/nFrames));
		a1Pular.addFrame( new QuadroDeAnimacao(100, 823, 56, 109, aniDuration/nFrames));
		a1Pular.addFrame( new QuadroDeAnimacao(175, 805, 52, 127+25, aniDuration/nFrames));
		a1Pular.addFrame( new QuadroDeAnimacao(251, 798, 54, 137+50, aniDuration/nFrames));
		a1Pular.addFrame( new QuadroDeAnimacao(326, 812, 50, 122+25, aniDuration/nFrames));
		a1Pular.addFrame( new QuadroDeAnimacao(396, 810, 50, 124, aniDuration/nFrames));
		a1Pular.addFrame( new QuadroDeAnimacao(465, 819, 55, 114, aniDuration/nFrames));
		
		/** ATAQUE_AGACHADO */
		/** Animação de agachar + animação de atacar agachado */
		Animacao a1AtaqueAgachado = new Animacao();
		nFrames = 5;
		//Agachar
		a1AtaqueAgachado.addFrame( new QuadroDeAnimacao(32, 1212, 53, 83, aniDuration/nFrames));
		a1AtaqueAgachado.addFrame( new QuadroDeAnimacao(115, 1227, 57, 68, aniDuration/nFrames));
		a1AtaqueAgachado.addFrame( new QuadroDeAnimacao(197, 1235, 61, 61, aniDuration/nFrames));
		//AtaqueAgachado
		a1AtaqueAgachado.addFrame( new QuadroDeAnimacao(24, 1344, 69, 60, aniDuration/nFrames));
		a1AtaqueAgachado.addFrame( new QuadroDeAnimacao(118, 1344, 95, 61, (aniDuration/nFrames) * 2));
		
		/** ATAQUE_PULO */
		Animacao a1AtaquePulo = new Animacao();
		nFrames = 6;
		a1AtaquePulo.addFrame( new QuadroDeAnimacao(25, 1822, 63, 87, aniDuration/nFrames));
		a1AtaquePulo.addFrame( new QuadroDeAnimacao(112, 1821, 68, 89+25, aniDuration/nFrames));
		a1AtaquePulo.addFrame( new QuadroDeAnimacao(202, 1783, 59, 125+55, aniDuration/nFrames));
		a1AtaquePulo.addFrame( new QuadroDeAnimacao(293, 1783, 53, 125+50, aniDuration/nFrames));
		a1AtaquePulo.addFrame( new QuadroDeAnimacao(376, 1782, 50, 125+25, aniDuration/nFrames));
		a1AtaquePulo.addFrame( new QuadroDeAnimacao(455, 1805, 59, 105, aniDuration/nFrames));

		/** ATAQUE */
		Animacao a1Ataque= new Animacao();
		nFrames = 3;
		a1Ataque.addFrame( new QuadroDeAnimacao(253, 269, 60, 94, aniDuration/nFrames));
		a1Ataque.addFrame( new QuadroDeAnimacao(333, 268, 74, 95, aniDuration/nFrames));
		a1Ataque.addFrame( new QuadroDeAnimacao(432, 268, 108, 94, aniDuration/nFrames));

		/** WINNER */
		Animacao a1Win= new Animacao();
		nFrames = 4;
		a1Win.addFrame( new QuadroDeAnimacao(496, 2479, 52, 83, aniDuration/nFrames));
		a1Win.addFrame( new QuadroDeAnimacao(572, 2474, 61, 88, aniDuration/nFrames));
		a1Win.addFrame( new QuadroDeAnimacao(660, 2465, 60, 97, aniDuration/nFrames));
		a1Win.addFrame( new QuadroDeAnimacao(745, 2440, 55, 122, aniDuration/nFrames));

		a1Win.setLoop(false);

		/** DEAD */
		Animacao a1Dead= new Animacao();
		nFrames = 5;
		a1Dead.addFrame( new QuadroDeAnimacao(482, 2097, 66, 82, aniDuration/nFrames));
		a1Dead.addFrame( new QuadroDeAnimacao(578, 2091, 82, 89, aniDuration/nFrames));
		a1Dead.addFrame( new QuadroDeAnimacao(676, 2115, 101, 45, aniDuration/nFrames));
		a1Dead.addFrame( new QuadroDeAnimacao(849, 2246, 122, 41, aniDuration/nFrames));
		a1Dead.addFrame( new QuadroDeAnimacao(984, 2264, 128, 31, aniDuration/nFrames));

		a1Dead.setLoop(false);


		/**Adiciona as animações do player 1 na sua animação */
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evParar, a1Parar);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evDefender, a1Defender);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evAgachar, a1Agachar);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evPular, a1Pular);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evAtaqueAgachado, a1AtaqueAgachado);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evAtaquePulo, a1AtaquePulo);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evAtaque, a1Ataque);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evMorto, a1Dead);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_FIRST_PLAYER, EAcoesDoLutador.evVencedor, a1Win);

		
		
		/** ANIMAÇÃO DO PLAYER 2 */
		/** PARAR */
		Animacao a2Parar = new Animacao();
		nFrames = 5;
		a2Parar.addFrame( new QuadroDeAnimacao(7, 14, 60, 90, aniDuration/nFrames));
		a2Parar.addFrame( new QuadroDeAnimacao(94, 15, 60, 90, aniDuration/nFrames));
		a2Parar.addFrame( new QuadroDeAnimacao(183, 14, 60, 90, aniDuration/nFrames));
		a2Parar.addFrame( new QuadroDeAnimacao(279, 11, 57, 93, aniDuration/nFrames));
		a2Parar.addFrame( new QuadroDeAnimacao(367, 12, 58, 92, aniDuration/nFrames));
		
		/** DEFENDER */
		Animacao a2Defender = new Animacao();
		nFrames = 1;
//		a2Defender.addFrame( new QuadroDeAnimacao(443, 2335, 62, 92, aniDuration/nFrames));
		a2Defender.addFrame( new QuadroDeAnimacao(525, 2334, 64, 93, aniDuration/nFrames));
		
		/** AGACHAR */
		Animacao a2Agachar = new Animacao();
		nFrames = 3;
		a2Agachar.addFrame( new QuadroDeAnimacao(32, 1212, 53, 83, aniDuration/nFrames));
		a2Agachar.addFrame( new QuadroDeAnimacao(115, 1227, 57, 68, aniDuration/nFrames));
		a2Agachar.addFrame( new QuadroDeAnimacao(197, 1235, 61, 61, aniDuration/nFrames));
		
		/** PULAR */
		Animacao a2Pular = new Animacao();
		nFrames = 7;
		a2Pular.addFrame( new QuadroDeAnimacao(16, 847, 56, 85, aniDuration/nFrames));
		a2Pular.addFrame( new QuadroDeAnimacao(100, 823, 56, 109, aniDuration/nFrames));
		a2Pular.addFrame( new QuadroDeAnimacao(175, 805, 52, 127+25, aniDuration/nFrames));
		a2Pular.addFrame( new QuadroDeAnimacao(251, 798, 54, 137+50, aniDuration/nFrames));
		a2Pular.addFrame( new QuadroDeAnimacao(326, 812, 50, 122+25, aniDuration/nFrames));
		a2Pular.addFrame( new QuadroDeAnimacao(396, 810, 50, 124, aniDuration/nFrames));
		a2Pular.addFrame( new QuadroDeAnimacao(465, 819, 55, 114, aniDuration/nFrames));
		
		/** ATAQUE_AGACHADO */
		/** Animação de agachar + animação de atacar agachado */
		Animacao a2AtaqueAgachado = new Animacao();
		nFrames = 5;
		//Agachar
		a2AtaqueAgachado.addFrame( new QuadroDeAnimacao(32, 1212, 53, 83, aniDuration/nFrames));
		a2AtaqueAgachado.addFrame( new QuadroDeAnimacao(115, 1227, 57, 68, aniDuration/nFrames));
		a2AtaqueAgachado.addFrame( new QuadroDeAnimacao(197, 1235, 61, 61, aniDuration/nFrames));
		//AtaqueAgachado
		a2AtaqueAgachado.addFrame( new QuadroDeAnimacao(24, 1344, 69, 60, aniDuration/nFrames));
		a2AtaqueAgachado.addFrame( new QuadroDeAnimacao(118, 1344, 95, 61, (aniDuration/nFrames) * 2));
		
		/** ATAQUE_PULO */
		Animacao a2AtaquePulo = new Animacao();
		nFrames = 6;
		a2AtaquePulo.addFrame( new QuadroDeAnimacao(25, 1822, 63, 87, aniDuration/nFrames));
		a2AtaquePulo.addFrame( new QuadroDeAnimacao(112, 1821, 68, 89+25, aniDuration/nFrames));
		a2AtaquePulo.addFrame( new QuadroDeAnimacao(202, 1783, 59, 125+55, aniDuration/nFrames));
		a2AtaquePulo.addFrame( new QuadroDeAnimacao(293, 1783, 53, 125+50, aniDuration/nFrames));
		a2AtaquePulo.addFrame( new QuadroDeAnimacao(376, 1782, 50, 125+25, aniDuration/nFrames));
		a2AtaquePulo.addFrame( new QuadroDeAnimacao(455, 1805, 59, 105, aniDuration/nFrames));

		/** ATAQUE */
		Animacao a2Ataque= new Animacao();
		nFrames = 3;
		a2Ataque.addFrame( new QuadroDeAnimacao(253, 269, 60, 94, aniDuration/nFrames));
		a2Ataque.addFrame( new QuadroDeAnimacao(333, 268, 74, 95, aniDuration/nFrames));
		a2Ataque.addFrame( new QuadroDeAnimacao(432, 268, 108, 94, aniDuration/nFrames));

		/** WINNER */
		Animacao a2Win= new Animacao();
		nFrames = 4;
		a2Win.addFrame( new QuadroDeAnimacao(496, 2479, 52, 83, aniDuration/nFrames));
		a2Win.addFrame( new QuadroDeAnimacao(572, 2474, 61, 88, aniDuration/nFrames));
		a2Win.addFrame( new QuadroDeAnimacao(660, 2465, 60, 97, aniDuration/nFrames));
		a2Win.addFrame( new QuadroDeAnimacao(745, 2440, 55, 122, aniDuration/nFrames));
		
		a2Win.setLoop(false);


		/** DEAD */
		Animacao a2Dead= new Animacao();
		nFrames = 5;
		a2Dead.addFrame( new QuadroDeAnimacao(482, 2097, 66, 82, aniDuration/nFrames));
		a2Dead.addFrame( new QuadroDeAnimacao(578, 2091, 82, 89, aniDuration/nFrames));
		a2Dead.addFrame( new QuadroDeAnimacao(676, 2115, 101, 45, aniDuration/nFrames));
		a2Dead.addFrame( new QuadroDeAnimacao(849, 2246, 122, 41, aniDuration/nFrames));
		a2Dead.addFrame( new QuadroDeAnimacao(984, 2264, 128, 31, aniDuration/nFrames));

		a2Dead.setLoop(false);

		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evParar, a2Parar);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evDefender, a2Defender);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evAgachar, a2Agachar);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evPular, a2Pular);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evAtaqueAgachado, a2AtaqueAgachado);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evAtaquePulo, a2AtaquePulo);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evAtaque, a2Ataque);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evMorto, a2Dead);
		drawPanel.setupPlayerAnimation(PainelDeDesenho.ID_SECOND_PLAYER, EAcoesDoLutador.evVencedor, a2Win);
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		float maxPlayerLife = 100.0f;
		
		// Inicia os componentes estaticos
		Stage.initStaticComponents();

		// Cria lá os caras que vÃ£o lutar...
		Lutador a = ia1, b = ra1;

		// Seta-os como inimigos
		a.setEnemy(b);
		b.setEnemy(a);

		// Abre a janela pra exibir a luta
		FramePrincipal meuFrame = new FramePrincipal();
		meuFrame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		final String defaultFrameName = meuFrame.getTitle();

		PainelDeDesenho drawPanel = meuFrame.getPainelDeDesenho();
		criaLutadoresGUI( drawPanel );
		
		// Modificar o nome dos lutadores, que será exibido
		drawPanel.setPlayerName( PainelDeDesenho.ID_FIRST_PLAYER, a.getName() );
		drawPanel.setPlayerName( PainelDeDesenho.ID_SECOND_PLAYER, b.getName() );

		// Ajusta o tamanho do frame
		Dimension desiredClientAreaSize = new Dimension( 720, 224 );
		meuFrame.setMinimumSize( desiredClientAreaSize );
		meuFrame.setPreferredSize( desiredClientAreaSize );		
		meuFrame.setResizable( true );
		meuFrame.setVisible(true);

		Insets insets = meuFrame.getInsets();
		int insetWidth = insets.left + insets.right;
	    int insetHeight = insets.top + insets.bottom;
	    meuFrame.setSize( (int)desiredClientAreaSize.getWidth() + insetWidth,
	    		(int)desiredClientAreaSize.getHeight() + insetHeight );

	    // inicia a musica
	    new Thread( new MP3("theme.mp3", true) ).start();
	    
	    // NÃºmero e vitÃ³rias de A e B.
		int nVA = 0, nVB = 0, empates = 0;

		for (int i = 0; i < Stage.nIterations; i++) {
			
			if( i == Stage.nIterations-1 )
				Stage.aniDuration = 250;
			
			meuFrame.setTitle(defaultFrameName + " (luta " + (i+1) + " / " + nIterations + ") - " + a.getName() + " " + nVA + " | " + b.getName() + " " + nVB + " | Empates: " + empates);

			if (i == Stage.nIterations - 1) {
				if (a instanceof QAgent)
					((QAgent) a).setTraining(false);
				if (b instanceof QAgent)
					((QAgent) b).setTraining(false);
			}
			a.setLife(Stage.initialLife);
			b.setLife(Stage.initialLife);
			a.setState(EAcoesDoLutador.evParar);
			b.setState(EAcoesDoLutador.evParar);

			System.out.println();
			System.out.println("[INICIANDO FIGHT " + (i+1) + " / " + nIterations + "]");
			System.out.println(a.getName() + " life inicial: " + a.getLife());
			System.out.println(b.getName() + " life inicial: " + b.getLife());
			System.out.println();

			resetReadyThreads();

			Thread at = new Thread(a);
			Thread bt = new Thread(b);
			at.start();
			bt.start();
			
			// Configura a interface gráfica inicial
			drawPanel.updateFighterAction(PainelDeDesenho.ID_FIRST_PLAYER, a.getState());
			drawPanel.updateFighterAction(PainelDeDesenho.ID_SECOND_PLAYER, b.getState());

			drawPanel.setPlayerLifeBar( PainelDeDesenho.ID_FIRST_PLAYER, a.getLife() / maxPlayerLife );
			drawPanel.setPlayerLifeBar( PainelDeDesenho.ID_SECOND_PLAYER, b.getLife() / maxPlayerLife );

			// Enquanto o fight tiver rolando
			while (!a.isDead() && !b.isDead()) {
				// Enquanto as duas Threads nÃ£o estiverem prontas, dorme,
				// hiberna, 10 milisegundos eternos por toda a eTHernidade.
				int readyThreadsCount = 0;
				while ((readyThreadsCount = getReadyThreadsCount()) != 2) {
					if (readyThreadsCount > 2) {
						System.err
								.println("FUUUUUUUUUUUUUUUUUU threads prontas("
										+ readyThreadsCount
										+ "), nro do fight(" + i + ")");
						System.err.flush();
						System.exit(-12345);
					}
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				// Desenha a ação tomada
				drawPanel.updateFighterAction(PainelDeDesenho.ID_FIRST_PLAYER, a.getState());
				drawPanel.updateFighterAction(PainelDeDesenho.ID_SECOND_PLAYER, b.getState());
				while ( drawPanel.isPlayerAnimationRepeating(PainelDeDesenho.ID_FIRST_PLAYER) == false ||
						drawPanel.isPlayerAnimationRepeating(PainelDeDesenho.ID_SECOND_PLAYER) == false )
				{
					drawPanel.updateFighterAnimations();
					
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

				// Processa os novos estados e verifica se alguÃ©m levou dano!!!
				// YEAH! Rock'n Roll!
				// Processa o 1Âº cara
				Stage.handleActions(a, b);
				// Processa o 2Âº cara
				Stage.handleActions(b, a);

				drawPanel.setPlayerLifeBar( PainelDeDesenho.ID_FIRST_PLAYER, a.getLife() / maxPlayerLife );
				drawPanel.setPlayerLifeBar( PainelDeDesenho.ID_SECOND_PLAYER, b.getLife() / maxPlayerLife );

				System.out.println(a.getName() + " life: " + a.getLife());
				System.out.println(b.getName() + " life: " + b.getLife());
				System.out.println();

				// Notifica as Threads que elas jÃ¡ podem continuar seu processamento!
				resetReadyThreads();
				synchronized (a) {
					a.notify();
				}
				synchronized (b) {
					b.notify();
				}
			}
			
			// Toca a animação "morrendo"
			drawPanel.updateFighterAction( PainelDeDesenho.ID_FIRST_PLAYER,
					a.isDead() ? EAcoesDoLutador.evMorto : EAcoesDoLutador.evVencedor );
			drawPanel.updateFighterAction( PainelDeDesenho.ID_SECOND_PLAYER,
					b.isDead() ? EAcoesDoLutador.evMorto : EAcoesDoLutador.evVencedor );

			// Espera as threads terminarem
			while (at.isAlive() || bt.isAlive()) {
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			at = null;
			bt = null;
			
			// Espera as animações terminarem
			while ( drawPanel.isPlayerAnimationRepeating(PainelDeDesenho.ID_FIRST_PLAYER) == false ||
					drawPanel.isPlayerAnimationRepeating(PainelDeDesenho.ID_SECOND_PLAYER) == false )
			{
				try {
					Thread.sleep(100);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}

			// Imprime life no final da luta
			System.out.println(a.getName() + " life: " + a.getLife());
			System.out.println(b.getName() + " life: " + b.getLife());

			// Verifica quem foi o ganhador
			boolean aMorreu = a.isDead();
			boolean bMorreu = b.isDead();
			if ( aMorreu && bMorreu )
			{
				System.out.println("EMPATE!!! QUE LUTA EMOCIONANTE!!!!");
				empates++;
			}
			else if ( aMorreu ) {
				System.out.println(b.getName() + " ganhou!");
				nVB++;
			} else {
				System.out.println(a.getName() + " ganhou!");
				nVA++;
			}

			// Pausa dramática no final da luta.
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
		System.out.println();
		System.out.println("NÂº de vitorias de " + a.getName() + ": " + nVA);
		System.out.println("NÂº de vitorias de " + b.getName() + ": " + nVB);
		System.out.println();

		if (a instanceof QAgent) {
			System.out.println();
			System.out.println("QTABLE de " + a.getName());
			((QAgent) a).printQTable();
		}
		if (b instanceof QAgent) {
			System.out.println();
			System.out.println("QTABLE de " + b.getName());
			((QAgent) b).printQTable();
		}

	}

}
