import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	
	private HashMap<String, HashMap<String, Integer>> _pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
	private ArrayList<String> _chamadasRecebidasParaGuardarPontuacao = new ArrayList<String>();
	private boolean _simulacaoPontuacaoInvalida;
	
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		if(_simulacaoPontuacaoInvalida) 
			throw new PontuacaoInvalidaException("Simulação do mock de pontuacao invalida");
		_chamadasRecebidasParaGuardarPontuacao.add(usuario + " " + pontos + " " + tipo);
	}
	
	@Override
	public long recuperarPontos(String usuario, String tipo) {
		if(usuarioComTipoDePontoNaoExiste(usuario, tipo))
			return 0;
		return _pontuacoesUsuarios.get(usuario).get(tipo);
		
	}
	
	private boolean usuarioComTipoDePontoNaoExiste(String usuario, String tipoDePonto) {
		if(usuarioNaoExiste(usuario)) return true;
		return !_pontuacoesUsuarios.get(usuario).containsKey(tipoDePonto);
	}
	private boolean usuarioNaoExiste(String usuario) {
		return !_pontuacoesUsuarios.containsKey(usuario);
	}
	
	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		ArrayList<String> usuarios = new ArrayList<String>();
		for (String usuario : _pontuacoesUsuarios.keySet())  
		    usuarios.add(usuario);
		return usuarios;
	}
	
	@Override
	public ArrayList<String> recuperarTiposDePonto(String usuario) {
		ArrayList<String> tiposDePontoDoUsuario = new ArrayList<String>();
		if(usuarioNaoExiste(usuario))
			return tiposDePontoDoUsuario;// vazio
		
		for ( String tipo : this._pontuacoesUsuarios.get(usuario).keySet() )
			tiposDePontoDoUsuario.add(tipo);
		
		return tiposDePontoDoUsuario;
	}
	
	public void verificaChamadasGuardarPontuacao(ArrayList<String> chamadasEsperadas) {
		assertEquals(chamadasEsperadas, _chamadasRecebidasParaGuardarPontuacao);
	}
	public void simulePontuacaoInvalida() {
		_simulacaoPontuacaoInvalida = true;
	}
	//                          <usuario (ex:"guerra"), <tipo (ex:"moeda"), pontos (ex: 2)>>
	public void setPontuacoesUsuarios(HashMap<String, HashMap<String, Integer>> pontuacoesDoUsuario) {
		this._pontuacoesUsuarios = pontuacoesDoUsuario;
	}
}
