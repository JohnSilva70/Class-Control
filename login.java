
package sistemaacademico2.pkg0;

import java.util.Scanner;

public class login {
    public static Aluno fazerLogin(Scanner scanner, Aluno[] alunos, int totalAlunos) {
        System.out.print("Usuário: ");
        String usuario = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        for (int i = 0; i < totalAlunos; i++) {
            if (alunos[i].autenticar(usuario, senha)) {
                System.out.println("Login realizado com sucesso!");
                return alunos[i];
            }
        }

        System.out.println("Usuário ou senha incorretos.");
        return null;
    }
}
