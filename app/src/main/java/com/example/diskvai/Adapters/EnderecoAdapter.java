package com.example.diskvai.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity;
import com.example.diskvai.Activities.InterfaceEmpresa.EmpresaHomeActivity;
import com.example.diskvai.Models.Endereco;
import com.example.diskvai.Models.Pedido;
import com.example.diskvai.R;

import java.util.List;

public class EnderecoAdapter  extends BaseAdapter {

    private Context context;
    private List<Endereco> enderecoLista;

    public EnderecoAdapter(Context context,List enderecoLista) {
        this.context = context;
        this.enderecoLista = enderecoLista;
    }

    @Override
    public int getCount() {
        return enderecoLista != null ? enderecoLista.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return enderecoLista.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view1 = LayoutInflater.from(context).inflate(
                R.layout.adapter_endereco,
                parent,
                false
        );
        LinearLayout enderecoView = view1.findViewById(R.id.enderecoView);
        TextView id = view1.findViewById(R.id.id);
        TextView endereco = view1.findViewById(R.id.endereco);

        enderecoView.setOnClickListener(view -> {
            if(context instanceof ListarProdutosCliActivity){
                ((ListarProdutosCliActivity)context).setEnderecoID(enderecoLista.get(position).getID(), enderecoLista.get(position).getEndereco());
            }
        });
        endereco.setText(enderecoLista.get(position).getEndereco());
        id.setText("ID: " + enderecoLista.get(position).getID());


        return view1;
    }
}
