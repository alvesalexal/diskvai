package com.example.diskvai.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity;
import com.example.diskvai.Models.Produto;
import com.example.diskvai.R;

import java.util.List;

public class ProdutoCarrinhoAdapter extends BaseAdapter {

    private Context context;
    private List<Produto> produtoLista;
    Activity act;
    public ProdutoCarrinhoAdapter(Context context,Activity act, List produtoLista) {
        this.act=act;
        this.context=context;
        this.produtoLista=produtoLista;
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = act.getLayoutInflater().inflate(R.layout.adapter_produto_carrinho, parent, false);
        final Produto prod = produtoLista.get(position);

        TextView nome = (TextView) view.findViewById(R.id.nome);
        TextView preco = (TextView) view.findViewById(R.id.preco);
        ImageView imagem = (ImageView) view.findViewById(R.id.foto);
        EditText quantidade = view.findViewById(R.id.quantidade);
        nome.setText(prod.getNome());
        preco.setText(String.valueOf(quantidade.getText()));

        view.findViewById(R.id.quantidade).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    preco.setText(String.valueOf (prod.getPreco()*Integer.parseInt(String.valueOf(quantidade.getText()))));
                }
            }
        });

        view.findViewById(R.id.excluir).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            }
        });

        return view;
    }



    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

}
