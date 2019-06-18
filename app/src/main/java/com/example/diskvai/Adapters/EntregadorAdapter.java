package com.example.diskvai.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceCadastro.CompletarCadastroFacebookActivity;
import com.example.diskvai.Activities.InterfaceEmpresa.CadastrarEntregadoresEmpActivity;
import com.example.diskvai.Activities.InterfaceEmpresa.EditarEntregadorActivity;
import com.example.diskvai.Activities.InterfaceEmpresa.ListarEntregadorActivity;
import com.example.diskvai.Models.Entregador;
import com.example.diskvai.R;

import java.util.List;

public class EntregadorAdapter extends BaseAdapter {

    private final List<Entregador> entregadores;
    private final Activity act;
    Context context;

    public EntregadorAdapter(List<Entregador> entregadores, Activity act, Context context) {
        this.entregadores = entregadores;
        this.act = act;
        this.context = context;
    }

    @Override
    public int getCount() {
        return entregadores.size();
    }

    @Override
    public Object getItem(int position) {
        return entregadores.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.adapter_entregador, parent, false);
        final Entregador func = entregadores.get(position);

        TextView nome = (TextView) view.findViewById(R.id.nome);
        //TextView descricao = (TextView) view.findViewById(Integer.parseInt("entregador"));
        ImageView imagem = (ImageView) view.findViewById(R.id.foto);

        nome.setText(func.getNome_ent());
        //imagem.setImageResource(func.getFoto());

        view.findViewById(R.id.editar).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(context, EditarEntregadorActivity.class);
                Bundle parameters = new Bundle();
                parameters.putString("Nome", entregadores.get(position).getNome_ent());
                parameters.putString("Email", entregadores.get(position).getEmail());
                parameters.putString("Telefone", entregadores.get(position).getTelefone());
                intent.putExtras(parameters);
                context.startActivity(intent);
            }
        });

        view.findViewById(R.id.excluir).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        return view;
    }

    private void clickEditar(String s, int pos){ // <-- Este método é chamado ao clicar em Editar (abrir todos os dados do entregador)
        Toast.makeText(act, "Clicou em editar " + s, Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, EditarEntregadorActivity.class);

        context.startActivity(intent);
    }

    private void clickExcluir(String s, int pos){ // <-- Este método é chamado ao clicar em Excluir
        Toast.makeText(act, "Clicou em excluir " + s, Toast.LENGTH_SHORT).show();
    }

}