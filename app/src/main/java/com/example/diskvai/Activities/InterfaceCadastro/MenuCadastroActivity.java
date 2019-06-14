package com.example.diskvai.Activities.InterfaceCadastro;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.diskvai.R;

public class MenuCadastroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_cadastro);
    }

    public void CadastroCli(View view) {
        Intent intent = new Intent(this, CadastroClienteActivity.class);
        startActivity(intent);
    }

    public void CadastroEmp(View view) {
        Intent intent = new Intent(this, CadastroEmpresaActivity.class);
        startActivity(intent);
    }

    public void CadastroEntr(View view) {
        Intent intent = new Intent(this, CadastroEntregadorActivity.class);
        startActivity(intent);
    }

    //voltar ao menu anterior
    public void back(View view) {
        this.finish();
    }
}
