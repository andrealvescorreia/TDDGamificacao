import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import org.junit.Test;
/*
 * A classe Armazenamento deve ser capaz de realizar as seguintes operações:

Armazenar que um usuário recebeu uma quantidade de um tipo de ponto. Por exemplo: o usuário "guerra" recebeu "10" pontos do tipo "estrela"

Recuperar quantos pontos de um tipo tem um usuário. Por exemplo: retornar quantos pontos do tipo "estrela" tem o usuário "guerra"

Retornar todos os usuários que já receberam algum tipo de ponto.

Retornar todos os tipos de ponto que já foram registrados para algum usuário.

Observação: os dados devem ser armazenados em um arquivo e como serão armazenados fica a critério do aprendiz. A seção "Formas de implementar o armazenamento em arquivo" dá algumas sugestões.
 
Os testes da classe Armazenamento devem ser feitos utilizando arquivos.
 
É deixado livre a forma como os dados de pontuação do usuário serão armazenado em um arquivo, desde que os requisitos sejam cumpridos. 
É importante que outras classes não dependam de forma alguma de como é feita essa armazenagem no arquivo.

Uma abordagem para armazenar os dados seria fazer isso de uma forma incremental. Sempre só adicionando dados no arquivo. 
Nesse caso, para saber a pontuação de um usuário, seria necessário percorrer todo o arquivo procurando por todos os dados a respeito dele.

Outra abordagem seria ter um registro para cada usuário dentro do arquivo e modificar esse registro à medida que novos dados forem chegando. 

Independente da abordagem de armazenar no arquivo, você pode também guardar um cache dos dados em memória. O único requisito nesse caso é que se a aplicação cair, deve-se recuperar todos os dados armazenados.
 *
 */

public class ArmazenamentoTest {

	@Test
	public void armazenaPontos() {
		Armazenamento a = new Armazenamento();
		a.guardarPontos("guerra", 10, "estrela");

		Scanner myReader = null;
		try {
			myReader = new Scanner(new File("saida.json"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}

		String data = "";
		while (myReader.hasNextLine()) {
			data += myReader.nextLine();
		}
		myReader.close();
		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"}]", data);

		// assertEquals(10, a.recuperarPontos("guerra", "estrela"));
	}

}