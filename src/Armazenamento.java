import java.util.ArrayList;

import armazenamento.excecoes.FalhaNoArmazenamentoException;
import pontuacao.excecoes.PontuacaoInvalidaException;

public interface Armazenamento {
	public void guardar(Pontuacao pontuacao) 
			throws PontuacaoInvalidaException, FalhaNoArmazenamentoException;
	public long recuperarPontos(String usuario, String tipo);
	public ArrayList<String> recuperarUsuariosRegistrados();
	public ArrayList<String> recuperarTiposDePonto(String usuario);
}