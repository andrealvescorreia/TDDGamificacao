import java.util.ArrayList;
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
		ArrayList<String> tiposDePontosDoUsuario = armazenamentoPontuacoes.recuperarTiposPontuacao(usuario);
		HashMap<String, Integer> pontuacoesDoUsuario = new HashMap<String, Integer>();
		
		for(int i = 0; i < tiposDePontosDoUsuario.size(); i++) {
			String tipo = tiposDePontosDoUsuario.get(i);
			int pontos = (int) armazenamentoPontuacoes.recuperarPontos(usuario, tipo);
			pontuacoesDoUsuario.put(tipo, pontos);
		}
		
		return pontuacoesDoUsuario;
	}

}
