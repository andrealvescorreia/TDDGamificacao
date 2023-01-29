import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import excecoes.PontuacaoInvalidaException;

public class ArmazenamentoArquivoTest {
	Armazenamento armazenamento;

	@Before
	public void criarArmazenamento() {
		limparArquivoDeArmazenamento();
		armazenamento = new ArmazenamentoArquivo();
	}

	@After
	public void limparArquivoDeArmazenamento() {
		escreverNoArquivoDeArmazenamento("");
	}
	
	public void escreverNoArquivoDeArmazenamento(String conteudo) {
		try {
			FileWriter fileWriter = new FileWriter(ArmazenamentoArquivo.CAMINHO_ARQUIVO);
			fileWriter.write(conteudo);
			fileWriter.close();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	public String lerDadosBrutosArmazenamento() {
		String dados = "";
		try {
			Scanner leitor = new Scanner(new File(ArmazenamentoArquivo.CAMINHO_ARQUIVO));
			while (leitor.hasNextLine())
				dados += leitor.nextLine();
			leitor.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		return dados;
	}

	@Test
	public void armazenaPontuacao() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"}]"
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacaoPontosInvalidos() {
		try {
			armazenamento.guardarPontuacao("guerra", -1, "estrela");
			fail();
		} catch (PontuacaoInvalidaException e) {}
		
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacaoUsuarioInvalido() {
		try {
			armazenamento.guardarPontuacao("", 1, "estrela");
			fail();
		} catch (PontuacaoInvalidaException e) {}
		
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacaoTipoInvalido() {
		try {
			armazenamento.guardarPontuacao("guerra", 1, "");
			fail();
		} catch (PontuacaoInvalidaException e) {}
		
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacoes() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		armazenamento.guardarPontuacao("guerra", 1, "estrela");
		armazenamento.guardarPontuacao("guerra", 9, "estrela");

		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"},"
				    + "{\"tipo\":\"estrela\",\"pontos\":1,\"usuario\":\"guerra\"},"
				    + "{\"tipo\":\"estrela\",\"pontos\":9,\"usuario\":\"guerra\"}]"
				    , lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesTipos() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 1, "curtida");
		armazenamento.guardarPontuacao("guerra", 9, "favorito");

		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"},"
					+ "{\"tipo\":\"estrela\",\"pontos\":5,\"usuario\":\"guerra\"},"
					+ "{\"tipo\":\"curtida\",\"pontos\":1,\"usuario\":\"guerra\"},"
					+ "{\"tipo\":\"favorito\",\"pontos\":9,\"usuario\":\"guerra\"}]"
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesUsuarios() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		armazenamento.guardarPontuacao("marco", 5, "estrela");
		armazenamento.guardarPontuacao("tadeu", 1, "curtida");

		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":10,\"usuario\":\"guerra\"},"
					+ "{\"tipo\":\"estrela\",\"pontos\":5,\"usuario\":\"marco\"},"
					+ "{\"tipo\":\"curtida\",\"pontos\":1,\"usuario\":\"tadeu\"}]"
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void recuperaPontuacaoSimples() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		assertEquals(10, armazenamento.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontuacaoTotal() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		armazenamento.guardarPontuacao("guerra", 4, "estrela");
		armazenamento.guardarPontuacao("guerra", 7, "estrela");
		assertEquals(21, armazenamento.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontuacaoDeTipoEspecifico() {
		armazenamento.guardarPontuacao("guerra", 10, "estrela");
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 24, "comentario");
		armazenamento.guardarPontuacao("guerra", 1, "comentario");
		assertEquals(15, armazenamento.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontuacaoDeTipoEUsuarioEspecifico() {
		armazenamento.guardarPontuacao("guerra", 6, "estrela");
		armazenamento.guardarPontuacao("guerra", 3, "comentario");
		armazenamento.guardarPontuacao("marco", 24, "estrela");
		armazenamento.guardarPontuacao("tadeu", 1, "comentario");
		armazenamento.guardarPontuacao("marco", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 4, "estrela");
		assertEquals(10, armazenamento.recuperarPontos("guerra", "estrela"));
	}
	
	@Test
	public void recuperaPontuacaoDeUsuarioInexistente() {
		assertEquals(0, armazenamento.recuperarPontos("xablau", "estrela"));
	}
	
	@Test
	public void recuperaPontuacaoDeTipoInexistenteParaUsuario() {
		armazenamento.guardarPontuacao("guerra", 1, "estrela");
		armazenamento.guardarPontuacao("guerra", 1, "comentario");
		
		assertEquals(0, armazenamento.recuperarPontos("guerra", "moeda"));
		armazenamento.guardarPontuacao("maria", 1, "moeda");
		assertEquals(0, armazenamento.recuperarPontos("guerra", "moeda"));
	}
	
	@Test
	public void simularArmazenamentoCaiu() {
		armazenamento.guardarPontuacao("guerra", 6, "estrela");
		armazenamento.guardarPontuacao("guerra", 3, "comentario");
		armazenamento.guardarPontuacao("marco", 24, "estrela");
		armazenamento.guardarPontuacao("tadeu", 1, "comentario");
		armazenamento.guardarPontuacao("marco", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 4, "estrela");
		
		// aqui ocorre a simulação:
		// limpa o cache, forçando que os dados sejam recuperados do arquivo.
		armazenamento = new ArmazenamentoArquivo();
		assertEquals(10, armazenamento.recuperarPontos("guerra", "estrela"));
		
		
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("tadeu", 2, "comentario");
		armazenamento.guardarPontuacao("maria", 50, "estrela");
		
		assertEquals(15, armazenamento.recuperarPontos("guerra", "estrela"));
		assertEquals(3, armazenamento.recuperarPontos("tadeu", "comentario"));
		assertEquals(29, armazenamento.recuperarPontos("marco", "estrela"));
		assertEquals(3, armazenamento.recuperarPontos("guerra", "comentario"));
		assertEquals(50, armazenamento.recuperarPontos("maria", "estrela"));
	}
	
	
	@Test
	public void recuperaUmUsuario() {
		ArrayList<String> usuariosEsperados = new ArrayList<String>();
		usuariosEsperados.add("guerra");
		armazenamento.guardarPontuacao("guerra", 1, "estrela");
		
		assertEquals(usuariosEsperados, armazenamento.recuperarUsuariosRegistrados());
	}
	
	@Test
	public void recuperaVariosUsuarios() {
		ArrayList<String> usuariosEsperados = new ArrayList<String>();
		usuariosEsperados.add("guerra");
		usuariosEsperados.add("maria");
		usuariosEsperados.add("jose");
		armazenamento.guardarPontuacao("guerra", 1, "estrela");
		armazenamento.guardarPontuacao("maria", 1, "comentario");
		armazenamento.guardarPontuacao("maria", 1, "comentario");
		armazenamento.guardarPontuacao("jose", 1, "estrela");
		armazenamento.guardarPontuacao("jose", 10, "curtida");
		
		assertEquals(usuariosEsperados, armazenamento.recuperarUsuariosRegistrados());
		
	}
	
	@Test
	public void simularArquivoInvalido() {
		escreverNoArquivoDeArmazenamento("Esse Texto É Invalido!");
		assertEquals("Esse Texto É Invalido!", lerDadosBrutosArmazenamento());
		armazenamento = new ArmazenamentoArquivo();
		armazenamento.guardarPontuacao("guerra", 1, "estrela");
		armazenamento.guardarPontuacao("maria", 1, "comentario");
		escreverNoArquivoDeArmazenamento("Esse Texto É Invalido!");
		assertEquals("Esse Texto É Invalido!", lerDadosBrutosArmazenamento());
		armazenamento.guardarPontuacao("guerra", 1, "favorito");
		
		assertEquals("[{\"tipo\":\"estrela\",\"pontos\":1,\"usuario\":\"guerra\"},"
			    	+ "{\"tipo\":\"comentario\",\"pontos\":1,\"usuario\":\"maria\"},"
			    	+ "{\"tipo\":\"favorito\",\"pontos\":1,\"usuario\":\"guerra\"}]"
			    	, lerDadosBrutosArmazenamento());
	}
	
	
	@Test
	public void recuperaUmTipoDePontuacao() {
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		ArrayList<String> tiposRecuperados = armazenamento.recuperarTiposPontuacao("guerra");
		assertEquals(1, tiposRecuperados.size());
		assertEquals("estrela", tiposRecuperados.get(0));
	}
	
	@Test
	public void recuperaVariosTiposDePontuacao() {
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 5, "comentario");
		armazenamento.guardarPontuacao("guerra", 5, "moeda");
		
		ArrayList<String> tiposEsperados = new ArrayList<>(Arrays.asList("estrela", "comentario", "moeda"));

		ArrayList<String> tiposRecuperados = armazenamento.recuperarTiposPontuacao("guerra");
		assertEquals(tiposEsperados, tiposRecuperados);
	}
	
	@Test
	public void recuperaVariosTiposDePontuacaoUsuariosDiferentes() {
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 5, "estrela");
		armazenamento.guardarPontuacao("guerra", 5, "comentario");
		armazenamento.guardarPontuacao("guerra", 5, "moeda");
		
		armazenamento.guardarPontuacao("maria", 5, "curtida");
		armazenamento.guardarPontuacao("tadeu", 5, "estrela");
		armazenamento.guardarPontuacao("jose", 5, "moeda");
		armazenamento.guardarPontuacao("jose", 5, "compartilhamento");
		
		ArrayList<String> tiposEsperadosGuerra = new ArrayList<>(Arrays.asList("estrela", "comentario", "moeda"));
		ArrayList<String> tiposRecuperadosGuerra = armazenamento.recuperarTiposPontuacao("guerra");
		assertEquals(tiposEsperadosGuerra, tiposRecuperadosGuerra);
		
		ArrayList<String> tiposEsperadosMaria = new ArrayList<>(Arrays.asList("curtida"));
		ArrayList<String> tiposRecuperadosMaria = armazenamento.recuperarTiposPontuacao("maria");
		assertEquals(tiposEsperadosMaria, tiposRecuperadosMaria);
		
		ArrayList<String> tiposEsperadosJose = new ArrayList<>(Arrays.asList("moeda", "compartilhamento"));
		ArrayList<String> tiposRecuperadosJose = armazenamento.recuperarTiposPontuacao("jose");
		assertEquals(tiposEsperadosJose, tiposRecuperadosJose);
	}
}
