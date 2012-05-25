package gui;

import java.util.Vector;

/** Armazena uma animação de um sprite, que consiste em uma sequência de frames. */
public class Animacao {

	// CONSTANTES
	/** Uma timestamp inválida, que indica que não ouveram atualizações de frames na animação. */
	private static final int INVALID_TIMESTAMP = -1;
	
	// PROPRIEDADES
	/** O vetor com todos os frames da animação. */
	private Vector<QuadroDeAnimacao> m_frames = new Vector<QuadroDeAnimacao>();
	/** Indica o índice do frame atual no vetor de frames. */
	private int m_currentFrameIndex = 0;
	/** Timestamp do último momento em que a animação fez uma mudança de quadro. */
	private long m_lastFrameUpdateTime = INVALID_TIMESTAMP;
	/** Flag indicando se a animação está sendo tocada. */
	private boolean m_isPlaying = false;
	/** Flag indicando se a animação deve ser tocada em loop. */
	private boolean m_isLooping = true;
	/** N�mero de vezes que a anima��o j� tocou completamente (do primeiro ao �ltimo frame). */
	private Integer m_timesPlayed = 0;

	// MÉTODOS
	/** Adiciona um novo quadro à animação.
	 * @param frame O quadro a ser adicionado à animação. */
	public void addFrame( QuadroDeAnimacao frame )
	{
		m_frames.add(frame);
	}


	/** Obtém o total de frames na animação.
	 * @return Retorna o número total de frames na animação. */
	public int getTotalFrames()
	{
		return m_frames.size();
	}
	
	/** Obtém um quadro específico da animação.
	 * @param index O índice do quadro que será obtido.
	 * @return Retorna o quadro especificado. */
	public QuadroDeAnimacao getFrameByIndex( int index )
	{
		return m_frames.get(index);
	}
	
	
	/** Obtém o quadro atual que está sendo tocado pela animação.
	 * @return Retorna o quadro atual. */
	public QuadroDeAnimacao getCurrentFrame()
	{
		return m_frames.get(m_currentFrameIndex);
	}

	
	/** Configura uma anima��o para entrar em loop.
	 * @param loop Flag indicando se a anima��o deve ser tocada em loop. */
	public void setLoop( boolean loop )
	{
		m_isLooping = loop;
	}

	
	/** Verifica se a anima��o deve ser tocada em loop.
	 * @return Retorna true caso a anima��o deva ser tocada em loop. */
	public boolean isLooping()
	{
		return m_isLooping;
	}
	
	
	/** Configura uma animação para ser tocada.
	 * @param loop Flag indicando se a animação deve ser tocada em loop. */
	public void play()
	{
		m_lastFrameUpdateTime = getCurrentTime();
		m_isPlaying = true;
		m_currentFrameIndex = 0;
		synchronized (m_timesPlayed) {
			m_timesPlayed = 0;
		}
	}
	
	/** Configura a animação para ser parada. */
	public void stop()
	{
		m_isLooping = false;
		m_isPlaying = false;
	}
	
	/** Obt�m o n�mero de vezes que a anima��o j� tocou completamente.
	 * @return Retorna o n�mero de vezes que a anima��o j� foi tocada. */
	public int getNumberOfPlayedTimes()
	{
		synchronized (m_timesPlayed) {
			return m_timesPlayed;
		}
	}

	/** Obtém o tempo atual, para ser usado no cálculo de tempo transcorrido.
	 * @return Retorna o tempo atual, em milisegundos. */
	private static long getCurrentTime()
	{
		return System.currentTimeMillis();
	}
	
	
	/** Atualiza as propriedades internas da animação. */
	public void updateAnimation()
	{
		if ( m_isPlaying == false )
			return;

		// Verifica se deve haver uma mudança de frames
		QuadroDeAnimacao quadroAtual = m_frames.get(m_currentFrameIndex);

		long currentTime = getCurrentTime();
		long ellapsedTimeMs = currentTime - m_lastFrameUpdateTime;
		if ( ellapsedTimeMs > quadroAtual.getDurationMs() )
		{
			m_currentFrameIndex++;
			if ( m_currentFrameIndex == m_frames.size() )
			{
				if ( m_isLooping )
					m_currentFrameIndex = 0;
				else
				{
					m_currentFrameIndex--;
					stop();
				}
				synchronized(m_timesPlayed) {
					m_timesPlayed++;
				}
			}
			m_lastFrameUpdateTime = currentTime;
		}
	}
}
