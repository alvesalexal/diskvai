package com.example.diskvai.Activities.InterfaceCliente;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceEmpresa.CadastrarProdutoActivity;
import com.example.diskvai.Activities.InterfaceEmpresa.EmpresaHomeActivity;
import com.example.diskvai.Activities.InterfaceEmpresa.ListarProdutosActivity;
import com.example.diskvai.Adapters.EnderecoAdapter;
import com.example.diskvai.Adapters.ProdutoCarrinhoAdapter;
import com.example.diskvai.Adapters.ProdutoCliAdapter;
import com.example.diskvai.Models.Endereco;
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
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ListarProdutosCliActivity extends AppCompatActivity {

    String id_empresa,id_cliente,nome_vendedor, id_endereco = null, id_forma_pagamento = null;
    private JSONObject jsonObject;
    List<Produto> produtoLista, produtoListaCar;
    List<Endereco> enderecoLista;
    ProgressDialog progressDialog;
    TextView nomeVendedor, carrinhoTitulo;
    ListView listView2, listView;
    Button enviarBtn, formaPagamentoBtn, enderecoBtn, listarEnderecos;
    Boolean menuIsOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos_cli);

        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID_Empresa");
        id_cliente = intent.getStringExtra("ID_Cliente");
        nome_vendedor = intent.getStringExtra("Nome_Vend");

        nomeVendedor = findViewById(R.id.nome_Vendedor);
        nomeVendedor.setText(nome_vendedor);
        resgatarProdutos();
        listView = findViewById(R.id.listview);
        enviarBtn = findViewById(R.id.enviarPedidoBtn);
        formaPagamentoBtn = findViewById(R.id.formaPagamentoBtn);
        enderecoBtn = findViewById(R.id.enderecoBtn);
        listView2 = findViewById(R.id.listviewPedido);
        carrinhoTitulo = findViewById(R.id.carrinhoTitulo);
        listarEnderecos = findViewById(R.id.enderecos);


        produtoListaCar = new ArrayList<Produto>();

        enviarPedido(this);

        enderecoBtn.setOnClickListener(view -> {
            resgatarEnderecos();
        });

        formaPagamentoBtn.setOnClickListener(view -> {
            resgatarFormasPagamento();
        });

        listarEnderecos.setOnClickListener(view -> {
            listarEnderecos();
        });
    }

    private void resgatarProdutos() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarProdutos.php").newBuilder();
            urlBuilder.addQueryParameter("id_empresa", id_empresa);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity.this, "",
                    "Carregando Produtos", true);

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
                                        progressDialog.cancel();
                                        alert("Não há produtos cadastrados");
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

    private void listar(JSONArray jsonArray) {
        produtoLista = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String id = jsonChildNode.optString("ID");
                String nome = jsonChildNode.optString("Nome_prod");
                String descricao = jsonChildNode.optString("Descricao");
                Double preco = Double.parseDouble(jsonChildNode.optString("Preco"));
                String url_img = jsonChildNode.optString("Foto");

                Produto produto = new Produto(id, nome, descricao, preco, url_img);
                produtoLista.add(produto);
            }

            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(new ProdutoCliAdapter(this, produtoLista));
            //listView2.setAdapter(new ProdutoCliAdapter(this, produtoListaCar));
            listView2.setAdapter(new ProdutoCarrinhoAdapter(this,this, produtoListaCar));
            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar produtos");
        }
    }

    private void alert(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        this.finish();
    }

    public void enviarPedido(Context context) {
        enviarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (produtoListaCar.size() > 0) {
                    if(id_forma_pagamento!=null) {
                        if(id_endereco!=null) {
                            Pedido pedido = new Pedido();
                            pedido.setProdutoLista(produtoListaCar);
                            pedido.setFormaPagamento(formaPagamentoBtn.getText().toString());
                            pedido.setEndereco(enderecoBtn.getText().toString());

                            new AlertDialog.Builder(context)
                                    .setTitle("Confirmar Envio do Pedido")
                                    .setIcon(R.drawable.ic_icons8_trash)
                                    .setMessage("Subotal: R$" + pedido.getValor().toString() +
                                            "\nForma de Pagamento :" + pedido.getFormaPagamento() +
                                            "\nEndereço: " + pedido.getEndereco() + "\nID Cliente = " + id_cliente)
                                    .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                                        progressDialog = ProgressDialog.show(context, "",
                                                "Enviando Pedido", true);

                                        OkHttpClient client = new OkHttpClient();

                                        RequestBody multiPartBody = new MultipartBody.Builder()
                                                .setType(MultipartBody.FORM)
                                                .addFormDataPart("valor", pedido.getValor().toString())
                                                .addFormDataPart("id_cliente", id_cliente)
                                                .addFormDataPart("id_empresa", id_empresa)
                                                .addFormDataPart("id_endereco", id_endereco)
                                                .addFormDataPart("id_forma_pagamento", id_forma_pagamento)
                                                .addFormDataPart("id_produtos", pedido.getIDArray().toString())
                                                .build();
                                        Request request = new Request.Builder()
                                                .url("http://gabriellacastro.com.br/disk_vai/inserirPedido.php")
                                                .post(multiPartBody)
                                                .build();


//                            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/inserirPedido.php").newBuilder();
//                            urlBuilder.addQueryParameter("valor", pedido.getValor().toString());
//                            urlBuilder.addQueryParameter("id_empresa", id_empresa);
//                            urlBuilder.addQueryParameter("id_cliente", id_cliente);
//                            urlBuilder.addQueryParameter("id_endereco", id_endereco);
//                            urlBuilder.addQueryParameter("id_forma_pagamento", id_forma_pagamento);
//                            urlBuilder.addQueryParameter("id_produtos", pedido.getIDArray().toString());
//
//                            String url = urlBuilder.build().toString();
//
//                            Request request = new Request.Builder().url(url).build();

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
                                                        if (a[1].split("'")[0].equals("Pedido Cadastrado com Sucesso!")) {
                                                            alert("Pedido Cadastrado com Sucesso!");
                                                            progressDialog.cancel();
                                                            progressDialog = null;
                                                        } else {
                                                            alert(a[1]);
                                                            progressDialog.cancel();
                                                            progressDialog = null;
                                                        }
                                                    } catch (IOException e) {
                                                        e.printStackTrace();
                                                        progressDialog.cancel();
                                                        progressDialog = null;
                                                    }
                                                });

                                            }
                                        });
                                    })
                                    .setNegativeButton(android.R.string.no, null).show();
                        } else {
                            alert("Selecione um Endereço");
                        }
                    } else {
                        alert("Escolha uma Forma de Pagamento");
                    }
                }else {
                    alert("Insira Produtos no Carrinho");
                }
            }
        });


    }

    public void adicionarProdutoCarrinho(Produto produto) {
        produtoListaCar.add(produto);
        atualizarListCarrinho();
        alert(produto.getNome()+" adicionado ao carrinho");
        atualizarQtdItensCarrinho();
    }
    public void removerProdutoCarrinho(int position, Produto produto) {
        produtoListaCar.remove(position);
        atualizarListCarrinho();
        alert(produto.getNome()+" removido do carrinho");
        atualizarQtdItensCarrinho();
    }

    private void atualizarQtdItensCarrinho(){
        Button quantidadeItens = findViewById(R.id.quantidadeItens);
        String s = String.valueOf(produtoListaCar.size());
        quantidadeItens.setText(s);


    }

    private void atualizarListCarrinho(){

        listView2.setAdapter(new ProdutoCarrinhoAdapter(this,this, produtoListaCar));
    }

    public void abrirFecharCarrinho(View view) {
        if(menuIsOpen) {
            menuIsOpen = false;
            carrinhoTitulo.setText("Ver Carrinho");
            enviarBtn.setVisibility(View.GONE);
            listView2.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
            enderecoBtn.setVisibility(View.GONE);
            formaPagamentoBtn.setVisibility(View.GONE);

        } else {
            menuIsOpen = true;
            enderecoBtn.setVisibility(View.VISIBLE);
            formaPagamentoBtn.setVisibility(View.VISIBLE);
            carrinhoTitulo.setText("Minimizar Carrinho");
            enviarBtn.setVisibility(View.VISIBLE);
            listView2.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }
    }


    public void resgatarEnderecos() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarEnderecos.php").newBuilder();
            urlBuilder.addQueryParameter("id_cliente", id_cliente);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity.this, "",
                    "Carregando Endereços", true);


            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert("deu lenha");
                        }
                    });
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
                                        progressDialog.cancel();

                                        listarEnderecos(jsonArray);
                                    } else {
                                        progressDialog.cancel();
                                        alert("Cadastre um Endereco primeiro");
                                        //enviar pra activity de cadastrar endereço
                                    }
                                } catch (JSONException e) {
                                    alert("erro no json");
                                    progressDialog.cancel();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                progressDialog.cancel();
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.cancel();
        }
    }

    public void resgatarFormasPagamento() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/formaPagamento.php?").newBuilder();
            urlBuilder.addQueryParameter("id_empresa", id_empresa);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity.this, "",
                    "Carregando Formas de Pagamento", true);


            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            alert("deu lenha");
                        }
                    });
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
                                        progressDialog.cancel();
                                        listarFormasPagamento(jsonArray);
                                    } else {
                                        progressDialog.cancel();
                                    }
                                } catch (JSONException e) {
                                    progressDialog.cancel();
                                    alert("erro no json");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                progressDialog.cancel();
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
            progressDialog.cancel();
        }
    }

    private void listarEnderecos(JSONArray jsonArray) {
        enderecoLista = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String id = jsonChildNode.optString("ID");
                String rua = jsonChildNode.optString("Rua");
                String numero = jsonChildNode.optString("Numero");
                String complemento = jsonChildNode.optString("Complemento");
                String bairro = jsonChildNode.optString("Bairro");
                String cidade = jsonChildNode.optString("Cidade");
                String estado = jsonChildNode.optString("Estado");
                String cep = jsonChildNode.optString("CEP");


                Endereco endereco = new Endereco(id, rua, numero, complemento, bairro, cidade, estado, cep);
                enderecoLista.add(endereco);
            }

            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            final View customView = View.inflate(this, R.layout.custom_dialog_view, null);
            builder.setView(customView);


            final AlertDialog backDialog = builder.create();

            backDialog.setTitle("Escolha um Endereço");

            backDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancelar",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            backDialog.cancel();
                        }
                    });
            backDialog.setCanceledOnTouchOutside(false);
            backDialog.show();

            EnderecoAdapter adapter = new EnderecoAdapter(this, enderecoLista);

            ListView listView = (ListView) customView
                    .findViewById(R.id.list_view);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int location, long id) {
                    backDialog.cancel();
                }
            });


//            ListView listView = findViewById(R.id.listview);
//            listView.setAdapter(new ProdutoCliAdapter(this, produtoLista));
//            //listView2.setAdapter(new ProdutoCliAdapter(this, produtoListaCar));
//            listView2.setAdapter(new ProdutoCarrinhoAdapter(this,this, produtoListaCar));
//            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar Endereços");
        }
    }

    public void listarFormasPagamento(JSONArray jsonArray) {
        ArrayList<String> formasPagamento = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String descricao = jsonChildNode.optString("Descricao");


                formasPagamento.add(descricao);
            }

            final Dialog dialog = new Dialog(ListarProdutosCliActivity.this);
            dialog.setContentView(R.layout.popup_produtos_pedido);
            dialog.setTitle("Escolher Forma de Pagamento");
            ListView formaPagamentoLista = (ListView) dialog.findViewById(R.id.List);


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ListarProdutosCliActivity.this, android.R.layout.simple_list_item_1, formasPagamento);
            formaPagamentoLista.setAdapter(adapter);
            formaPagamentoLista.setOnItemClickListener((adapterView, view, i, l) -> {
                switch (adapterView.getItemAtPosition(i).toString()) {
                    case "Crédito Diners Club":
                        setFormaPagamentoID("1", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Crédito Mastercard":
                        setFormaPagamentoID("2", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Crédito Visa":
                        setFormaPagamentoID("3", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Débito Mastercard":
                        setFormaPagamentoID("4", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Débito Visa":
                        setFormaPagamentoID("5", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Pagamento online Mercado Pago":
                        setFormaPagamentoID("6", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Pagamento online PagSeguro":
                        setFormaPagamentoID("7", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Pagamento online PayPal":
                        setFormaPagamentoID("8", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Boleto":
                        setFormaPagamentoID("9", adapterView.getItemAtPosition(i).toString());
                        break;
                    case "Dinheiro":
                        setFormaPagamentoID("10", adapterView.getItemAtPosition(i).toString());
                        break;

                }
                dialog.dismiss();

            });
            dialog.show();


        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar Endereços");
        }
    }

    public void setEnderecoID(String id, String endereco) {
        id_endereco = id;
        enderecoBtn.setText(endereco);
    }

    public void setFormaPagamentoID(String id, String forma_pagamento) {
        id_forma_pagamento = id;
        formaPagamentoBtn.setText(forma_pagamento);
    }

    public void listarEnderecos(){
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarEnderecos.php").newBuilder();
            urlBuilder.addQueryParameter("id_cliente", id_cliente);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity.this, "",
                    "Carregando Endereços", true);

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
                                        progressDialog.cancel();
                                        alert("Não há enderecos cadastrados");
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

    void todosEnderecos(){

    }
}