package model;

public class Aluno {
    private int id;
    private String nome;
    private int idade;
    private String email;
    private String senha;
    private String status;

    public Aluno() {}
    public Aluno(int id, String nome, int idade, String email, String senha, String status) {
        this.id=id; this.nome=nome; this.idade=idade; this.email=email; this.senha=senha; this.status=status;
    }
    public int getId() { return id; }
    public void setId(int id) { this.id=id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome=nome; }
    public int getIdade() { return idade; }
    public void setIdade(int idade) { this.idade=idade; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email=email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha=senha; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status=status; }
}
