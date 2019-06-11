package com.example.diskvai;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProdutoSimplesAdapter extends AppCompatActivity {

    private Context context;
    private Produto produto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produto_simples_adapter);
    }

    public ProdutoSimplesAdapter(Context context){
        this.context = context;
    }
}
