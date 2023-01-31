import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	
	private ArrayList<String> _chamadasRecebidasParaGuardarPontuacao = new ArrayList<String>();
	private boolean _simulacaoPontuacaoInvalida;
	private HashMap<String, HashMap<String, Integer>> _pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		if(_simulacaoPontuacaoInvalida) 
			throw new PontuacaoInvalidaException("Simulação do mock de pontuacao invalida");
		_chamadasRecebidasParaGuardarPontuacao.add(usuario + " " + pontos + " " + tipo);
	}
	
	@Override
	public long recuperarPontos(String usuario, String tipo) {
		int pontos = 0;
		for ( String chaveUsuario : this._pontuacoesUsuarios.keySet() ) {
			if(chaveUsuario.equals(usuario)) {
				/*if(_pontuacoesUsuarios.get(chaveUsuario).get(tipo)) {
					
				}*/
			}
		    /*if(chaveUsuario.equals(tipo) && usuario.equals()) 
		    	pontos = _pontuacoesUsuarios.get(chaveTipo);*/
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
		for ( String tipo : this._pontuacoesUsuarios.keySet() ) {
		    tiposDePontoDoUsuario.add(tipo);
		}
		return tiposDePontoDoUsuario;
	}
	
	
	
	public void verificaChamadasGuardarPontuacao(ArrayList<String> chamadasEsperadas) {
		assertEquals(chamadasEsperadas, _chamadasRecebidasParaGuardarPontuacao);
	}
	public void simulePontuacaoInvalida() {
		_simulacaoPontuacaoInvalida = true;
	}
	//                          <usuario (ex:guerra), <tipo (ex:moeda), pontos (ex: 2)>>
	public void setPontuacoesUsuarios(HashMap<String, HashMap<String, Integer>> pontuacoesDoUsuario) {
		this._pontuacoesUsuarios = pontuacoesDoUsuario;
	}

}
