package com.find.dog.Retrofit;

import android.content.Context;
import android.util.Log;

import com.find.dog.utils.ToastUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RetroFitUtil<T> {
    private Call<BaseEntity<T>> mCall;
    private Context mContext;
    private final int SUCCESS = 0;
    private final String TAG = "response";

    public RetroFitUtil(Context context, Call call) {
        mCall = call;
        mContext = context;
    }

    public void cancel(){
        if(mCall!=null)
            mCall.cancel();
    }
    public void request(final ResponseListener listener) {
        mCall.enqueue(new Callback<BaseEntity<T>>() {
            @Override
            public void onResponse(Call<BaseEntity<T>> call, Response<BaseEntity<T>> response) {
                if (response.isSuccessful() && response.errorBody() == null) {
                    if (response.body().getCode() == SUCCESS) {
                        listener.onSuccess((T) response.body().getData());
                    } else {
                        ToastUtil.showTextToast(mContext, response.body().getMessage());
                        listener.onFail();
                    }

                } else {
                    ToastUtil.showTextToast(mContext, "网络请求异常！ "+response.message());
                }
            }

            @Override
            public void onFailure(Call<BaseEntity<T>> call, Throwable t) {
                listener.onFail();
//                ToastUtil.showTextToast(mContext, "！"+t.getMessage());
                Log.i("result","onFailure->"+t.getMessage());
            }
        });
    }

    public interface ResponseListener<T> {
        void onSuccess(T t);

        void onFail();
    }
}