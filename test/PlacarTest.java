import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PlacarTest {

	@Test
	public void guardarUmaPontuacao() {
		MockArmazenamento mockArmazenamento = new MockArmazenamento();
		Placar p = new Placar(mockArmazenamento);
		p.adicionarPontuacao("guerra", 10, "estrela");
		
		// assegura que a classe Placar realmente manda os dados para serem salvos pela classe Armazenamento.
		mockArmazenamento.verificaPedido("guerra", 10, "estrela");
	}

}
