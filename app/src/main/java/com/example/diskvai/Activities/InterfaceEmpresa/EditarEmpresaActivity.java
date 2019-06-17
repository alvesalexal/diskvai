package com.example.diskvai.Activities.InterfaceEmpresa;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diskvai.Helpers.TratamentoDados;
import com.example.diskvai.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditarEmpresaActivity extends AppCompatActivity {

    String id_empresa, nome_empresa, email_empresa, login_empresa, telefone_empresa;
    EditText nome, login, telefone, senha, nova_senha;
    Button btnEditar;
    private AutoCompleteTextView email;
    private String resposta;
    private String a[]={"#"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil_activty);
        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");
        nome_empresa = intent.getStringExtra("Nome");
        email_empresa = intent.getStringExtra("Email");
        login_empresa = intent.getStringExtra("Login");
        telefone_empresa = intent.getStringExtra("Telefone");


        read();
        nome.setText(nome_empresa);
        email.setText(email_empresa);
        login.setText(login_empresa);
        telefone.setText(telefone_empresa);

        // Tratamento de Dados
        nomeValido();
        emailValido();
        senhaValida();




        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String Texto = email.getText().toString();
                if(!Texto.equals("@")) {
                    String[] ArrayTexto = Texto.split("@");
                    login.setText(ArrayTexto[0]);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        btnEditar.setOnClickListener(view -> {
            if (validaCadastro()) {
                try {
                    btnEditar.setEnabled(false);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/editarVendedor.php").newBuilder();
                    urlBuilder.addQueryParameter("id", id_empresa);
                    urlBuilder.addQueryParameter("nome_vend", nome.getText().toString());
                    urlBuilder.addQueryParameter("email", email.getText().toString());
                    urlBuilder.addQueryParameter("login", login.getText().toString());
                    urlBuilder.addQueryParameter("telefone", telefone.getText().toString());
                    urlBuilder.addQueryParameter("senha", senha.getText().toString());
                    urlBuilder.addQueryParameter("nova_senha", nova_senha.getText().toString());
                    urlBuilder.addQueryParameter("url_foto", null);


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

                btnEditar.setEnabled(true);
            }

        });
    }



    public void read() {
        nome = findViewById(R.id.Nome);
        email = findViewById(R.id.EndEmail);
        login = findViewById(R.id.Login);
        telefone = findViewById(R.id.Telefone);
        senha = findViewById(R.id.Senha);
        nova_senha = findViewById(R.id.Novasenha);
        btnEditar = findViewById(R.id.editar);
    }

    public void back(View view) {
        this.finish();
    }


    private boolean validaCadastro() {
        if (camposOK()) {
            if (!nome.getText().toString().equals("") &&
                    !(senha.getText().toString().equals("")) &&
                    !(login.getText().toString().equals("")) &&
                    !(email.getText().toString().equals("")) &&
                    !(nova_senha.getText().toString().equals(""))
            ) {
                return true;
            } else {
                alert("Preencha todos os campos");
                return false;
             }
        }
        return false;
    }
    private boolean camposOK() {
        if(nome.getError()==null&&email.getError()==null&&
                login.getError()==null&&
                telefone.getError()==null&&
                senha.getError()==null&&
                nova_senha.getError()==null) {
            return true;
        }
        return false;
    }


    private void limparcampos() {
        nome.setText("");
        email.setText("");
        telefone.setText("");
        senha.setText("");
        nova_senha.setText("");
        login.setText("");

    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_LONG).show();
    }


    // métodos dos tratamentos de dados

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

    public void emailValido() {
        TratamentoDados valida = new TratamentoDados();
        email.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(valida.isEmail(email.getText().toString())) {
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    email.setError(null);

                } else {
                    email.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    email.setError("Insira um Email Válido");
                }
            }
        });
    }


    public void senhaValida() {
        TratamentoDados valida = new TratamentoDados();
        nova_senha.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if(valida.senhaValida(nova_senha.getText().toString())) {
                    nova_senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nova_senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    nova_senha.setError(null);
                } else {
                    nova_senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nova_senha.setError("A senha deve ter 6 ou mais dígitos e pode incluir letras, números e caracteres especiais");
                }

            }
        });
    }

}
