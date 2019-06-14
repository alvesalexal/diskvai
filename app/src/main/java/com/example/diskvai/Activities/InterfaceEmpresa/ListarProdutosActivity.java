package com.example.diskvai.Activities.InterfaceEmpresa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diskvai.Adapters.ProdutoAdapter;
import com.example.diskvai.Models.Produto;
import com.example.diskvai.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ListarProdutosActivity extends AppCompatActivity {

    String id_empresa;
    private JSONObject jsonObject;
    List<Produto> produtoLista;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos);

        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");



        resgatarProdutos();
    }

    private void resgatarProdutos() {
        try {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarProdutos.php").newBuilder();
        urlBuilder.addQueryParameter("id_empresa", id_empresa);


        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(ListarProdutosActivity.this, "",
                    "Carregando Produtos", true);

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                alert("deu lenha");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            //alert(response.body().string());
                            try {
                                String data = response.body().string();
                                JSONArray jsonArray = new JSONArray(data);
                                if(jsonArray.length()!=0){
                                    //jsonObject = jsonArray.getJSONObject(0);

                                    listar(jsonArray);
                                } else {
                                    alert("Não há produtos cadastrados");
                                }
                            } catch (JSONException e) {
                                alert("erro no json");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    private void listar(JSONArray jsonArray) {
        produtoLista = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String id = jsonChildNode.optString("ID");
                String nome = jsonChildNode.optString("Nome_prod");
                String descricao = jsonChildNode.optString("Descricao");
                Double preco = Double.parseDouble(jsonChildNode.optString("Preco"));
                String url_img = jsonChildNode.optString("Foto");

                Produto produto = new Produto(id, nome, descricao, preco, url_img);
                produtoLista.add(produto);
            }

            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(new ProdutoAdapter(this, produtoLista));
            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar produtos");
        }
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        this.finish();
    }

    public void cadastrarProduto(View view) {
            Intent intent;
            Bundle parameters = new Bundle();
            parameters.putString("ID", id_empresa);
            intent = new Intent(this, CadastrarProdutoActivity.class);
            intent.putExtras(parameters);
            startActivity(intent);
    }
}
