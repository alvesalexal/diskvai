package com.example.diskvai.Adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.diskvai.Activities.InterfaceEmpresa.EmpresaHomeActivity;
import com.example.diskvai.Models.Pedido;
import com.example.diskvai.Models.Produto;
import com.example.diskvai.R;

import java.util.ArrayList;
import java.util.List;

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
        Button recusarPedido = view1.findViewById(R.id.recusarPedido);

        endereco.setText(pedidoLista.get(position).getEndereco());
        cliente.setText("Cliente: " + pedidoLista.get(position).getCliente());
        subtotal.setText("Subtotal: R$ " + pedidoLista.get(position).getValor());
        status.setText(pedidoLista.get(position).getStatus());
        formaPagamento.setText("("+pedidoLista.get(position).getFormaPagamento()+")");


        verProdutos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(context instanceof EmpresaHomeActivity){
                    ((EmpresaHomeActivity)context).mostrarProdutosPedido(pedidoLista.get(position).getId());
                }
            }
        });

        int color = Color.rgb(33, 150, 243);
        switch (pedidoLista.get(position).getStatus()) {
            case "Pendente":
                color = Color.rgb(33, 150, 243);
                aceitarPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(context instanceof EmpresaHomeActivity){
                            ((EmpresaHomeActivity)context).alterarStatusPedido(pedidoLista.get(position).getId(), "Aguardando entregador");
                        }


                    }
                });
                recusarPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(context instanceof EmpresaHomeActivity){
                            ((EmpresaHomeActivity)context).alterarStatusPedido(pedidoLista.get(position).getId(), "Cancelado");
                        }


                    }
                });
                break;
            case "Aguardando entregador":
                color = Color.rgb(255, 193, 7);
                aceitarPedido.setVisibility(View.GONE);
                recusarPedido.setText("Cancelar");
                recusarPedido.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(context instanceof EmpresaHomeActivity){
                            ((EmpresaHomeActivity)context).alterarStatusPedido(pedidoLista.get(position).getId(), "Cancelado");
                        }


                    }
                });
                break;
            case "Cancelado":
                color = Color.RED;
                aceitarPedido.setVisibility(View.GONE);
                recusarPedido.setVisibility(View.GONE);
                break;
            case "A caminho":
                color = Color.rgb(16, 209, 25);
                aceitarPedido.setVisibility(View.GONE);
                recusarPedido.setVisibility(View.GONE);
                break;

        }

        Drawable[] drawables = status.getCompoundDrawables();
        if (drawables[2] != null) {  // left drawable
            drawables[2].setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }



        return view1;
    }
}

