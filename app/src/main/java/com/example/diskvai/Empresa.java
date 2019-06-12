package com.example.diskvai;

public class Empresa {

    private String nome;
    private String telefone;
    private double vlrFrete;
    private String id;
    private String url_imagem;

    public Empresa(String id, String nome, String telefone, double vlrFrete, String url_imagem) {
        this.nome = nome;
        this.telefone = telefone;
        this.vlrFrete = vlrFrete;
        this.id = id;
        this.url_imagem = url_imagem;
    }

    public double getVlrFrete() {
        return vlrFrete;
    }

    public String getDescricao() {
        return telefone;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getUrl_imagem() {
        return url_imagem;
    }
}

