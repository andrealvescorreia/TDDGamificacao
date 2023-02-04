import org.json.simple.JSONObject;

import excecoes.AdicionarPontosInvalidosException;
import excecoes.ObjetoJsonIvalidoException;
import excecoes.PontuacaoInvalidaException;

public class Pontuacao{
		private String usuario;
		private long pontos;
		private String tipo;
		
		Pontuacao(String usuario, long pontos, String tipo) 
				throws PontuacaoInvalidaException {
			validarPontuacao(usuario, pontos, tipo);
			this.usuario = usuario;
			this.pontos = pontos;
			this.tipo = tipo;
		}
		Pontuacao(JSONObject jsonPontuacao) 
				throws PontuacaoInvalidaException {
			validarJSONObject(jsonPontuacao);
			long pontos = (long)jsonPontuacao.get("pontos");
			String usuario = (String) jsonPontuacao.get("usuario");
			String tipo = (String) jsonPontuacao.get("tipo");
			validarPontuacao(usuario, pontos, tipo);
			this.usuario = usuario;
			this.pontos = pontos;
			this.tipo = tipo;
		}
		
		private void validarJSONObject(JSONObject jsonPontuacao) {
			if(jsonPontuacao.get("pontos")  == null 
			|| jsonPontuacao.get("usuario") == null 
			|| jsonPontuacao.get("tipo")    == null) 
				throw new ObjetoJsonIvalidoException("Objeto Json em formato incorreto");
			
			if(jsonPontuacao.get("usuario") instanceof String 
			&& jsonPontuacao.get("pontos") instanceof Long
			&& jsonPontuacao.get("tipo")   instanceof String)
				return;
			
			throw new ObjetoJsonIvalidoException("Objeto Json em formato incorreto");
			
		}
		
		private void validarPontuacao(String usuario, long pontos, String tipo) 
				throws PontuacaoInvalidaException {
			if(pontos < 1) 
				throw new PontuacaoInvalidaException("Pontos não podem ser inferiores a 1");
			if(usuario.equals("")) 
				throw new PontuacaoInvalidaException("Usuário inválido (\"\") ");
			if(usuario.contains(" "))
				throw new PontuacaoInvalidaException("Usuário inválido (possui espaço(s)) ");
			if(tipo.equals("")) 
				throw new PontuacaoInvalidaException("Tipo inválido (\"\") ");
			if(tipo.contains(" "))
				throw new PontuacaoInvalidaException("Tipo inválido (possui espaço(s)) ");
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
		public void addPontos(long pontos) {
			if(pontos < 1) 
				throw new AdicionarPontosInvalidosException("Pontos não podem ser inferiores a 1");
			this.pontos += pontos;
		}
	}