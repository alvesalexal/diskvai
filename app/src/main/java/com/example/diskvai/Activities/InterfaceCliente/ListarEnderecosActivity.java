package com.example.diskvai.Activities.InterfaceCliente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceEmpresa.CadastrarEntregadoresEmpActivity;
import com.example.diskvai.Adapters.EnderecoAdapter;
import com.example.diskvai.Adapters.EnderecoAdapter_List;
import com.example.diskvai.Adapters.EntregadorAdapter;
import com.example.diskvai.Models.Endereco;
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


public class ListarEnderecosActivity extends AppCompatActivity {


    ArrayList<Endereco> enderecos;
    String id_cliente;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listar_enderecos);

        Intent intent = this.getIntent();
        id_cliente = intent.getStringExtra("ID_Cliente");

        resgatarEnderecos();
    }

    private void resgatarEnderecos() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();
            alert(id_cliente);
            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarEnderecos.php").newBuilder();
            urlBuilder.addQueryParameter("id_cliente", id_cliente);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(ListarEnderecosActivity.this, "",
                    "Carregando Enderecos", true);

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
                                        alert("Não há enderecos cadastrados");
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
        enderecos = new ArrayList<Endereco>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                int id = jsonChildNode.optInt("ID");
                String rua = jsonChildNode.optString("Rua");
                String numero = jsonChildNode.optString("Numero");
                String complemento = jsonChildNode.optString("Complemento");
                String bairro = jsonChildNode.optString("Bairro");
                String cidade = jsonChildNode.optString("Cidade");
                String estado = jsonChildNode.optString("Estado");
                String cep = jsonChildNode.optString("CEP");

                Endereco endereco = new Endereco(String.valueOf(id),rua,numero,complemento,bairro,cidade,estado,cep);
                enderecos.add(endereco);
            }

            ListView listaDeEnderecos = (ListView) findViewById(R.id.listview);

            //EnderecoAdapter_List enderecoAdapter = new EnderecoAdapter_List(ListarEnderecosActivity.this, enderecos);
            listaDeEnderecos.setAdapter(new EnderecoAdapter_List(ListarEnderecosActivity.this, enderecos));
            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar Endereço");
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
        parameters.putString("ID", id_cliente);
        intent = new Intent(this, CadastrarEntregadoresEmpActivity.class);
        startActivityForResult(intent, 1);
    }

    public void excluirEndereco(String id_endereco) {
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
                .setTitle("Excluir Endereço?")
                .setIcon(R.drawable.ic_icons8_trash)
                .setMessage("Digite sua senha")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {
                    if(!input.getText().toString().equals("")) {
                        OkHttpClient client = new OkHttpClient();

                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/excluirEndereco.php").newBuilder();
                        urlBuilder.addQueryParameter("ID", id_endereco);
                        urlBuilder.addQueryParameter("ID_Cli", id_cliente);
                        urlBuilder.addQueryParameter("senha_cli", input.getText().toString());

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
                                            resgatarEnderecos();
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

    public void cadastrarEndereco(View view) {
        Intent intent = new Intent(this, CadastrarEnderecoActivity.class);
        intent.putExtra("ID_Cliente", id_cliente);
        startActivityForResult(intent, 1);
    }

    public void editarEndereco(Endereco endereco) {
        Intent intent = new Intent(this, EditarEnderecoActivity.class);
        intent.putExtra("ID_Comprador", id_cliente);
        intent.putExtra("ID", endereco.getID());
        intent.putExtra("Rua", endereco.getRua());
        intent.putExtra("Numero", endereco.getNumero());
        intent.putExtra("Complemento", endereco.getComplemento());
        intent.putExtra("bairro", endereco.getBairro());
        intent.putExtra("cidade", endereco.getCidade());
        intent.putExtra("cep", endereco.getCep());
        intent.putExtra("estado", endereco.getEstado());
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if(resultCode == RESULT_OK){
                resgatarEnderecos();
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

}
