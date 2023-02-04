import static org.junit.Assert.*;

import org.json.simple.JSONObject;
import org.junit.Test;

import excecoes.AdicionarPontosInvalidosException;
import excecoes.ObjetoJsonIvalidoException;
import excecoes.PontuacaoInvalidaException;

public class PontuacaoTest {

	@Test
	public void criarPontuacao() {
		var p = new Pontuacao("guerra", 10, "estrela");
		assertEquals("guerra", p.getUsuario());
		assertEquals(10, p.getPontos());
		assertEquals("estrela", p.getTipo());
	}
	
	@Test(expected = PontuacaoInvalidaException.class)
	public void pontuacaoUsuarioVazio() {
		new Pontuacao("", 10, "estrela");
	}
	@Test(expected = PontuacaoInvalidaException.class)
	public void pontuacaoUsuarioComEspaco() {
		new Pontuacao("eduardo guerra", 10, "estrela");
	}
	@Test(expected = PontuacaoInvalidaException.class)
	public void pontuacaoTipoVazio() {
		new Pontuacao("guerra", 10, "");
	}
	@Test(expected = PontuacaoInvalidaException.class)
	public void pontuacaoTipoComEspaco() {
		new Pontuacao("guerra", 10, "moeda de ouro");
	}
	@Test
	public void adicionarPontos() {
		var p = new Pontuacao("guerra", 10, "estrela");
		p.addPontos(10);
		assertEquals(20, p.getPontos());
	}
	@Test
	public void adicionarPontosInvalidos() {
		var p = new Pontuacao("guerra", 10, "estrela");
		try {
			p.addPontos(-10);
			fail();
		} catch (AdicionarPontosInvalidosException e) {}
		
		assertEquals(10, p.getPontos());
	}
	
	
	@Test
	@SuppressWarnings("unchecked")
	public void criarPontuacaoUsandoJSONObject() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    "estrela");
		var p = new Pontuacao(jsonPontuacao);
		assertEquals("guerra",  p.getUsuario());
		assertEquals(10,        p.getPontos());
		assertEquals("estrela", p.getTipo());
	}
	
	@Test(expected = PontuacaoInvalidaException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsuarioVazioUsandoJSONObject() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "");
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    "estrela");
		new Pontuacao(jsonPontuacao);
	}
	
	@Test(expected = PontuacaoInvalidaException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsuarioComEspacoUsandoJSONObject() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "eduardo guerra");
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    "estrela");
		new Pontuacao(jsonPontuacao);
	}
	@Test(expected = PontuacaoInvalidaException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoTipoVazioUsandoJSONObject() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    "");
		new Pontuacao(jsonPontuacao);
	}
	@Test(expected = PontuacaoInvalidaException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoTipoComEspacoUsandoJSONObject() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    "estrela cadente");
		new Pontuacao(jsonPontuacao);
	}
	
	@Test(expected = ObjetoJsonIvalidoException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsandoJSONObjectSemUsuario() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("tipo",   "estrela");
		jsonPontuacao.put("pontos", (long)10);
		new Pontuacao(jsonPontuacao);
	}
	
	@Test(expected = ObjetoJsonIvalidoException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsandoJSONObjectSemTipo() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("pontos",  (long)10);
		new Pontuacao(jsonPontuacao);
	}
	
	@Test(expected = ObjetoJsonIvalidoException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsandoJSONObjectSemPontos() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("tipo",    "estrela");
		new Pontuacao(jsonPontuacao);
	}
	@Test(expected = ObjetoJsonIvalidoException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsandoJSONObjectUsuarioNaoString() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", 999);
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    "estrela");
		
		new Pontuacao(jsonPontuacao);
	}
	@Test(expected = ObjetoJsonIvalidoException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsandoJSONObjectPontosNaoNumerico() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("pontos",  "nao sou um numero");
		jsonPontuacao.put("tipo",    "estrela");
		
		new Pontuacao(jsonPontuacao);
	}
	@Test(expected = ObjetoJsonIvalidoException.class)
	@SuppressWarnings("unchecked")
	public void pontuacaoUsandoJSONObjectTipoNaoString() {
		var jsonPontuacao = new JSONObject();
		jsonPontuacao.put("usuario", "guerra");
		jsonPontuacao.put("pontos",  (long)10);
		jsonPontuacao.put("tipo",    999);
		
		new Pontuacao(jsonPontuacao);
	}
	
	@Test
	@SuppressWarnings("unchecked")
	public void toJSONObject() {
		var p = new Pontuacao("guerra", 10, "estrela");
		var jsonEsperado = new JSONObject();
		jsonEsperado.put("usuario", "guerra");
		jsonEsperado.put("pontos", (long)10);
		jsonEsperado.put("tipo", "estrela");
		assertEquals(jsonEsperado, p.toJSONObject());
	}
}
