package com.example.diskvai;


public class Produto {

    private String nome;
    private String descricao;
    private double preco;
    private String id;
    private String url_imagem;

    public Produto(String id, String nome, String descricao, double preco, String url_imagem) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.id = id;
        this.url_imagem = url_imagem;
    }

    public double getPreco() {
        return preco;
    }

    public String getDescricao() {
        return descricao;
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
