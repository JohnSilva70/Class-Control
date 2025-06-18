package sistema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;

public class Aluno extends Usuario {
    private String ra;
    private ArrayList<Disciplina> disciplinas;

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
        System.out.println("\n--- Minhas Notas (Banco) ---");

        try {
            Map<String, Double> notas = UsuarioDAO.getNotasPorAluno(this.getLogin());
            if (notas.isEmpty()) {
                System.out.println("📭 Nenhuma nota lançada ainda.");
            } else {
                for (Map.Entry<String, Double> entry : notas.entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue());
                }
            }
        } catch (SQLException e) {
            System.out.println("Erro ao buscar notas.");
            e.printStackTrace();
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
                    exibirDisciplinasEAvisos();
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
                    System.out.println("❌ Opção inválida.");
            }
        }
    }

    private void exibirDisciplinasEAvisos() {
        if (disciplinas.isEmpty()) {
            System.out.println("📭 Nenhuma disciplina cadastrada.");
            return;
        }

        for (Disciplina d : disciplinas) {
            System.out.println("\n📘 Disciplina: " + d.getNome());
            Double nota = d.getNota(this);
            System.out.println("Nota: " + (nota != null ? nota : "Sem nota"));
            System.out.println("Avisos:");
            if (d.getAvisos().isEmpty()) {
                System.out.println("- Nenhum aviso.");
            } else {
                for (String aviso : d.getAvisos()) {
                    System.out.println("- " + aviso);
                }
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
        if (disciplinas.isEmpty()) {
            System.out.println("- Nenhuma disciplina cadastrada.");
        } else {
            for (Disciplina d : disciplinas) {
                System.out.println("- " + d.getNome());
            }
        }
    }
}
