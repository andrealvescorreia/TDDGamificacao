import org.json.simple.JSONObject;

import excecoes.PontuacaoInvalidaException;

public class Pontuacao{
		private String usuario;
		private long pontos;
		private String tipo;
		
		Pontuacao(String usuario, long pontos, String tipo){
			if(pontos < 1) 
				throw new PontuacaoInvalidaException("Pontos não podem ser inferiores a 1");
			if(usuario.equals("")) 
				throw new PontuacaoInvalidaException("Usuário inválido (\"\") ");
			if(tipo.equals("")) 
				throw new PontuacaoInvalidaException("Tipo inválido (\"\") ");
			
			this.usuario = usuario;
			this.pontos = pontos;
			this.tipo = tipo;
		}
		Pontuacao(JSONObject json){
			long pontos = (long) json.get("pontos");
			String usuario = (String) json.get("usuario");
			String tipo = (String) json.get("tipo");
			
			if(pontos < 1) 
				throw new PontuacaoInvalidaException("Pontos não podem ser inferiores a 1");
			if(usuario.equals("")) 
				throw new PontuacaoInvalidaException("Usuário inválido (\"\") ");
			if(tipo.equals("")) 
				throw new PontuacaoInvalidaException("Tipo inválido (\"\") ");
			
			this.usuario = usuario;
			this.pontos = pontos;
			this.tipo = tipo;
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
	}