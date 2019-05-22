package com.example.diskvai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.aakira.expandablelayout.ExpandableLayoutListener;
import com.github.aakira.expandablelayout.ExpandableLinearLayout;
import com.github.aakira.expandablelayout.ExpandableRelativeLayout;

public class PrincipalEmp extends AppCompatActivity {

    ExpandableLinearLayout menuLateral, infoPerfil;
    Button editarPerfil, pedidos, cadastrarProduto, cadastrarEntregador, logout;
    ImageView imgPerfil;
    TextView nome_empresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal_emp);

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
                cadastrarEntregador.setText("");
                logout.setText("");
                infoPerfil.collapse();
            }

            @Override
            public void onOpened() {
                editarPerfil.setText("Editar Perfil");
                pedidos.setText("Pedidos");
                cadastrarProduto.setText("Cadastrar Produto");
                cadastrarEntregador.setText("Cadastrar Entregador");
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
        cadastrarEntregador = findViewById(R.id.cadastrarEntregador);
        imgPerfil = findViewById(R.id.imgPerfil);
        nome_empresa = findViewById(R.id.nomeEmpresa);
        menuLateral = findViewById(R.id.menuLateral);
        menuLateral.setClosePosition(100);
        logout = findViewById(R.id.logout);
    }

    public void fechar(View view) {
        menuLateral.toggle();
    }
}
