
package sistemaacademico2.pkg0;

import java.util.Scanner;

public class cadastro {
    public static Aluno criarConta(Scanner scanner) {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("RA: ");
        String ra = scanner.nextLine();
        System.out.print("Email ou Celular: ");
        String contato = scanner.nextLine();
        System.out.print("Data de Nascimento: ");
        String nascimento = scanner.nextLine();
        System.out.print("Nome de usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.println("Conta criada com sucesso!");

        return new Aluno(nome, ra, contato, nascimento, usuario, senha);
    }
}
