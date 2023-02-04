package pontuacao.excecoes;

public class PontuacaoInvalidaException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public PontuacaoInvalidaException (String msg){
		super(msg);
	}
}
