package com.example.diskvai;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/*
<?php
 include('db_valores.php');

 $conexao = mysqli_connect($servidor, $usuario, $senha, $bdados);

 $ID_empresa=$_GET['id_empresa'];

 $query = "select ID, Nome_prod, Descricao, Preco, Foto from Produto where fk_Vendedor_ID = '".$ID_empresa."'";


 $result = mysqli_query($conexao,$query);

 $dados = array();

 while($linha = mysqli_fetch_array($result)){
 $linha = array_map('utf8_encode', $linha);
 array_push($dados, $linha);
 }



 echo json_encode($dados);


 mysqli_close($conexao);

 ?>*/

public class ListarEntregador extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listarEntregador);

        List<Entregador> entregadores = todosOsEntregadores();

        ListView listaDeEntregadores = (ListView) findViewById(R.id.listView);

        AdapterEntregador adapterEntregador = new AdapterEntregador(entregadores, this);
        listaDeEntregadores.setAdapter(adapterEntregador);
    }

    private List<Entregador> todosOsEntregadores() { // Mudar aqui. Os dados virão do BD!
        ArrayList<Entregador> array = new ArrayList<>(Arrays.asList(
                new Entregador("Silvia",R.drawable.icon,1,"ana","a","sdfsf","123435","a","svfd")
        ));

        return array;
    }
}
