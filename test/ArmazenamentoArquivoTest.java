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

import armazenamento.excecoes.FalhaNoArmazenamentoException;
import pontuacao.excecoes.PontuacaoInvalidaException;

public class ArmazenamentoArquivoTest {
	Armazenamento arm;
	static final String CAMINHO_ARQUIVO = "saida.json";
	
	@Before
	public void inicializarArmazenamento() {
		try {
			arm = new ArmazenamentoArquivo(CAMINHO_ARQUIVO);
		} catch (FalhaNoArmazenamentoException e) {
			e.printStackTrace();
			fail();
		}
	}

	@After
	public void limparArquivoDeArmazenamento() {
		escreverNoArquivoDeArmazenamento("");
	}
	
	public void escreverNoArquivoDeArmazenamento(String conteudo) {
		try {
			var fileWriter = new FileWriter(CAMINHO_ARQUIVO);
			fileWriter.write(conteudo);
			fileWriter.close();
			assertEquals(conteudo, lerDadosBrutosArmazenamento());
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	public String lerDadosBrutosArmazenamento() {
		var dados = "";
		try {
			var leitor = new Scanner(new File(CAMINHO_ARQUIVO));
			while (leitor.hasNextLine())
				dados += leitor.nextLine();
			leitor.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			fail();
		}
		return dados;
	}
	
	private String jsonPontuacao(String usuario, int pontos, String tipo) {
		return "{\"tipo\":\""    + tipo    + "\","+
				"\"pontos\":"    + pontos  + ","+
				"\"usuario\":\"" + usuario + "\"}";
	}
	
	@Test
	public void armazenaPontuacao() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		
		assertEquals("["+jsonPontuacao("guerra", 10, "estrela")+"]"
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacaoPontosInvalidos() {
		try {
			arm.guardarPontuacao(new Pontuacao("guerra", -1, "estrela"));
			fail();
		} catch (PontuacaoInvalidaException e) {}
		
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	
	
	@Test
	public void armazenaPontuacaoUsuarioVazio() {
		try {
			arm.guardarPontuacao(new Pontuacao("", 1, "estrela"));
			fail();
		} catch (PontuacaoInvalidaException e) {}
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacaoUsuarioComEspaco() {
		try {
			arm.guardarPontuacao(new Pontuacao("eduardo guerra", 1, "estrela"));
			fail();
		} catch (PontuacaoInvalidaException e) {}
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacaoTipoVazio() {
		try {
			arm.guardarPontuacao(new Pontuacao("guerra", 1, ""));
			fail();
		} catch (PontuacaoInvalidaException e) {}
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacaoTipoComEspaco() {
		try {
			arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela brilhante"));
			fail();
		} catch (PontuacaoInvalidaException e) {}
		assertEquals("", lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacoes() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  9, "estrela"));

		assertEquals("["+jsonPontuacao("guerra", 20, "estrela")+"]"
				    , lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesTipos() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  1, "curtida"));
		arm.guardarPontuacao(new Pontuacao("guerra",  9, "favorito"));
		
		assertEquals("["
						+ jsonPontuacao("guerra", 15, "estrela")+","
						+ jsonPontuacao("guerra",  1, "curtida")+","
						+ jsonPontuacao("guerra",  9, "favorito")
					+"]"
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesUsuarios() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		arm.guardarPontuacao(new Pontuacao("marco",   5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("tadeu",   1, "curtida"));
		
		assertEquals("["
						+ jsonPontuacao("guerra", 10, "estrela")+","
						+ jsonPontuacao("marco",   5, "estrela")+","
						+ jsonPontuacao("tadeu",   1, "curtida")
					+"]"
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void recuperaPontosUmaPontuacao() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontosVariasPontuacoes() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  4, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  7, "estrela"));
		assertEquals(21, arm.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontosVariosTiposDePontuacoes() {
		arm.guardarPontuacao(new Pontuacao("guerra", 10, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra",  5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 24, "comentario"));
		arm.guardarPontuacao(new Pontuacao("guerra",  1, "comentario"));
		assertEquals(15, arm.recuperarPontos("guerra", "estrela"));
		assertEquals(25, arm.recuperarPontos("guerra", "comentario"));
	}
	
	@Test
	public void recuperaPontosVariosUsuariosETiposDePontuacoes() {
		arm.guardarPontuacao(new Pontuacao("guerra", 6, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 3, "comentario"));
		arm.guardarPontuacao(new Pontuacao("marco", 24, "estrela"));
		arm.guardarPontuacao(new Pontuacao("tadeu",  1, "comentario"));
		arm.guardarPontuacao(new Pontuacao("marco",  5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 4, "estrela"));
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
		assertEquals(29, arm.recuperarPontos("marco",  "estrela"));
		assertEquals( 3, arm.recuperarPontos("guerra", "comentario"));
		assertEquals( 1, arm.recuperarPontos("tadeu",  "comentario"));
	}
	
	@Test
	public void recuperaPontosUsuarioInexstente() {
		assertEquals(0, arm.recuperarPontos("xablau", "estrela"));
	}
	
	@Test
	public void recuperaPontosTipoInexistenteParaOUsuario() {
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "comentario"));
		arm.guardarPontuacao(new Pontuacao("maria",  1, "moeda"));
		assertEquals(0, arm.recuperarPontos("guerra", "moeda"));
	}
	
	@Test
	public void recuperarDadosDoArquivo() {
		String jsonPontuacoes = "["
				+ jsonPontuacao("guerra", 10, "estrela")+","
				+ jsonPontuacao("marco",   5, "estrela")+","
				+ jsonPontuacao("tadeu",   1, "curtida")
			+"]";
		escreverNoArquivoDeArmazenamento(jsonPontuacoes);
		
		inicializarArmazenamento();
		
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
		assertEquals( 5, arm.recuperarPontos("marco",  "estrela"));
		assertEquals( 1, arm.recuperarPontos("tadeu",  "curtida"));
	}
	
	@Test
	public void simularArmazenamentoCaiu() {
		arm.guardarPontuacao(new Pontuacao("guerra", 6, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 3, "comentario"));
		arm.guardarPontuacao(new Pontuacao("marco", 24, "estrela"));
		arm.guardarPontuacao(new Pontuacao("tadeu",  1, "comentario"));
		arm.guardarPontuacao(new Pontuacao("marco",  5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 4, "estrela"));
		
		// aqui ocorre a simulação:
		// limpa o cache, forçando com que o "armazenamento" recupere dados do arquivo.
		inicializarArmazenamento();
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
		assertEquals( 3, arm.recuperarPontos("guerra", "comentario"));
		assertEquals(29, arm.recuperarPontos("marco",  "estrela"));
		
		arm.guardarPontuacao(new Pontuacao("guerra", 5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("tadeu",  2, "comentario"));
		arm.guardarPontuacao(new Pontuacao("maria", 50, "estrela"));
		
		assertEquals(15, arm.recuperarPontos("guerra", "estrela"));
		assertEquals( 3, arm.recuperarPontos("tadeu",  "comentario"));
		assertEquals(50, arm.recuperarPontos("maria",  "estrela"));
	}
	
	
	@Test
	public void recuperaUmUsuario() {
		var usuariosEsperados = new ArrayList<String>(
			Arrays.asList("guerra"));
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela"));
		
		assertEquals(usuariosEsperados, arm.recuperarUsuariosRegistrados());
	}
	
	@Test
	public void recuperaVariosUsuarios() {
		var usuariosEsperados = new ArrayList<String>(
			Arrays.asList("guerra", "maria", "jose"));

		arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("maria",  1, "comentario"));
		arm.guardarPontuacao(new Pontuacao("jose",   1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("jose",  10, "curtida"));
		arm.guardarPontuacao(new Pontuacao("maria",  1, "comentario"));
		
		assertEquals(usuariosEsperados, arm.recuperarUsuariosRegistrados());
	}
	
	@Test
	public void simularArquivoInvalido() {
		escreverNoArquivoDeArmazenamento("Esse Texto É Invalido!");
		inicializarArmazenamento();// força o armazenamento a tentar recuperar dados invalidos!
		
		
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("maria", 1, "comentario"));
		escreverNoArquivoDeArmazenamento("Esse Texto É Invalido!");
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "favorito"));
		
		assertEquals("["+jsonPontuacao("guerra", 1, "estrela")+","
						+jsonPontuacao("maria",  1, "comentario")+","
						+jsonPontuacao("guerra", 1, "favorito")+"]"
			    	, lerDadosBrutosArmazenamento());
	}
	
	
	@Test
	public void recuperaUmTipoDePonto() {
		arm.guardarPontuacao(new Pontuacao("guerra", 5, "estrela"));
		ArrayList<String> tiposRecuperados = arm.recuperarTiposDePonto("guerra");
		assertEquals(1, tiposRecuperados.size());
		assertEquals("estrela", tiposRecuperados.get(0));
	}
	
	@Test
	public void recuperaVariosTiposDePontos() {
		arm.guardarPontuacao(new Pontuacao("guerra", 5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 5, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 5, "comentario"));
		arm.guardarPontuacao(new Pontuacao("guerra", 5, "moeda"));
		
		var tiposEsperados = new ArrayList<>(Arrays.asList(
				"estrela", "comentario", "moeda"));

		var tiposRecuperados = arm.recuperarTiposDePonto("guerra");
		assertEquals(tiposEsperados, tiposRecuperados);
	}
	
	@Test
	public void recuperaVariosTiposDePontosUsuariosDiferentes() {
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "comentario"));
		arm.guardarPontuacao(new Pontuacao("guerra", 1, "moeda"));
		
		arm.guardarPontuacao(new Pontuacao("maria",  1, "curtida"));
		arm.guardarPontuacao(new Pontuacao("tadeu",  1, "estrela"));
		arm.guardarPontuacao(new Pontuacao("jose",   1, "moeda"));
		arm.guardarPontuacao(new Pontuacao("jose",   1, "compartilhamento"));
		
		var tiposEsperadosGuerra = new ArrayList<>(
				Arrays.asList("estrela", "comentario", "moeda"));
		var tiposRecuperadosGuerra = arm.recuperarTiposDePonto("guerra");
		assertEquals(tiposEsperadosGuerra, tiposRecuperadosGuerra);
		
		var tiposEsperadosJose = new ArrayList<>(
				Arrays.asList("moeda", "compartilhamento"));
		var tiposRecuperadosJose = arm.recuperarTiposDePonto("jose");
		assertEquals(tiposEsperadosJose, tiposRecuperadosJose);
		
		var tiposEsperadosMaria = new ArrayList<>(
				Arrays.asList("curtida"));
		var tiposRecuperadosMaria = arm.recuperarTiposDePonto("maria");
		assertEquals(tiposEsperadosMaria, tiposRecuperadosMaria);
	}
}
