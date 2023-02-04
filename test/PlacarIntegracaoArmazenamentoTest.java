import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import pontuacao.excecoes.PontuacaoInvalidaException;

public class PlacarIntegracaoArmazenamentoTest {
	Placar placar;
	static final String CAMINHO_ARQUIVO = "saida.json";
	
	@Before
	public void inicializarPlacar() {
		try {
			var arm = new ArmazenamentoArquivo(CAMINHO_ARQUIVO);
			placar = new Placar(arm);
		} catch (IOException e) {
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
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		}
	}
	
	
	@Test
	public void adicionarPontuacoes() {
		placar.adicionarPontuacao("guerra", 10, "estrela");
		placar.adicionarPontuacao("guerra", 30, "estrela");
		placar.adicionarPontuacao("guerra", 15, "estrela");
		placar.adicionarPontuacao("guerra", 15, "curtida");
		placar.adicionarPontuacao("guerra",  2, "comentario");
		placar.adicionarPontuacao("guerra",  3, "comentario");
		var pontuacoesEsperadas = new HashMap<String, Integer>();
		pontuacoesEsperadas.put("estrela",   55);
		pontuacoesEsperadas.put("curtida",   15);
		pontuacoesEsperadas.put("comentario", 5);
		assertEquals(pontuacoesEsperadas, placar.pontuacoes("guerra"));
	}
	
	@Test
	public void ranking() {
		placar.adicionarPontuacao("guerra", 10, "estrela");
		placar.adicionarPontuacao("guerra",  8, "comentario");
		placar.adicionarPontuacao("guerra",  1, "favorito");
		
		placar.adicionarPontuacao("tadeu",   6, "favorito");
		placar.adicionarPontuacao("tadeu",  12, "curtida");
		placar.adicionarPontuacao("tadeu",   2, "moeda");
		placar.adicionarPontuacao("tadeu",  25, "estrela");
		
		placar.adicionarPontuacao("maria",  37, "moeda");
		placar.adicionarPontuacao("maria",   8, "comentario");
		placar.adicionarPontuacao("maria",   1, "favorito");
		
		placar.adicionarPontuacao("marco",   5, "favorito");
		placar.adicionarPontuacao("marco",  11, "curtida");
		placar.adicionarPontuacao("marco",  10, "moeda");
		placar.adicionarPontuacao("marco",   1, "energia");
		
		var rankingEstrela = placar.ranking("estrela");
		assertEquals( 2, 		  rankingEstrela.size());
		assertEquals( "tadeu 25", rankingEstrela.get(0));// 1°
		assertEquals("guerra 10", rankingEstrela.get(1));// 2°
		
		var rankingFavorito = placar.ranking("favorito");
		assertEquals( 3, 		  rankingFavorito.size());
		assertEquals("tadeu 6",   rankingFavorito.get(0));// 1°
		assertEquals("marco 5",   rankingFavorito.get(1));// 2°
		assertEquals("maria 1",   rankingFavorito.get(2));// 3°
		
		var rankingCurtida = placar.ranking("curtida");
		assertEquals( 2, 		  rankingCurtida.size());
		assertEquals("tadeu 12",  rankingCurtida.get(0));// 1°
		assertEquals("marco 11",  rankingCurtida.get(1));// 2°
		
		var rankingMoeda   = placar.ranking("moeda");
		assertEquals( 3, 		  rankingMoeda.size());
		assertEquals("maria 37",  rankingMoeda.get(0));// 1°
		assertEquals("marco 10",  rankingMoeda.get(1));// 2°
		assertEquals("tadeu 2",   rankingMoeda.get(2));// 3°
		
		var rankingEnergia = placar.ranking("energia");
		assertEquals( 1, 		  rankingEnergia.size());
		assertEquals("marco 1",   rankingEnergia.get(0));// 1°
	}
}
