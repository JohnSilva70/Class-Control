
package sistemaacademico2.pkg0;

public class Materia {
    private String nome;
    private String professor;
    private int cargaHoraria;
    private double notaTotal;
    private double porcentagemPresenca;

    public Materia(String nome, String professor, int cargaHoraria, double notaTotal, double porcentagemPresenca) {
        this.nome = nome;
        this.professor = professor;
        this.cargaHoraria = cargaHoraria;
        this.notaTotal = notaTotal;
        this.porcentagemPresenca = porcentagemPresenca;
    }

    public void exibirInformacoes() {
        System.out.println("Matéria: " + nome);
        System.out.println("Professor: " + professor);
        System.out.println("Carga Horária: " + cargaHoraria + " horas");
        System.out.println("Nota Total: " + notaTotal);
        System.out.println("Presença: " + porcentagemPresenca + "%");
        System.out.println();
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }
}
