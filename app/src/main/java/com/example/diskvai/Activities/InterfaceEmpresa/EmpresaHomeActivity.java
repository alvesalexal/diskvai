package com.example.diskvai.Activities.InterfaceEmpresa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Adapters.PedidoAdapter;
import com.example.diskvai.Adapters.ProdutoAdapter;
import com.example.diskvai.Models.Pedido;
import com.example.diskvai.Models.Produto;
import com.example.diskvai.R;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class EmpresaHomeActivity extends AppCompatActivity {

    ExpandableLinearLayout menuLateral, infoPerfil;
    Button editarPerfil, pedidos, listarProduto, listarEntregadores, logout;
    ImageView imgPerfil;
    TextView nome_empresa;
    String id_empresa, empresa_nome, login_empresa, email_empresa, telefone_empresa;
    ListView listaPedidos;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_emp);
        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");
        empresa_nome = intent.getStringExtra("Nome_empresa");
        login_empresa = intent.getStringExtra("Login_empresa");
        email_empresa= intent.getStringExtra("Email_empresa");
        telefone_empresa= intent.getStringExtra("Telefone_empresa");


        read();
        menu();

        nome_empresa.setText(empresa_nome);

    }

    public void menu() {
        menuLateral.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                infoPerfil.expand();
                listaPedidos.setVisibility(View.GONE);
            }

            @Override
            public void onPreClose() {
                editarPerfil.setText("");
                pedidos.setText("");
                listarProduto.setText("");
                listarEntregadores.setText("");
                logout.setText("");
                infoPerfil.collapse();

            }

            @Override
            public void onOpened() {
                editarPerfil.setText("Editar Perfil");
                pedidos.setText("Pedidos");
                listarProduto.setText("Produtos");
                listarEntregadores.setText("Entregadores");
                logout.setText("Logout");
            }

            @Override
            public void onClosed() {
                listaPedidos.setVisibility(View.VISIBLE);
                resgatarPedidos();
            }
        });
    }

    public void read() {
        listaPedidos = findViewById(R.id.pedidosLista);
        infoPerfil = findViewById(R.id.infoPerfil);
        editarPerfil = findViewById(R.id.editarPerfil);
        pedidos = findViewById(R.id.pedidos);
        listarProduto = findViewById(R.id.listarProduto);
        listarEntregadores = findViewById(R.id.listarEntregador);
        imgPerfil = findViewById(R.id.imgPerfil);
        nome_empresa = findViewById(R.id.nomeEmpresa);
        menuLateral = findViewById(R.id.menuLateral);
        menuLateral.setClosePosition(100);
        logout = findViewById(R.id.logout);

    }

    public void fechar(View view) {
        menuLateral.toggle();
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }

    public  void listarProdutos(View view) {
        Intent intent;
        Bundle parameters = new Bundle();
        parameters.putString("ID", id_empresa);
        intent = new Intent(this, ListarProdutosActivity.class);
        intent.putExtras(parameters);
        startActivity(intent);
    }

    public void listarEntregadores(View view) {
        Intent intent;
        Bundle parameters = new Bundle();
        parameters.putString("ID", id_empresa);
        intent = new Intent(this, ListarEntregadorActivity.class);
        intent.putExtras(parameters);
        startActivity(intent);
    }

    public void editarPerfil(View view) {
        Intent intent;
        Bundle parameters = new Bundle();
        parameters.putString("ID", id_empresa);
        parameters.putString("Nome", empresa_nome);
        parameters.putString("Login", login_empresa);
        parameters.putString("Email", email_empresa);
        parameters.putString("Telefone", telefone_empresa);
        intent = new Intent(this, EditarEmpresaActivity.class);
        intent.putExtras(parameters);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {

            if(resultCode == RESULT_OK){

                empresa_nome = data.getStringExtra("Nome");
                email_empresa = data.getStringExtra("Email");
                login_empresa = data.getStringExtra("Login");
                telefone_empresa = data.getStringExtra("Telefone");
                nome_empresa.setText(empresa_nome);
            }
            if (resultCode == RESULT_CANCELED) {

            }
        }
    }

    private void resgatarPedidos() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarPedidos.php").newBuilder();
            urlBuilder.addQueryParameter("id_empresa", id_empresa);


            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(EmpresaHomeActivity.this, "",
                    "Atualizando Pedidos", true);

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
                                        listarPedidos(jsonArray);
                                    } else {
                                        alert("Não há pedidos pendentes");
                                        progressDialog.cancel();
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

    public void listarPedidos(JSONArray jsonArray) {
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try {
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String pedido_id = jsonChildNode.optString("ID");
                String nome_comprador = jsonChildNode.optString("Nome_comp");
                Double valor = Double.parseDouble(jsonChildNode.optString("Valor"));
                String forma_pagamento = jsonChildNode.optString("Forma_Pagamento");
                String status = jsonChildNode.optString("status");

                // endereço
                String rua = jsonChildNode.optString("Rua");
                String numero = jsonChildNode.optString("Numero");
                String complemento = jsonChildNode.optString("Complemento");
                String bairro = jsonChildNode.optString("Bairro");
                String cidade = jsonChildNode.optString("Cidade");
                String estado = jsonChildNode.optString("Estado");
                String cep = jsonChildNode.optString("Cep");

                Pedido pedido = new Pedido(pedido_id, nome_comprador);
                pedido.setEndereco(rua + ", " + numero + ", " + complemento + ", " + bairro + ". " + cidade + " - " + estado + ". " + cep);
                pedido.setFormaPagamento(forma_pagamento);
                pedido.setValor(valor);
                pedido.setStatus(status);
                pedidos.add(pedido);

            }


            listaPedidos.setAdapter(new PedidoAdapter(this, pedidos));
            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar produtos");
        }

    }
}
