import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;

import pontuacao.excecoes.PontuacaoInvalidaException;

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
		var pontuacoesDoUsuario = new HashMap<String, Integer>();
		for(String tipo: armazenamento.recuperarTiposDePonto(usuario)) {
			int pontos = (int) armazenamento.recuperarPontos(usuario, tipo);
			pontuacoesDoUsuario.put(tipo, pontos);
		}
		return pontuacoesDoUsuario;
	}

	public ArrayList<String> ranking(String tipoDePonto) {
		var rankingTreeMap = new TreeMap<Integer, String>(Comparator.reverseOrder());
		
		for(String usuario: armazenamento.recuperarUsuariosRegistrados()) {
			ArrayList<String> tiposDePontosDoUsuario = armazenamento.recuperarTiposDePonto(usuario);
			if(tiposDePontosDoUsuario.contains(tipoDePonto)) {
				int pontuacaoDoUsuario = (int) armazenamento.recuperarPontos(usuario, tipoDePonto);
				rankingTreeMap.put(pontuacaoDoUsuario, usuario);
			}
		}
		
		var rankingDoTipoDePonto = new ArrayList<String>();
		for(var pontuacao : rankingTreeMap.entrySet())
			rankingDoTipoDePonto.add(pontuacao.getValue()+" "+pontuacao.getKey());
		return rankingDoTipoDePonto;
	}
}