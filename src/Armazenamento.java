import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


public class Armazenamento {
	public static final String CAMINHO_ARQUIVO = "saida.json";
	private JSONArray dados =  new JSONArray();// cache dos dados que também ficam no arquivo
	
	@SuppressWarnings("unchecked")
	public void guardarPontos(String usuario, int pontos, String tipo) {
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

	public int recuperarPontos(String usuario, String tipo) {
		int pontuacaoTotal = 0;
		for (int i = 0; i < dados.size(); i++) {
			JSONObject pontuacao = (JSONObject) dados.get(i);
			if(pontuacao.get("usuario") == usuario && pontuacao.get("tipo") == tipo) {
				pontuacaoTotal += (int) pontuacao.get("pontos");
			}
		}
		return pontuacaoTotal;
	}

}
