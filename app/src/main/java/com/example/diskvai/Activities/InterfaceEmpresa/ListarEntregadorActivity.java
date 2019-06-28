package com.example.diskvai.Activities.InterfaceEmpresa;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceCadastro.CadastroEntregadorActivity;
import com.example.diskvai.Activities.LoginActivity;
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
                    "Carregando Entregadores", true);

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
                                        progressDialog.cancel();
                                        progressDialog = null;
                                    }
                                } catch (JSONException e) {
                                    alert("erro no json");
                                    progressDialog.cancel();
                                    progressDialog = null;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                progressDialog.cancel();
                                progressDialog = null;
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

            ListView listaDeEntregadores = (ListView) findViewById(R.id.listview);

            EntregadorAdapter entregadorAdapter = new EntregadorAdapter(entregadores, ListarEntregadorActivity.this,this);
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

    public void cadastrarEntregadores(View view) {
        Intent intent;
        Bundle parameters = new Bundle();
        parameters.putString("ID", id_empresa);
        intent = new Intent(this, CadastrarEntregadoresEmpActivity.class);
        startActivityForResult(intent, 1);
    }

    public void excluirEntregador(String id_entregador) {
        final EditText input = new EditText(this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            input.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        new AlertDialog.Builder(this)
                .setTitle("Deletar Entregador?")
                .setIcon(R.drawable.ic_icons8_trash)
                .setMessage("Digite sua senha")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    if(!input.getText().toString().equals("")) {
                        OkHttpClient client = new OkHttpClient();

                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/excluirEntregador.php").newBuilder();
                        urlBuilder.addQueryParameter("ID", id_entregador);
                        urlBuilder.addQueryParameter("senha_empresa", input.getText().toString());


                        String url = urlBuilder.build().toString();

                        Request request = new Request.Builder().url(url).build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                runOnUiThread(() -> {
                                    try {
                                        String a[] = {"#"};
                                        String resposta = (response.body().string());
                                        a = resposta.split("#");
                                        if (a[1].split("'")[0].equals("Senha Incorreta")) {
                                            alert("Senha Incorreta");

                                        } else {
                                            alert(a[1]);
                                            resgatarEntregadores();
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                });

                            }
                        });

                    } else {
                        alert("Digite a senha");
                    }
                })
                .setNegativeButton(android.R.string.no, null)
                .setView(input).show();

    }
}
