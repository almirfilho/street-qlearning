package gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;

import agents.AgentStates;
import agents.AgentStates.EAcoesDoLutador;


/** Um JPanel especial, onde serão desenhados os lutadores. */
public class PainelDeDesenho extends JPanel implements ComponentListener, WindowListener {
	private static final long serialVersionUID = -2436093752880552905L;

	// CONSTANTES
	/** Identifica o primeiro player. */
	public static final int ID_FIRST_PLAYER = 0;
	/** Identifica o segundo player. */
	public static final int ID_SECOND_PLAYER = 1;
	/** Indica o tamanho externo da barra de life. (largura) */
	private static final int OUTER_LIFE_BAR_WIDTH = 200;
	/** Indica o tamanho externo da barra de life. (altura) */
	private static final int OUTER_LIFE_BAR_HEIGHT = 25;
	/** Indica o espaçamento usado para desenhar a parte interna da barra de life. (eixo X) */
	private static final int INNER_LIFE_BAR_X_SPACING = 3;
	/** Indica o espaçamento usado para desenhar a parte interna da barra de life. (eixo Y) */
	private static final int INNER_LIFE_BAR_Y_SPACING = 3;
	/** Cor da parte externa da barra de life. */
	private static final Color OUTER_LIFE_BAR_COLOR = Color.WHITE;
	/** Cor da parte interna da barra de life, com HP alto. */
	private static final Color INNER_LIFE_BAR_COLOR_GOOD = Color.GREEN;
	/** Cor da parte interna da barra de life, com HP médio. */
	private static final Color INNER_LIFE_BAR_COLOR_MEDIUM = Color.YELLOW;
	/** Cor da parte interna da barra de life, com HP baixo. */
	private static final Color INNER_LIFE_BAR_COLOR_BAD = Color.RED;
	/** Cor da borda da barra de life. */
	private static final Color OUTER_LIFE_BAR_BORDER_COLOR = Color.BLACK;
	/** Cor usada para desenhar o nome do lutador. */
	private static final Color LIFE_BAR_PLAYER_NAME_COLOR = Color.BLACK;

	// PROPRIEDADES
	/** Mapeia cada ação de cada lutador para uma animação correspondente a esta ação. */
	private HashMap<AgentStates.EAcoesDoLutador, Animacao> m_animacoes[];
	/** As imagens de cada lutador. */
	private Image m_imgLutador[];
	/** A imagem do fundo da luta. */
	private Image m_imgBackground;
	/** Guarda as ações atuais dos lutadores. */
	private AgentStates.EAcoesDoLutador m_estadoLutadores[];
	/** Guarda o life dos dois lutadores. Vai de 0 a 1. */
	private float m_life[];
	/** Thread usada para repintar periodicamente o painel. */
	private AtualizadorDoPainel m_updater = new AtualizadorDoPainel();
	/** Os nomes dos lutadores. */
	private String m_fighterNames[] = { "", "" };
	/** A fonte usada para desenhar os nomes dos lutadores. */
	private Font m_namesFont = null;

	// MÉTODOS
	/** Construtor.
	 * @param parentFrame O frame que será o pai do painel. */
	@SuppressWarnings("unchecked")
	public PainelDeDesenho( JFrame parentFrame ) {
		m_imgLutador = new Image[2];

		m_animacoes = new HashMap[2];
		m_animacoes[0] = new HashMap<AgentStates.EAcoesDoLutador, Animacao>();
		m_animacoes[1] = new HashMap<AgentStates.EAcoesDoLutador, Animacao>();

		m_estadoLutadores = new AgentStates.EAcoesDoLutador[2];
		m_estadoLutadores[0] = EAcoesDoLutador.evParar;
		m_estadoLutadores[1] = EAcoesDoLutador.evParar;
		
		m_life = new float[] { 1.0f, 1.0f };

		addComponentListener(this);
		parentFrame.addWindowListener(this);
		
		m_updater.start();
		
		m_imgBackground = loadImage("/images/background.png");
	}
	
	/** Modifica a animação de um lutador quando ele estiver executando uma determinada ação.
	 * @param idPlayer O identificador do jogador para o qual a animação será associada.
	 *    Usar {@link #ID_FIRST_PLAYER} ou {@link #ID_SECOND_PLAYER}.
	 * @param acao A ação que estará associada à animação.
	 * @param anim A animação quer será usada na ação selecionada para o player especificado.
	 */
	public void setupPlayerAnimation( int idPlayer, AgentStates.EAcoesDoLutador acao, Animacao anim )
	{
		m_animacoes[idPlayer].put(acao, anim);
	}
	
	
	/** Altera a animação atual do player especificado, para que ela seja exibida na tela.
	 * @param idPlayer O identificador do jogador para o qual a animação será associada.
	 *    Usar {@link #ID_FIRST_PLAYER} ou {@link #ID_SECOND_PLAYER}.
	 * @param acao A ação que será exibida. */
	public void updateFighterAction( int idPlayer, AgentStates.EAcoesDoLutador acao )
	{
		m_estadoLutadores[idPlayer] = acao;
		m_animacoes[idPlayer].get(acao).play();
	}


	/** Chama as rotinas de atualização das animações dos lutadores. */
	public void updateFighterAnimations()
	{
		for ( int f = m_animacoes.length - 1; f >= 0; f-- )
		{
			Animacao playerAnim = m_animacoes[f].get( m_estadoLutadores[f] );
			playerAnim.updateAnimation();
		}
	}


	/** Carrega uma imagem para um lutador.
	 * @param idPlayer O identificador do jogador que terá sua imagem modificada.
	 *    Usar {@link #ID_FIRST_PLAYER} ou {@link #ID_SECOND_PLAYER}.
	 * @param imageFile O arquivo de onde a imagem será carregada.
	 * @return Retorna true caso tudo tenha corrido bem. */
	public boolean setPlayerImage( int idPlayer, String imageFile )
	{
		m_imgLutador[idPlayer] = loadImage(imageFile);
		return (m_imgLutador[idPlayer] != null);
	}
	
	
	/** Modifica o nome que será exibido para um dos lutadores.
	 * @param idPlayer O identificador do jogador que terá sua imagem modificada.
	 *    Usar {@link #ID_FIRST_PLAYER} ou {@link #ID_SECOND_PLAYER}.
	 * @param nome O nome do jogador. */
	public void setPlayerName( int idPlayer, String nome )
	{
		m_fighterNames[idPlayer] = nome;
	}


	/** Carrega uma imagem a partir do JAR ou de um arquivo local.
	 * @param imageFile A imagem a ser carregada.
	 * @return Retorna a imagem carregada em caso de sucesso, ou null em caso de falha. */
	private Image loadImage( String imageFile )
	{
		// Tenta carregar a imagem
		URL urlImagem = PainelDeDesenho.class.getResource(imageFile);
		Image imagem = null;
		try {
			imagem = ImageIO.read( urlImagem );
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return imagem;
	}
	
	/** Modifica a barra de HP do personagem.
	 * @param idPlayer O identificador do jogador que terá seu HP modificado.
	 * @param life Um valor de 0 a 1, indicando a quantidade de life do personagem.
	 */
	public void setPlayerLifeBar( int idPlayer, float life )
	{
		if ( life < 0 )
			life = 0;
		m_life[idPlayer] = life;
	}
	
	
	/** Verifica se a animação atual de um jogador já foi tocada completamente (do primeiro ao
	 * último frame) pelo menos uma vez.
	 * @param idPlayer O identificador do jogador.
	 * @return Retorna true caso a animação atual do jogador já tenha sido tocada completamente,
	 *    pelo menos uma vez.*/
	public boolean isPlayerAnimationRepeating( int idPlayer )
	{
		return (m_animacoes[idPlayer].get( m_estadoLutadores[idPlayer] ).getNumberOfPlayedTimes() >= 1);
	}


	/** Desenha uma barra de life na tela.
	 * @param x A posição superior-esquerda onde a barra será desenhada (coordenada X).
	 * @param y A posição superior-esquerda onde a barra será desenhada (coordenada Y).
	 * @param life A quantidade de life na barra de life (variando de 0 a 1).
	 * @param name O nome do lutador.
	 */
	private void drawLifeBar( Graphics g, int x, int y, float life, String name )
	{
		final int lifeBarWidth = (int)((OUTER_LIFE_BAR_WIDTH - 2 * INNER_LIFE_BAR_X_SPACING) * life);

		g.setColor( OUTER_LIFE_BAR_COLOR );
		g.fillRect( x, y, OUTER_LIFE_BAR_WIDTH, OUTER_LIFE_BAR_HEIGHT);

		Color lifeBarColor;
		if ( life >= 0.7f )
			lifeBarColor = INNER_LIFE_BAR_COLOR_GOOD;
		else if ( life >= 0.2f )
			lifeBarColor = INNER_LIFE_BAR_COLOR_MEDIUM;
		else
			lifeBarColor = INNER_LIFE_BAR_COLOR_BAD;
		g.setColor( lifeBarColor );
		g.fillRect( x + INNER_LIFE_BAR_X_SPACING,
				y + INNER_LIFE_BAR_Y_SPACING,
				lifeBarWidth,
				OUTER_LIFE_BAR_HEIGHT - 2 * INNER_LIFE_BAR_Y_SPACING + 1);
		g.setColor( OUTER_LIFE_BAR_BORDER_COLOR );
		g.drawRect( x + INNER_LIFE_BAR_X_SPACING,
				y + INNER_LIFE_BAR_Y_SPACING,
				OUTER_LIFE_BAR_WIDTH - 2 * INNER_LIFE_BAR_X_SPACING,
				OUTER_LIFE_BAR_HEIGHT - 2 * INNER_LIFE_BAR_Y_SPACING);
		g.setColor( OUTER_LIFE_BAR_BORDER_COLOR );
		g.drawRect( x, y, OUTER_LIFE_BAR_WIDTH, OUTER_LIFE_BAR_HEIGHT);

		// Verifica se a fonte usada para desenhar os nomes já foi inicializada
		if ( m_namesFont == null )
			m_namesFont = g.getFont().deriveFont( Font.BOLD );
		g.setFont( m_namesFont );
		
		// Desenha nomes dos lutadores
		FontMetrics fontMetrics = g.getFontMetrics( g.getFont() );
		Rectangle2D nameBounds = fontMetrics.getStringBounds(name, g);
		final int nameX = x + (int)(OUTER_LIFE_BAR_WIDTH / 2 - nameBounds.getWidth() / 2 );
		final int nameY = y + (int)(OUTER_LIFE_BAR_HEIGHT / 2 + nameBounds.getHeight() / 2 );
		
		g.setColor(LIFE_BAR_PLAYER_NAME_COLOR);
		g.drawString(name, nameX, nameY );
	}


	/** Desenha o painel!
	 * @param g Objeto gráfico usado para desenhar no painél.
	 */
	@Override
	public void paint(Graphics g) {
		super.paint(g);

		// Limpa a tela
		final int panelWidth = this.getWidth();
		final int panelHeight = this.getHeight();
		g.setColor( this.getBackground() );
		g.fillRect(0, 0, panelWidth, panelHeight);
		g.drawImage( m_imgBackground, 0, 0, super.getWidth(),super.getHeight(),null );
		
		// Obtém os quadros atuais da animação de cada jogador
		Animacao animJogador[] = new Animacao[2];
		QuadroDeAnimacao quadroJogador[] = new QuadroDeAnimacao[2];
		Rectangle rectQuadroJogador[] = new Rectangle[2];
		
		for ( int f = 0; f < 2; f++ )
		{
			animJogador[f] = m_animacoes[f].get( m_estadoLutadores[f] );
			quadroJogador[f] = animJogador[f].getCurrentFrame();
			rectQuadroJogador[f] = quadroJogador[f].getImageRect();
			
			animJogador[f].updateAnimation();
		}

		// Desenha os jogadores
		final int screenCenterX = panelWidth/2 + rectQuadroJogador[0].width/4;
		final int screenCenterY = panelHeight-5;
		final int xPlayer[] = new int[] {
				screenCenterX - rectQuadroJogador[0].width,
				screenCenterX
		};
		final int yPlayers[] = new int[] {
				screenCenterY - rectQuadroJogador[0].height,
				screenCenterY - rectQuadroJogador[1].height
		};

		for ( int f = 0; f < 2; f++ )
		{
			Image curImg = m_imgLutador[f];
			if ( curImg != null )
			{
				int x1 = xPlayer[f];
				int y1 = yPlayers[f];
				int x2 = xPlayer[f] + rectQuadroJogador[f].width;
				int y2 = yPlayers[f] + rectQuadroJogador[f].height;
				if ( f == ID_SECOND_PLAYER )
				{
					int temp = x1;
					x1 = x2;
					x2 = temp;
				}
				g.drawImage( curImg, x1, y1, x2, y2,
						rectQuadroJogador[f].x, rectQuadroJogador[f].y,
						rectQuadroJogador[f].x + rectQuadroJogador[f].width,
						rectQuadroJogador[f].y + rectQuadroJogador[f].height, null );
			}
		}
		
		// Desenha as barras de life dos jogadores
		drawLifeBar(g, 0, 0, m_life[0], m_fighterNames[0]);
		drawLifeBar(g, panelWidth - OUTER_LIFE_BAR_WIDTH, 0, m_life[1], m_fighterNames[1]);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
	}

	@Override
	public void componentResized(ComponentEvent arg0) {
		repaint();
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		repaint();
	}
	
	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		try {
			m_updater.desativar();
			m_updater.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		m_updater = null;
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}


	/** Uma thread que roda para atualizar o painel, periodicamente. */
	private class AtualizadorDoPainel extends Thread
	{
		// PROPRIEDADES
		/** Flag indicando se a thread deve continuar rodando. */
		private Boolean m_isRunning = true;
		
		// MÉTODOS
		/** Roda =D */
		@Override
		public void run() {
			while ( true )
			{
				synchronized (m_isRunning) {
					if ( m_isRunning == false )
						break;
				}

				repaint();
			}
		}
		
		/** Faz com que a thread pare de rodar. */
		public void desativar() {
			synchronized (m_isRunning) {
				m_isRunning = false;
			}
		}
	}
}
