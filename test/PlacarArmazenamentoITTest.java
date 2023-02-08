// TESTE DE INTEGRACAO ENTRE PLACAR E ARMAZENAMENTO.

import static org.junit.Assert.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import armazenamento.excecoes.FalhaNoArmazenamentoException;

public class PlacarArmazenamentoITTest {
	Placar placar;
	static final String CAMINHO_ARQUIVO = "saida.json";
	
	@Before
	public void inicializarPlacar() {
		try {
			escreverNoArquivoDeArmazenamento("");
			var arm = new ArmazenamentoArquivo(CAMINHO_ARQUIVO);
			placar = new Placar(arm);
		} catch (FalhaNoArmazenamentoException e) {
			e.printStackTrace();
			fail();
		}
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
		placar.adicionar(new Pontuacao("guerra", 10, "estrela"));
		placar.adicionar(new Pontuacao("guerra", 30, "estrela"));
		placar.adicionar(new Pontuacao("guerra", 15, "estrela"));
		placar.adicionar(new Pontuacao("guerra", 15, "curtida"));
		placar.adicionar(new Pontuacao("guerra",  2, "comentario"));
		placar.adicionar(new Pontuacao("guerra",  3, "comentario"));
		
		var guerraPontuacoesEsperadas = new HashMap<String, Integer>();
		guerraPontuacoesEsperadas.put("estrela",   55);
		guerraPontuacoesEsperadas.put("curtida",   15);
		guerraPontuacoesEsperadas.put("comentario", 5);
		
		assertEquals(guerraPontuacoesEsperadas, placar.pontuacoes("guerra"));
	}
	
	@Test
	public void ranking() {
		placar.adicionar(new Pontuacao("guerra", 10, "estrela"));
		placar.adicionar(new Pontuacao("guerra",  8, "comentario"));
		placar.adicionar(new Pontuacao("guerra",  1, "favorito"));
		
		placar.adicionar(new Pontuacao("tadeu",   6, "favorito"));
		placar.adicionar(new Pontuacao("tadeu",  12, "curtida"));
		placar.adicionar(new Pontuacao("tadeu",   2, "moeda"));
		placar.adicionar(new Pontuacao("tadeu",  25, "estrela"));
		
		placar.adicionar(new Pontuacao("maria",  37, "moeda"));
		placar.adicionar(new Pontuacao("maria",   8, "comentario"));
		placar.adicionar(new Pontuacao("maria",   1, "favorito"));
		
		placar.adicionar(new Pontuacao("marco",   5, "favorito"));
		placar.adicionar(new Pontuacao("marco",  11, "curtida"));
		placar.adicionar(new Pontuacao("marco",  10, "moeda"));
		placar.adicionar(new Pontuacao("marco",   1, "energia"));
		
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
		
		var rankingMoeda = placar.ranking("moeda");
		assertEquals( 3, 		  rankingMoeda.size());
		assertEquals("maria 37",  rankingMoeda.get(0));// 1°
		assertEquals("marco 10",  rankingMoeda.get(1));// 2°
		assertEquals( "tadeu 2",  rankingMoeda.get(2));// 3°
		
		var rankingEnergia = placar.ranking("energia");
		assertEquals( 1, 		  rankingEnergia.size());
		assertEquals("marco 1",   rankingEnergia.get(0));// 1°
	}
}
