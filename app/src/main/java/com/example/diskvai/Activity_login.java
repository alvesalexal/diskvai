package com.example.diskvai;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Activity_login extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private boolean permissaoInternet = false;

    private Button btnLogar;
    private SignInButton signInButton; // botão google
    private LoginButton loginButton; // botão face
    private EditText pass;
    private EditText user;
    private JSONObject jsonObject;

    private static final String TAG = "SignInActivity";
    private static final int RC_SIGN_IN = 9001;
    GoogleApiClient mGoogleApiClient;

    CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext()); // inicializar a sdk antes de inflar o Layout
        setContentView(R.layout.activity_login);
        btnLogar= (Button) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.usuario);
        pass = (EditText) findViewById(R.id.senha);
        //------------login google

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,  this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();
        signInButton = (SignInButton) findViewById(R.id.signInButton);



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });



        //---------- login facebook
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(isLoggedIn) {
            // se ja estiver logado pelo face ao entrar no app
        }

        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_fb);
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

                                try {
                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);

                                    OkHttpClient client = new OkHttpClient();

                                    HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/readLoginFb.php").newBuilder();
                                    urlBuilder.addQueryParameter("senha", id);
                                    urlBuilder.addQueryParameter("nome", nome);

                                    String url = urlBuilder.build().toString();

                                    Request request2 = new Request.Builder().url(url).build();

                                    client.newCall(request2).enqueue(new Callback() {
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
                                                        //alert(response.body().string());/*
                                                        try {
                                                            String data = response.body().string();
                                                            JSONArray jsonArray = new JSONArray(data);
                                                            if(jsonArray.length()!=0){
                                                                jsonObject = jsonArray.getJSONObject(0);
                                                                login(jsonObject);
                                                            }
                                                            else {
                                                                alert(String.valueOf(jsonArray.length()));
                                                                cadastroFb(id, nome);
                                                            }
                                                        } catch (JSONException e) {
                                                            alert("erro no json");
                                                            Log.e("ERRO",e.getMessage());
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

                                // comparar aqui se a string id esta cadastrada no bd, se não estiver. chama o método
                                // cadastroFb abaixo. Se ja estiver cadastrado ir pra activity do cliente já logado.


                            } catch (JSONException e) {
                                e.printStackTrace();
                                // caso o JSON não tiver id nem nome
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

        if(ActivityCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET)
                != PackageManager.PERMISSION_GRANTED) {
            permissaoInternet = false;
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
                                            //alert(response.body().string());
                                            try {
                                                String data = response.body().string();
                                                JSONArray jsonArray = new JSONArray(data);
                                                if(jsonArray.length()!=0){
                                                    jsonObject = jsonArray.getJSONObject(0);
                                                    login(jsonObject);
                                                }
                                                else alert("nome de usuario ou senha incorretos");
                                            } catch (JSONException e) {
                                                alert("erro no json");
                                                btnLogar.setEnabled(true);
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
            }
        });
    }

    private void alert(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    private void login(JSONObject jsonObjects) throws JSONException {
        Intent intent;
        switch (Integer.parseInt(jsonObjects.getString("tipo_cli"))){
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

//------------------------------login google

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent,RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleSignInResult(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) throws JSONException {
        Log.d(TAG,"handleSignInResult:"+result.isSuccess());
        if(result.isSuccess()){
            GoogleSignInAccount acct = result.getSignInAccount();
            String id=acct.getId();
            String nome=acct.getDisplayName();
            String email=acct.getEmail();

            try {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);

                OkHttpClient client = new OkHttpClient();

                HttpUrl.Builder urlBuilder = HttpUrl.parse("http://gabriellacastro.com.br/disk_vai/readLoginGoogle.php").newBuilder();
                urlBuilder.addQueryParameter("senha", id);
                urlBuilder.addQueryParameter("nome", nome);
                urlBuilder.addQueryParameter("email", email);

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
                                    //alert(response.body().string());/*
                                    try {
                                        String data = response.body().string();
                                        JSONArray jsonArray = new JSONArray(data);
                                        if(jsonArray.length()!=0){
                                            jsonObject = jsonArray.getJSONObject(0);
                                            login(jsonObject);
                                        }
                                        else {
                                            alert(String.valueOf(jsonArray.length()));
                                            completarGoogle(id,nome, email);
                                        }
                                    } catch (JSONException e) {
                                        alert("erro no json");
                                        Log.e("ERRO",e.getMessage());
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
    }

    void completarGoogle(String id, String nome, String email){
        Intent intent;
        intent = new Intent(this, Activity_completar_cadastro_google.class);
        Bundle parameters = new Bundle();

        parameters.putString("id", id);
        parameters.putString("nome", nome);
        parameters.putString("email", email);

        intent.putExtras(parameters);
        startActivity(intent);
    }

    public void LoginGoogle(View view) {
        switch (view.getId()){
            case R.id.signInButton:
                signIn();
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG,"onConnectionFailed:"+connectionResult);
    }

    public void fbButton(View v) {
        loginButton.performClick();
    }

    public void googleButton(View v) {
        signIn();
    }
}