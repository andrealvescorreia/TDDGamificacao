import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import excecoes.PontuacaoInvalidaException;

import org.json.simple.JSONArray;


public class ArmazenamentoPontuacao implements Armazenamento {
	public static final String CAMINHO_ARQUIVO = "saida.json";
	private ArrayList<Pontuacao> _cachePontuacoes = new ArrayList<Pontuacao>();// cache dos dados que também ficam no arquivo
	public ArmazenamentoPontuacao(){
		// recupera dados salvos em arquivo (se houver algum)
		try {
			JSONParser parser = new JSONParser();
			JSONArray jsonPontuacoes = (JSONArray) parser.parse(new FileReader(CAMINHO_ARQUIVO));
			for (int i = 0; i < jsonPontuacoes.size(); i++) {
				Pontuacao pontuacao = new Pontuacao( (JSONObject)jsonPontuacoes.get(i) );
				_cachePontuacoes.add(pontuacao);
			}
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
	
	

	public void guardarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
		Pontuacao pontuacao = new Pontuacao(usuario, pontos, tipo);
		_cachePontuacoes.add(pontuacao);
		salvarCacheNoArquivo();
	}
	
	private void salvarCacheNoArquivo() {
		JSONArray jsonPontuacoes = jsonDoCachePontuacoes();
		try {
			FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write(jsonPontuacoes.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private JSONArray jsonDoCachePontuacoes() {
		JSONArray jsonArrayPontuacoes = new JSONArray();
		for (int i = 0; i < _cachePontuacoes.size(); i++) {
			jsonArrayPontuacoes.add(_cachePontuacoes.get(i).toJSONObject());
		}
		return jsonArrayPontuacoes;
	}
	
	public long recuperarPontos(String usuario, String tipo) {
		long totalPontosRecuperados = 0;
		for (int i = 0; i < _cachePontuacoes.size(); i++) {
			Pontuacao pontuacao = _cachePontuacoes.get(i);
			if(usuario.equals(pontuacao.getUsuario()) && tipo.equals(pontuacao.getTipo())) {
				totalPontosRecuperados += pontuacao.getPontos();
			}
		}
		return totalPontosRecuperados;
	}

	public ArrayList<String> recuperarUsuariosRegistrados() {
		ArrayList<String> usuarios = new ArrayList<String>();
		for (int i = 0; i < _cachePontuacoes.size(); i++) {
			Pontuacao pontuacao = _cachePontuacoes.get(i);
			String usuario = pontuacao.getUsuario();
			if(usuarios.contains(usuario)) continue;
			usuarios.add(usuario);
		}
		return usuarios;
	}

	public ArrayList<String> recuperarTiposPontuacao(String usuario) {
		ArrayList<String> tiposDePontuacaoDoUsuario = new ArrayList<>();
		for (int i = 0; i < _cachePontuacoes.size(); i++) {
			Pontuacao pontuacao = _cachePontuacoes.get(i);
			if(tiposDePontuacaoDoUsuario.contains(pontuacao.getTipo())) 
				continue;
			if(usuario.equals(pontuacao.getUsuario()))
				tiposDePontuacaoDoUsuario.add(pontuacao.getTipo());
		}
		return tiposDePontuacaoDoUsuario;
	}
	
	
}