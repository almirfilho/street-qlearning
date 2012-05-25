package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

/** O frame onde a aplica��o estar� rodando. */
public class FramePrincipal extends JFrame {
	private static final long serialVersionUID = -5620839732900370082L;

	// PROPRIEDADES
	/** O pain�l onde as a��es do jogo ser�o desenhada. */
	private final PainelDeDesenho m_painelDesenho;

	// M�TODOS
	/** Construtor. */
	public FramePrincipal() {
		super("Estriter Faiter");

		setLayout( new GridLayout(1,1) );
		setSize( new Dimension(800, 600) );
		setMinimumSize(new Dimension(430, 300));

		m_painelDesenho = new PainelDeDesenho( this );
		add(m_painelDesenho);
	}
	
	/** Obt�m o painel onde os desenhos s�o feitos.
	 * @return Retorna o pain�l de desenhos. */
	public PainelDeDesenho getPainelDeDesenho()
	{
		return m_painelDesenho;
	}
}
