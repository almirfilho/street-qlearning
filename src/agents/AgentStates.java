package agents;

/** Provê funcionalidades ligadas aos estados dos agentes. */
public class AgentStates {

	// PROPRIEDADES
	/** Enumera��o com todos os estados dos lutadores. */
	public enum EAcoesDoLutador
	{
		evParar,
		evDefender,
		evAgachar,
		evPular,
		evAtaqueAgachado,
		evAtaquePulo,
		evAtaque,
		
		// Estados especiais
		TOTAL_STATES,
		evMorto,
		evVencedor
	}
	
	// MÉTODOS
	/** Obtém o nome de um estado dado.
	 * @param estado O estado cujo nome deve ser obtido. */
	public static String getStateName( EAcoesDoLutador estado )
	{
		String retorno = null;
		switch ( estado )
		{
		case evParar:
			retorno = "PARAR";
			break;
		case evDefender:
			retorno = "DEFENDER";
			break;
		case evAgachar:
			retorno = "AGACHAR";
			break;
		case evPular:
			retorno = "PULAR";
			break;
		case evAtaqueAgachado:
			retorno = "ATAQUE_AGACHADO";
			break;
		case evAtaquePulo:
			retorno = "ATAQUE_PULO";
			break;
		case evAtaque:
			retorno = "ATAQUE";
			break;
		default:
			retorno = "DESCONHECIDO";
			break;
		}
		return retorno;
	}
	
	/** Obtém o estado, a partir de uma string.
	 * @param stateName O nome do estado que será pesquisado.
	 * @return Retorna o estado correspondente ao nome dado, em caso de sucesso.
	 *    Retorna uma referência nula em caso de falha. */
	public static EAcoesDoLutador getStateByName( String stateName )
	{
		EAcoesDoLutador possibleStates[] = EAcoesDoLutador.values();
		for ( EAcoesDoLutador curState : possibleStates )
		{
			String curPossibleStateName = getStateName(curState);
			if ( stateName.equals( curPossibleStateName ) )
				return curState;
		}
		return null;
	}


	/** Obtém um estado, dado o seu índice na enumeração.
	 * @param index O índice do estado a ser obtido.
	 * @return Retorna o estado correspondente ao índice especificado. */
	public static EAcoesDoLutador getStateByIndex( int index )
	{
		EAcoesDoLutador possibleStates[] = EAcoesDoLutador.values();
		return possibleStates[index];
	}
}
