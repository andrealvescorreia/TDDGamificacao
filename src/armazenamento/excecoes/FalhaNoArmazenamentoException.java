package armazenamento.excecoes;
public class FalhaNoArmazenamentoException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public FalhaNoArmazenamentoException (String msg){
		super(msg);
	}
}
