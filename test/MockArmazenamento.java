import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	private ArrayList<String> execucoes = new ArrayList<String>();
	private boolean pontuacaoInvalida;
	private ArrayList<String> tiposDePontoDoUsuario = new ArrayList<String>();
	private Integer pontosDoTipo;
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		if(pontuacaoInvalida) {
			throw new PontuacaoInvalidaException("Simulação do mock de pontuacao invalida");
		}
		execucoes.add(usuario + " " + pontos + " " + tipo);
	}

	@Override
	public long recuperarPontos(String usuario, String tipo) {
		return pontosDoTipo;
	}

	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> recuperarTiposPontuacao(String usuario) {
		return tiposDePontoDoUsuario;
	}
	
	public void verifica(ArrayList<String> execucoesEsperadas) {
		assertEquals(execucoesEsperadas, execucoes);
	
	}

	public void simulePontuacaoInvalida() {
		pontuacaoInvalida = true;
	}

	
	public void setPontuacoesUsuario(HashMap<String, Integer> pontuacoesUsuario) {
		for ( String tipo : pontuacoesUsuario.keySet() ) {
		    this.tiposDePontoDoUsuario.add(tipo);
		    this.pontosDoTipo = pontuacoesUsuario.get(tipo);
		}
	}

}
