import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

import excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	private String usuarioRecebido;
	private long pontosRecebido;
	private String tipoRecebido;
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		usuarioRecebido = usuario;
		pontosRecebido = pontos;
		tipoRecebido = tipo;
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
	
	public void verificaPedido(String usuarioEsperado, long pontosEsperado, String tipoEsperado) {
		assertEquals(usuarioEsperado, usuarioRecebido);
		assertEquals(pontosEsperado, pontosRecebido);
		assertEquals(tipoEsperado, tipoRecebido);
	}

}
