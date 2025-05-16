
package sistemaacademico2.pkg0;

public class Aluno {
    private String nome;
    private String ra;
    private String contato;
    private String dataNascimento;
    private String usuario;
    private String senha;
    private Materia[] materias;

    public Aluno(String nome, String ra, String contato, String dataNascimento, String usuario, String senha) {
        this.nome = nome;
        this.ra = ra;
        this.contato = contato;
        this.dataNascimento = dataNascimento;
        this.usuario = usuario;
        this.senha = senha;

        materias = new Materia[2];
        materias[0] = new Materia("Modelagem de Software", "Wilson Pereira", 40, 85.0, 92.0);
        materias[1] = new Materia("Programação de Soluções Computacionais", "Hissamu shirado", 60, 90.0, 96.0);
    }

    public boolean autenticar(String usuario, String senha) {
        return this.usuario.equals(usuario) && this.senha.equals(senha);
    }

    public void exibirPerfil() {
        System.out.println("=== Perfil do Aluno ===");
        System.out.println("Nome: " + nome);
        System.out.println("RA: " + ra);
        System.out.println("Contato: " + contato);
        System.out.println("Data de Nascimento: " + dataNascimento);
        System.out.println();
    }

    public void exibirTelaInicial() {
        System.out.println("=== Tela Inicial ===");
        int totalHoras = 0;
        for (Materia m : materias) {
            m.exibirInformacoes();
            totalHoras += m.getCargaHoraria();
        }
        System.out.println("Carga Horária Total: " + totalHoras + " horas");
        System.out.println();
    }

    public void exibirAvisos() {
        System.out.println("=== Avisos ===");
        System.out.println("- Entrega do projeto final até dia 20/06.");
        System.out.println("- Revisão da prova dia 18/06 às 09:00.");
        System.out.println();
    }

    public String getUsuario() {
        return usuario;
    }
}
