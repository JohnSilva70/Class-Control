import java.util.ArrayList;
import java.util.Scanner;

public class Aluno extends Usuario {
    private String ra;
    ArrayList<Disciplina> disciplinas;

    public Aluno(String nome, String login, String senha, String email, String dataNascimento, String ra) {
        super(nome, login, senha, email, dataNascimento);
        this.ra = ra;
        this.disciplinas = new ArrayList<>();
    }
    public String getRa() {
        return ra;
    }
    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }

    public void adicionarDisciplina(Disciplina d) {
        if (d != null && !disciplinas.contains(d)) {
            disciplinas.add(d);
        }
    }
    public void listarDisciplinas() {
        if (disciplinas.isEmpty()) {
            System.out.println("📭 Nenhuma disciplina matriculada.");
        } else {
            System.out.println("\n--- Minhas Disciplinas ---");
            for (Disciplina d : disciplinas) {
                System.out.println("- " + d.getCodigo() + " | " + d.getNome());
            }
        }
    }
    public void verNotas() {
        System.out.println("\n--- Minhas Notas ---");
        boolean temNotas = false;
        for (Disciplina d : disciplinas) {
            Double nota = d.getNota(this); // ✅ CORRETO
            if (nota != null) {
                System.out.println("- " + d.getNome() + ": " + nota);
                temNotas = true;
            }
        }
        if (!temNotas) {
            System.out.println("📭 Nenhuma nota lançada ainda.");
        }
    }


    @Override
    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nBem-vindo(a), aluno(a) " + nome);
            System.out.println("1. Ver disciplinas e avisos");
            System.out.println("2. Ver perfil");
            System.out.println("3. Editar perfil");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    for (Disciplina d : disciplinas) {
                        System.out.println("\nDisciplina: " + d.getNome());
                        System.out.println("Nota: " + d.getNota(this));
                        System.out.println("Avisos:");
                        if (d.getAvisos().isEmpty()) {
                            System.out.println("- Nenhum aviso.");
                        } else {
                            for (String aviso : d.getAvisos()) {
                                System.out.println("- " + aviso);
                            }
                        }
                    }
                    break;
                case "2":
                    exibirPerfil();
                    break;
                case "3":
                    editarPerfil(scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    @Override
    public void exibirPerfil() {
        System.out.println("\n=== Perfil do Aluno ===");
        System.out.println("Nome: " + nome);
        System.out.println("Login: " + login);
        System.out.println("RA: " + ra);
        System.out.println("Email: " + email);
        System.out.println("Data de Nascimento: " + dataNascimento);
        System.out.println("Disciplinas:");
        for (Disciplina d : disciplinas) {
            System.out.println("- " + d.getNome());
        }
    }
}
