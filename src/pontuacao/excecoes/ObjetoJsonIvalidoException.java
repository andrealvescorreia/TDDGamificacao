package pontuacao.excecoes;

public class ObjetoJsonIvalidoException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public ObjetoJsonIvalidoException (String msg){
		super(msg);
	}
}
