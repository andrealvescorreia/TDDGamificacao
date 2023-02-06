import java.util.ArrayList;

import armazenamento.excecoes.FalhaNoArmazenamentoException;
import pontuacao.excecoes.PontuacaoInvalidaException;

public interface Armazenamento {
	public void guardarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException, FalhaNoArmazenamentoException;
	public long recuperarPontos(String usuario, String tipo);
	public ArrayList<String> recuperarUsuariosRegistrados();
	public ArrayList<String> recuperarTiposDePonto(String usuario);
}