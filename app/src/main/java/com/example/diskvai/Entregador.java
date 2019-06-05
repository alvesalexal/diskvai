package com.example.diskvai;

import android.graphics.drawable.Drawable;

public class Entregador {

    private String Nome_ent;
    private String Login;
    private String ID_Facebook;
    private String ID_Google;
    private String telefone;
    private String Email;
    private String senha;
    private int Foto;
    private int ID;

    public Entregador(String nome,int img,int ID,String Login,String ID_facebook,String ID_Google, String telefone,String Email,String senha) {
        this.Nome_ent = nome;
        this.Foto = img;
        this.Login = Login;
        this.ID_Facebook = ID_facebook;
        this.ID_Google = ID_Google;
        this.telefone = telefone;
        this.Email = Email;
        this.senha = senha;
        this.ID = ID;
    }


    public int getFoto() {
        return Foto;
    }

    public void setFoto(int img) {
        this.Foto = img;
    }


    public String getLogin() {
        return Login;
    }

    public void setLogin(String login) {
        Login = login;
    }

    public String getID_Facebook() {
        return ID_Facebook;
    }

    public void setID_Facebook(String ID_Facebook) {
        this.ID_Facebook = ID_Facebook;
    }

    public String getID_Google() {
        return ID_Google;
    }

    public void setID_Google(String ID_Google) {
        this.ID_Google = ID_Google;
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

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
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