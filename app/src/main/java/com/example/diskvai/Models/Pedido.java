package com.example.diskvai.Models;

import java.util.ArrayList;
import java.util.List;

public class Pedido {

    private String id, cliente, endereco, entregador = null, status, formaPagamento;

    private Double valor = 0.0;
    private List<Produto> produtoLista;

    public Pedido(String id, String cliente) {
        this.id = id;
        this.cliente = cliente;
        produtoLista = new ArrayList<>();
    }

    public void associarEntregador(String entregador) {
        this.entregador = entregador;
    }

    public void adicionarProduto(Produto produto) {
        produtoLista.add(produto);
        calculaValor();
    }

    public void removerProduto(Produto produto) {
        produtoLista.remove(produto);
        calculaValor();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCliente() {
        return cliente;
    }

    public void setCliente(String cliente) {
        this.cliente = cliente;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setFormaPagamento(String formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public String getFormaPagamento() {
        return formaPagamento;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEntregador() {
        return entregador;
    }

    public void setEntregador(String entregador) {
        this.entregador = entregador;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getValor() {
        return valor;
    }
    public void calculaValor() {
        valor = 0.0;
        for (int i = 0; i < produtoLista.size(); i++) {
            valor += produtoLista.get(i).getPreco();
        }
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }
}
