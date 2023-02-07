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
	
	private void escreverNoArquivoDeArmazenamento(String conteudo) {
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
	
	
	private String lerDadosBrutosArmazenamento() {
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
	
	private String json(ArrayList<Pontuacao> pontuacoes) {
		String jsonPontuacoes = "[";
		for (Pontuacao p: pontuacoes) {
			jsonPontuacoes += p.toJSONString();
			if(p != pontuacoes.get(pontuacoes.size()-1)) {
				jsonPontuacoes += ",";
			}
		}
		return jsonPontuacoes + "]";
	}
	
	private void guardar(ArrayList<Pontuacao> pontuacoes) {
		for (Pontuacao p : pontuacoes) {
			arm.guardar(p);
		}
	}
	
	@Test
	public void armazenaPontuacao() {
		var pontuacao = new Pontuacao("guerra", 10, "estrela");
		arm.guardar(pontuacao);
		
		assertEquals("["+pontuacao.toJSONString()+"]"
					, lerDadosBrutosArmazenamento());
	}
	
	@Test
	public void armazenaPontuacoesMesmoUsuarioETipo() {
		arm.guardar(new Pontuacao("guerra", 10, "estrela"));
		arm.guardar(new Pontuacao("guerra",  1, "estrela"));
		arm.guardar(new Pontuacao("guerra",  9, "estrela"));
		
		var pontuacaoEsperada = new Pontuacao("guerra", 20, "estrela");
		assertEquals("["+pontuacaoEsperada.toJSONString()+"]"
				    , lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesMesmoUsuarioTiposDiferentes() {
		var pontuacoesEsperadas = new ArrayList<Pontuacao>();
		pontuacoesEsperadas.add(new Pontuacao("guerra", 10, "estrela"));
		pontuacoesEsperadas.add(new Pontuacao("guerra",  1, "curtida"));
		pontuacoesEsperadas.add(new Pontuacao("guerra",  9, "favorito"));

		guardar(pontuacoesEsperadas);
		assertEquals(json(pontuacoesEsperadas)
				, lerDadosBrutosArmazenamento());
	}

	@Test
	public void armazenaPontuacoesDiferentesUsuarios() {
		var pontuacoesEsperadas = new ArrayList<Pontuacao>();
		pontuacoesEsperadas.add(new Pontuacao("guerra", 10, "estrela"));
		pontuacoesEsperadas.add( new Pontuacao("marco",   5, "estrela"));
		pontuacoesEsperadas.add( new Pontuacao("tadeu",   1, "curtida"));
		guardar(pontuacoesEsperadas);
		assertEquals(json(pontuacoesEsperadas)
					, lerDadosBrutosArmazenamento());
	}

	@Test
	public void recuperaPontosUmaPontuacao() {
		arm.guardar(new Pontuacao("guerra", 10, "estrela"));
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontosVariasPontuacoes() {
		arm.guardar(new Pontuacao("guerra", 10, "estrela"));
		arm.guardar(new Pontuacao("guerra",  4, "estrela"));
		arm.guardar(new Pontuacao("guerra",  7, "estrela"));
		assertEquals(21, arm.recuperarPontos("guerra", "estrela"));
	}

	@Test
	public void recuperaPontosVariosTiposDePontuacoes() {
		arm.guardar(new Pontuacao("guerra", 15, "estrela"));
		arm.guardar(new Pontuacao("guerra", 25, "comentario"));

		assertEquals(15, arm.recuperarPontos("guerra", "estrela"));
		assertEquals(25, arm.recuperarPontos("guerra", "comentario"));
	}
	
	@Test
	public void recuperaPontosVariosUsuariosETiposDePontuacoes() {
		arm.guardar(new Pontuacao("guerra", 10, "estrela"));
		arm.guardar(new Pontuacao("marco", 29, "estrela"));
		arm.guardar(new Pontuacao("guerra", 3, "comentario"));
		arm.guardar(new Pontuacao("tadeu",  1, "comentario"));

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
		arm.guardar(new Pontuacao("guerra", 1, "estrela"));
		arm.guardar(new Pontuacao("guerra", 1, "comentario"));
		arm.guardar(new Pontuacao("maria",  1, "moeda"));
		assertEquals(0, arm.recuperarPontos("guerra", "moeda"));
	}
	
	@Test
	public void recuperarDadosDoArquivo() {
		var pontuacoes = new ArrayList<Pontuacao>();
		pontuacoes.add(new Pontuacao("guerra", 10, "estrela"));
		pontuacoes.add(new Pontuacao("marco",   5, "estrela"));
		pontuacoes.add(new Pontuacao("tadeu",   1, "curtida"));
		
		escreverNoArquivoDeArmazenamento(json(pontuacoes));
		
		inicializarArmazenamento();
		
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
		assertEquals( 5, arm.recuperarPontos("marco",  "estrela"));
		assertEquals( 1, arm.recuperarPontos("tadeu",  "curtida"));
	}
	
	@Test
	public void simularArmazenamentoCaiu() {
		arm.guardar(new Pontuacao("guerra", 6, "estrela"));
		arm.guardar(new Pontuacao("guerra", 3, "comentario"));
		arm.guardar(new Pontuacao("marco", 24, "estrela"));
		arm.guardar(new Pontuacao("tadeu",  1, "comentario"));
		arm.guardar(new Pontuacao("marco",  5, "estrela"));
		arm.guardar(new Pontuacao("guerra", 4, "estrela"));
		
		// aqui ocorre a simulação:
		// limpa o cache, forçando com que o "armazenamento" recupere dados do arquivo.
		inicializarArmazenamento();
		assertEquals(10, arm.recuperarPontos("guerra", "estrela"));
		assertEquals( 3, arm.recuperarPontos("guerra", "comentario"));
		assertEquals(29, arm.recuperarPontos("marco",  "estrela"));
		
		arm.guardar(new Pontuacao("guerra", 5, "estrela"));
		arm.guardar(new Pontuacao("tadeu",  2, "comentario"));
		arm.guardar(new Pontuacao("maria", 50, "estrela"));
		
		assertEquals(15, arm.recuperarPontos("guerra", "estrela"));
		assertEquals( 3, arm.recuperarPontos("tadeu",  "comentario"));
		assertEquals(50, arm.recuperarPontos("maria",  "estrela"));
	}
	
	
	@Test
	public void recuperaUmUsuario() {
		var usuariosEsperados = new ArrayList<String>(
			Arrays.asList("guerra"));
		arm.guardar(new Pontuacao("guerra", 1, "estrela"));
		
		assertEquals(usuariosEsperados, arm.recuperarUsuariosRegistrados());
	}
	
	@Test
	public void recuperaVariosUsuarios() {
		var usuariosEsperados = new ArrayList<String>(
			Arrays.asList("guerra", "maria", "jose"));

		arm.guardar(new Pontuacao("guerra", 1, "estrela"));
		arm.guardar(new Pontuacao("maria",  1, "comentario"));
		arm.guardar(new Pontuacao("jose",   1, "estrela"));
		arm.guardar(new Pontuacao("jose",  10, "curtida"));
		arm.guardar(new Pontuacao("maria",  1, "comentario"));
		
		assertEquals(usuariosEsperados, arm.recuperarUsuariosRegistrados());
	}
	
	@Test
	public void simularArquivoInvalido() {
		var p1 = new Pontuacao("guerra", 1, "estrela");
		var p2 = new Pontuacao("maria", 1, "comentario");
		var p3 = new Pontuacao("guerra", 1, "favorito");
		var pontuacoesEsperadas = new ArrayList<Pontuacao>();
		pontuacoesEsperadas.add(p1);
		pontuacoesEsperadas.add(p2);
		pontuacoesEsperadas.add(p3);
		escreverNoArquivoDeArmazenamento("Esse Texto É Invalido!");
		inicializarArmazenamento();// força o armazenamento a tentar recuperar dados invalidos!
		
		
		arm.guardar(p1);
		arm.guardar(p2);
		escreverNoArquivoDeArmazenamento("Esse Texto É Invalido!");
		arm.guardar(p3);
		
		assertEquals(json(pontuacoesEsperadas), lerDadosBrutosArmazenamento());
	}
	
	
	@Test
	public void recuperaUmTipoDePonto() {
		arm.guardar(new Pontuacao("guerra", 5, "estrela"));
		ArrayList<String> tiposRecuperados = arm.recuperarTiposDePonto("guerra");
		assertEquals(1, tiposRecuperados.size());
		assertEquals("estrela", tiposRecuperados.get(0));
	}
	
	@Test
	public void recuperaVariosTiposDePontos() {
		arm.guardar(new Pontuacao("guerra", 5, "estrela"));
		arm.guardar(new Pontuacao("guerra", 5, "estrela"));
		arm.guardar(new Pontuacao("guerra", 5, "comentario"));
		arm.guardar(new Pontuacao("guerra", 5, "moeda"));
		
		var tiposEsperados = new ArrayList<>(Arrays.asList(
				"estrela", "comentario", "moeda"));

		var tiposRecuperados = arm.recuperarTiposDePonto("guerra");
		assertEquals(tiposEsperados, tiposRecuperados);
	}
	
	@Test
	public void recuperaVariosTiposDePontosUsuariosDiferentes() {
		arm.guardar(new Pontuacao("guerra", 1, "estrela"));
		arm.guardar(new Pontuacao("guerra", 1, "estrela"));
		arm.guardar(new Pontuacao("guerra", 1, "comentario"));
		arm.guardar(new Pontuacao("guerra", 1, "moeda"));
		
		arm.guardar(new Pontuacao("maria",  1, "curtida"));
		arm.guardar(new Pontuacao("tadeu",  1, "estrela"));
		arm.guardar(new Pontuacao("jose",   1, "moeda"));
		arm.guardar(new Pontuacao("jose",   1, "compartilhamento"));
		
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
