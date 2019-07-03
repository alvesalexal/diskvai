package com.example.diskvai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.diskvai.Activities.InterfaceCliente.ListarEnderecosActivity;
import com.example.diskvai.Activities.InterfaceCliente.ListarProdutosCliActivity;
import com.example.diskvai.Models.Endereco;
import com.example.diskvai.R;

import java.util.List;

public class EnderecoAdapter_List extends BaseAdapter {

    private Context context;
    private List<Endereco> enderecoLista;

    public EnderecoAdapter_List(Context context, List enderecoLista) {
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
                R.layout.adapter_endereco_list,
                parent,
                false
        );
        LinearLayout enderecoView = view1.findViewById(R.id.enderecoView);
        TextView id = view1.findViewById(R.id.id);
        TextView endereco = view1.findViewById(R.id.endereco);

        enderecoView.setOnClickListener(view -> {
            if(context instanceof ListarProdutosCliActivity){
                //((ListarProdutosCliActivity)context).setEnderecoID(enderecoLista.get(position).getID(), enderecoLista.get(position).getEndereco());
            }
        });
        endereco.setText(enderecoLista.get(position).getEndereco());
        id.setText("ID: " + enderecoLista.get(position).getID());

        view1.findViewById(R.id.excluir).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof ListarEnderecosActivity){
                    ((ListarEnderecosActivity)context).excluirEndereco(String.valueOf(enderecoLista.get(position).getID()));
                }
            }
        });


        view1.findViewById(R.id.editar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof ListarEnderecosActivity){
                    ((ListarEnderecosActivity)context).editarEndereco(enderecoLista.get(position));
                }
            }
        });

        return view1;
    }

    private void alert(String string) {
        Toast.makeText(context,string,Toast.LENGTH_SHORT).show();
    }


}
