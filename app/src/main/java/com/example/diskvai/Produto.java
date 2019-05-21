package com.example.diskvai;

import android.os.StrictMode;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Produto {

    private String urlInserirProduto = "http://...";
    private String urlAlterarProduto = "http://...";

    private String nome;
    private String descricao;
    private double preco;
    private String id_empresa;

    public Produto(String nome, String descricao, double preco, String id_empresa) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.id_empresa = id_empresa;
    }

    public void publicarProduto() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(urlInserirProduto).newBuilder();
            urlBuilder.addQueryParameter("nome_prod", nome);
            urlBuilder.addQueryParameter("desc_prod", descricao);
            urlBuilder.addQueryParameter("preco_prod", Double.toString(preco));
            urlBuilder.addQueryParameter("id_empresa", id_empresa);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void editarProduto(String nome, String descricao, double preco, String id_empresa) {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse(urlAlterarProduto).newBuilder();
            urlBuilder.addQueryParameter("nome_prod", nome);
            urlBuilder.addQueryParameter("desc_prod", descricao);
            urlBuilder.addQueryParameter("preco_prod", Double.toString(preco));
            urlBuilder.addQueryParameter("id_empresa", id_empresa);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
