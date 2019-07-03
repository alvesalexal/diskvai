package com.example.diskvai.Activities.InterfaceCliente;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceEmpresa.ListarEntregadorActivity;
import com.example.diskvai.Helpers.TratamentoDados;
import com.example.diskvai.R;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EditarEnderecoActivity extends AppCompatActivity {

    String id_endereco,id_comprador, rua, numero, complemento, bairro, cidade, cep, estado;
    EditText Rua, Numero, Complemento, Estado, Bairro, Cidade, Cep;
    Button btnEditar;
    private String resposta;
    private String a[]={"#"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_endereco);
        Intent intent = this.getIntent();
        id_endereco = intent.getStringExtra("ID");
        id_comprador = intent.getStringExtra("ID_Comprador");
        rua = intent.getStringExtra("Rua");
        numero = intent.getStringExtra("Numero");
        complemento = intent.getStringExtra("Complemento");
        bairro = intent.getStringExtra("bairro");
        cidade = intent.getStringExtra("cidade");
        cep = intent.getStringExtra("cep");
        estado = intent.getStringExtra("estado");

        read();
        Rua.setText(rua);
        Numero.setText(numero);
        Complemento.setText(complemento);
        Bairro.setText(bairro);
        Cidade.setText(cidade);
        Cep.setText(cep);
        Estado.setText(estado);

        btnEditar.setOnClickListener(view -> {
            if (validaCadastro()) {
                try {
                    btnEditar.setEnabled(false);
                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    OkHttpClient client = new OkHttpClient();

                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/editarEndereco.php").newBuilder();
                    urlBuilder.addQueryParameter("id", id_endereco);
                    urlBuilder.addQueryParameter("id_comprador", id_comprador);
                    urlBuilder.addQueryParameter("rua", Rua.getText().toString());
                    urlBuilder.addQueryParameter("numero", Numero.getText().toString());
                    urlBuilder.addQueryParameter("complemento", Complemento.getText().toString());
                    urlBuilder.addQueryParameter("bairro", Bairro.getText().toString());
                    urlBuilder.addQueryParameter("cidade", Cidade.getText().toString());
                    urlBuilder.addQueryParameter("cep", Cep.getText().toString());
                    urlBuilder.addQueryParameter("estado", Estado.getText().toString());

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
                                            alert("endereço ja cadastrado");
                                        }
                                        else {
                                            alert(a[1]);
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

                btnEditar.setEnabled(true);
            }

        });
    }



    public void read() {
        Rua = findViewById(R.id.rua);
        Numero = findViewById(R.id.numero);
        Complemento = findViewById(R.id.complemento);
        Bairro = findViewById(R.id.bairro);
        Cidade = findViewById(R.id.cidade);
        Cep = findViewById(R.id.cep);
        Estado = findViewById(R.id.estado);

        btnEditar=findViewById(R.id.editar);
    }

    public void back(View view) {
        this.finish();
    }


    private boolean validaCadastro() {
        if (!Rua.getText().toString().equals("") &&
                !(Numero.getText().toString().equals("")) &&
                !(Cidade.getText().toString().equals("")) &&
                !(Bairro.getText().toString().equals("")) &&
                !(Cep.getText().toString().equals("")) &&
                !(Estado.getText().toString().equals(""))
        ) {
            return true;
        } else {
            alert("Preencha todos os campos");
            return false;
        }
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_LONG).show();
    }


}