import static org.junit.Assert.fail;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.JSONArray;


public class Armazenamento {
	public static final String CAMINHO_ARQUIVO = "saida.json";
	private JSONArray dados = new JSONArray();// cache dos dados que também ficam no arquivo
	
	
	public Armazenamento(){
		// recupera dados salvos em arquivo (se houver algum)
		
		try {
			JSONParser parser = new JSONParser();
			JSONObject jo = (JSONObject) parser.parse(new FileReader(CAMINHO_ARQUIVO));
			dados = (JSONArray) jo.get("dados");
		}
		catch (Exception e){
			System.out.println("deu ruim para parse");
			e.printStackTrace();
			criarArquivoLimpo();
		}

	
	}
	
	@SuppressWarnings("unchecked")
	private void criarArquivoLimpo() {
		JSONObject jsonDados = new JSONObject();
		jsonDados.put("dados", new JSONArray());
		
		System.out.println("ASSASASAD");
		try {
			FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write(jsonDados.toJSONString());
			fileWriter.close();
			System.out.println("criado arquivo limpoASSSSSSSSSS");
		} catch (Exception e) {
			System.out.println("oxiaa");
			e.printStackTrace();
			
		}
	}
	
	@SuppressWarnings("unchecked")
	public void guardarPontos(String usuario, int pontos, String tipo) {
		JSONObject pontuacao = new JSONObject();
		
		pontuacao.put("tipo", tipo);
		pontuacao.put("pontos", pontos);
		pontuacao.put("usuario", usuario);
		
		
		dados.add(pontuacao);
		
		JSONObject jsonDados = new JSONObject();
		jsonDados.put("dados", dados);
		try {
			FileWriter fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write(jsonDados.toJSONString());
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public int recuperarPontos(String usuario, String tipo) {
		int pontuacaoTotal = 0;
		for (int i = 0; i < dados.size(); i++) {
			
			JSONObject pontuacao = (JSONObject) dados.get(i);

			if(usuario.equals((String) pontuacao.get("usuario")) && tipo.equals((String) pontuacao.get("tipo"))) {
				System.out.println(pontuacao.get("pontos"));
				
				if(pontuacao.get("pontos") instanceof Integer) {
					pontuacaoTotal += (int) pontuacao.get("pontos");
				} else {
					pontuacaoTotal += Math.toIntExact((long) pontuacao.get("pontos"));
				}
				
			}
		}
		return pontuacaoTotal;
	}

}
