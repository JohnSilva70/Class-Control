package sistema;

import java.util.Scanner;

public abstract class Usuario {
    protected String nome;
    protected String login;
    protected String senha;
    protected String email;
    protected String dataNascimento;

    public Usuario(String nome, String login, String senha, String email, String dataNascimento) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.dataNascimento = dataNascimento;
    }

    // Getters
    public String getNome() {
        return nome;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    // Setters
    public void setNome(String nome) {
        if (nome != null && !nome.trim().isEmpty()) {
            this.nome = nome.trim();
        }
    }

    public void setEmail(String email) {
        if (email != null && email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            this.email = email.trim();
        }
    }

    public void setDataNascimento(String dataNascimento) {
        if (dataNascimento != null && dataNascimento.matches("\\d{2}/\\d{2}/\\d{4}")) {
            this.dataNascimento = dataNascimento.trim();
        }

    }

    public void setSenha(String senha) {
        if (senha != null && !senha.trim().isEmpty()) {
            this.senha = senha.trim();
        }
    }

    // Autenticação simples
    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }

    // Edição de perfil via terminal
    public void editarPerfil(Scanner scanner) {
        System.out.println("\n--- Editar Perfil ---");

        System.out.print("Novo nome (pressione Enter para manter): ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) {
            this.nome = novoNome;
        }

        System.out.print("Novo email (pressione Enter para manter): ");
        String novoEmail = scanner.nextLine().trim();
        if (!novoEmail.isEmpty()) {
            this.email = novoEmail;
        }

        System.out.print("Nova data de nascimento (pressione Enter para manter): ");
        String novaData = scanner.nextLine().trim();
        if (!novaData.isEmpty()) {
            this.dataNascimento = novaData;
        }

        System.out.println("✅ Perfil atualizado com sucesso.");
    }

    // Métodos abstratos para especialização
    public abstract void exibirMenu();
    public abstract void exibirPerfil();
}
