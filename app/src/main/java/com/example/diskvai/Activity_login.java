package com.example.diskvai;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_login extends AppCompatActivity {

    private boolean permissaoArmazenamento = false;
    private String a[]={""};
    private Button btnLogar;
    private EditText pass;
    private EditText user;
    private String resposta="888";

    CallbackManager callbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // inicializar a sdk antes de inflar o Layout
        setContentView(R.layout.activity_login);


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn) {
            // se ja estiver logado pelo face ao entrar no app
        }

        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = findViewById(R.id.login_fb);
        loginButton.setReadPermissions(Arrays.asList("public_profile", "email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        (object, response) -> {
                            Log.v("LoginActivity", response.toString());

                            try {
                                String id = object.getString("id");
                                String nome = object.getString("name");

                                // comparar aqui se a string id esta cadastrada no bd, se não estiver. chama o método
                                // cadastroFb abaixo. Se ja estiver cadastrado ir pra activity do cliente já logado.
                                cadastroFb(id, nome);

                            } catch (JSONException e) {
                                e.printStackTrace();
                                // caso o JSON não tiver id nem nome
                            }
                            try {
                                String email = object.getString("email");
                            } catch (JSONException e) {
                                e.printStackTrace();
                                // se não tiver email no JSON
                            }

                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                // se cancelar o login pelo facebook
            }

            @Override
            public void onError(FacebookException error) {
                // se der erro o login do facebook
            }
        });

        btnLogar= (Button) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.senha);

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissaoArmazenamento = false;
            Log.e("Permissão", "Sem permissão");
            Toast.makeText(this,"sem permissao de internet",Toast.LENGTH_SHORT).show();
        }

        btnLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(user.getText().toString().equals("")) {
                    user.setError("Digite um Login Válido");
                }
                if(pass.getText().toString().equals("")) {
                    pass.setError("Digite uma Senha Válida");
                }
                if(user.getError()==null&&pass.getError()==null) {
                    btnLogar.setEnabled(false);
                    try {
                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                        StrictMode.setThreadPolicy(policy);

                        OkHttpClient client = new OkHttpClient();

                        HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/readLogin.php").newBuilder();
                        urlBuilder.addQueryParameter("usuario", user.getText().toString());
                        urlBuilder.addQueryParameter("senha", pass.getText().toString());

                        String url = urlBuilder.build().toString();

                        Request request = new Request.Builder().url(url).build();

                        client.newCall(request).enqueue(new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                alert("deu lenha");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            resposta = (response.body().string());
                                            a = resposta.split("#");
                                            login(a);
                                            //int tam = a.length;
                                            //alert(a[tam-1]);
                                            //btnLogar.setEnabled(true);
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
            }
        });
    }

    private void alert(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private void login(String resposta[]){
        if(resposta[1].equals("sucesso")){
            Intent intent;
            switch (Integer.parseInt(resposta[2])){
                case 1:
                    intent = new Intent(this, PrincipalCli.class);
                    startActivity(intent);
                break;
                case 2:
                    intent = new Intent(this, PrincipalEmp.class);
                    startActivity(intent);
                break;
                case 3:
                    intent = new Intent(this, PrincipalEntr.class);
                    startActivity(intent);
                break;
            }
        }
        else {
            alert("Nome Usuario ou Senha incorretos");
        }
        btnLogar.setEnabled(true);
    }

    public void Cadastro(View view) {
        Intent intent = new Intent(this, TipoCadastro.class);
        startActivity(intent);
    }

    public void cadastroFb(String id, String nome) {
        // passa os parametros para a activity de completar o cadastro com telefone
        // para se inserido no bd
        Bundle parameters = new Bundle();
        parameters.putString("id", id);
        parameters.putString("nome", nome);
        Intent intent;
        intent = new Intent(this, Activity_completar_cadastro_fb.class);
        intent.putExtras(parameters);
        startActivity(intent);
    }

    public void printKeyHash() {
        // método para pegar a HashKey para inserir no facebook developer para a sdk funcionar
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.diskvai", PackageManager.GET_SIGNATURES);
            for(Signature signature:info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}