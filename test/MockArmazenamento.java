import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import pontuacao.excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	
	private HashMap<String, HashMap<String, Integer>> _pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
	private ArrayList<String> _chamadasRecebidasParaGuardarPontuacao = new ArrayList<String>();
	private boolean _simulacaoPontuacaoInvalida;
	
	
	@Override
	public void guardarPontuacao(Pontuacao pontuacao){
		if(_simulacaoPontuacaoInvalida) 
			throw new PontuacaoInvalidaException("Simulação do mock de pontuacao invalida");
		_chamadasRecebidasParaGuardarPontuacao.add(pontuacao.getUsuario() + " " + pontuacao.getPontos() + " " + pontuacao.getTipo());
	}
	
	@Override
	public long recuperarPontos(String usuario, String tipo) {	
		HashMap<String,Integer> pontuacoesDoUsuario = Optional.ofNullable(this._pontuacoesUsuarios.get(usuario))
					  										  .orElse(new HashMap<String,Integer>());
		return pontuacoesDoUsuario.get(tipo);
	}
	
	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		var usuarios = new ArrayList<String>();
		for (String usuario : _pontuacoesUsuarios.keySet())  
		    usuarios.add(usuario);
		return usuarios;
	}
	
	@Override
	public ArrayList<String> recuperarTiposDePonto(String usuario) {
		var tiposDePontoDoUsuario = new ArrayList<String>();
		HashMap<String,Integer> pontuacoesDoUsuario = Optional.ofNullable(this._pontuacoesUsuarios.get(usuario))
                	   										  .orElse(new HashMap<String,Integer>());
		for (String tipo : pontuacoesDoUsuario.keySet())
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