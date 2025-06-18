package sistema;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sistema {
    private static final ArrayList<Usuario> usuarios = new ArrayList<>();
    public static final Scanner scanner = new Scanner(System.in);

    public static ArrayList<Usuario> getUsuarios() {
        return usuarios;
    }

    public static void main(String[] args) {
        Autenticacao.setUsuarios(new ArrayList<>());
        Autenticacao.inicializarUsuariosFicticios();

        Autenticacao.mostrarEstatisticas();

        while (true) {
            System.out.println("\n=== Sistema ClassControl ===");
            System.out.println("1. Login");
            System.out.println("2. Cadastrar novo usuário");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1" -> login();
                case "2" -> Autenticacao.cadastrarUsuario();
                case "0" -> {
                    System.out.println("Encerrando o sistema.");
                    return;
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

    private static void login() {
        int tentativas = 3;
        while (tentativas-- > 0) {
            System.out.print("Login: ");
            String login = scanner.nextLine().trim();

            System.out.print("Senha: ");
            String senha = scanner.nextLine().trim();

            Usuario usuario = Autenticacao.autenticarUsuario(login, senha);

            if (usuario != null) {
                usuario.exibirMenu();
                return;
            } else {
                System.out.println("❌ Login ou senha incorretos. Tentativas restantes: " + tentativas);
            }
        }
        System.out.println("🔒 Muitas tentativas. Voltando ao menu.");
    }

    public static class Autenticacao {
        private static List<Usuario> usuarios = new ArrayList<>();

        public static void setUsuarios(List<Usuario> lista) {
            usuarios = lista;
        }

        public static String cadastrarUsuario(String nome, String login, String senha, String email,
                                              String dataNascimento, String tipo, String ra) {
            if (loginExists(login)) return "Erro: Nome de usuário já está em uso.";
            if (!validarNome(nome)) return "Erro: Nome inválido.";
            if (!validarSenha(senha)) return "Erro: Senha muito curta.";
            if (!validarEmail(email)) return "Erro: E-mail inválido.";
            if (!validarTipo(tipo)) return "Erro: Tipo de usuário inválido.";

            if (tipo.equalsIgnoreCase("aluno")) {
                if (ra == null || ra.trim().isEmpty()) return "Erro: RA é obrigatório para alunos.";
                Aluno aluno = new Aluno(nome, login, senha, email, dataNascimento, ra);
                usuarios.add(aluno);
                return "Cadastro de aluno realizado com sucesso!";
            } else {
                Professor prof = new Professor(nome, login, senha, email, dataNascimento);
                usuarios.add(prof);
                return "Cadastro de professor realizado com sucesso!";
            }
        }

        private static boolean loginExists(String login) {
            for (Usuario u : usuarios) {
                if (u.getLogin().equalsIgnoreCase(login)) return true;
            }
            return false;
        }

        private static boolean validarNome(String nome) {
            return nome != null && nome.trim().length() >= 3;
        }

        private static boolean validarSenha(String senha) {
            return senha != null && senha.length() >= 6;
        }

        private static boolean validarEmail(String email) {
            return email != null && email.contains("@") && email.contains(".");
        }

        private static boolean validarTipo(String tipo) {
            return tipo != null && (tipo.equalsIgnoreCase("aluno") || tipo.equalsIgnoreCase("professor"));
        }

        public static Usuario autenticarUsuario(String login, String senha) {
            for (Usuario u : usuarios) {
                if (u.getLogin().equals(login) && u.autenticar(senha)) {
                    return u;
                }
            }
            return null;
        }

        public static void cadastrarUsuario() {
            Scanner scanner = Sistema.scanner;
            System.out.println("=== Cadastro de Usuário ===");

            String nome;
            while (true) {
                System.out.print("Nome completo: ");
                nome = scanner.nextLine().trim();
                if (validarNome(nome)) break;
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
                if (loginExists(login)) {
                    System.out.println("⚠️ Login já em uso. Escolha outro.");
                } else {
                    break;
                }
            }

            String senha;
            while (true) {
                System.out.print("Senha: ");
                senha = scanner.nextLine();
                if (validarSenha(senha)) break;
                System.out.println("⚠️ Senha muito curta. Mínimo de 6 caracteres.");
            }

            String email;
            while (true) {
                System.out.print("E-mail: ");
                email = scanner.nextLine().trim();
                if (validarEmail(email)) break;
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

            if (tipo.equals("a")) {
                String ra;
                while (true) {
                    System.out.print("RA: ");
                    ra = scanner.nextLine().trim();
                    if (!ra.isEmpty()) break;
                    System.out.println("⚠️ RA não pode ser vazio.");
                }
                String resultado = cadastrarUsuario(nome, login, senha, email, dataNascimento, "aluno", ra);
                System.out.println(resultado.startsWith("Erro") ? "❌ " + resultado : "✅ " + resultado);
            } else if (tipo.equals("p")) {
                String resultado = cadastrarUsuario(nome, login, senha, email, dataNascimento, "professor", null);
                System.out.println(resultado.startsWith("Erro") ? "❌ " + resultado : "✅ " + resultado);
            } else {
                System.out.println("⚠️ Tipo inválido. Cadastro cancelado.");
            }
        }

        public static void inicializarUsuariosFicticios() {
            usuarios.add(new Aluno("João Silva", "joao123", "senha123", "joao@email.com", "01/01/2000", "123456"));
            usuarios.add(new Professor("Maria Oliveira", "maria4", "senha4", "prof@gmail.com", "02/02/1985"));
        }

        public static void mostrarEstatisticas() {
            if (usuarios == null || usuarios.isEmpty()) {
                System.out.println("Nenhum usuário cadastrado ainda.");
                return;
            }

            int totalUsuarios = usuarios.size();
            int totalAlunos = 0;
            int totalProfessores = 0;
            int totalDisciplinas = 0;
            int totalAvisos = 0;

            for (Usuario u : usuarios) {
                if (u instanceof Aluno a) {
                    totalAlunos++;
                    totalDisciplinas += a.getDisciplinas().size();
                    for (Disciplina d : a.getDisciplinas()) {
                        totalAvisos += d.getAvisos().size();
                    }
                } else if (u instanceof Professor p) {
                    totalProfessores++;
                    totalDisciplinas += p.getDisciplinas().size();
                    for (Disciplina d : p.getDisciplinas()) {
                        totalAvisos += d.getAvisos().size();
                    }
                }
            }

            System.out.println("=== 📊 Estatísticas do Sistema ===");
            System.out.println("👥 Total de usuários: " + totalUsuarios);
            System.out.println("🧑‍🎓 Alunos: " + totalAlunos);
            System.out.println("👨‍🏫 Professores: " + totalProfessores);
            System.out.println("📘 Disciplinas cadastradas: " + totalDisciplinas);
            System.out.println("📢 Total de avisos publicados: " + totalAvisos);
            System.out.println("===================================");
        }
    }
}
