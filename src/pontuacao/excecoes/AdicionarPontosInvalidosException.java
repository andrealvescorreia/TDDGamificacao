package pontuacao.excecoes;

public class AdicionarPontosInvalidosException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public AdicionarPontosInvalidosException (String msg){
		super(msg);
	}
}
