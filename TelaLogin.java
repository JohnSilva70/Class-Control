package sistema;

import javax.swing.*;
import java.awt.*;

public class TelaLogin extends JFrame {

    public TelaLogin() {
        setTitle("Class Control");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 350);
        setLocationRelativeTo(null);
        setLayout(null);

        getContentPane().setBackground(new Color(250, 250, 250));

        // Logo
        ImageIcon originalIcon = new ImageIcon("src/resources/logo 2.png");
        Image imagemRedimensionada = originalIcon.getImage().getScaledInstance(200, 120, Image.SCALE_SMOOTH);
        ImageIcon iconRedimensionado = new ImageIcon(imagemRedimensionada);
        JLabel logoLabel = new JLabel(iconRedimensionado);
        logoLabel.setBounds(50, 80, 200, 120);
        logoLabel.setOpaque(false);
        add(logoLabel);

        // Campos
        JLabel usuarioLabel = new JLabel("Usuário:");
        usuarioLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        usuarioLabel.setBounds(320, 35, 100, 20);
        add(usuarioLabel);

        JTextField usuarioField = new JTextField();
        usuarioField.setBounds(320, 60, 220, 35);
        usuarioField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(usuarioField);

        JLabel senhaLabel = new JLabel("Senha:");
        senhaLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        senhaLabel.setBounds(320, 90, 100, 20);
        add(senhaLabel);

        JPasswordField senhaField = new JPasswordField();
        senhaField.setBounds(320, 115, 220, 35);
        senhaField.setFont(new Font("Arial", Font.PLAIN, 14));
        add(senhaField);

        // Botão Entrar
        JButton entrarButton = new JButton("Entrar") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                Color color1 = new Color(102, 204, 255);
                Color color2 = new Color(0, 102, 204);
                GradientPaint gp = new GradientPaint(0, 0, color1, getWidth(), getHeight(), color2);
                g2d.setPaint(gp);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2d.setColor(Color.WHITE);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2d.drawString(getText(), x, y);
                g2d.dispose();
            }

            public boolean isContentAreaFilled() { return false; }
            public boolean isBorderPainted() { return false; }
        };
        entrarButton.setBounds(320, 160, 220, 35);
        entrarButton.setFont(new Font("Arial", Font.BOLD, 14));
        add(entrarButton);

        // Texto e botão cadastro
        JLabel textoCadastro = new JLabel("Não possui uma conta? Inscreva-se agora.");
        textoCadastro.setFont(new Font("Arial", Font.PLAIN, 12));
        textoCadastro.setBounds(320, 205, 250, 20);
        add(textoCadastro);

        JButton cadastrarButton = new JButton("Cadastre-se");
        cadastrarButton.setBounds(360, 240, 140, 30);
        cadastrarButton.setBackground(new Color(180, 220, 240));
        cadastrarButton.setFont(new Font("Arial", Font.PLAIN, 13));
        add(cadastrarButton);

        // Ações
        entrarButton.addActionListener(e -> {
            String login = usuarioField.getText();
            String senha = new String(senhaField.getPassword());

            try {
                UsuarioDAO usuarioDAO = new UsuarioDAO();
                Usuario usuarioAutenticado = usuarioDAO.fazerLogin(login, senha);

                if (usuarioAutenticado != null && usuarioAutenticado.getSenha().equals(senha)) {
                    JOptionPane.showMessageDialog(null, "Bem-vindo, " + usuarioAutenticado.getLogin() + "!");
                    dispose(); // Fecha a tela de login
                    SwingUtilities.invokeLater(() -> new MenuPrincipal(usuarioAutenticado));
                } else {
                    JOptionPane.showMessageDialog(null, "Usuário ou senha inválidos.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erro ao acessar o banco de dados.");
            }
        });

        cadastrarButton.addActionListener(e -> {
            new TelaCadastro();
            dispose(); // Fecha a tela de login
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaLogin::new);
    }
}
