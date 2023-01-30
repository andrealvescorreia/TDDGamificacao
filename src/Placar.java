import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class Placar {
	
	private Armazenamento armazenamentoPontuacoes;
	
	public Placar(Armazenamento armazenamento) {
		armazenamentoPontuacoes = armazenamento;
	}

	public void adicionarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
		armazenamentoPontuacoes.guardarPontuacao(usuario, pontos, tipo);
	}

	public HashMap<String, Integer> pontuacoes(String usuario) {
		return new HashMap<String, Integer>();
	}

}
