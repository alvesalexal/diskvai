package com.example.diskvai.Activities.InterfaceEmpresa;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceEntregador.EntregadorHomeActivity;
import com.example.diskvai.Activities.PoliticaDePrivacidadeActivity;
import com.example.diskvai.Helpers.TratamentoDados;
import com.example.diskvai.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CadastrarEntregadoresEmpActivity extends AppCompatActivity {

    private Button btnCadastrar, btnvoltar;
    private EditText nome;
    private EditText telefone;
    private EditText senha;
    private EditText confirmasenha;
    private EditText login;
    private String resposta,id_empresa;
    private String a[]={"#"};
    private AutoCompleteTextView email;
    private com.rey.material.widget.CheckBox checkBox;

    private void read() {
        checkBox = findViewById(R.id.checkbox);
        btnCadastrar = findViewById(R.id.cadastrar);
        btnvoltar = findViewById(R.id.voltar);
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
        setContentView(R.layout.activity_cadastrar_entregadores_emp);
        read();
        nomeValido();
        emailValido();
        senhaValida();
        senhaConfirmada();

        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

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
    }

    private void principal(){
        this.finish();
    }

    private boolean validaCadastro() {
        if(camposOK()) {
            if ((senha.getText().toString()).equals(confirmasenha.getText().toString()) && !(senha.getText().toString()).equals("")) {
                if (!nome.getText().toString().equals("") &&
                        !(senha.getText().toString().equals("")) &&
                        !(login.getText().toString().equals("")) &&
                        !(email.getText().toString().equals(""))
                ){
                    return true;
                }
                    else {
                    alert("Preencha todos os campos");
                    return  false;
                }
            }
        } else {
            alert("Confira os dados e tente novamente");
            //limparcampos();
        }
        return false;
    }

    private boolean camposOK() {
        if(nome.getError()==null&&email.getError()==null&&
                login.getError()==null&&
                telefone.getError()==null&&
                senha.getError()==null&&
                confirmasenha.getError()==null) {
            return true;
        }
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
        senha.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if(valida.senhaValida(senha.getText().toString())) {
                    senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    senha.setError(null);
                } else {
                    senha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    senha.setError("A senha deve ter 6 ou mais dígitos e pode incluir letras, números e caracteres especiais");
                }
                if(!confirmasenha.getText().toString().equals("")) {
                    if(confirmasenha.getText().toString().equals(senha.getText().toString())) {
                        confirmasenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        confirmasenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                        confirmasenha.setError(null);
                    } else {
                        confirmasenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                        confirmasenha.setError("As senhas digitadas não conferem");
                    }
                }
            }
        });
    }

    public void senhaConfirmada() {
        TratamentoDados valida = new TratamentoDados();
        confirmasenha.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                if(confirmasenha.getText().toString().equals(senha.getText().toString())&&!confirmasenha.getText().toString().equals("")) {
                    confirmasenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    confirmasenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    confirmasenha.setError(null);
                } else {
                    confirmasenha.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    confirmasenha.setError("As senhas digitadas não conferem");

                }
            }
        });
    }

    public void cadastrarEntregadores(View view) {
        if (validaCadastro()) {
            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/inserirEntregador.php").newBuilder();
                urlBuilder.addQueryParameter("Ent_Nome", nome.getText().toString());
                urlBuilder.addQueryParameter("id_empresa", id_empresa);
                urlBuilder.addQueryParameter("Ent_TEL", telefone.getText().toString());
                urlBuilder.addQueryParameter("Ent_LOGIN", login.getText().toString());
                urlBuilder.addQueryParameter("Ent_SENHA", senha.getText().toString());
                urlBuilder.addQueryParameter("Ent_EMAIL", email.getText().toString());
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

                                    if(a[1].split("'")[0].equals("Nome de usuario ou email já cadastrados")){
                                        alert("Nome de usuario ou email já cadastrados");
                                    }
                                    else {
                                        //alert(a[1]);
                                        Log.d(id_empresa+"-ERRO",a[1]);
                                        setResult(RESULT_OK);
                                        finish();
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
}
