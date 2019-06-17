package com.example.diskvai.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.diskvai.Models.Empresa;
import com.example.diskvai.R;

import java.util.List;

public class EmpresaAdapter  extends BaseAdapter {

        private Context context;
        private List<Empresa> empresaList;

        public EmpresaAdapter(Context context,List empresaList) {
            this.context = context;
            this.empresaList = empresaList;
        }

        @Override
        public int getCount() {
            return empresaList != null ? empresaList.size() : 0;
        }

        @Override
        public Object getItem(int position) {
            return empresaList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = LayoutInflater.from(context).inflate(
                    R.layout.adapter_empresa,
                    parent,
                    false
            );

            TextView id = view1.findViewById(R.id.id);
            TextView nome = view1.findViewById(R.id.nome);
            TextView telefone = view1.findViewById(R.id.telefone);
            TextView preco = view1.findViewById(R.id.vlrFrete);

            id.setText("ID: " + empresaList.get(position).getId());
            nome.setText("Nome: " +empresaList.get(position).getNome());
            telefone.setText("Telefone: " + empresaList.get(position).getDescricao());
            preco.setText("Valor Frete: R$ " + empresaList.get(position).getVlrFrete());

            return view1;
        }

}
