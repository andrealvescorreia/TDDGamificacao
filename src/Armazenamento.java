import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;


public class Armazenamento {
	public static final String CAMINHO_ARQUIVO = "saida.json";
	private JSONArray dados = new JSONArray();// cache dos dados que também ficam no arquivo
	
	
	
	public Armazenamento(){
		// recupera dados salvos em arquivo (se houver algum)
		try {
			JSONParser parser = new JSONParser();
			dados = (JSONArray) parser.parse(new FileReader(CAMINHO_ARQUIVO));
		}
		catch (Exception e){
			criarArquivoLimpo();
		}
	}
	
	
	private void criarArquivoLimpo() {
		try {
			FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write("");
			fileWriter.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public void guardarPontuacao(String usuario, long pontos, String tipo) {
		if(pontos < 1) 
			throw new PontuacaoInvalidaException("Pontos não podem ser inferiores a 1");
		if(usuario.equals("")) 
			throw new PontuacaoInvalidaException("Usuário inválido (\"\") ");
		if(tipo.equals("")) 
			throw new PontuacaoInvalidaException("Tipo inválido (\"\") ");
		
		JSONObject pontuacao = new JSONObject();
		
		pontuacao.put("tipo", tipo);
		pontuacao.put("pontos", pontos);
		pontuacao.put("usuario", usuario);
	
		dados.add(pontuacao);
		
		try {
			FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write(dados.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public long recuperarPontos(String usuario, String tipo) {
		long totalPontosRecuperados = 0;
		for (int i = 0; i < dados.size(); i++) {			
			JSONObject pontuacao = (JSONObject) dados.get(i);
			String tipoDaPontuacao = (String) pontuacao.get("tipo");
			String usuarioDaPontuacao = (String) pontuacao.get("usuario");
			if(usuario.equals(usuarioDaPontuacao) && tipo.equals(tipoDaPontuacao)) {
				totalPontosRecuperados += (long) pontuacao.get("pontos");
			}
		}
		return totalPontosRecuperados;
	}


	public ArrayList<String> recuperarUsuariosRegistrados() {
		ArrayList<String> usuarios = new ArrayList<String>();
		for (int i = 0; i < dados.size(); i++) {
			JSONObject pontuacao = (JSONObject) dados.get(i);
			String usuario = (String) pontuacao.get("usuario");
			if(usuarios.contains(usuario) == false) {
				usuarios.add(usuario);
			}
		}
		return usuarios;
	}


	public ArrayList<String> recuperarTiposPontuacao(String usuario) {
		ArrayList<String> tiposRecuperados = new ArrayList<>();
		for (int i = 0; i < dados.size(); i++) {
			JSONObject pontuacao = (JSONObject) dados.get(i);
			String tipoPontuacao = (String)pontuacao.get("tipo");
			String usuarioPontuacao = (String)pontuacao.get("usuario");
			if(usuario.equals(usuarioPontuacao) && tiposRecuperados.contains(tipoPontuacao) == false) {
				tiposRecuperados.add(tipoPontuacao);
			}
		}

		return tiposRecuperados;
	}

}
