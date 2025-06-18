package sistema;

import sistema.UsuarioDAO;
import sistema.Usuario;
import sistema.Aluno;
import sistema.Professor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaCadastro extends JFrame {

    public TelaCadastro() {
        setTitle("Cadastro - Class Control");
        setSize(600, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Layout principal da JFrame
        setLayout(new BorderLayout());

        // Painel principal com GridBagLayout para organizar os campos
        JPanel painel = new JPanel(new GridBagLayout());
        painel.setBackground(new Color(100, 150, 180));
        add(painel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2; // ocupa duas colunas para título

        JLabel titulo = new JLabel("CADASTRA-SE AQUI", SwingConstants.CENTER);
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painel.add(titulo, gbc);

        gbc.gridwidth = 1; // volta para uma coluna para os campos
        gbc.gridy++;

        JTextField nomeField = criarCampoComPlaceholder("Nome completo");
        gbc.gridx = 0;
        painel.add(nomeField, gbc);

        gbc.gridy++;
        JTextField usuarioField = criarCampoComPlaceholder("Nome de usuário");
        painel.add(usuarioField, gbc);

        gbc.gridy++;
        JTextField emailField = criarCampoComPlaceholder("E-mail");
        painel.add(emailField, gbc);

        gbc.gridy++;
        JPasswordField senhaField = criarSenhaComPlaceholder("Criar senha");
        painel.add(senhaField, gbc);

        gbc.gridy++;
        JPasswordField confirmarSenhaField = criarSenhaComPlaceholder("Confirmar a senha");
        painel.add(confirmarSenhaField, gbc);

        gbc.gridy++;
        JLabel tipoLabel = new JLabel("Tipo de usuário:");
        tipoLabel.setForeground(Color.WHITE);
        painel.add(tipoLabel, gbc);

        gbc.gridy++;
        String[] tiposUsuario = {"Aluno", "Professor"};
        JComboBox<String> tipoUsuarioCombo = new JComboBox<>(tiposUsuario);
        painel.add(tipoUsuarioCombo, gbc);

        gbc.gridy++;
        JTextField raField = criarCampoComPlaceholder("RA");
        painel.add(raField, gbc);
        raField.setVisible(true);

        tipoUsuarioCombo.addActionListener(e -> {
            raField.setVisible("Aluno".equals(tipoUsuarioCombo.getSelectedItem()));
            painel.revalidate();
            painel.repaint();
        });

        gbc.gridy++;
        JLabel dataLabel = new JLabel("Data de nascimento");
        dataLabel.setForeground(Color.WHITE);
        painel.add(dataLabel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;

        // Data - dia, mês, ano
        String[] dias = new String[31];
        for (int i = 1; i <= 31; i++) dias[i - 1] = String.valueOf(i);

        String[] meses = {
                "janeiro", "fevereiro", "março", "abril", "maio", "junho",
                "julho", "agosto", "setembro", "outubro", "novembro", "dezembro"
        };

        String[] anos = new String[100];
        for (int i = 0; i < 100; i++) anos[i] = String.valueOf(2025 - i);

        JPanel dataPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        dataPanel.setBackground(new Color(100, 150, 180));

        JComboBox<String> diaBox = new JComboBox<>(dias);
        JComboBox<String> mesBox = new JComboBox<>(meses);
        JComboBox<String> anoBox = new JComboBox<>(anos);

        dataPanel.add(diaBox);
        dataPanel.add(mesBox);
        dataPanel.add(anoBox);

        painel.add(dataPanel, gbc);

        gbc.gridy++;
        gbc.gridx = 0;

        JLabel termos = new JLabel("<html><body style='width:400px'>"
                + "Ao clicar em Cadastrar-se, você concorda com nossos Termos, "
                + "Política de Privacidade e Política de Cookies.</body></html>");
        termos.setForeground(Color.WHITE);
        termos.setFont(new Font("Arial", Font.PLAIN, 11));
        painel.add(termos, gbc);

        gbc.gridy++;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        // Botão CADASTRAR customizado
        JButton cadastrarBtn = new JButton("CADASTRAR") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(102, 178, 255), getWidth(), getHeight(), new Color(0, 102, 204));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            public boolean isContentAreaFilled() { return false; }
            public boolean isBorderPainted() { return false; }
        };
        cadastrarBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cadastrarBtn.setPreferredSize(new Dimension(200, 40));
        painel.add(cadastrarBtn, gbc);

        gbc.gridy++;
        JButton voltarBtn = new JButton("VOLTAR") {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, new Color(255, 153, 153), getWidth(), getHeight(), new Color(204, 0, 0));
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(Color.WHITE);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.drawString(getText(), x, y);
                g2.dispose();
            }

            public boolean isContentAreaFilled() { return false; }
            public boolean isBorderPainted() { return false; }
        };
        voltarBtn.setFont(new Font("Arial", Font.BOLD, 14));
        voltarBtn.setPreferredSize(new Dimension(200, 40));
        painel.add(voltarBtn, gbc);

        voltarBtn.addActionListener(e -> {
            new TelaLogin();
            dispose();
        });

        cadastrarBtn.addActionListener(e -> {
            String nome = nomeField.getText().trim();
            String login = usuarioField.getText().trim();
            String email = emailField.getText().trim();
            String senha = String.valueOf(senhaField.getPassword());
            String confirmarSenha = String.valueOf(confirmarSenhaField.getPassword());
            String tipoUsuario = ((String) tipoUsuarioCombo.getSelectedItem()).toLowerCase();
            String ra = raField.getText().trim();

            String dia = (String) diaBox.getSelectedItem();
            String mes = String.valueOf(mesBox.getSelectedIndex() + 1);
            if (mes.length() == 1) mes = "0" + mes;
            String ano = (String) anoBox.getSelectedItem();
            String dataNascimento = dia + "/" + mes + "/" + ano;

            if (!senha.equals(confirmarSenha)) {
                JOptionPane.showMessageDialog(null, "As senhas não coincidem.", "Erro", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                Usuario usuario;

                java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
                java.time.LocalDate nascimento = java.time.LocalDate.parse(dataNascimento, formatter);

                if ("aluno".equals(tipoUsuario)) {
                    usuario = new Aluno(nome, login, senha, email, nascimento.toString(), ra);
                } else {
                    usuario = new Professor(nome, login, senha, email, nascimento.toString());
                }

                UsuarioDAO dao = new UsuarioDAO();
                dao.adicionarUsuario(usuario);

                JOptionPane.showMessageDialog(null, "Usuário cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new TelaLogin();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Erro ao cadastrar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }

    private JTextField criarCampoComPlaceholder(String placeholder) {
        JTextField field = new JTextField(placeholder);
        field.setForeground(Color.GRAY);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (field.getText().equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                }
            }

            public void focusLost(FocusEvent e) {
                if (field.getText().isEmpty()) {
                    field.setForeground(Color.GRAY);
                    field.setText(placeholder);
                }
            }
        });

        return field;
    }

    private JPasswordField criarSenhaComPlaceholder(String placeholder) {
        JPasswordField field = new JPasswordField(placeholder);
        field.setForeground(Color.GRAY);
        field.setEchoChar((char) 0);

        field.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (String.valueOf(field.getPassword()).equals(placeholder)) {
                    field.setText("");
                    field.setForeground(Color.BLACK);
                    field.setEchoChar('●');
                }
            }

            public void focusLost(FocusEvent e) {
                if (String.valueOf(field.getPassword()).isEmpty()) {
                    field.setText(placeholder);
                    field.setForeground(Color.GRAY);
                    field.setEchoChar((char) 0);
                }
            }
        });

        return field;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaCadastro());
    }
}
