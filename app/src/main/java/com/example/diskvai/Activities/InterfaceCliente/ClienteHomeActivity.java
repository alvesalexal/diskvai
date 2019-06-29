package com.example.diskvai.Activities.InterfaceCliente;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceEmpresa.ListarProdutosActivity;
import com.example.diskvai.Adapters.EmpresaAdapter;
import com.example.diskvai.Models.Empresa;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.google.android.gms.common.internal.safeparcel.SafeParcelable.NULL;

public class ClienteHomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener  {

    String id_cliente,id_empresa;
    private JSONObject jsonObject;
    List<Empresa> empresaLista;
    ProgressDialog progressDialog;
    private Empresa empresa;
    private ListView listView;

    ExpandableLinearLayout menuLateral;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_cli);

        listView = findViewById(R.id.listview);
        listView.setAdapter(new EmpresaAdapter(this, empresaLista));

        Intent intent = this.getIntent();
        id_cliente = intent.getStringExtra("ID_Cliente");
        id_empresa = intent.getStringExtra("ID_Empresa");
        menuLateral = findViewById(R.id.menuLateral);
        menuLateral.setClosePosition(100);

        listView.setOnItemClickListener(this);
        resgatarProdutos();
        SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resgatarProdutos(); // your code
                pullToRefresh.setRefreshing(false);
            }
        });

        menu();
    }

    private void resgatarProdutos() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarVendedores.php").newBuilder();
            //urlBuilder.addQueryParameter("id_empresa", id_empresa);

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(ClienteHomeActivity.this, "",
                    "Carregando Vendedores", true);

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
                                        alert("Não há Empresas cadastradas");
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
        empresaLista = new ArrayList<Empresa>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String id = jsonChildNode.optString("ID");
                String nome = jsonChildNode.optString("Nome_vend");
                String telefone = jsonChildNode.optString("Telefone");
                Double vlrFrete = jsonChildNode.optDouble("Valor_Frete");
                String url_img = jsonChildNode.optString("Foto");

                Empresa empresa = new Empresa(id, nome, telefone, vlrFrete, url_img);
                empresaLista.add(empresa);
            }

            ListView listView = findViewById(R.id.listview);
            listView.setAdapter(new EmpresaAdapter(this, empresaLista));
            progressDialog.dismiss();

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar Vendedores");
        }
    }

    public void onItemClick(AdapterView<?> parent, View view, int i, long id) {
        empresa = empresaLista.get(i);
        Intent intent = new Intent(this, ListarProdutosCliActivity.class);
        intent.putExtra("ID_Empresa",empresa.getId());
        intent.putExtra("Nome_Vend",empresa.getNome());

        alert(empresa.getId()+"-"+empresa.getNome());

        startActivity(intent);
    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }

    public void back(View view) {
        this.finish();
    }

    private void menu() {
        menuLateral.setListener(new ExpandableLayoutListener() {
            @Override
            public void onAnimationStart() {

            }

            @Override
            public void onAnimationEnd() {

            }

            @Override
            public void onPreOpen() {
                listView.setVisibility(View.GONE);
            }

            @Override
            public void onPreClose() {

            }

            @Override
            public void onOpened() {

            }

            @Override
            public void onClosed() {
                listView.setVisibility(View.VISIBLE);
            }
        });
    }

    public void fechar(View view) {
        menuLateral.toggle();
    }


}
