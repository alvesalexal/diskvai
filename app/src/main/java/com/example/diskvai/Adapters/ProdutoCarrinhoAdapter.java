package com.example.diskvai.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity;
import com.example.diskvai.Models.Produto;
import com.example.diskvai.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProdutoCarrinhoAdapter extends BaseAdapter {

    private Context context;
    private List<Produto> produtoLista;
    Activity act;
    public ProdutoCarrinhoAdapter(Context context,Activity act, List produtoLista) {
        this.act=act;
        this.context=context;
        this.produtoLista=produtoLista;
    }

    @Override
    public int getCount() {
        return produtoLista != null ? produtoLista.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return produtoLista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = LayoutInflater.from(context).inflate(
                R.layout.adapter_produto_carrinho,
                parent,
                false
        );

        TextView id = view1.findViewById(R.id.id);
        TextView nome = view1.findViewById(R.id.nome);
        TextView descricao = view1.findViewById(R.id.descricao);
        TextView preco = view1.findViewById(R.id.preco);
        ImageButton adicionar = view1.findViewById(R.id.editar);
        ImageButton excluir = view1.findViewById(R.id.excluir);
        CircleImageView imagem = view1.findViewById(R.id.imgProduto);

        id.setText("ID: " + produtoLista.get(position).getId());
        nome.setText(produtoLista.get(position).getNome());
        descricao.setText("Descrição: " + produtoLista.get(position).getDescricao());
        preco.setText("Preço: R$ " + produtoLista.get(position).getPreco());
        if(produtoLista.get(position).getUrl_imagem()==null||produtoLista.get(position).getUrl_imagem().equals("")) {
            Picasso.get().load(R.mipmap.perfil_empresa).into(imagem);
        } else {
            Picasso.get()
                    .load(produtoLista.get(position).getUrl_imagem())
                    .placeholder(R.mipmap.box)
                    .error(R.mipmap.box)
                    .into(imagem);
        }

        view1.findViewById(R.id.excluir).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(context instanceof ListarProdutosCliActivity){
                    ((ListarProdutosCliActivity)context).removerProdutoCarrinho(position,produtoLista.get(position));
                }
            }
        });
        return view1;
    }
    void alert(String a){
        Toast.makeText(context,a,Toast.LENGTH_SHORT).show();
    }
}
