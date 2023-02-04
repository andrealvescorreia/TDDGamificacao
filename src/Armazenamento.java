import java.util.ArrayList;

import pontuacao.excecoes.PontuacaoInvalidaException;

public interface Armazenamento {
	public void guardarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException;
	public long recuperarPontos(String usuario, String tipo);
	public ArrayList<String> recuperarUsuariosRegistrados();
	public ArrayList<String> recuperarTiposDePonto(String usuario);
}