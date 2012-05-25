package gui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JFrame;

/** O frame onde a aplicação estará rodando. */
public class FramePrincipal extends JFrame {
	private static final long serialVersionUID = -5620839732900370082L;

	// PROPRIEDADES
	/** O painél onde as ações do jogo serão desenhada. */
	private final PainelDeDesenho m_painelDesenho;

	// MÉTODOS
	/** Construtor. */
	public FramePrincipal() {
		super("Estriter Faiter");

		setLayout( new GridLayout(1,1) );
		setSize( new Dimension(800, 600) );
		setMinimumSize(new Dimension(430, 300));

		m_painelDesenho = new PainelDeDesenho( this );
		add(m_painelDesenho);
	}
	
	/** Obtém o painel onde os desenhos são feitos.
	 * @return Retorna o painél de desenhos. */
	public PainelDeDesenho getPainelDeDesenho()
	{
		return m_painelDesenho;
	}
}
