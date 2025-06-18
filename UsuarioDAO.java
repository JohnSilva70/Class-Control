package sistema;

import javax.swing.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class UsuarioDAO {

    // Inserção de novo usuário
    public void adicionarUsuario(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuarios (nome, login, senha, email, data_nascimento, tipo) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getLogin());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario.getEmail());
            stmt.setDate(5, java.sql.Date.valueOf(usuario.getDataNascimento()));
            stmt.setString(6, usuario instanceof Aluno ? "aluno" : "professor");

            stmt.executeUpdate();

            if (usuario instanceof Aluno) {
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    int usuarioId = rs.getInt(1);
                    String ra = ((Aluno) usuario).getRa();

                    String sqlAluno = "INSERT INTO alunos (usuario_id, ra) VALUES (?, ?)";
                    try (PreparedStatement stmtAluno = con.prepareStatement(sqlAluno)) {
                        stmtAluno.setInt(1, usuarioId);
                        stmtAluno.setString(2, ra);
                        stmtAluno.executeUpdate();
                    }
                }
            }
        }
    }

    // Busca usuário por login
    public Usuario buscarPorLogin(String login) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int usuarioId = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String nome = rs.getString("nome");
                String senha = rs.getString("senha");
                String email = rs.getString("email");
                String dataFormatada = rs.getDate("data_nascimento")
                        .toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if ("professor".equalsIgnoreCase(tipo)) {
                    return new Professor(nome, login, senha, email, dataFormatada);
                } else if ("aluno".equalsIgnoreCase(tipo)) {
                    String sqlAluno = "SELECT ra FROM alunos WHERE usuario_id = ?";
                    try (PreparedStatement stmtAluno = con.prepareStatement(sqlAluno)) {
                        stmtAluno.setInt(1, usuarioId);
                        ResultSet rsAluno = stmtAluno.executeQuery();
                        if (rsAluno.next()) {
                            String ra = rsAluno.getString("ra");
                            return new Aluno(nome, login, senha, email, dataFormatada, ra);
                        }
                    }
                }
            }
        }
        return null;
    }

    // Atualiza informações do usuário
    public static void atualizarUsuario(Usuario usuario) throws SQLException {
        String sql = "UPDATE usuarios SET nome = ?, email = ?, data_nascimento = ? WHERE login = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());

            // Trata a data como String (dd/MM/yyyy) e converte para java.sql.Date
            String dataTexto = usuario.getDataNascimento(); // Ex: "13/06/2025"
            java.sql.Date dataSQL;

            try {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                sdf.setLenient(false);
                java.util.Date dataUtil = sdf.parse(dataTexto);
                dataSQL = new java.sql.Date(dataUtil.getTime());
            } catch (ParseException e) {
                throw new IllegalArgumentException("❌ Data de nascimento inválida: " + dataTexto);
            }

            stmt.setDate(3, dataSQL);
            stmt.setString(4, usuario.getLogin());

            stmt.executeUpdate();
        }
    }


    // Login
    public Usuario fazerLogin(String login, String senha) throws SQLException {
        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, senha);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int usuarioId = rs.getInt("id");
                String tipo = rs.getString("tipo");
                String nome = rs.getString("nome");
                String email = rs.getString("email");
                String dataNascimento = rs.getDate("data_nascimento")
                        .toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

                if ("professor".equalsIgnoreCase(tipo)) {
                    return new Professor(nome, login, senha, email, dataNascimento);
                } else if ("aluno".equalsIgnoreCase(tipo)) {
                    String sqlAluno = "SELECT ra FROM alunos WHERE usuario_id = ?";
                    try (PreparedStatement stmtAluno = con.prepareStatement(sqlAluno)) {
                        stmtAluno.setInt(1, usuarioId);
                        ResultSet rsAluno = stmtAluno.executeQuery();
                        if (rsAluno.next()) {
                            String ra = rsAluno.getString("ra");
                            return new Aluno(nome, login, senha, email, dataNascimento, ra);
                        }
                    }
                }
            }
        }
        return null;
    }

    // Busca disciplinas
    public static ArrayList<Disciplina> buscarDisciplinas() throws SQLException {
        ArrayList<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT nome, codigo FROM disciplinas";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Disciplina(rs.getString("nome"), rs.getString("codigo")));
            }
        }
        return lista;
    }

    // Busca alunos
    public static ArrayList<Aluno> buscarAlunos() throws SQLException {
        ArrayList<Aluno> lista = new ArrayList<>();
        String sql = "SELECT u.nome, u.login, u.senha, u.email, u.data_nascimento, a.ra " +
                "FROM usuarios u JOIN alunos a ON u.id = a.usuario_id";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Aluno aluno = new Aluno(
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("email"),
                        rs.getDate("data_nascimento").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getString("ra")
                );
                lista.add(aluno);
            }
        }
        return lista;
    }

    // Busca notas de aluno
    public static ArrayList<String> buscarNotasPorAluno(String login) throws SQLException {
        ArrayList<String> lista = new ArrayList<>();
        String sql = "SELECT d.nome AS disciplina, n.nota " +
                "FROM notas n " +
                "JOIN disciplinas d ON n.disciplina_id = d.id " +
                "JOIN alunos a ON n.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, login);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                lista.add("[" + rs.getString("disciplina") + "] Nota: " + rs.getDouble("nota"));
            }
        }
        return lista;
    }

    public static void adicionarDisciplina(String codigo, String nome) throws SQLException {
        String sql = "INSERT INTO disciplinas (codigo, nome) VALUES (?, ?)";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, nome);
            stmt.executeUpdate();
        }
    }

    public static void adicionarNota(Usuario usuario, String disciplina, Double nota) throws SQLException {
        String sql = "INSERT INTO notas (disciplina_id, aluno_id, nota) " +
                "SELECT d.id, a.usuario_id, ? " +
                "FROM disciplinas d " +
                "JOIN matriculas m ON d.id = m.disciplina_id " +
                "JOIN alunos a ON m.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "WHERE u.login = ? AND d.nome = ?";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setDouble(1, nota);                 // 1º parâmetro: nota
            stmt.setString(2, usuario.getLogin());   // 2º parâmetro: login do aluno
            stmt.setString(3, disciplina);           // 3º parâmetro: nome da disciplina

            int linhasAfetadas = stmt.executeUpdate();
            if (linhasAfetadas == 0) {
                // Isso indica que a combinação aluno/disciplina não foi encontrada (ex: não está matriculado)
                throw new SQLException("Não foi possível adicionar nota: aluno não matriculado na disciplina.");
            }
        }
    }

    public static void listarNotasDoAluno(Usuario usuario, DefaultListModel<String> modelo) throws SQLException {
        modelo.clear(); // Limpa a lista antes de adicionar
        String sql = "SELECT d.nome AS disciplina, n.nota " +
                "FROM notas n " +
                "JOIN disciplinas d ON n.disciplina_id = d.id " +
                "JOIN alunos a ON n.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getLogin());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String disciplinaNome = rs.getString("disciplina");
                double nota = rs.getDouble("nota");
                modelo.addElement(disciplinaNome + ": " + nota);
            }
        }
    }

    public static void addAlunoDisciplina(Usuario usuario, String disciplina) throws SQLException {
        String sql = "INSERT INTO matriculas (aluno_id, disciplina_id) " +
                "SELECT a.usuario_id, d.id " +
                "FROM alunos a " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "JOIN disciplinas d ON d.nome = ? " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, disciplina);
            stmt.setString(2, usuario.getLogin());
            stmt.executeUpdate();
        }
    }

    // Buscar aluno por nome
    public static Aluno buscarAlunoPorNome(String nome) throws SQLException {
        String sql = "SELECT u.nome, u.login, u.senha, u.email, u.data_nascimento, a.ra " +
                "FROM usuarios u JOIN alunos a ON u.id = a.usuario_id " +
                "WHERE u.nome = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Aluno(
                        rs.getString("nome"),
                        rs.getString("login"),
                        rs.getString("senha"),
                        rs.getString("email"),
                        rs.getDate("data_nascimento").toLocalDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                        rs.getString("ra")
                );
            }
        }
        return null;
    }

    // Buscar avisos por disciplina (sem campo 'data')
    public static List<Aviso> getAvisosPorDisciplina(String nomeDisciplina) throws SQLException {
        List<Aviso> avisos = new ArrayList<>();
        String sql = "SELECT a.titulo, a.texto, u.nome AS autor " +
                "FROM avisos a " +
                "JOIN disciplinas d ON a.disciplina_id = d.id " +
                "JOIN usuarios u ON a.autor_id = u.id " +
                "WHERE d.nome = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, nomeDisciplina);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aviso aviso = new Aviso(
                        rs.getString("titulo"),
                        rs.getString("texto"),
                        rs.getString("autor")
                );
                avisos.add(aviso);
            }
        }
        return avisos;
    }

    // Notas por disciplina
    public static Map<String, Double> getNotasPorAluno(String loginAluno) throws SQLException {
        Map<String, Double> notas = new HashMap<>();
        String sql = "SELECT d.nome AS disciplina, n.nota " +
                "FROM notas n " +
                "JOIN disciplinas d ON n.disciplina_id = d.id " +
                "JOIN alunos a ON n.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, loginAluno);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                notas.put(rs.getString("disciplina"), rs.getDouble("nota"));
            }
        }
        return notas;
    }


    // Lista avisos por usuário
    public List<Aviso> listarAvisosDoBanco(Usuario usuario) throws SQLException {
        List<Aviso> avisos = new ArrayList<>();
        String sql = "SELECT a.titulo, a.texto, u.nome AS autor " +
                "FROM avisos a " +
                "JOIN usuarios u ON a.autor_id = u.id " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getLogin());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Aviso aviso = new Aviso(
                        rs.getString("titulo"),
                        rs.getString("texto"),
                        rs.getString("autor")
                );
                avisos.add(aviso);
            }
        }
        return avisos;
    }
    public List<Disciplina> buscarTodasDisciplinas() throws SQLException {
        List<Disciplina> lista = new ArrayList<>();
        String sql = "SELECT nome, codigo FROM disciplinas";

        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new Disciplina(rs.getString("nome"), rs.getString("codigo")));
            }
        }
        return lista;
    }

    public static void mostrarDisciplinasDoAluno(Usuario usuario, DefaultListModel<String> modelo) throws SQLException {
        modelo.clear(); // limpa a lista antes de adicionar
        String sql = "SELECT d.nome AS disciplina " +
                "FROM matriculas m " +
                "JOIN alunos a ON m.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "JOIN disciplinas d ON m.disciplina_id = d.id " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getLogin());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addElement(rs.getString("disciplina"));
            }
        }
    }
   public static void mostrarNotasDoAluno(Usuario usuario, DefaultListModel<String> modelo) throws SQLException {
        modelo.clear(); // limpa a lista antes de adicionar
        String sql = "SELECT d.nome AS disciplina, n.nota " +
                "FROM notas n " +
                "JOIN disciplinas d ON n.disciplina_id = d.id " +
                "JOIN alunos a ON n.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "WHERE u.login = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, usuario.getLogin());
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                modelo.addElement(rs.getString("disciplina") + ": " + rs.getDouble("nota"));
            }
        }
    }
    public static boolean alunoMatriculado(String login, String disciplina) throws SQLException {
        String sql = "SELECT 1 FROM matriculas m " +
                "JOIN alunos a ON m.aluno_id = a.usuario_id " +
                "JOIN usuarios u ON a.usuario_id = u.id " +
                "JOIN disciplinas d ON d.id = m.disciplina_id " +
                "WHERE u.login = ? AND d.nome = ?";
        try (Connection con = ConexaoDB.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setString(1, login);
            stmt.setString(2, disciplina);
            return stmt.executeQuery().next();
        }
    }



}
