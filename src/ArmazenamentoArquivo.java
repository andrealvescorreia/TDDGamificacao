import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import excecoes.PontuacaoInvalidaException;

import org.json.simple.JSONArray;


public class ArmazenamentoArquivo implements Armazenamento {
	public static final String CAMINHO_ARQUIVO = "saida.json";
	private ArrayList<Pontuacao> _cachePontuacoes = new ArrayList<Pontuacao>();// cache dos dados que também ficam no arquivo
	public ArmazenamentoArquivo(){
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
	
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
		//Pontuacao pontuacao = new Pontuacao(usuario, pontos, tipo);
		for(Pontuacao pontuacaoExistente : _cachePontuacoes) {
			if(pontuacaoExistente.getUsuario().equals(usuario) && pontuacaoExistente.getTipo().equals(tipo)) {
				pontuacaoExistente.addPontos(pontos);
				salvarCacheNoArquivo();
				return;
			}
		}
		Pontuacao novaPontuacao = new Pontuacao(usuario, pontos, tipo);
		_cachePontuacoes.add(novaPontuacao);
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
		for(Pontuacao pontuacao : _cachePontuacoes) 
			jsonArrayPontuacoes.add(pontuacao.toJSONObject());
		return jsonArrayPontuacoes;
	}
	
	@Override
	public long recuperarPontos(String usuario, String tipo) {
		for(Pontuacao pontuacao : _cachePontuacoes) {
			if(usuario.equals(pontuacao.getUsuario()) && tipo.equals(pontuacao.getTipo()))
				return pontuacao.getPontos();
		}
		return 0;
	}
	
	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		var usuarios = new ArrayList<String>();
		for(Pontuacao pontuacao : _cachePontuacoes) {
			String usuario = pontuacao.getUsuario();
			if(usuarios.contains(usuario)) continue;
			usuarios.add(usuario);
		}
		return usuarios;
	}
	
	@Override
	public ArrayList<String> recuperarTiposDePonto(String usuario) {
		var tiposDePontosDoUsuario = new ArrayList<String>();
		for(Pontuacao pontuacao : _cachePontuacoes) {
			if(tiposDePontosDoUsuario.contains(pontuacao.getTipo())) 
				continue;
			if(usuario.equals(pontuacao.getUsuario()))
				tiposDePontosDoUsuario.add(pontuacao.getTipo());
		}
		return tiposDePontosDoUsuario;
	}
}