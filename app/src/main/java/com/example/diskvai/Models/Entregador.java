package com.example.diskvai.Models;

import android.graphics.drawable.Drawable;

public class Entregador {

    private String Nome_ent;
    private String telefone;
    private String Email;
    private String Foto;
    private int ID;

    public Entregador(String nome,String foto,int ID,String telefone,String Email) {
        this.Nome_ent = nome;
        this.Foto = foto;
        this.telefone = telefone;
        this.Email = Email;
        this.ID = ID;
    }


    public String getFoto() {
        return Foto;
    }

    public void setFoto(String img) {
        this.Foto = img;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome_ent() {
        return Nome_ent;
    }

    public void setNome_ent(String nome_ent) {
        Nome_ent = nome_ent;
    }
}