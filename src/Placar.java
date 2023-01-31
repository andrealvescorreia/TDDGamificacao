import java.util.ArrayList;
import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class Placar {
	
	private Armazenamento armazenamento;
	
	public Placar(Armazenamento armazenamento) {
		this.armazenamento = armazenamento;
	}

	public void adicionarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
		armazenamento.guardarPontuacao(usuario, pontos, tipo);
	}

	public HashMap<String, Integer> pontuacoes(String usuario) {
		HashMap<String, Integer> pontuacoesDoUsuario = new HashMap<String, Integer>();
		ArrayList<String> tiposDePontosDoUsuario = armazenamento.recuperarTiposPontuacao(usuario);
		for(String tipo: tiposDePontosDoUsuario) {
			int pontos = (int) armazenamento.recuperarPontos(usuario, tipo);
			pontuacoesDoUsuario.put(tipo, pontos);
		}
		return pontuacoesDoUsuario;
	}

	public ArrayList<String> ranking(String tipoDePonto) {
		ArrayList<String> rankingDoTipoDePonto = new ArrayList<String>();
		ArrayList<String> todosOsUsuarios = armazenamento.recuperarUsuariosRegistrados();
		
		for(String usuario: todosOsUsuarios) {
			ArrayList<String> tiposDePontosDoUsuario = armazenamento.recuperarTiposPontuacao(usuario);
			if(tiposDePontosDoUsuario.contains(tipoDePonto)) {
				long pontuacaoDoUsuario = armazenamento.recuperarPontos(usuario, tipoDePonto);
				rankingDoTipoDePonto.add(usuario+" "+pontuacaoDoUsuario);
			}
		}
		return rankingDoTipoDePonto;
	}

}
