import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
//import com.google.gson.JsonIOException;

public class Main {

	public static void gsonWrite() {
		// Nao funciona :( ...
		Gson gson = new Gson();
		try {
			gson.toJson(123.45, new FileWriter("saida.json"));
			System.out.println("Arquivo criado/editado!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static void simpleJsonWrite() {
		// Cria um Objeto JSON
		JSONObject jsonObject = new JSONObject();

		FileWriter writeFile = null;

		// Armazena dados em um Objeto JSON
		jsonObject.put("nome", "Allan");
		jsonObject.put("sobrenome", "Romanato");
		jsonObject.put("pais", "Brasil");
		jsonObject.put("estado", "SP");

		try {
			writeFile = new FileWriter("saida.json");
			// Escreve no arquivo conteudo do Objeto JSON
			writeFile.write(jsonObject.toJSONString());
			writeFile.close();
			System.out.println("Arquivo criado/editado!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void simpleJsonRead() {
		JSONObject jsonObject;
		//Cria o parse de tratamento
		JSONParser parser = new JSONParser();
		//Variaveis que irao armazenar os dados do arquivo JSON
		String nome;
		String sobrenome;
		String estado;
		String pais;
		
		try {
			jsonObject = (JSONObject) parser.parse(new FileReader(
					"saida.json"));
			
			//Salva nas variaveis os dados retirados do arquivo
			nome = (String) jsonObject.get("nome");
			sobrenome = (String) jsonObject.get("sobrenome");
			estado = (String) jsonObject.get("estado");
			pais = (String) jsonObject.get("pais");

			System.out.printf(
					"Nome: %s\nSobrenome: %s\nEstado: %s\nPais: %s\n",
					nome, sobrenome, estado, pais);
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
	}
	
	public static void main(String[] args) {
		simpleJsonRead();
	}

}
