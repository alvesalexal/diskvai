package com.example.diskvai.Activities.InterfaceEmpresa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diskvai.Adapters.EntregadorAdapter;
import com.example.diskvai.Models.Entregador;
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


public class ListarEntregadorActivity extends AppCompatActivity {


    List<Entregador> entregadores;
    String id_empresa;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_entregador);

        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");

        resgatarEntregadores();
    }


    private void resgatarEntregadores() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarEntregadores.php").newBuilder();
            urlBuilder.addQueryParameter("id_empresa", id_empresa);


            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(ListarEntregadorActivity.this, "",
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
                                        alert("Não há entregadores cadastrados");
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
        entregadores = new ArrayList<Entregador>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                int id = jsonChildNode.optInt("ID");
                String nome = jsonChildNode.optString("Nome_ent");
                String email = jsonChildNode.optString("Email");
                String Telefone = jsonChildNode.optString("Telefone");
                String url_img = jsonChildNode.optString("Foto");

                Entregador entregador = new Entregador(nome,url_img,id,Telefone,email);
                entregadores.add(entregador);
            }

            ListView listaDeEntregadores = (ListView) findViewById(R.id.listView);

            EntregadorAdapter entregadorAdapter = new EntregadorAdapter(entregadores, ListarEntregadorActivity.this);
            listaDeEntregadores.setAdapter(entregadorAdapter);
            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar Entrgadores");
        }
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        this.finish();
    }
}
