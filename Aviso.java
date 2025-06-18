package sistema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Aviso {
    private String titulo;
    private String mensagem;
    private String autor;


    // Lista estática para armazenar avisos em memória (opcional)
    private static List<Aviso> listaAvisos = new ArrayList<>();

    // Construtor com geração automática da data atual
    public Aviso(String titulo, String mensagem, String autor) {
        this.titulo = titulo;
        this.mensagem = mensagem;
        this.autor = autor;

    }



    public String getTitulo() {
        return titulo;
    }

    public String getMensagem() {
        return mensagem;
    }

    public String getAutor() {
        return autor;
    }

    // Métodos para manipular a lista em memória (opcional)
    public static List<Aviso> getAvisos() {
        return listaAvisos;
    }

    public static void adicionarAviso(Aviso aviso, Usuario usuario, String disciplina) throws SQLException {
        if (usuario == null) {
            throw new IllegalArgumentException("Usuário autor do aviso não pode ser nulo.");
        }

        String sql = "INSERT INTO avisos (titulo, texto, disciplina_id, autor_id) " +
                "SELECT ?, ?, d.id, u.id " +
                "FROM disciplinas d, usuarios u " +
                "WHERE d.nome = ? AND u.login = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, aviso.getTitulo());
            stmt.setString(2, aviso.getMensagem());
            stmt.setString(3, disciplina);
            stmt.setString(4, usuario.getLogin());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Falha ao inserir aviso: disciplina ou usuário não encontrado.");
            }
        }

    }

    // Representação textual do aviso
    @Override
    public String toString() {
        return   autor + ": " + titulo + " - " + mensagem;
    }
}
