package com.example.diskvai;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CadastrarProdutoActivity extends AppCompatActivity {

    private EditText nome, descricao, preco, frete;
    private TextView precoFinal;
    private String id_empresa, resposta;
    private String a[]={"#"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_produto);

        read();
        nomeValido();
        descricaoValida();
        precoValido();

        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");
    }

    public void read() {
        nome = findViewById(R.id.nome);
        descricao = findViewById(R.id.descricao);
        preco = findViewById(R.id.preco);
    }

    public void back(View view) {
        this.finish();
    }

    public Boolean validaCadastro() {
        Boolean flag = false;
        if(nome.getError()==null&&descricao.getError()==null&&preco.getError()==null) {
            flag = true;
        }


        return flag;
    }

    public void nomeValido() {
        nome.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(nome.getText().toString().length()>=2) {
                    nome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nome.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    nome.setError(null);
                } else {
                    nome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nome.setError("Insira um nome válido com dois ou mais caracteres");
                }
            }
        });
    }
    public void descricaoValida() {
        descricao.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(descricao.getText().toString().length()>=10) {
                    descricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    descricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    descricao.setError(null);
                } else {
                    descricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    descricao.setError("Coloque mais detalhes sobre seu produto");
                }
            }
        });
    }
    public void precoValido() {
        preco.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(!preco.getText().toString().equals("")&&Double.parseDouble(preco.getText().toString())!=0) {
                    Double preco_valor = Double.parseDouble(preco.getText().toString());
                    preco.setText("" + preco_valor);
                    preco.setError(null);
                } else {
                    preco.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    preco.setError("Digite um valor válido e maior que R$ 00,00");
                }
            }
        });

    }

    public void cadastrarProduto(View view) {
        if (validaCadastro()) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/inserirProduto.php").newBuilder();
                urlBuilder.addQueryParameter("nome_produto", nome.getText().toString());
                urlBuilder.addQueryParameter("url_foto", "");
                urlBuilder.addQueryParameter("id_empresa", id_empresa);
                urlBuilder.addQueryParameter("desc_produto", descricao.getText().toString());
                urlBuilder.addQueryParameter("preco", preco.getText().toString());


                String url = urlBuilder.build().toString();

                Request request = new Request.Builder()
                        .url(url)
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    resposta = (response.body().string());
                                    a=resposta.split("#");

                                    if(a[1].split("'")[0].equals("Duplicate entry ")){
                                        alert("email ja cadastrado");
                                    }
                                    else {
                                        alert(a[1]);
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
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }
}
