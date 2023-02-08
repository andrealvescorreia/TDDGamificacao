
import org.json.simple.JSONObject;

import pontuacao.excecoes.AdicionarPontosInvalidosException;
import pontuacao.excecoes.ObjetoJsonIvalidoException;
import pontuacao.excecoes.PontuacaoInvalidaException;

public class Pontuacao{
	private String usuario;
	private long pontos;
	private String tipo;

	Pontuacao(String usuario, long pontos, String tipo) throws PontuacaoInvalidaException {
		validarPontuacao(usuario, pontos, tipo);
		this.usuario = usuario;
		this.pontos  = pontos;
		this.tipo    = tipo;
	}

	Pontuacao(JSONObject jsonPontuacao) throws PontuacaoInvalidaException {
		validarJSONObject(jsonPontuacao);
		long pontos    = (long) jsonPontuacao.get("pontos");
		String usuario = (String) jsonPontuacao.get("usuario");
		String tipo    = (String) jsonPontuacao.get("tipo");
		validarPontuacao(usuario, pontos, tipo);
		this.usuario = usuario;
		this.pontos  = pontos;
		this.tipo    = tipo;
	}

	public void addPontos(long pontos) {
		validarAdicionarPontos(pontos);
		this.pontos += pontos;
	}

	public String toJSONString() {
		return this.toJSONObject().toJSONString();
	}

	@SuppressWarnings("unchecked")
	public JSONObject toJSONObject() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("usuario", usuario);
		jsonObj.put("pontos", pontos);
		jsonObj.put("tipo", tipo);
		return jsonObj;
	}

	public String getUsuario() {
		return this.usuario;
	}

	public String getTipo() {
		return this.tipo;
	}

	public long getPontos() {
		return this.pontos;
	}

	private void validarPontuacao(String usuario, long pontos, String tipo) {
		validarPontos(pontos);
		validarUsuario(usuario);
		validarTipo(tipo);
	}

	private void validarPontos(long pontos) {
		if (pontos < 1)
			throw new PontuacaoInvalidaException("Pontos não podem ser inferiores a 1");
	}

	private void validarUsuario(String usuario) {
		if (usuario.equals(""))
			throw new PontuacaoInvalidaException("Usuário inválido (\"\")");
		if (usuario.contains(" "))
			throw new PontuacaoInvalidaException("Usuário inválido (possui espaço(s))");
	}

	private void validarTipo(String tipo) {
		if (tipo.equals(""))
			throw new PontuacaoInvalidaException("Tipo inválido (\"\")");
		if (tipo.contains(" "))
			throw new PontuacaoInvalidaException("Tipo inválido (possui espaço(s))");

	}
	
	private void validarAdicionarPontos(long pontos)
			throws PontuacaoInvalidaException {
		if (pontos < 1)
			throw new AdicionarPontosInvalidosException("Nao é possível adicionar pontos inferiores a 1");
	}
	
	private void validarJSONObject(JSONObject jsonPontuacao) {
		if (faltaChaveValida(jsonPontuacao))
			throw new ObjetoJsonIvalidoException("Objeto Json em formato incorreto (faltando chave(s) valida(s))");
		if (temValorDeTipoInvalido(jsonPontuacao))
			throw new ObjetoJsonIvalidoException("Objeto Json em formato incorreto (valor(es) de tipo(s) invalido(s))");

	}

	private boolean faltaChaveValida(JSONObject jsonPontuacao) {
		return (   jsonPontuacao.get("pontos")  == null 
			    || jsonPontuacao.get("usuario") == null
				|| jsonPontuacao.get("tipo")    == null);
	}

	private boolean temValorDeTipoInvalido(JSONObject jsonPontuacao) {
		return !(  jsonPontuacao.get("usuario") instanceof String 
				&& jsonPontuacao.get("pontos")  instanceof Long
				&& jsonPontuacao.get("tipo")    instanceof String);
	}

}