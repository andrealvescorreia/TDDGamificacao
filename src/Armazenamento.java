import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;


public class Armazenamento {

	@SuppressWarnings("unchecked")
	public void guardarPontos(String usuario, int pontos, String tipo) {
		JSONObject jsonObject = new JSONObject();
		JSONArray ja = new JSONArray();
		
		
		jsonObject.put("tipo", tipo);
		jsonObject.put("pontos", pontos);
		jsonObject.put("usuario", usuario);
		
		
		ja.add(jsonObject);
		
		FileWriter writeFile = null;
		try {
			writeFile = new FileWriter("saida.json");
			writeFile.write(ja.toJSONString());
			writeFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
