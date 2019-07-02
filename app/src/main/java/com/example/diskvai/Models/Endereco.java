package com.example.diskvai.Models;

import android.support.annotation.Nullable;

public class Endereco {

    private String ID, rua, numero, complemento = null, bairro, cidade, estado, cep;

    public Endereco(String ID, String rua, String numero, @Nullable String complemento, String bairro, String cidade, String estado, String cep) {
        this.ID = ID;
        this.rua = rua;
        this.numero = numero;
        if (complemento!= null || !complemento.equals("")) {
            this.complemento = complemento;
        }
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;

    }

    public String getID() {
        return ID;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public String getEstado() {
        return estado;
    }

    public String getCep() {
        return cep;
    }

    public String getEndereco() {
        if(complemento!=null) {
            return rua + ", " + numero + ", " + complemento + ". " + bairro + ", " + cidade + " - " + estado + "(" + cep + ")";
        } else {
            return rua + ", " + numero + ". " + bairro + ", " + cidade + " - " + estado + "(" + cep + ")";
        }
    }
}
