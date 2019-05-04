package com.example.diskvai;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class telaTeste extends AppCompatActivity {

    TextView texto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_teste);
        texto = findViewById(R.id.textView);
        texto.setText("oi!\nParabens!\nDeu tudo certo!!");
    }
}
