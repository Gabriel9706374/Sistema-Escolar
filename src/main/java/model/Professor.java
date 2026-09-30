package model;

public class Professor {
    private int id;
    private String nome;
    private int idade;
    private String email;
    private String senha;

    public Professor() {}
    public Professor(int id, String nome, int idade, String email, String senha) {
        this.id=id; this.nome=nome; this.idade=idade; this.email=email; this.senha=senha;
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
}
