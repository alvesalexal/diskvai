package com.example.diskvai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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

public class Activity_login extends AppCompatActivity {

    private boolean permissaoArmazenamento = false;
    private String usuario="Alex";
    private String senha = "123";
    private Button btnLogar;
    private EditText pass;
    private EditText user;
    private String resposta="888";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogar= (Button) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.senha);

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissaoArmazenamento = false;
            Log.e("Permissão", "Sem permissão");
            Toast.makeText(this,"sem permissao de internet",Toast.LENGTH_SHORT).show();
        }

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnLogar.setEnabled(false);
                try {
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/readLogin.php").newBuilder();
                    urlBuilder.addQueryParameter("usuario", user.getText().toString());
                    urlBuilder.addQueryParameter("senha", pass.getText().toString());

                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

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
                                        resposta = (response.body().string());
                                        String a[] = resposta.split("#");
                                        login(a[1]);
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
        });
    }

    private void alert(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private void login(String resposta){
        if(resposta.equals("sucesso")){
            Intent intent = new Intent(this, telaTeste.class);
            startActivity(intent);
        }
        else {
            alert("Nome Usuario ou Senha incorretos");
        }
        btnLogar.setEnabled(true);
    }

    public void Cadastro(View view) {
        Intent intent = new Intent(this, TipoCadastro.class);
        startActivity(intent);
    }

}
