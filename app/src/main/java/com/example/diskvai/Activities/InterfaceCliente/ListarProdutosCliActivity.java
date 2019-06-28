package com.example.diskvai.Activities.InterfaceCliente;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Adapters.ProdutoCarrinhoAdapter;
import com.example.diskvai.Adapters.ProdutoCliAdapter;
import com.example.diskvai.Models.Produto;
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

public class ListarProdutosCliActivity extends AppCompatActivity {

    String id_empresa,id_cliente,nome_vendedor;
    private JSONObject jsonObject;
    List<Produto> produtoLista, produtoListaCar;
    ProgressDialog progressDialog;
    TextView nomeVendedor;
    ListView listView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_produtos_cli);

        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID_Empresa");
        //id_cliente = intent.getStringExtra("ID_Clie");
        nome_vendedor = intent.getStringExtra("Nome_Vend");

        nomeVendedor = findViewById(R.id.nome_Vendedor);
        nomeVendedor.setText(nome_vendedor);
        resgatarProdutos();

        produtoListaCar = new ArrayList<Produto>();
        listView2 = findViewById(R.id.listviewPedido);
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

    public void enviarPedido(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Enviar Pedido?")
                .setIcon(R.drawable.ic_icons8_trash)
                .setMessage("Digite sua senha")
                .setPositiveButton(android.R.string.yes, (dialog, whichButton) -> {

                    OkHttpClient client = new OkHttpClient();
                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/excluirCadastro.php").newBuilder();
                    urlBuilder.addQueryParameter("ID", id_empresa);
                    urlBuilder.addQueryParameter("tabela", "Vendedor");

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
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                        }
                    });
                })
                .setNegativeButton(android.R.string.no, null)
                .setView(view).show();
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
        TextView textView = findViewById(R.id.quantidadeItens);
        if(!produtoListaCar.isEmpty()){
            String s = String.valueOf(produtoListaCar.size());
            if(produtoListaCar.size()==1) s+=" Item no carrinho";
            else s+=" Itens no carrinho";
            textView.setText(s);
        }
    }

    private void atualizarListCarrinho(){
        ListView listView = findViewById(R.id.listviewPedido);
        listView.setAdapter(new ProdutoCarrinhoAdapter(this,this, produtoListaCar));
    }
}