package com.example.diskvai;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Activity_completar_cadastro_fb extends AppCompatActivity {

    private Button btnCadastrar, btnvoltar;
    private EditText telefone;

    private String resposta;
    private String a[]={"#"};

    private void read() {
        btnCadastrar = findViewById(R.id.cadastrar);
        telefone = (EditText) findViewById(R.id.telefone);
        telefone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completar_cadastro_fb);
        Intent intent = this.getIntent();
        String id = intent.getStringExtra("id");
        String nome = intent.getStringExtra("nome");
        read();

        // cadastrar no bd: id, nome e telefone
        // ir para activity principal do cliente

    }

    public void back(View view) {
        this.finish();
    }
}
