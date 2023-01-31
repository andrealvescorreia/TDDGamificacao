import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import excecoes.PontuacaoInvalidaException;

public class PlacarTest {
	MockArmazenamento mockArmazenamento;
	Placar placar;
	HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios;
	
	@Before
	public void setUp() {
		mockArmazenamento = new MockArmazenamento();
		placar = new Placar(mockArmazenamento);
		pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
	}
	

	@Test
	public void adicionarUmaPontuacao() {
		placar.adicionarPontuacao("guerra", 10, "estrela");
		ArrayList<String> execucaoEsperada = new ArrayList<>(
			Arrays.asList("guerra 10 estrela")
		);
		// assegura que a classe Placar realmente manda os dados para serem salvos pelo Armazenamento.
		mockArmazenamento.verificaChamadasGuardarPontuacao(execucaoEsperada);
	}
	
	@Test
	public void adicionarMultiplasPontuacoes() {
		placar.adicionarPontuacao("guerra", 10, "estrela");
		placar.adicionarPontuacao("marco", 1, "moeda");
		placar.adicionarPontuacao("marco", 9, "moeda");
		placar.adicionarPontuacao("maria", 4, "estrela");
		placar.adicionarPontuacao("maria", 2, "curtida");
		
		
		ArrayList<String> execucoesEsperadas = new ArrayList<>(
			Arrays.asList("guerra 10 estrela", 
						  "marco 1 moeda", 
						  "marco 9 moeda",
						  "maria 4 estrela",
						  "maria 2 curtida")
		);
		mockArmazenamento.verificaChamadasGuardarPontuacao(execucoesEsperadas);
	}
	
	@Test
	public void adicionarPontuacaoInvalida() {
		mockArmazenamento.simulePontuacaoInvalida();
		try {
			placar.adicionarPontuacao("INVALIDA", 0, "INVALIDA");
			fail();
		} catch(PontuacaoInvalidaException e) {}
	}
	
	@Test
	public void pontuacoesDeUsuarioSemPontos() {
		HashMap<String, Integer> pontuacaoVazia = new HashMap<String, Integer>();
		assertEquals(pontuacaoVazia, placar.pontuacoes("jeremias"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComUmTipoDePonto() {
		HashMap<String, Integer> pontuacoesUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesUsuarioGuerra.put("estrela", 10);
		
		
		pontuacoesUsuarios.put("guerra", pontuacoesUsuarioGuerra);
		
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
		assertEquals(pontuacoesUsuarioGuerra, placar.pontuacoes("guerra"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComMultiplosTiposDePontos() {
		HashMap<String, Integer> pontuacoesUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesUsuarioGuerra.put("estrela", 10);
		pontuacoesUsuarioGuerra.put("moeda", 25);
		pontuacoesUsuarioGuerra.put("curtida", 404);
		
		pontuacoesUsuarios.put("guerra", pontuacoesUsuarioGuerra);

		assertEquals(pontuacoesUsuarioGuerra, placar.pontuacoes("guerra"));
	}
	@Test
	public void quandoNaoHaUsuariosEntaoNaoHaRanking() {
		ArrayList<String> rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	@Test
	public void quandoHaApenasUmUsuarioPoremEleNaoTemOTipoEstrelaEntaoNaoHaRankingEstrela() {
		HashMap<String, Integer> pontuacoesDoUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesDoUsuarioGuerra.put("curtida", 10);
		pontuacoesDoUsuarioGuerra.put("comentario", 1);
		pontuacoesDoUsuarioGuerra.put("favorito", 5);
		
		pontuacoesUsuarios.put("guerra", pontuacoesDoUsuarioGuerra);
		
		ArrayList<String> rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	
	@Test
	public void quandoHaVariosUsuariosPoremNenhumTemOTipoEstrelaEntaoNaoHaRankingEstrela() {
		HashMap<String, Integer> pontuacoesDoUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesDoUsuarioGuerra.put("curtida", 10);
		pontuacoesDoUsuarioGuerra.put("comentario", 10);
		pontuacoesDoUsuarioGuerra.put("favorito", 10);
		
		HashMap<String, Integer> pontuacoesDoUsuarioTadeu = new HashMap<String, Integer>();
		pontuacoesDoUsuarioTadeu.put("favorito", 5);
		pontuacoesDoUsuarioTadeu.put("curtida", 10);
		pontuacoesDoUsuarioTadeu.put("moeda", 10);
		
		pontuacoesUsuarios.put("guerra", pontuacoesDoUsuarioGuerra);
		pontuacoesUsuarios.put("tadeu", pontuacoesDoUsuarioTadeu);

		ArrayList<String> rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	@Test
	public void quandoHaApenasUmUsuarioEEleTemOTipoEstrelaEntaoORankingEstrelaTemApenasEle() {
		HashMap<String, Integer> pontuacoesDoUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesDoUsuarioGuerra.put("moeda", 2);
		pontuacoesDoUsuarioGuerra.put("estrela", 10);
		pontuacoesDoUsuarioGuerra.put("curtida", 3);
		
		pontuacoesUsuarios.put("guerra", pontuacoesDoUsuarioGuerra);
		
		ArrayList<String> rankingEsperado = new ArrayList<String>();
		rankingEsperado.add("guerra 10");
		assertEquals(rankingEsperado, placar.ranking("estrela"));
	}
	
	
	
	
	
	
}
