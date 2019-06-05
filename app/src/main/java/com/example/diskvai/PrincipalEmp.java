package com.example.diskvai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class PrincipalEmp extends AppCompatActivity {

    ExpandableLinearLayout menuLateral, infoPerfil;
    Button editarPerfil, pedidos, cadastrarProduto, listarEntregadores, logout;
    ImageView imgPerfil;
    TextView nome_empresa;
    String id_empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_emp);
        Intent intent = this.getIntent();
        id_empresa = intent.getStringExtra("ID");
        alert("empresa de id:" + id_empresa);

        read();
        menu();



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
            }

            @Override
            public void onPreClose() {
                editarPerfil.setText("");
                pedidos.setText("");
                cadastrarProduto.setText("");
                listarEntregadores.setText("");
                logout.setText("");
                infoPerfil.collapse();
            }

            @Override
            public void onOpened() {
                editarPerfil.setText("Editar Perfil");
                pedidos.setText("Pedidos");
                cadastrarProduto.setText("Cadastrar Produto");
                listarEntregadores.setText("Cadastrar Entregador");
                logout.setText("Logout");
            }

            @Override
            public void onClosed() {

            }
        });
    }

    public void read() {
        infoPerfil = findViewById(R.id.infoPerfil);
        editarPerfil = findViewById(R.id.editarPerfil);
        pedidos = findViewById(R.id.pedidos);
        cadastrarProduto = findViewById(R.id.cadastrarProduto);
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

    public void cadastrarProdutos(View view) {
        Intent intent;
        Bundle parameters = new Bundle();
        parameters.putString("ID", id_empresa);
        intent = new Intent(this, CadastrarProdutoActivity.class);
        intent.putExtras(parameters);
        startActivity(intent);
    }

    public void listarEntregadores(View view) {
        Intent intent;
        Bundle parameters = new Bundle();
        parameters.putString("ID", id_empresa);
        intent = new Intent(this, ListarEntregador.class);
        intent.putExtras(parameters);
        startActivity(intent);
    }
}
