import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
//import com.google.gson.JsonIOException;

public class Main {
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Gson gson = new Gson();
		try {
			gson.toJson(123.45, new FileWriter("saida.json"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("foi");
	}

}
