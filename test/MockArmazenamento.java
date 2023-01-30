import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	private ArrayList<String> execucoes = new ArrayList<String>();
	private boolean pontuacaoInvalida;
	private HashMap<String, Integer> pontuacoesUsuario = new HashMap<String, Integer>();
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		if(pontuacaoInvalida) {
			throw new PontuacaoInvalidaException("Simulação do mock de pontuacao invalida");
		}
		execucoes.add(usuario + " " + pontos + " " + tipo);
	}

	@Override
	public long recuperarPontos(String usuario, String tipo) {
		int pontos = 0;
		for ( String chaveTipo : this.pontuacoesUsuario.keySet() ) {
		    if(chaveTipo.equals(tipo)) {
		    	pontos = pontuacoesUsuario.get(chaveTipo);
		    }
		}
		return pontos;
		
	}

	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> recuperarTiposPontuacao(String usuario) {
		ArrayList<String> tiposDePontoDoUsuario = new ArrayList<String>();
		for ( String tipo : this.pontuacoesUsuario.keySet() ) {
		    tiposDePontoDoUsuario.add(tipo);
		}
		return tiposDePontoDoUsuario;
	}
	
	public void verifica(ArrayList<String> execucoesEsperadas) {
		assertEquals(execucoesEsperadas, execucoes);
	
	}

	public void simulePontuacaoInvalida() {
		pontuacaoInvalida = true;
	}

	
	public void setPontuacoesUsuario(HashMap<String, Integer> pontuacoesUsuario) {
		this.pontuacoesUsuario = pontuacoesUsuario;
	}

}
