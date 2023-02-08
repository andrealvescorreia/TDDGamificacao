import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

import pontuacao.excecoes.PontuacaoInvalidaException;

public class MockArmazenamento implements Armazenamento {
	
	private HashMap<String, HashMap<String, Integer>> _pontuacoesUsuarios;
	private ArrayList<String> _chamadasRecebidasParaGuardarPontuacao = new ArrayList<String>();
	
	public void setPontuacoesUsuarios(HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios) {
		this._pontuacoesUsuarios = pontuacoesUsuarios;
	}
	
	@Override
	public void guardar(Pontuacao pontuacao){
		_chamadasRecebidasParaGuardarPontuacao.add(
				pontuacao.getUsuario() + " " + pontuacao.getPontos() + " " + pontuacao.getTipo()
		);
	}
	
	public void verificaChamadasGuardarPontuacao(ArrayList<String> chamadasEsperadas) {
		assertEquals(chamadasEsperadas, _chamadasRecebidasParaGuardarPontuacao);
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
}