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
		ArrayList<String> tiposDePontosDoUsuario = armazenamento.recuperarTiposDePonto(usuario);
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
			ArrayList<String> tiposDePontosDoUsuario = armazenamento.recuperarTiposDePonto(usuario);
			if(tiposDePontosDoUsuario.contains(tipoDePonto)) {
				long pontuacaoDoUsuario = armazenamento.recuperarPontos(usuario, tipoDePonto);
				rankingDoTipoDePonto.add(usuario+" "+pontuacaoDoUsuario);
			}
		}
		return ordenarRanking(rankingDoTipoDePonto);
	}
	
	private ArrayList<String> ordenarRanking(ArrayList<String> ranking){
		ArrayList<String> rankingOrdenado = ranking;
		for(int i = 0; i < rankingOrdenado.size() - 1; i++) {
	      for(int j = 0; j < rankingOrdenado.size() - 1 - i; j++) {
	    	int valor1 = Integer.parseInt(rankingOrdenado.get(j).split(" ")[1]);
	    	int valor2 = Integer.parseInt(rankingOrdenado.get(j + 1).split(" ")[1]);
	        if(valor1 < valor2) {
	          String aux = rankingOrdenado.get(j);
	          rankingOrdenado.set(j, rankingOrdenado.get(j+1));
	          rankingOrdenado.set(j+1, aux);
	        }
	      }
	    }
		return rankingOrdenado;
	}
}
