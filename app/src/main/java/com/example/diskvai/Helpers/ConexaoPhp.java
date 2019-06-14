package com.example.diskvai.Helpers;


import android.app.Activity;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ConexaoPhp {

    //ferramentas para conexao
    private OkHttpClient client;
    private HttpUrl.Builder urlBuilder;

    public ConexaoPhp(String phpURL) {
        if(phpURL.equals(null))
            throw new IllegalArgumentException("A URL para a conexao com o php nao pode ser nula");
        urlBuilder = HttpUrl.parse(phpURL).newBuilder();
        client = new OkHttpClient();
    }

    public void add(String tag, String valor) {
        urlBuilder.addQueryParameter(tag, valor);
    }

    public JSONObject enviar() {
        JSONObject jsonObject=null;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        String url = urlBuilder.build().toString();

        Request request = new Request.Builder().url(url).build();

        Call call = client.newCall(request);

        try {
            String data = call.execute().body().string();
            JSONArray jsonArray = new JSONArray(data);
            jsonObject = jsonArray.getJSONObject(0);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }





}
