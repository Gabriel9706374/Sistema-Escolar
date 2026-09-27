package model;

public class Nota {
    private int id;
    private int alunoId;
    private int turmaId;
    private int disciplinaId;
    private double nota1;
    private double nota2;
    private double notaFinal;
    private String status;

    public Nota() {}
    public Nota(int id, int alunoId, int turmaId, int disciplinaId, double nota1, double nota2, double notaFinal, String status) {
        this.id=id; this.alunoId=alunoId; this.turmaId=turmaId; this.disciplinaId=disciplinaId;
        this.nota1=nota1; this.nota2=nota2; this.notaFinal=notaFinal; this.status=status;
    }
    public int getId(){return id;} public void setId(int v){id=v;}
    public int getAlunoId(){return alunoId;} public void setAlunoId(int v){alunoId=v;}
    public int getTurmaId(){return turmaId;} public void setTurmaId(int v){turmaId=v;}
    public int getDisciplinaId(){return disciplinaId;} public void setDisciplinaId(int v){disciplinaId=v;}
    public double getNota1(){return nota1;} public void setNota1(double v){nota1=v;}
    public double getNota2(){return nota2;} public void setNota2(double v){nota2=v;}
    public double getNotaFinal(){return notaFinal;} public void setNotaFinal(double v){notaFinal=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
}
