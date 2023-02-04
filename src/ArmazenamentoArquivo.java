import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import pontuacao.excecoes.PontuacaoInvalidaException;

import org.json.simple.JSONArray;


public class ArmazenamentoArquivo implements Armazenamento {
	private final String _CAMINHO_ARQUIVO;
	private ArrayList<Pontuacao> _cachePontuacoes;
	
	public ArmazenamentoArquivo(String CAMINHO_ARQUIVO) throws IOException{
		_cachePontuacoes = new ArrayList<Pontuacao>();
		_CAMINHO_ARQUIVO = CAMINHO_ARQUIVO;
		// recupera dados salvos em arquivo (se houver algum)
		
			JSONParser parser = new JSONParser();
			JSONArray jsonPontuacoes;
			try {
				jsonPontuacoes = (JSONArray) parser.parse(new FileReader(_CAMINHO_ARQUIVO));
				for (int i = 0; i < jsonPontuacoes.size(); i++) {
					Pontuacao pontuacao = new Pontuacao( (JSONObject)jsonPontuacoes.get(i) );
					_cachePontuacoes.add(pontuacao);
				}
			} catch (IOException e) {
				throw e;
			} catch( ParseException e) {
				criarArquivoLimpo();
			}
			
		
	}
	
	private void criarArquivoLimpo() throws IOException {
		try {
			var fileWriter = new FileWriter(_CAMINHO_ARQUIVO);
			fileWriter.write("");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	
	@Override
	public void guardarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
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
		try {
			FileWriter fileWriter = new FileWriter(_CAMINHO_ARQUIVO);
			fileWriter.write(jsonDoCacheDePontuacoes());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	private String jsonDoCacheDePontuacoes() {
		JSONArray jsonArrayPontuacoes = new JSONArray();
		for(Pontuacao pontuacao : _cachePontuacoes) 
			jsonArrayPontuacoes.add(pontuacao.toJSONObject());
		return jsonArrayPontuacoes.toJSONString();
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