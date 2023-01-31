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
		
		mockArmazenamento.setPontuacoesDoUsuario(pontuacoesUsuarioGuerra);
		assertEquals(pontuacoesUsuarioGuerra, placar.pontuacoes("guerra"));
	}
	
	@Test
	public void pontuacoesDeUsuarioComMultiplosTiposDePontos() {
		HashMap<String, Integer> pontuacoesUsuarioGuerra = new HashMap<String, Integer>();
		pontuacoesUsuarioGuerra.put("estrela", 10);
		pontuacoesUsuarioGuerra.put("moeda", 25);
		pontuacoesUsuarioGuerra.put("curtida", 404);
		
		mockArmazenamento.setPontuacoesDoUsuario(pontuacoesUsuarioGuerra);
		assertEquals(pontuacoesUsuarioGuerra, placar.pontuacoes("guerra"));
	}
	
	
}
