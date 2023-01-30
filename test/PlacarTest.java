import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlacarTest {

	@Test
	public void guardarUmaPontuacao() {
		MockArmazenamento mockArmazenamento = new MockArmazenamento();
		Placar p = new Placar(mockArmazenamento);
		p.adicionarPontuacao("guerra", 10, "estrela");
		
		
		ArrayList<String> execucaoEsperada = new ArrayList<>(
				Arrays.asList("guerra 10 estrela")
		);
		
		// assegura que a classe Placar realmente manda os dados para serem salvos pelo Armazenamento.
		mockArmazenamento.verificaExecucoes(execucaoEsperada);
	}
	
	@Test
	public void guardarMultiplasPontuacoes() {
		MockArmazenamento mockArmazenamento = new MockArmazenamento();
		Placar p = new Placar(mockArmazenamento);
		p.adicionarPontuacao("guerra", 10, "estrela");
		p.adicionarPontuacao("marco", 1, "moeda");
		p.adicionarPontuacao("marco", 9, "moeda");
		p.adicionarPontuacao("maria", 4, "estrela");
		p.adicionarPontuacao("maria", 2, "curtida");
		
		
		ArrayList<String> execucoesEsperadas = new ArrayList<>(
				Arrays.asList("guerra 10 estrela", 
							  "marco 1 moeda", 
							  "marco 9 moeda",
							  "maria 4 estrela",
							  "maria 2 curtida")
		);
		mockArmazenamento.verificaExecucoes(execucoesEsperadas);
	}

}
