import java.util.ArrayList;

public interface Armazenamento {
	public void guardarPontuacao(String usuario, long pontos, String tipo);
	public long recuperarPontos(String usuario, String tipo);
	public ArrayList<String> recuperarUsuariosRegistrados();
	public ArrayList<String> recuperarTiposPontuacao(String usuario);
}
