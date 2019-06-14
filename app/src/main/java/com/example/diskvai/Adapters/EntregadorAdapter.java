package com.example.diskvai.Adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Models.Entregador;
import com.example.diskvai.R;

import java.util.List;

public class EntregadorAdapter extends BaseAdapter {

    private final List<Entregador> entregadores;
    private final Activity act;

    public EntregadorAdapter(List<Entregador> entregadores, Activity act) {
        this.entregadores = entregadores;
        this.act = act;
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
                clickEditar(entregadores.get(position).getNome_ent());
            }
        });

        view.findViewById(R.id.excluir).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                clickExcluir(entregadores.get(position).getNome_ent());
            }
        });

        return view;
    }

    private void clickEditar(String s){ // <-- Este método é chamado ao clicar em Editar (abrir todos os dados do entregador)
        Toast.makeText(act, "Clicou em editar " + s, Toast.LENGTH_SHORT).show();
    }

    private void clickExcluir(String s){ // <-- Este método é chamado ao clicar em Excluir
        Toast.makeText(act, "Clicou em excluir " + s, Toast.LENGTH_SHORT).show();
    }
}
