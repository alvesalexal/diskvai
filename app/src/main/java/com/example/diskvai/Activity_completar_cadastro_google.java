package com.example.diskvai;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_completar_cadastro_google extends AppCompatActivity {

    private Button btnCadastrar, btnvoltar;
    private EditText telefone;

    private String resposta;
    private String a[]={"#"};

    private void read() {
        btnCadastrar = findViewById(R.id.cadastrar);
        telefone = (EditText) findViewById(R.id.telefone);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro_google);
        Intent intent = this.getIntent();
        String id = intent.getStringExtra("id");
        String nome = intent.getStringExtra("nome");
        String email = intent.getStringExtra("email");
        read();

        btnCadastrar.setOnClickListener(view -> {
            if (!telefone.getText().equals("")) {
                try {
                    btnCadastrar.setEnabled(false);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/insertCli.php").newBuilder();
                    urlBuilder.addQueryParameter("C_Nome", nome);
                    urlBuilder.addQueryParameter("C_Tel", telefone.getText().toString());
                    urlBuilder.addQueryParameter("C_Senha", id);
                    urlBuilder.addQueryParameter("C_LOGIN", email);
                    urlBuilder.addQueryParameter("C_Email", email);

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
                                            principal(a);
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
                btnCadastrar.setEnabled(true);
            }
            else alert("forneça o telefone");
        });
    }
    private void principal(String []a){
        if(a[1].equals("Cadastrado com Sucesso!")) {
            Intent intent = new Intent(this, PrincipalCli.class);
            startActivity(intent);
            telefone.setText("");
        }
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }
    public void back(View view) {
        this.finish();
    }
}
