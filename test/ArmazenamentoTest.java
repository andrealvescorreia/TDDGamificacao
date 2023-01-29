import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
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
	Armazenamento armazenamento;
	
	@Before
	public void criarArmazenamento() {
		limparArmazenamento();
		armazenamento = new Armazenamento();
	}
	
	public String lerDadosBrutosArmazenamento() {
		String dados = "";
		try {
			Scanner leitor = new Scanner(new File(Armazenamento.CAMINHO_ARQUIVO));
			while (leitor.hasNextLine())
				dados += leitor.nextLine();
			leitor.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		return dados;
	}

	
	@After
	public void limparArmazenamento() {
		try {
			FileWriter fileWriter = new FileWriter(Armazenamento.CAMINHO_ARQUIVO);
			fileWriter.write("");
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void armazenaPontuacao() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"}]", lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoes() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		armazenamento.guardarPontos("guerra", 1, "estrela");
		armazenamento.guardarPontos("guerra", 9, "estrela");

		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"},"
				+ "{\"tipo\":\"estrela\",\"pontos\":1,\"usuario\":\"guerra\"},"
				+ "{\"tipo\":\"estrela\",\"pontos\":9,\"usuario\":\"guerra\"}]", lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesTipos() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		armazenamento.guardarPontos("guerra", 5, "estrela");
		armazenamento.guardarPontos("guerra", 1, "curtida");
		armazenamento.guardarPontos("guerra", 9, "favorito");

		assertEquals(
				"[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"},"
						+ "{\"tipo\":\"estrela\",\"pontos\":5,\"usuario\":\"guerra\"},"
						+ "{\"tipo\":\"curtida\",\"pontos\":1,\"usuario\":\"guerra\"},"
						+ "{\"tipo\":\"favorito\",\"pontos\":9,\"usuario\":\"guerra\"}]",
				lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesUsuarios() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		armazenamento.guardarPontos("marco", 5, "estrela");
		armazenamento.guardarPontos("tadeu", 1, "curtida");

		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"},"
				+ "{\"tipo\":\"estrela\",\"pontos\":5,\"usuario\":\"marco\"},"
				+ "{\"tipo\":\"curtida\",\"pontos\":1,\"usuario\":\"tadeu\"}]", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void recuperaPontuacaoSimples() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		assertEquals(10, armazenamento.recuperarPontos("guerra", "estrela"));
	}
	
	@Test
	public void recuperaPontuacaoTotal() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		armazenamento.guardarPontos("guerra", 4, "estrela");
		armazenamento.guardarPontos("guerra", 7, "estrela");
		assertEquals(21, armazenamento.recuperarPontos("guerra", "estrela"));
	}
	
	@Test
	public void recuperaPontuacaoDeTipoEspecifico() {
		armazenamento.guardarPontos("guerra", 10, "estrela");
		armazenamento.guardarPontos("guerra", 5, "estrela");
		armazenamento.guardarPontos("guerra", 24, "comentario");
		armazenamento.guardarPontos("guerra", 1, "comentario");
		assertEquals(15, armazenamento.recuperarPontos("guerra", "estrela"));
	}
	
	@Test
	public void recuperaPontuacaoDeTipoEUsuarioEspecifico() {
		armazenamento.guardarPontos("guerra", 6, "estrela");
		armazenamento.guardarPontos("guerra", 3, "comentario");
		armazenamento.guardarPontos("marco", 24, "estrela");
		armazenamento.guardarPontos("tadeu", 1, "comentario");
		armazenamento.guardarPontos("marco", 5, "estrela");
		armazenamento.guardarPontos("guerra", 4, "estrela");
		assertEquals(10, armazenamento.recuperarPontos("guerra", "estrela"));
	}
	
	@Test
	public void simularArmazenamentoCaiu() {
		armazenamento.guardarPontos("guerra", 6, "estrela");
		armazenamento.guardarPontos("guerra", 3, "comentario");
		armazenamento.guardarPontos("marco", 24, "estrela");
		armazenamento.guardarPontos("tadeu", 1, "comentario");
		armazenamento.guardarPontos("marco", 5, "estrela");
		armazenamento.guardarPontos("guerra", 4, "estrela");
		
		armazenamento = new Armazenamento();// limpa o cache, forçando que os dados sejam recuperados do arquivo.
		
		armazenamento.guardarPontos("guerra", 5, "estrela");
		assertEquals(15, armazenamento.recuperarPontos("guerra", "estrela"));
	}
	
	
}
