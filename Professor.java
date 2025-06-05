import java.util.Scanner;
import java.util.ArrayList;

public class Professor extends Usuario {
    ArrayList<Disciplina> disciplinas;

    public Professor(String nome, String login, String senha, String email, String dataNascimento) {
        super(nome, login, senha, email, dataNascimento);
        this.disciplinas = new ArrayList<>();
    }

    public ArrayList<Disciplina> getDisciplinas() {
        return disciplinas;
    }
    public void adicionarDisciplina(Scanner scanner) {
        System.out.println("\n--- Adicionar Disciplina ---");
        System.out.print("Nome: ");
        String nome = scanner.nextLine().trim();

        System.out.print("Código (ex: INF1010): ");
        String codigo = scanner.nextLine().trim().toUpperCase();

        if (nome.isEmpty() || codigo.isEmpty()) {
            System.out.println("⚠️ Nome e código são obrigatórios.");
            return;
        }

        for (Disciplina d : disciplinas) {
            if (d.getNome().equalsIgnoreCase(nome) || d.getCodigo().equalsIgnoreCase(codigo)) {
                System.out.println("⚠️ Já existe uma disciplina com esse nome ou código.");
                return;
            }
        }

        disciplinas.add(new Disciplina(nome, codigo));
        System.out.println("✅ Disciplina adicionada com sucesso.");
    }

    @Override
    public void exibirMenu() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nBem-vindo(a), professor(a) " + nome);
            System.out.println("1. Adicionar aviso");
            System.out.println("2. Ver perfil");
            System.out.println("3. Editar perfil");
            System.out.println("4. Adicionar disciplina");
            System.out.println("5. Adicionar nota a aluno");
            System.out.println("6. Matricular aluno em disciplina");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    for (int i = 0; i < disciplinas.size(); i++) {
                        System.out.println((i + 1) + ". " + disciplinas.get(i).getNome());
                    }
                    System.out.print("Escolha a disciplina: ");
                    try {
                        int idx = Integer.parseInt(scanner.nextLine()) - 1;
                        Disciplina disc = disciplinas.get(idx);
                        System.out.print("Digite o aviso: ");
                        String mensagem = scanner.nextLine();
                        String data = java.time.LocalDate.now().toString();
                        Aviso aviso = new Aviso(mensagem, nome, data);
                        disc.adicionarAviso(String.valueOf(aviso));
                        System.out.println("✅ Aviso adicionado.");
                    } catch (Exception e) {
                        System.out.println("❌ Opção inválida.");
                    }
                    break;
                case "2":
                    exibirPerfil();
                    break;
                case "3":
                    editarPerfil(scanner);
                    break;
                case "4":
                    adicionarDisciplina(scanner);
                    break;
                case "5":
                    adicionarNota(scanner, Sistema.getUsuarios());
                    break;
                case "6":
                    matricularAlunoEmDisciplina(scanner, Sistema.getUsuarios());
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
    public void adicionarNota(Scanner scanner, ArrayList<Usuario> usuarios) {
        if (disciplinas.isEmpty()) {
            System.out.println("📭 Você ainda não criou nenhuma disciplina.");
            return;
        }

        System.out.println("\n--- Suas Disciplinas ---");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNome() + " (" + disciplinas.get(i).getCodigo() + ")");
        }

        System.out.print("Escolha o número da disciplina: ");
        int escolha = Integer.parseInt(scanner.nextLine()) - 1;

        if (escolha < 0 || escolha >= disciplinas.size()) {
            System.out.println("⚠️ Escolha inválida.");
            return;
        }

        Disciplina disciplinaEscolhida = disciplinas.get(escolha);

        // Filtra os alunos matriculados nesta disciplina
        ArrayList<Aluno> alunosMatriculados = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Aluno aluno && aluno.getDisciplinas().contains(disciplinaEscolhida)) {
                alunosMatriculados.add(aluno);
            }
        }

        if (alunosMatriculados.isEmpty()) {
            System.out.println("📭 Nenhum aluno matriculado nesta disciplina.");
            return;
        }

        System.out.println("\n--- Alunos Matriculados ---");
        for (int i = 0; i < alunosMatriculados.size(); i++) {
            System.out.println((i + 1) + ". " + alunosMatriculados.get(i).getNome());
        }

        System.out.print("Escolha o número do aluno: ");
        int alunoIndex = Integer.parseInt(scanner.nextLine()) - 1;

        if (alunoIndex < 0 || alunoIndex >= alunosMatriculados.size()) {
            System.out.println("⚠️ Escolha inválida.");
            return;
        }

        Aluno alunoSelecionado = alunosMatriculados.get(alunoIndex);

        System.out.print("Digite a nota do aluno (0.0 a 10.0): ");
        double nota = Double.parseDouble(scanner.nextLine());

        if (nota < 0 || nota > 10) {
            System.out.println("⚠️ Nota inválida.");
            return;
        }

        disciplinaEscolhida.adicionarNota(alunoSelecionado, nota);
        System.out.println("✅ Nota registrada com sucesso.");
    }
    public void matricularAlunoEmDisciplina(Scanner scanner, ArrayList<Usuario> usuarios) {
        if (disciplinas.isEmpty()) {
            System.out.println("❌ Você ainda não criou nenhuma disciplina.");
            return;
        }

        System.out.println("\n--- Suas Disciplinas ---");
        for (int i = 0; i < disciplinas.size(); i++) {
            System.out.println((i + 1) + ". " + disciplinas.get(i).getNome());
        }

        System.out.print("Escolha a disciplina (número): ");
        int opcaoDisciplina;
        try {
            opcaoDisciplina = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("❌ Opção inválida.");
            return;
        }

        if (opcaoDisciplina < 0 || opcaoDisciplina >= disciplinas.size()) {
            System.out.println("❌ Disciplina inválida.");
            return;
        }

        Disciplina disciplinaEscolhida = disciplinas.get(opcaoDisciplina);

        // Listar alunos
        ArrayList<Aluno> alunosDisponiveis = new ArrayList<>();
        for (Usuario u : usuarios) {
            if (u instanceof Aluno aluno) {
                alunosDisponiveis.add(aluno);
            }
        }

        if (alunosDisponiveis.isEmpty()) {
            System.out.println("⚠️ Nenhum aluno cadastrado.");
            return;
        }

        System.out.println("\n--- Alunos Cadastrados ---");
        for (int i = 0; i < alunosDisponiveis.size(); i++) {
            System.out.println((i + 1) + ". " + alunosDisponiveis.get(i).getNome());
        }

        System.out.print("Escolha o aluno (número): ");
        int opcaoAluno;
        try {
            opcaoAluno = Integer.parseInt(scanner.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("❌ Opção inválida.");
            return;
        }

        if (opcaoAluno < 0 || opcaoAluno >= alunosDisponiveis.size()) {
            System.out.println("❌ Aluno inválido.");
            return;
        }

        Aluno alunoSelecionado = alunosDisponiveis.get(opcaoAluno);

        alunoSelecionado.adicionarDisciplina(disciplinaEscolhida);

        System.out.println("✅ Aluno " + alunoSelecionado.getNome() +
                " matriculado na disciplina " + disciplinaEscolhida.getNome() + ".");
    }



    @Override
    public void exibirPerfil() {
        System.out.println("\n=== Perfil do Professor ===");
        System.out.println("Nome: " + nome);
        System.out.println("Login: " + login);
        System.out.println("Email: " + email);
        System.out.println("Data de Nascimento: " + dataNascimento);
        System.out.println("Disciplinas:");
        for (Disciplina d : disciplinas) {
            System.out.println("- " + d.getNome());
        }

    }
}

