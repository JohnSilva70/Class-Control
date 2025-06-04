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

    public boolean autenticar(String senha) {
        return this.senha.equals(senha);
    }

    public void alterarSenha(String novaSenha) {
        this.senha = novaSenha;
    }

    public void editarPerfil(Scanner scanner) {
        System.out.print("Novo nome (" + nome + "): ");
        String novoNome = scanner.nextLine().trim();
        if (!novoNome.isEmpty()) this.nome = novoNome;

        System.out.print("Novo e-mail (" + email + "): ");
        String novoEmail = scanner.nextLine().trim();
        if (!novoEmail.isEmpty() && novoEmail.contains("@") && novoEmail.contains(".")) {
            this.email = novoEmail;
        } else if (!novoEmail.isEmpty()) {
            System.out.println("⚠️ E-mail inválido. Mantido o atual.");
        }

        System.out.print("Nova data de nascimento (" + dataNascimento + "): ");
        String novaData = scanner.nextLine().trim();
        if (!novaData.isEmpty() && novaData.matches("\\d{2}/\\d{2}/\\d{4}")) {
            this.dataNascimento = novaData;
        } else if (!novaData.isEmpty()) {
            System.out.println("⚠️ Data inválida. Mantida a atual.");
        }

        System.out.print("Deseja alterar a senha? (s/n): ");
        if (scanner.nextLine().equalsIgnoreCase("s")) {
            System.out.print("Nova senha: ");
            this.senha = scanner.nextLine().trim();
        }

        System.out.println("✅ Perfil atualizado com sucesso.");
    }

    public abstract void exibirMenu();
    public abstract void exibirPerfil();
}
