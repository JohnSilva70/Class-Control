package sistema;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public ArrayList<String> getAvisos() {
        return new ArrayList<>(avisos);
    }

    public void adicionarAviso(String aviso) {
        if (aviso != null && !aviso.trim().isEmpty()) {
            avisos.add(aviso);
        } else {
            System.out.println("⚠️ Aviso inválido. Mensagem vazia ou nula.");
        }
    }

    public void carregarAvisosDoBanco() {
        try {
            List<Aviso> avisosDoBanco = UsuarioDAO.getAvisosPorDisciplina(this.nome);
            avisos.clear();
            for (Aviso aviso : avisosDoBanco) {
                String avisoFormatado = String.format("%s: %s (%s)", aviso.getTitulo(), aviso.getMensagem(), aviso.getAutor());
                avisos.add(avisoFormatado);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao carregar avisos do banco para disciplina " + nome + ": " + e.getMessage());
        }
    }

    public void adicionarNota(Aluno aluno, double nota) {
        if (aluno == null) {
            System.out.println("⚠️ Aluno inválido.");
            return;
        }
        if (nota < 0 || nota > 10) {
            System.out.println("⚠️ Nota fora do intervalo permitido (0.0 - 10.0).");
            return;
        }
        notas.put(aluno, nota);
    }

    public Double getNota(Aluno aluno) {
        return notas.get(aluno);
    }

    public Double getNotaPorAluno(String nomeAluno) {
        for (Map.Entry<Aluno, Double> entry : notas.entrySet()) {
            if (entry.getKey().getNome().equalsIgnoreCase(nomeAluno)) {
                return entry.getValue();
            }
        }
        return null;
    }

    public void listarAvisos() {
        System.out.println("\n📌 Avisos da disciplina " + nome + ":");
        if (avisos.isEmpty()) {
            System.out.println("📭 Nenhum aviso disponível.");
        } else {
            for (String aviso : avisos) {
                System.out.println("- " + aviso);
            }
        }
    }

    public void listarNotas() {
        System.out.println("\n📊 Notas da disciplina " + nome + ":");
        if (notas.isEmpty()) {
            System.out.println("📭 Nenhuma nota registrada.");
        } else {
            for (Map.Entry<Aluno, Double> entry : notas.entrySet()) {
                System.out.println("- " + entry.getKey().getNome() + ": " + entry.getValue());
            }
        }
    }

}
