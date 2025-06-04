import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import javax.swing.*;
import java.awt.*;

public class SistemaClassControl {
    private static ArrayList<Usuario> usuarios = new ArrayList<>();

    public static void main(String[] args) {
        mostrarTelaPrincipal();
    }

    private static void mostrarTelaPrincipal() {
        JFrame frame = new JFrame("Sistema ClassControl");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());


        class MenuPrincipalGUI {

            public static void exibirMenuPrincipal(Usuario usuario) {
                JFrame frame = new JFrame("Menu Principal - Usuário: " + usuario.getNome());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setSize(500, 400);
                frame.setLayout(new BorderLayout());

                JLabel lblTitulo = new JLabel("Bem-vindo, " + usuario.getNome(), SwingConstants.CENTER);
                lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
                frame.add(lblTitulo, BorderLayout.NORTH);

                JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
                JButton btnSair = new JButton("Sair");
                btnSair.addActionListener(e -> frame.dispose());
                panel.add(btnSair);

                frame.add(panel, BorderLayout.CENTER);
                frame.setVisible(true); // Fundamental para exibir a tela
            }

            private static String calcularEstatisticas(Usuario usuario) {
                StringBuilder estatisticas = new StringBuilder();

                if (usuario instanceof Aluno) {
                    Aluno aluno = (Aluno) usuario;
                    estatisticas.append("=== Estatísticas do Aluno ===\n");
                    estatisticas.append("Nome: ").append(aluno.getNome()).append("\n");
                    estatisticas.append("Disciplinas cadastradas: ").append(aluno.getDisciplinas().size()).append("\n");
                    if (!aluno.getDisciplinas().isEmpty()) {
                        estatisticas.append("Disciplinas: \n");
                        aluno.getDisciplinas().forEach(d -> estatisticas.append(" - ").append(d.getNome()).append("\n"));
                    }
                } else if (usuario instanceof Professor) {
                    Professor professor = (Professor) usuario;
                    estatisticas.append("=== Estatísticas do Professor ===\n");
                    estatisticas.append("Nome: ").append(professor.getNome()).append("\n");
                    estatisticas.append("Disciplinas ministradas: ").append(professor.getDisciplinas().size()).append("\n");
                    if (!professor.getDisciplinas().isEmpty()) {
                        estatisticas.append("Disciplinas: \n");
                        professor.getDisciplinas().forEach(d -> estatisticas.append(" - ").append(d.getNome()).append("\n"));
                    }
                }

                return estatisticas.toString();
            }
        }
        JPanel panel = new JPanel(new GridLayout(4, 1, 10, 10));
        JLabel lblTitulo = new JLabel("Bem-vindo ao Sistema ClassControl", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        panel.add(lblTitulo);

        JButton btnLogin = new JButton("Login");
        btnLogin.addActionListener(e -> mostrarTelaLogin(frame));
        panel.add(btnLogin);

        JButton btnCadastro = new JButton("Cadastrar Novo Usuário");
        btnCadastro.addActionListener(e -> mostrarTelaCadastro(frame));
        panel.add(btnCadastro);

        JButton btnEstatisticas = new JButton("Ver Estatísticas");
        btnEstatisticas.addActionListener(e -> mostrarEstatisticas(frame));
        panel.add(btnEstatisticas);

        frame.add(panel, BorderLayout.CENTER);
        frame.setVisible(true);
    }

    private static void mostrarTelaLogin(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Login", true);
        dialog.setSize(300, 200);
        dialog.setLayout(new GridLayout(4, 2, 10, 10));

        dialog.add(new JLabel("Login:"));
        JTextField txtLogin = new JTextField();
        dialog.add(txtLogin);

        dialog.add(new JLabel("Senha:"));
        JPasswordField txtSenha = new JPasswordField();
        dialog.add(txtSenha);

        JButton btnEntrar = new JButton("Entrar");
        btnEntrar.addActionListener(e -> {
            String login = txtLogin.getText();
            String senha = new String(txtSenha.getPassword());

            Usuario usuario = autenticarUsuario(login, senha);
            if (usuario != null) {
                JOptionPane.showMessageDialog(dialog, "Login bem-sucedido! Acesse o menu do usuário.");
                dialog.dispose();
            } else {
                JOptionPane.showMessageDialog(dialog, "Login ou senha incorretos.", "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
        dialog.add(btnEntrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.add(btnCancelar);

        dialog.setVisible(true);
    }

    private static void mostrarTelaCadastro(JFrame parentFrame) {
        JDialog dialog = new JDialog(parentFrame, "Cadastro de Usuário", true);
        dialog.setSize(400, 400);
        dialog.setLayout(new GridLayout(8, 2, 10, 10));

        dialog.add(new JLabel("Nome completo:"));
        JTextField txtNome = new JTextField();
        dialog.add(txtNome);

        dialog.add(new JLabel("Login:"));
        JTextField txtLogin = new JTextField();
        dialog.add(txtLogin);

        dialog.add(new JLabel("Senha:"));
        JPasswordField txtSenha = new JPasswordField();
        dialog.add(txtSenha);

        dialog.add(new JLabel("E-mail:"));
        JTextField txtEmail = new JTextField();
        dialog.add(txtEmail);

        dialog.add(new JLabel("Data de nascimento:"));
        JTextField txtDataNascimento = new JTextField("dd/MM/yyyy");
        dialog.add(txtDataNascimento);

        dialog.add(new JLabel("Tipo de usuário (a: Aluno, p: Professor):"));
        JTextField txtTipo = new JTextField();
        dialog.add(txtTipo);

        JButton btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> {
            String nome = txtNome.getText();
            String login = txtLogin.getText();
            String senha = new String(txtSenha.getPassword());
            String email = txtEmail.getText();
            String dataNascimento = txtDataNascimento.getText();
            String tipo = txtTipo.getText().toLowerCase();

            if (validarCadastro(nome, login, senha, email, dataNascimento, tipo)) {
                if (tipo.equals("a")) {
                    String ra = JOptionPane.showInputDialog("Informe seu RA:");
                    Aluno aluno = new Aluno(nome, login, senha, email, dataNascimento, ra);
                    usuarios.add(aluno);
                    JOptionPane.showMessageDialog(dialog, "Aluno cadastrado com sucesso!");
                } else if (tipo.equals("p")) {
                    Professor professor = new Professor(nome, login, senha, email, dataNascimento);
                    usuarios.add(professor);
                    JOptionPane.showMessageDialog(dialog, "Professor cadastrado com sucesso!");
                }
                dialog.dispose();
            }
        });
        dialog.add(btnCadastrar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dialog.dispose());
        dialog.add(btnCancelar);

        dialog.setVisible(true);
    }

    private static boolean validarCadastro(String nome, String login, String senha, String email, String dataNascimento, String tipo) {
        if (nome.length() < 3 || !login.matches("[a-zA-Z0-9]{4,}") || senha.length() < 6 || !email.contains("@") || !dataNascimento.matches("\\d{2}/\\d{2}/\\d{4}")) {
            JOptionPane.showMessageDialog(null, "Informações inválidas. Verifique os dados e tente novamente.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        if (!tipo.equals("a") && !tipo.equals("p")) {
            JOptionPane.showMessageDialog(null, "Tipo de usuário inválido. Escolha 'a' ou 'p'.", "Erro", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private static void mostrarEstatisticas(JFrame parentFrame) {
        StringBuilder estatisticas = new StringBuilder();

        int totalUsuarios = usuarios.size();
        int totalAlunos = (int) usuarios.stream().filter(u -> u instanceof Aluno).count();
        int totalProfessores = (int) usuarios.stream().filter(u -> u instanceof Professor).count();

        estatisticas.append("=== Estatísticas do Sistema ===\n");
        estatisticas.append("👥 Total de usuários: ").append(totalUsuarios).append("\n");
        estatisticas.append("🧑‍🎓 Alunos: ").append(totalAlunos).append("\n");
        estatisticas.append("👨‍🏫 Professores: ").append(totalProfessores).append("\n");

        JOptionPane.showMessageDialog(parentFrame, estatisticas.toString(), "Estatísticas", JOptionPane.INFORMATION_MESSAGE);
    }

    private static Usuario autenticarUsuario(String login, String senha) {
        for (Usuario u : usuarios) {
            if (u.getLogin().equals(login) && u.autenticar(senha)) {
                return u;
            }
        }
        return null;
    }
}
