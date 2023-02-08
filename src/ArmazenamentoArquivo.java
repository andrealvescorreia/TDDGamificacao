import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import armazenamento.excecoes.FalhaNoArmazenamentoException;

import org.json.simple.JSONArray;


public class ArmazenamentoArquivo implements Armazenamento {
	private final String _CAMINHO_ARQUIVO;
	private ArrayList<Pontuacao> _cachePontuacoes;
	
	public ArmazenamentoArquivo(String CAMINHO_ARQUIVO) throws FalhaNoArmazenamentoException{
		_cachePontuacoes = new ArrayList<Pontuacao>();
		_CAMINHO_ARQUIVO = CAMINHO_ARQUIVO;
		try {
			recuperarDadosSalvosEmArquivo();
		} catch (IOException e) {
			throw new FalhaNoArmazenamentoException(e.getMessage());
		}
	}
	
	private void recuperarDadosSalvosEmArquivo() throws IOException {
		JSONParser parser = new JSONParser();
		JSONArray jsonPontuacoes;
		try {
			jsonPontuacoes = (JSONArray) parser.parse(new FileReader(_CAMINHO_ARQUIVO));
			for (int i = 0; i < jsonPontuacoes.size(); i++) {
				Pontuacao pontuacao = new Pontuacao( (JSONObject)jsonPontuacoes.get(i) );
				_cachePontuacoes.add(pontuacao);
			}
		} catch(ParseException e) {
			criarArquivoLimpo();
		}
	}
	
	private void criarArquivoLimpo() throws IOException {
		var fileWriter = new FileWriter(_CAMINHO_ARQUIVO);
		fileWriter.write("");
		fileWriter.close();
	}
	
	
	@Override
	public void guardar(Pontuacao novaPontuacao) 
			throws FalhaNoArmazenamentoException {
		for (Pontuacao pontuacaoExistente : _cachePontuacoes) {
			if (mesmoUsuarioETipo(pontuacaoExistente, novaPontuacao)) {
				pontuacaoExistente.addPontos(novaPontuacao.getPontos());
				salvarCacheNoArquivo();
				return;
			}
		}
		_cachePontuacoes.add(novaPontuacao);
		salvarCacheNoArquivo();
	}
	
	private boolean mesmoUsuarioETipo(Pontuacao p1, Pontuacao p2) {
		return (p1.getUsuario().equals(p2.getUsuario()) 
			 && p1.getTipo().equals(p2.getTipo()));
	}
	
	
	private void salvarCacheNoArquivo(){
		try {
			FileWriter fileWriter = new FileWriter(_CAMINHO_ARQUIVO);
			fileWriter.write(jsonDoCacheDePontuacoes());
			fileWriter.close();
		} catch (IOException e) {
			throw new FalhaNoArmazenamentoException(e.getMessage());
		}
	}
	
	@SuppressWarnings("unchecked")
	private String jsonDoCacheDePontuacoes() {
		JSONArray jsonPontuacoes = new JSONArray();
		for(Pontuacao pontuacao : _cachePontuacoes) 
			jsonPontuacoes.add(pontuacao.toJSONObject());
		return jsonPontuacoes.toJSONString();
	}
	
	@Override
	public long recuperarPontos(String usuario, String tipo) {
		for (Pontuacao pontuacao : _cachePontuacoes) {
			if (usuario.equals(pontuacao.getUsuario()) && tipo.equals(pontuacao.getTipo()))
				return pontuacao.getPontos();
		}
		return 0;
	}
	
	@Override
	public ArrayList<String> recuperarUsuariosRegistrados() {
		var usuarios = new ArrayList<String>();
		for (Pontuacao pontuacao : _cachePontuacoes) {
			String usuario = pontuacao.getUsuario();
			if (usuarios.contains(usuario)) continue;
			usuarios.add(usuario);
		}
		return usuarios;
	}
	
	@Override
	public ArrayList<String> recuperarTiposDePonto(String usuario) {
		var tiposDePontosDoUsuario = new ArrayList<String>();
		for (Pontuacao pontuacao : _cachePontuacoes) {
			if (tiposDePontosDoUsuario.contains(pontuacao.getTipo())) 
				continue;
			if (usuario.equals(pontuacao.getUsuario()))
				tiposDePontosDoUsuario.add(pontuacao.getTipo());
		}
		return tiposDePontosDoUsuario;
	}
}