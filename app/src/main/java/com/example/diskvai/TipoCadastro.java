package com.example.diskvai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class TipoCadastro extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipo_cadastro);
    }

    public void CadastroCli(View view) {
        Intent intent = new Intent(this, CadastroCliente.class);
        startActivity(intent);
    }

    public void back(View view) {
        this.finish();
    }

    public void CadastroEmp(View view) {
        Intent intent = new Intent(this, CadastroEmpresa.class);
        startActivity(intent);
    }

    public void CadastroEntr(View view) {
        Intent intent = new Intent(this, CadastroEntregador.class);
        startActivity(intent);
    }
}
