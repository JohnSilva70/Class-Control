
package sistemaacademico2.pkg0;

import java.util.Scanner;

 public class SistemaAcademico2 {
    private static Aluno[] alunos = new Aluno[10];
    private static int totalAlunos = 0;
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("1 - Fazer login");
            System.out.println("2 - Criar conta");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    Aluno logado = login.fazerLogin(scanner, alunos, totalAlunos);
                    if (logado != null) {
                        menuAluno(logado);
                    }
                    break;
                case 2:
                    if (totalAlunos < alunos.length) {
                        Aluno novo = cadastro.criarConta(scanner);
                        alunos[totalAlunos] = novo;
                        totalAlunos++;
                    } else {
                        System.out.println("Limite de alunos atingido.");
                    }
                    break;
                case 0:
                    System.out.println("Encerrando o sistema...");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }

    private static void menuAluno(Aluno aluno) {
        while (true) {
            System.out.println("1 - Ver perfil");
            System.out.println("2 - Ver avisos");
            System.out.println("3 - Tela inicial");
            System.out.println("0 - Logout");
            System.out.print("Escolha: ");
            int opcao = scanner.nextInt();
            scanner.nextLine();

            switch (opcao) {
                case 1:
                    aluno.exibirPerfil();
                    break;
                case 2:
                    aluno.exibirAvisos();
                    break;
                case 3:
                    aluno.exibirTelaInicial();
                    break;
                case 0:
                    System.out.println("Logout realizado.");
                    return;
                default:
                    System.out.println("Opção inválida.");
            }
        }
    }
}
