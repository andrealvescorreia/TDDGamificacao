import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import excecoes.PontuacaoInvalidaException;

public class PlacarTest {
	Placar placar;
	MockArmazenamento mockArmazenamento;
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
		tadeuPontuacoes  = new HashMap<String, Integer>();
		marcoPontuacoes  = new HashMap<String, Integer>();
		mariaPontuacoes  = new HashMap<String, Integer>();
		pontuacoesUsuarios.put("guerra", guerraPontuacoes);
		pontuacoesUsuarios.put("tadeu",  tadeuPontuacoes);
		pontuacoesUsuarios.put("maria",  mariaPontuacoes);
		pontuacoesUsuarios.put("marco",  marcoPontuacoes);
	}
	

	@Test
	public void adicionarUmaPontuacao() {
		placar.adicionarPontuacao("guerra", 10, "estrela");
		var execucaoEsperada = new ArrayList<>(
			Arrays.asList("guerra 10 estrela")
		);
		// assegura que a classe Placar realmente manda os dados para serem salvos pelo Armazenamento.
		mockArmazenamento.verificaChamadasGuardarPontuacao(execucaoEsperada);
	}
	
	@Test
	public void adicionarMultiplasPontuacoes() {
		placar.adicionarPontuacao("guerra", 10, "estrela");
		placar.adicionarPontuacao("guerra",  5, "estrela");
		placar.adicionarPontuacao("guerra",  2, "moeda");
		placar.adicionarPontuacao("marco",   1, "estrela");
		placar.adicionarPontuacao("marco",   9, "moeda");
		placar.adicionarPontuacao("maria",   2, "curtida");
		
		var chamadasEsperadas = new ArrayList<>(
			Arrays.asList("guerra 10 estrela", 
						  "guerra 5 estrela",
						  "guerra 2 moeda",
						  "marco 1 estrela", 
						  "marco 9 moeda",
						  "maria 2 curtida")
		);
		mockArmazenamento.verificaChamadasGuardarPontuacao(chamadasEsperadas);
	}
	
	@Test(expected = PontuacaoInvalidaException.class )
	public void adicionarPontuacaoInvalida() {
		mockArmazenamento.simulePontuacaoInvalida();
		placar.adicionarPontuacao("SIMULACAO", 0, "SIMULACAO");
	}
	
	@Test
	public void usuarioSemPontuacoes() {
		var semPontuacoes = new HashMap<String, Integer>();
		assertEquals(semPontuacoes, placar.pontuacoes("jeremias"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComUmTipoDePonto() {
		guerraPontuacoes.put("estrela", 1);
		assertEquals(guerraPontuacoes, placar.pontuacoes("guerra"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComMultiplosTiposDePontos() {
		guerraPontuacoes.put("estrela",  1);
		guerraPontuacoes.put("moeda",    1);
		guerraPontuacoes.put("curtida",  1);

		assertEquals(guerraPontuacoes, placar.pontuacoes("guerra"));
	}
	@Test
	public void rankingQuandoNaoHaUsuarios() {
		var rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	@Test
	public void rankingQuandoOUnicoUsuarioNaoTemOTipoDePonto() {
		guerraPontuacoes.put("curtida",    1);
		guerraPontuacoes.put("comentario", 1);
		guerraPontuacoes.put("favorito",   1);
		
		var rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	
	@Test
	public void rankingQuandoNenhumDosUsuariosTemOTIpoDoPonto() {
		guerraPontuacoes.put("curtida",    1);
		guerraPontuacoes.put("comentario", 1);
		guerraPontuacoes.put("favorito",   1);

		tadeuPontuacoes.put ("favorito",   1);
		tadeuPontuacoes.put ("curtida",    1);
		tadeuPontuacoes.put ("moeda",      1);

		var rankingVazio = new ArrayList<String>();
		assertEquals(rankingVazio, placar.ranking("estrela"));
	}
	
	@Test
	public void rankingQuandoOUnicoUsuarioTemOTipoDePonto() {
		guerraPontuacoes.put("moeda",    2);
		guerraPontuacoes.put("estrela", 10);
		guerraPontuacoes.put("curtida",  3);
		
		assertEquals(1, 		  placar.ranking("estrela").size());
		assertEquals("guerra 10", placar.ranking("estrela").get(0));
	}
	
	
	@Test
	public void rankingQuandoApenasUmDosUsuariosTemOTipoDePonto(){
		guerraPontuacoes.put("estrela",   10);
		guerraPontuacoes.put("comentario", 8);
		guerraPontuacoes.put("favorito",   1);	

		tadeuPontuacoes.put ("favorito",   5);
		tadeuPontuacoes.put ("curtida",   11);
		tadeuPontuacoes.put ("moeda",      2);
		
		assertEquals(1, 		  placar.ranking("estrela").size());
		assertEquals("guerra 10", placar.ranking("estrela").get(0));
	}
	
	
	@Test
	public void rankingQuandoTodosOsUsuariosTemOTipoDePonto() {
		guerraPontuacoes.put("estrela",   10);
		guerraPontuacoes.put("comentario", 8);
		guerraPontuacoes.put("favorito",   1);	

		tadeuPontuacoes.put ("favorito",   5);
		tadeuPontuacoes.put ("curtida",   11);
		tadeuPontuacoes.put ("moeda",      2);
		tadeuPontuacoes.put ("estrela",   25);
		
		assertEquals(2, 		  placar.ranking("estrela").size());
		assertEquals("tadeu 25",  placar.ranking("estrela").get(0));// 1°
		assertEquals("guerra 10", placar.ranking("estrela").get(1));// 2°
	}
	
	@Test
	public void rankingQuandoAlgunsUsuariosTemOTipoDePonto() {
		guerraPontuacoes.put("estrela",   10);
		guerraPontuacoes.put("comentario", 8);
		guerraPontuacoes.put("favorito",   1);
		
		tadeuPontuacoes.put ("favorito",   5);
		tadeuPontuacoes.put ("curtida",   11);
		tadeuPontuacoes.put ("moeda",      2);
		tadeuPontuacoes.put ("estrela",   25);
		
		mariaPontuacoes.put ("moeda",     37);
		mariaPontuacoes.put ("comentario", 8);
		mariaPontuacoes.put ("favorito",   1);
		
		marcoPontuacoes.put ("favorito",   5);
		marcoPontuacoes.put ("curtida",   11);
		marcoPontuacoes.put ("moeda",      2);
		
		assertEquals(2, 		  placar.ranking("estrela").size());
		assertEquals("tadeu 25",  placar.ranking("estrela").get(0));// 1°
		assertEquals("guerra 10", placar.ranking("estrela").get(1));// 2°
	}
}