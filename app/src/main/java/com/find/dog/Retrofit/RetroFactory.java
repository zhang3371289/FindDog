package com.find.dog.Retrofit;


import com.find.dog.utils.MyManger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFactory {

    private static final long DEFAULT_TIMEOUT = 30;

    private Retrofit retrofit;

    private JSONObject systemObject;

    private static volatile RetroFactory instance;

    private String getBaseUrl(){
        return MyManger.BASE;
    }

    private RetroFactory() {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                    .retryOnConnectionFailure(false)
                    .build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(getBaseUrl())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();
        }
    }

    public static RetroFactory getIstance() {
        if (instance == null) {
            synchronized (RetroFactory.class) {
                if (instance == null) {
                    instance = new RetroFactory();
                }
            }
        }
        return instance;
    }

    public RetroFitRequestEngine getStringService() {
        RetroFitRequestEngine service = retrofit.create(RetroFitRequestEngine.class);
        return service;
    }

    public RequestBody getrequestBody(Map<String, String> parameters) {
        JSONObject object = addParams(parameters);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        return requestBody;
    }
    public RequestBody getrequestBody(JSONObject jsonObject) {
        JSONObject object = addParamsWithJson(jsonObject);
        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), object.toString());
        return requestBody;
    }

    private JSONObject addDefaultParams(JSONObject systemObject) {
        return systemObject;
    }


    private JSONObject addParams(Map<String, String> parameters) {
        // TODO Auto-generated method stub
        JSONObject object = new JSONObject();
        if(systemObject==null){
            systemObject = new JSONObject();
            addDefaultParams(systemObject);
        }
//        try {
//            object.put("system", systemObject);
//        } catch (JSONException e) {
//        }
        if (parameters != null) {
            Iterator<Map.Entry<String, String>> iter = parameters.entrySet()
                    .iterator();
            JSONObject postObject = new JSONObject();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String key = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (key != null && value != null) {
                    try {
                        postObject.put((String) key, value);
                    } catch (JSONException e) {
                    }
                }
            }
//            try {
//                object.put("postdata", postObject);
//            } catch (JSONException e) {
//            }
        }
        return object;
    }
    private JSONObject addParamsWithJson(JSONObject obj) {
        // TODO Auto-generated method stub
        JSONObject object = new JSONObject();
        if(systemObject==null){
            systemObject = new JSONObject();
            addDefaultParams(systemObject);
        }
        try {
            object.put("system", systemObject);
        } catch (JSONException e) {
        }
        if (obj != null) {
            try {
                object.put("postdata", obj);
            } catch (JSONException e) {
            }
        }
        return object;
    }
}
