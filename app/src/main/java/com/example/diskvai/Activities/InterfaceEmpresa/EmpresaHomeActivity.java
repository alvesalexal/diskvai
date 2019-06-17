package com.example.diskvai.Activities.InterfaceEmpresa;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.R;
import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;

public class EmpresaHomeActivity extends AppCompatActivity {

    ExpandableLinearLayout menuLateral, infoPerfil;
    Button editarPerfil, pedidos, listarProduto, listarEntregadores, logout;
    ImageView imgPerfil;
    TextView nome_empresa;
    String id_empresa, empresa_nome, login_empresa, email_empresa, telefone_empresa;

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
        alert("empresa de id:" + id_empresa);

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

            }
        });
    }

    public void read() {
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
        startActivity(intent);
    }
}
