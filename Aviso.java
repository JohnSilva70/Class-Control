public class Aviso {
    private String mensagem;
    private String autor;
    private String data;

    public Aviso(String mensagem, String autor, String data) {
        this.mensagem = mensagem;
        this.autor = autor;
        this.data = data;
    }

    public String toString() {
        return "[" + data + "] " + autor + ": " + mensagem;
    }
}


