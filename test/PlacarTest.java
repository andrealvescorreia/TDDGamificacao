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
	HashMap<String, Integer> guerraPontuacoes;
	HashMap<String, Integer> tadeuPontuacoes;
	HashMap<String, Integer> marcoPontuacoes;
	HashMap<String, Integer> mariaPontuacoes;
	@Before
	public void setUp() {
		mockArmazenamento = new MockArmazenamento();
		placar = new Placar(mockArmazenamento);
		pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
		guerraPontuacoes = new HashMap<String, Integer>();
		marcoPontuacoes = new HashMap<String, Integer>();
		tadeuPontuacoes = new HashMap<String, Integer>();
		mariaPontuacoes = new HashMap<String, Integer>();
		pontuacoesUsuarios.put("guerra", guerraPontuacoes);
		pontuacoesUsuarios.put("tadeu", tadeuPontuacoes);
		pontuacoesUsuarios.put("maria", mariaPontuacoes);
		pontuacoesUsuarios.put("marco", marcoPontuacoes);
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
		placar.adicionarPontuacao("guerra", 5, "estrela");
		placar.adicionarPontuacao("guerra", 2, "moeda");
		placar.adicionarPontuacao("marco", 1, "estrela");
		placar.adicionarPontuacao("marco", 9, "moeda");
		placar.adicionarPontuacao("maria", 2, "curtida");
		
		
		ArrayList<String> execucoesEsperadas = new ArrayList<>(
			Arrays.asList("guerra 10 estrela", 
						  "guerra 5 estrela",
						  "guerra 2 moeda",
						  "marco 1 estrela", 
						  "marco 9 moeda",
						  "maria 2 curtida")
		);
		mockArmazenamento.verificaChamadasGuardarPontuacao(execucoesEsperadas);
	}
	
	@Test
	public void adicionarPontuacaoInvalida() {
		mockArmazenamento.simulePontuacaoInvalida();
		try {
			placar.adicionarPontuacao("SIMULACAO", 0, "SIMULACAO");
			fail();
		} catch(PontuacaoInvalidaException e) {}
	}
	
	@Test
	public void pontuacoesDeUsuarioSemPontos() {
		HashMap<String, Integer> semPontuacoes = new HashMap<String, Integer>();
		assertEquals(semPontuacoes, placar.pontuacoes("jeremias"));
		assertEquals(semPontuacoes, placar.pontuacoes("wanderlei"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComUmTipoDePonto() {
		guerraPontuacoes.put("estrela", 10);
		assertEquals(guerraPontuacoes, placar.pontuacoes("guerra"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComMultiplosTiposDePontos() {
		guerraPontuacoes.put("estrela", 10);
		guerraPontuacoes.put("moeda", 25);
		guerraPontuacoes.put("curtida", 404);

		assertEquals(guerraPontuacoes, placar.pontuacoes("guerra"));
	}
	@Test
	public void quandoNaoHaUsuariosEntaoNaoHaRanking() {
		ArrayList<String> rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
		assertEquals(rankingVazio, placar.ranking("moeda"));
		assertEquals(rankingVazio, placar.ranking("curtida"));
	}
	
	@Test
	public void quandoHaApenasUmUsuarioPoremEleNaoTemOTipoEstrelaEntaoNaoHaRankingEstrela() {
		guerraPontuacoes.put("curtida", 10);
		guerraPontuacoes.put("comentario", 10);
		guerraPontuacoes.put("favorito", 10);
		
		ArrayList<String> rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	
	@Test
	public void quandoHaVariosUsuariosPoremNenhumTemOTipoEstrelaEntaoNaoHaRankingEstrela() {
		guerraPontuacoes.put("curtida", 10);
		guerraPontuacoes.put("comentario", 10);
		guerraPontuacoes.put("favorito", 10);

		tadeuPontuacoes.put("favorito", 10);
		tadeuPontuacoes.put("curtida", 10);
		tadeuPontuacoes.put("moeda", 10);

		ArrayList<String> rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	@Test
	public void quandoHaApenasUmUsuarioEEleTemOTipoEstrelaEntaoORankingEstrelaTemApenasEle() {
		guerraPontuacoes.put("moeda", 2);
		guerraPontuacoes.put("estrela", 10);
		guerraPontuacoes.put("curtida", 3);
		
		assertEquals(1, placar.ranking("estrela").size());
		assertEquals("guerra 10", placar.ranking("estrela").get(0));
	}
	
	
	@Test
	public void quandoHaVariosUsuariosPoremApenasUmTemOTipoEstrelaEntaoORankingEstrelaTemApenasEle(){
		guerraPontuacoes.put("estrela", 10);
		guerraPontuacoes.put("comentario", 8);
		guerraPontuacoes.put("favorito", 1);	

		tadeuPontuacoes.put("favorito", 5);
		tadeuPontuacoes.put("curtida", 11);
		tadeuPontuacoes.put("moeda", 2);
		
		assertEquals(1, placar.ranking("estrela").size());
		assertEquals("guerra 10", placar.ranking("estrela").get(0));
	}
	
	
	@Test
	public void quandoHaVariosUsuariosETodosTemOTipoEstrelaEntaoORankingEstrelaTemTodosOsUsuarios() {
		guerraPontuacoes.put("estrela", 10);
		guerraPontuacoes.put("comentario", 8);
		guerraPontuacoes.put("favorito", 1);	

		tadeuPontuacoes.put("favorito", 5);
		tadeuPontuacoes.put("curtida", 11);
		tadeuPontuacoes.put("moeda", 2);
		tadeuPontuacoes.put("estrela", 25);
		
		assertEquals(2, placar.ranking("estrela").size());
		assertEquals("tadeu 25", placar.ranking("estrela").get(0));// 1°
		assertEquals("guerra 10", placar.ranking("estrela").get(1));// 2°
	}
	
	@Test
	public void quandoHaVariosUsuariosEAlgunsTemOTipoEstrelaEntaoORankingEstrelaTemApenasEssesUsuarios() {
		guerraPontuacoes.put("estrela", 10);
		guerraPontuacoes.put("comentario", 8);
		guerraPontuacoes.put("favorito", 1);
		
		tadeuPontuacoes.put("favorito", 5);
		tadeuPontuacoes.put("curtida", 11);
		tadeuPontuacoes.put("moeda", 2);
		tadeuPontuacoes.put("estrela", 25);
		
		mariaPontuacoes.put("estrela", 37);
		mariaPontuacoes.put("comentario", 8);
		mariaPontuacoes.put("favorito", 1);
		
		marcoPontuacoes.put("favorito", 5);
		marcoPontuacoes.put("curtida", 11);
		marcoPontuacoes.put("moeda", 2);
		
		assertEquals(3, placar.ranking("estrela").size());
		assertEquals("maria 37", placar.ranking("estrela").get(0));// 1°
		assertEquals("tadeu 25", placar.ranking("estrela").get(1));// 2°
		assertEquals("guerra 10", placar.ranking("estrela").get(2));// 3°
	}
}
