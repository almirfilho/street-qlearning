package gui;

import java.awt.Rectangle;

/** Representa um quadro de uma animação de um sprite. */
public class QuadroDeAnimacao {

	// PROPRIEDADES
	/** O retângulo representando a parte da imagem que será exibida no frame. */
	private Rectangle m_imgRect = new Rectangle();
	/** Duração, em milisegundos, do frame. */
	private int m_duration;

	// MÉTODOS
	/** Constructor.
	 * @param x Coordenada do ponto superior esquerdo inicial do retângulo. (eixo X)
	 * @param y Coordenada do ponto superior esquerdo inicial do retângulo. (eixo Y)
	 * @param width A largura do retângulo que será exibido pelo frame.
	 * @param height A altura do retângulo que será exibido pelo frame.
	 * @param duration A duração do frame, em milisegundos.
	 */
	public QuadroDeAnimacao( int x, int y, int width, int height, int duration )
	{
		m_imgRect.setBounds(x, y, width, height);
		m_duration = duration;
	}
	
	/** Obtém o retângulo da imagem que deve ser exibido no frame.
	 * @return Retorna o retângulo da imagem, que deverá ser exibido pelo frame.
	 */
	public Rectangle getImageRect()
	{
		return m_imgRect;
	}


	/** Obtém a duração deste frame.
	 * @return Retorna a duração do frame, dada em milisegundos. */
	public int getDurationMs()
	{
		return m_duration;
	}
}
