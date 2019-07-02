package com.example.diskvai.Activities.InterfaceEmpresa;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.diskvai.Adapters.CategoriaAdapter;
import com.example.diskvai.R;
import com.pchmn.materialchips.ChipsInput;
import com.pchmn.materialchips.model.Chip;
import com.pchmn.materialchips.model.ChipInterface;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class EditarProdutoActivity extends AppCompatActivity {

    private int PICK_IMAGE_REQUEST = 1;

    private EditText nome, descricao, preco;

    ProgressDialog progressDialog;
    ProgressDialog cadastrandoProdutoProgress;
    CategoriaAdapter categoriaAdapter;
    ChipsInput chipsInput;

    ArrayList<Chip> list;


    private String resposta;
    private String a[]={"#"};

    private String id_produto, nome_produto, descricao_produto, url_foto;
    private Double preco_produto;

    private CircleImageView imageView;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_produto);

        read();
        nomeValido();
        descricaoValida();
        precoValido();
        resgatarCategorias();

        Intent intent = this.getIntent();
        id_produto = intent.getStringExtra("id");
        nome_produto = intent.getStringExtra("nome");
        descricao_produto = intent.getStringExtra("descricao");
        url_foto = intent.getStringExtra("url_foto");
        preco_produto = intent.getDoubleExtra("preco", 0.0);

        nome.setText(nome_produto);
        descricao.setText(descricao_produto);
        preco.setText(""+preco_produto);

        if(url_foto==null||url_foto.equals("")) {
            Picasso.get().load(R.mipmap.perfil_empresa).into(imageView);
        } else {
            Picasso.get()
                    .load(url_foto).memoryPolicy(MemoryPolicy.NO_CACHE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .placeholder(R.mipmap.box)
                    .error(R.mipmap.box)
                    .into(imageView);
        }

    }

    public void read() {
        nome = findViewById(R.id.nome);
        descricao = findViewById(R.id.descricao);
        preco = findViewById(R.id.preco);
        imageView = findViewById(R.id.imgProduto);
        chipsInput = findViewById(R.id.chips_input);
    }

    public void editarProduto(View view) {

        // obter as categorias selecionadas

        if (validaEdicao()) {
            try {

                cadastrandoProdutoProgress = ProgressDialog.show(EditarProdutoActivity.this, "",
                        "Fazendo alteraçoes no Produto", true);

                ArrayList<String> categoriasSelecionadas = new ArrayList<>();
                for(int i = 0; i < chipsInput.getSelectedChipList().size(); i++) {
                    categoriasSelecionadas.add(chipsInput.getSelectedChipList().get(i).getLabel());
                }
                JSONArray jsonArray = new JSONArray(categoriasSelecionadas);


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                String uploadImage = getStringImage(bitmap);

                final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");


                RequestBody multiPartBody = new MultipartBody.Builder()
                        .setType(MultipartBody.FORM)
                        .addFormDataPart("foto", uploadImage)
                        .addFormDataPart("nome_produto", nome.getText().toString())
                        .addFormDataPart("id_produto", id_produto)
                        .addFormDataPart("desc_produto", descricao.getText().toString())
                        .addFormDataPart("preco", preco.getText().toString())
                        .addFormDataPart("categorias", jsonArray.toString())
                        .build();
                Request request = new Request.Builder()
                        .url("http://gabriellacastro.com.br/disk_vai/editarProduto.php")
                        .post(multiPartBody)
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                try {
                                    resposta = (response.body().string());
                                    a=resposta.split("#");

                                    if(a[1].split("'")[0].equals("Duplicate entry ")){
                                        alert("um erro ocorreu");
                                        cadastrandoProdutoProgress.cancel();
                                        cadastrandoProdutoProgress = null;
                                    }
                                    else {
                                        alert(a[1]);
                                        cadastrandoProdutoProgress.cancel();
                                        cadastrandoProdutoProgress = null;
                                        setResult(RESULT_OK);
                                        finish();
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                    cadastrandoProdutoProgress.cancel();
                                    cadastrandoProdutoProgress = null;
                                }
                            }
                        });

                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Boolean validaEdicao() {
        Boolean flag = false;
        if(nome.getError()==null&&descricao.getError()==null&&preco.getError()==null) {
            flag = true;
        }


        return flag;
    }

    public void nomeValido() {
        nome.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(nome.getText().toString().length()>=2) {
                    nome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nome.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    nome.setError(null);
                } else {
                    nome.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    nome.setError("Insira um nome válido com dois ou mais caracteres");
                }
            }
        });
    }
    public void descricaoValida() {
        descricao.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(descricao.getText().toString().length()>=10) {
                    descricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    descricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_icons8_checkmark, 0);
                    descricao.setError(null);
                } else {
                    descricao.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    descricao.setError("Coloque mais detalhes sobre seu produto");
                }
            }
        });
    }
    public void precoValido() {
        preco.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus) {
                if(!preco.getText().toString().equals("")&&Double.parseDouble(preco.getText().toString())!=0) {
                    Double preco_valor = Double.parseDouble(preco.getText().toString());
                    preco.setText("" + preco_valor);
                    preco.setError(null);
                } else {
                    preco.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    preco.setError("Digite um valor válido e maior que R$ 00,00");
                }
            }
        });

    }

    public void back(View view) {
        this.finish();
    }

    public void showFileChooser(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                //bitmap = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), filePath), 512, 512, true);
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void resgatarCategorias() {
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);

            OkHttpClient client = new OkHttpClient();

            HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/listarCategorias.php").newBuilder();
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder().url(url).build();

            progressDialog = ProgressDialog.show(EditarProdutoActivity.this, "",
                    "Carregando Categorias", true);

            client.newCall(request).enqueue(new Callback() {

                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                //alert(response.body().string());
                                try {
                                    String data = response.body().string();
                                    JSONArray jsonArray = new JSONArray(data);
                                    if(jsonArray.length()!=0){
                                        //jsonObject = jsonArray.getJSONObject(0);

                                        listar(jsonArray);
                                    } else {
                                        progressDialog.cancel();
                                        progressDialog = null;
                                        alert("Não há produtos cadastrados");
                                    }
                                } catch (JSONException e) {
                                    alert("erro no json");
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void listar(JSONArray jsonArray) {
        list = new ArrayList<>();

        try {

            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonChildNode = (JSONObject) jsonArray.getJSONObject(i);
                String id = jsonChildNode.optString("ID");
                String nome = jsonChildNode.optString("Nome_cat");

                Chip chip = new Chip(nome, "");
                list.add(chip);


            }
            chipsInput.setFilterableList(list);
            chipsInput.addChipsListener(new ChipsInput.ChipsListener() {
                @Override
                public void onChipAdded(ChipInterface chip, int newSize) {

                }

                @Override
                public void onChipRemoved(ChipInterface chip, int newSize) {

                }

                @Override
                public void onTextChanged(CharSequence text) {
                    if(text.toString().equals(",")) {
                        chipsInput.getEditText().setText("");
                    }
                    if(text.toString().contains(",")&&!text.toString().equals(",")) {
                        String chiplabel[] = text.toString().split(",");
                        Chip chip = new Chip(chiplabel[0], "");
                        chipsInput.addChip(chip);
                    }
                }
            });

            progressDialog.cancel();
            progressDialog = null;

        } catch (JSONException e) {
            Toast.makeText(getApplicationContext(), "Error" + e.toString(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
            alert("Falha ao Carregar Categorias");
        }

    }

    private void alert(String valor) {
        Toast.makeText(this, valor, Toast.LENGTH_SHORT).show();
    }
}
