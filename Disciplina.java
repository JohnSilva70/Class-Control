package sistema;

import java.util.ArrayList;
import java.util.HashMap;

public class Disciplina {
    private String nome;
    private String codigo;
    private ArrayList<String> avisos;
    private HashMap<Aluno, Double> notas;

    public Disciplina(String nome, String codigo) {
        this.nome = nome;
        this.codigo = codigo;
        this.avisos = new ArrayList<>();
        this.notas = new HashMap<>();
    }

    // ====== Getters ======
    public String getNome() {
        return nome;
    }


    public String getCodigo() {
        return codigo;
    }

    public ArrayList<String> getAvisos() {
        return avisos;
    }

    public HashMap<Aluno, Double> getNotas() {
        return notas;
    }
    public void adicionarAviso(String aviso) {
        if (aviso != null && !aviso.trim().isEmpty()) {
            avisos.add(aviso);
        }
    }

    public void listarAvisos() {
        if (avisos.isEmpty()) {
            System.out.println("📭 Nenhum aviso nesta disciplina.");
        } else {
            System.out.println("📌 Avisos da disciplina " + nome + ":");
            for (String aviso : avisos) {
                System.out.println("- " + aviso);
            }
        }
    }

    public void adicionarNota(Aluno aluno, double nota) {
        if (aluno != null && nota >= 0 && nota <= 10) {
            notas.put(aluno, nota);
        }
    }

    public Double getNota(Aluno aluno) {
        return notas.getOrDefault(aluno, null);
    }

    public void listarNotas() {
        if (notas.isEmpty()) {
            System.out.println("📭 Nenhuma nota registrada ainda.");
        } else {
            System.out.println("📊 Notas da disciplina " + nome + ":");
            for (Aluno aluno : notas.keySet()) {
                System.out.println("- " + aluno.getNome() + ": " + notas.get(aluno));
            }
        }
    }
}
