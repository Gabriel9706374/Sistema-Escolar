package model;

public class Turma {
    private int id;
    private String nome;
    private int ano;
    public Turma() {}
    public Turma(int id, String nome, int ano) { this.id=id; this.nome=nome; this.ano=ano; }
    public int getId() { return id; }
    public void setId(int id) { this.id=id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome=nome; }
    public int getAno() { return ano; }
    public void setAno(int ano) { this.ano=ano; }
}
