import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import excecoes.PontuacaoInvalidaException;

import org.json.simple.JSONArray;


public class Armazenamento {
	public static final String CAMINHO_ARQUIVO = "saida.json";
	private JSONArray _dados = new JSONArray();// cache dos dados que também ficam no arquivo
	//private ArrayList<Pontuacao> _pontuacoes = new ArrayList<Pontuacao>();
	public Armazenamento(){
		// recupera dados salvos em arquivo (se houver algum)
		try {
			JSONParser parser = new JSONParser();
			_dados = (JSONArray) parser.parse(new FileReader(CAMINHO_ARQUIVO));
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
	public void guardarPontuacao(String usuario, long pontos, String tipo) 
			throws PontuacaoInvalidaException {
		Pontuacao pontuacao = new Pontuacao(usuario, pontos, tipo);
		_dados.add(pontuacao.toJSONObject());
		try {
			FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write(_dados.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public long recuperarPontos(String usuario, String tipo) {
		long totalPontosRecuperados = 0;
		for (int i = 0; i < _dados.size(); i++) {
			Pontuacao pontuacao = new Pontuacao( (JSONObject)_dados.get(i) );
			if(usuario.equals(pontuacao.getUsuario()) && tipo.equals(pontuacao.getTipo())) {
				totalPontosRecuperados += pontuacao.getPontos();
			}
		}
		return totalPontosRecuperados;
	}

	public ArrayList<String> recuperarUsuariosRegistrados() {
		ArrayList<String> usuarios = new ArrayList<String>();
		for (int i = 0; i < _dados.size(); i++) {
			Pontuacao pontuacao = new Pontuacao( (JSONObject)_dados.get(i) );
			String usuario = pontuacao.getUsuario();
			if(usuarios.contains(usuario) == false) {
				usuarios.add(usuario);
			}
		}
		return usuarios;
	}

	public ArrayList<String> recuperarTiposPontuacao(String usuario) {
		ArrayList<String> tiposDePontuacaoDoUsuario = new ArrayList<>();
		for (int i = 0; i < _dados.size(); i++) {
			JSONObject pontuacao = (JSONObject) _dados.get(i);
			String tipoDaPontuacao = (String)pontuacao.get("tipo");
			String usuarioDaPontuacao = (String)pontuacao.get("usuario");
			if(usuario.equals(usuarioDaPontuacao) && tiposDePontuacaoDoUsuario.contains(tipoDaPontuacao) == false) {
				tiposDePontuacaoDoUsuario.add(tipoDaPontuacao);
			}
		}
		return tiposDePontuacaoDoUsuario;
	}
}