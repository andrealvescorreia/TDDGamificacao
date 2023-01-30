import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	ArrayList<String> execucoes = new ArrayList<String>();
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		execucoes.add(usuario + " " + pontos + " " + tipo);
	}

	@Override
	public long recuperarPontos(String usuario, String tipo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> recuperarTiposPontuacao(String usuario) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void verifica(ArrayList<String> execucoesEsperadas) {
		assertEquals(execucoesEsperadas, execucoes);
	
	}

}
