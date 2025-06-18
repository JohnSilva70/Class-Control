package sistema;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class TelaPerfil extends JFrame {

    private String nome = "João Silva";
    private String email = "joao@email.com";
    private String dataNascimento = "01/01/1990";
    private String senha = "123456";

    private JLabel nomeLabel;
    private JLabel emailLabel;
    private JLabel dataLabel;
    private JLabel senhaLabel;

    public TelaPerfil() {
        setTitle("Perfil do Usuário");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel painelInfo = new JPanel();
        painelInfo.setLayout(new GridLayout(5, 1, 5, 5));
        painelInfo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        nomeLabel = new JLabel("Nome: " + nome);
        emailLabel = new JLabel("E-mail: " + email);
        dataLabel = new JLabel("Data de Nascimento: " + dataNascimento);
        senhaLabel = new JLabel("Senha: " + "*".repeat(senha.length()));

        painelInfo.add(nomeLabel);
        painelInfo.add(emailLabel);
        painelInfo.add(dataLabel);
        painelInfo.add(senhaLabel);

        JButton btnEditar = new JButton("Editar Perfil");
        btnEditar.addActionListener(e -> abrirDialogoEdicao());
        painelInfo.add(btnEditar);

        add(painelInfo, BorderLayout.CENTER);

        setVisible(true);
    }

    private void abrirDialogoEdicao() {
        JDialog dialog = new JDialog(this, "Editar Perfil", true);
        dialog.setSize(350, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new GridLayout(6, 2, 5, 5));
        dialog.setResizable(false);

        JTextField nomeField = new JTextField(nome);
        JTextField emailField = new JTextField(email);
        JTextField dataField = new JTextField(dataNascimento);
        JPasswordField senhaField = new JPasswordField(senha);

        dialog.add(new JLabel("Nome:"));
        dialog.add(nomeField);

        dialog.add(new JLabel("E-mail:"));
        dialog.add(emailField);

        dialog.add(new JLabel("Data de Nascimento (dd/mm/aaaa):"));
        dialog.add(dataField);

        dialog.add(new JLabel("Senha:"));
        dialog.add(senhaField);

        JButton btnSalvar = new JButton("Salvar");
        JButton btnCancelar = new JButton("Cancelar");

        btnSalvar.addActionListener(ev -> {
            String novoNome = nomeField.getText().trim();
            String novoEmail = emailField.getText().trim();
            String novaData = dataField.getText().trim();
            String novaSenha = new String(senhaField.getPassword()).trim();

            if (!novoNome.isEmpty()) {
                nome = novoNome;
            }

            if (!novoEmail.isEmpty() && novoEmail.contains("@") && novoEmail.contains(".")) {
                email = novoEmail;
            } else if (!novoEmail.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "⚠️ E-mail inválido. Mantido o atual.");
                return;
            }

            if (!novaData.isEmpty() && novaData.matches("\\d{2}/\\d{2}/\\d{4}")) {
                dataNascimento = novaData;
            } else if (!novaData.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "⚠️ Data inválida. Mantida a atual.");
                return;
            }

            if (!novaSenha.isEmpty()) {
                senha = novaSenha;
            }

            atualizarLabels();
            JOptionPane.showMessageDialog(dialog, "✅ Perfil atualizado com sucesso.");
            dialog.dispose();
        });

        btnCancelar.addActionListener(ev -> dialog.dispose());

        dialog.add(btnSalvar);
        dialog.add(btnCancelar);

        dialog.setVisible(true);
    }

    private void atualizarLabels() {
        nomeLabel.setText("Nome: " + nome);
        emailLabel.setText("E-mail: " + email);
        dataLabel.setText("Data de Nascimento: " + dataNascimento);
        senhaLabel.setText("Senha: " + "*".repeat(senha.length()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaPerfil());
    }
}
