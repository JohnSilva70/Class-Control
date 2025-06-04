import java.sql.*;
import java.util.Scanner;

public class Sistema {
    private static Connection connection;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Estabelecer a conexão com o banco de dados
        conectarBanco();

        // Mostrar estatísticas antes de iniciar
        mostrarEstatisticas();

        // Loop principal do sistema
        while (true) {
            System.out.println("\n=== Sistema ClassControl ===");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar novo usuário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    login();
                    break;
                case "2":
                    cadastrarUsuario();
                    break;
                case "0":
                    System.out.println("Encerrando o sistema.");
                    fecharConexao();
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void conectarBanco() {
        try {
            String url = "jdbc:sqlite:classcontrol.db"; // Nome do arquivo do banco de dados
            connection = DriverManager.getConnection(url);
            System.out.println("✅ Conexão com o banco de dados estabelecida.");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao conectar ao banco de dados: " + e.getMessage());
            System.exit(1);
        }
    }

    private static void fecharConexao() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("✅ Conexão com o banco encerrada.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao fechar o banco de dados: " + e.getMessage());
        }
    }

    private static void mostrarEstatisticas() {
        String sql = "SELECT COUNT(*) AS total FROM usuarios";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                int totalUsuarios = rs.getInt("total");
                System.out.println("=== 📊 Estatísticas do Sistema ===");
                System.out.println("👥 Total de usuários: " + totalUsuarios);
                System.out.println("===================================");
            }

        } catch (SQLException e) {
            System.out.println("⚠️ Não foi possível carregar as estatísticas: " + e.getMessage());
        }
    }

    private static void cadastrarUsuario() {
        System.out.println("=== Cadastro de Usuário ===");
        String nome;
        while (true) {
            System.out.print("Nome completo: ");
            nome = scanner.nextLine().trim();
            if (nome.length() >= 3) break;
            System.out.println("⚠️ Nome muito curto. Mínimo de 3 caracteres.");
        }

        String login;
        while (true) {
            System.out.print("Login desejado: ");
            login = scanner.nextLine().trim();
            if (!login.matches("[a-zA-Z0-9]{4,}")) {
                System.out.println("⚠️ Login inválido. Use apenas letras e números, mínimo 4 caracteres.");
                continue;
            }
            if (!loginDisponivel(login)) {
                System.out.println("⚠️ Login já em uso. Escolha outro.");
            } else break;
        }

        String senha;
        while (true) {
            System.out.print("Senha: ");
            senha = scanner.nextLine();
            if (senha.length() >= 6) break;
            System.out.println("⚠️ Senha muito curta. Mínimo de 6 caracteres.");
        }

        String email;
        while (true) {
            System.out.print("E-mail: ");
            email = scanner.nextLine().trim();
            if (email.contains("@") && email.contains(".")) break;
            System.out.println("⚠️ E-mail inválido.");
        }

        String dataNascimento;
        while (true) {
            System.out.print("Data de nascimento (dd/mm/aaaa): ");
            dataNascimento = scanner.nextLine().trim();
            if (dataNascimento.matches("\\d{2}/\\d{2}/\\d{4}")) break;
            System.out.println("⚠️ Data inválida. Use o formato correto.");
        }

        System.out.print("Você é aluno ou professor? (a/p): ");
        String tipo = scanner.nextLine().trim().toLowerCase();

        String ra = null;
        if (tipo.equals("a")) {
            while (true) {
                System.out.print("RA: ");
                ra = scanner.nextLine().trim();
                if (!ra.isEmpty()) break;
                System.out.println("⚠️ RA não pode ser vazio.");
            }
            tipo = "aluno";
        } else if (tipo.equals("p")) {
            tipo = "professor";
        } else {
            System.out.println("⚠️ Tipo inválido. Cadastro cancelado.");
            return;
        }

        // Inserir o usuário no banco de dados
        String sql = "INSERT INTO usuarios (nome, login, senha, email, data_nascimento, tipo, ra) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            pstmt.setString(2, login);
            pstmt.setString(3, senha);
            pstmt.setString(4, email);
            pstmt.setString(5, dataNascimento);
            pstmt.setString(6, tipo);
            pstmt.setString(7, ra);
            pstmt.executeUpdate();
            System.out.println("✅ Usuário cadastrado com sucesso.");
        } catch (SQLException e) {
            System.out.println("❌ Erro ao cadastrar usuário: " + e.getMessage());
        }
    }

    private static boolean loginDisponivel(String login) {
        String sql = "SELECT COUNT(*) AS total FROM usuarios WHERE login = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("total") == 0;
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao verificar login: " + e.getMessage());
        }
        return false;
    }

    private static void login() {
        System.out.print("Login: ");
        String login = scanner.nextLine().trim();

        System.out.print("Senha: ");
        String senha = scanner.nextLine().trim();

        String sql = "SELECT * FROM usuarios WHERE login = ? AND senha = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, login);
            pstmt.setString(2, senha);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                System.out.println("✅ Login realizado com sucesso!");
                // Aqui você pode expandir funcionalidades para os usuários logados
            } else {
                System.out.println("❌ Login ou senha incorretos.");
            }
        } catch (SQLException e) {
            System.out.println("❌ Erro ao tentar logar: " + e.getMessage());
        }
    }
}