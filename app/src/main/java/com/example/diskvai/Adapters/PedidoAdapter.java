package com.example.diskvai.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.diskvai.Models.Pedido;
import com.example.diskvai.Models.Produto;
import com.example.diskvai.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PedidoAdapter extends BaseAdapter {

    private Context context;
    private List<Pedido> pedidoLista;

    public PedidoAdapter(Context context,List pedidoLista) {
        this.context = context;
        this.pedidoLista = pedidoLista;
    }

    @Override
    public int getCount() {
        return pedidoLista != null ? pedidoLista.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return pedidoLista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = LayoutInflater.from(context).inflate(
                R.layout.adapter_pedido,
                parent,
                false
        );

        Button status = view1.findViewById(R.id.status);
        Button verProdutos = view1.findViewById(R.id.verProdutos);
        TextView endereco = view1.findViewById(R.id.endereco);
        TextView cliente = view1.findViewById(R.id.cliente);
        TextView subtotal = view1.findViewById(R.id.subtotal);
        TextView formaPagamento = view1.findViewById(R.id.formaPagamento);
        Button aceitarPedido = view1.findViewById(R.id.aceitarPedido);
        Button recusarPedudo = view1.findViewById(R.id.recusarPedido);

        endereco.setText(pedidoLista.get(position).getEndereco());
        cliente.setText("Cliente: " + pedidoLista.get(position).getCliente());
        subtotal.setText("Subtotal: R$ " + pedidoLista.get(position).getValor());
        status.setText(pedidoLista.get(position).getStatus());
        formaPagamento.setText("("+pedidoLista.get(position).getFormaPagamento()+")");

        return view1;
    }
}

