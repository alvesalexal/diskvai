package com.example.diskvai;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
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

public class CadastroEntregador extends AppCompatActivity {

    private Button btnCadastrar, btnvoltar;
    private EditText nome;
    private EditText telefone;
    private EditText senha;
    private EditText confirmasenha;
    private EditText login;
    private String resposta;
    private String a[]={"#"};
    private AutoCompleteTextView email;

    private void read() {
        btnCadastrar = findViewById(R.id.cadastrar);
        //btnvoltar = findViewById(R.id.voltar);
        nome = findViewById(R.id.Nome);
        senha = findViewById(R.id.senha);
        confirmasenha = findViewById(R.id.confirmaSenha);
        email = (AutoCompleteTextView) findViewById(R.id.EndEmail);
        login = findViewById(R.id.Login);
        telefone = (EditText) findViewById(R.id.telefone);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_entregador);

        read();
        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Texto = email.getText().toString();
                String[] ArrayTexto = Texto.split("@");
                login.setText(ArrayTexto[0]);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnCadastrar.setOnClickListener(view -> {
            if (validaCadastro()) {
                try {
                    btnCadastrar.setEnabled(false);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/insertEntr.php").newBuilder();
                    urlBuilder.addQueryParameter("Ent_Nome", nome.getText().toString());
                    urlBuilder.addQueryParameter("Ent_TEL", telefone.getText().toString());
                    urlBuilder.addQueryParameter("Ent_LOGIN", login.getText().toString());
                    urlBuilder.addQueryParameter("Ent_SENHA", senha.getText().toString());
                    urlBuilder.addQueryParameter("Ent_EMAIL", email.getText().toString());

                    String url = urlBuilder.build().toString();

                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {}

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    try {
                                        resposta = (response.body().string());
                                        a=resposta.split("#");
                                        alert(a[1]);
                                        principal(a);
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

        });
    }

    private void principal(String []a){
        if(a[1].equals("Cadastrado com Sucesso!")) {
            Intent intent = new Intent(this, PrincipalEntr.class);
            startActivity(intent);
            limparcampos();
        }
    }

    private boolean validaCadastro() {
        if ((senha.getText().toString()).equals(confirmasenha.getText().toString())&& !(senha.getText().toString()).equals("")) {
            if (!(nome.getText().equals("") &&
                    !(telefone.getText().equals("")) &&
                    !(senha.getText().equals("")) &&
                    !(login.getText().equals("")) &&
                    !(email.getText().equals(""))
            )) {
                //alert("Preencher todos os campos");
            }
            return true;
        }
        alert("Dados incorretos");
        //limparcampos();
        return false;
    }

    private void limparcampos() {
        nome.setText("");
        email.setText("");
        telefone.setText("");
        senha.setText("");
        confirmasenha.setText("");
        login.setText("");

    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_LONG).show();
    }

    public void back(View view) {
        this.finish();
    }
}
