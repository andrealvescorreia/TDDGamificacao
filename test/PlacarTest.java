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
	
	@Before
	public void antes() {
		mockArmazenamento = new MockArmazenamento();
		placar = new Placar(mockArmazenamento);
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
		assertEquals(new HashMap<String, Integer>(), placar.pontuacoes("jeremias"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComUmTipoDePonto() {
		HashMap<String, Integer> pontuacoesUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesUsuarioGuerra.put("estrela", 10);
		
		HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios =  new HashMap<String, HashMap<String, Integer>>();
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
		
		HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios =  new HashMap<String, HashMap<String, Integer>>();
		pontuacoesUsuarios.put("guerra", pontuacoesUsuarioGuerra);
		
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
		assertEquals(pontuacoesUsuarioGuerra, placar.pontuacoes("guerra"));
	}
	@Test
	public void quandoNaoHaUsuariosEntaoNaoHaRanking() {
		ArrayList<HashMap<String, Integer>> rankingVazio = new ArrayList<HashMap<String, Integer>>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	@Test
	public void quandoHaApenasUmUsuarioPoremEleNaoTemOTipoEstrelaEntaoNaoHaRankingEstrela() {
		HashMap<String, Integer> pontuacaoUsuarioGuerra = new HashMap<String, Integer>();
		pontuacaoUsuarioGuerra.put("curtida", 10);
		pontuacaoUsuarioGuerra.put("comentario", 1);
		pontuacaoUsuarioGuerra.put("favorito", 10);
		
		

		HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios =  new HashMap<String, HashMap<String, Integer>>();
		pontuacoesUsuarios.put("guerra", pontuacaoUsuarioGuerra);
		
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
		
		ArrayList<HashMap<String, Integer>> rankingVazio = new ArrayList<HashMap<String, Integer>>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	
	@Test
	public void quandoHaVariosUsuariosPoremNenhumTemOTipoEstrelaEntaoNaoHaRankingEstrela() {
		HashMap<String, Integer> pontuacoesUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesUsuarioGuerra.put("curtida", 10);
		pontuacoesUsuarioGuerra.put("comentario", 10);
		pontuacoesUsuarioGuerra.put("favorito", 10);
		
		HashMap<String, Integer> pontuacoesUsuarioTadeu = new HashMap<String, Integer>();
		pontuacoesUsuarioTadeu.put("favorito", 5);
		pontuacoesUsuarioTadeu.put("curtida", 10);
		pontuacoesUsuarioTadeu.put("moeda", 10);
		
		HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
		pontuacoesUsuarios.put("guerra", pontuacoesUsuarioGuerra);
		pontuacoesUsuarios.put("tadeu", pontuacoesUsuarioTadeu);
		
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
		
		ArrayList<HashMap<String, Integer>> rankingVazio = new ArrayList<HashMap<String, Integer>>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	/*@Test
	public void quandoHaApenasUmUsuarioEEleTemOTipoEstrelaEntaoORankingEstrelaTemApenasEle() {
		HashMap<String, Integer> pontuacoesUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesUsuarioGuerra.put("estrela", 10);
		
		HashMap<String, HashMap<String, Integer>> pontuacoesUsuarios = new HashMap<String, HashMap<String, Integer>>();
		pontuacoesUsuarios.put("guerra", pontuacoesUsuarioGuerra);
		
		mockArmazenamento.setPontuacoesUsuarios(pontuacoesUsuarios);
		
		HashMap<String, HashMap<String, Integer>> rankingEsperado = new HashMap<String, HashMap<String, Integer>>();
		
		HashMap<String, Integer> primeiraPosicaoNoRankingEstrela = new HashMap<String, Integer>();
		primeiraPosicaoNoRankingEstrela.put("guerra", 10);
		rankingEsperado.put("1°", primeiraPosicaoNoRankingEstrela);
	}*/
}
