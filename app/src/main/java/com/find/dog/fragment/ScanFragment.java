package com.find.dog.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.find.dog.Retrofit.RetroFactory;
import com.find.dog.Retrofit.RetroFitUtil;
import com.find.dog.activity.FindActivity;
import com.find.dog.activity.MainActivity;
import com.find.dog.activity.UpLoadActivity;
import com.find.dog.data.UserInfoUpdate;
import com.find.dog.R;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.activity.CaptureActivity;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.encoding.EncodingHandler;
import com.google.zxing.qrcode.QRCodeReader;
import com.find.dog.utils.RGBLuminanceSource;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import okhttp3.RequestBody;

/**
 * Created by zhangzhongwei on 2017/6/27.
 * 扫一扫Fragment
 */

public class ScanFragment extends Fragment implements View.OnClickListener {

    private TextView resultTextView;
    private EditText qrStrEditText;
    private ImageView qrImgImageView;
    private String time;
    private File file = null;
    private Context mContext;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_scan, container, false);
        intview(rootView);
        return rootView;
    }

    private void intview(View rootView) {
        mContext = getActivity();
        resultTextView = (TextView) rootView.findViewById(R.id.qrCodeText);
        qrStrEditText = (EditText) rootView.findViewById(R.id.text);
        qrImgImageView = (ImageView) rootView.findViewById(R.id.QrCode);
        rootView.findViewById(R.id.openQrCodeScan).setOnClickListener(this);
        rootView.findViewById(R.id.CreateQrCode).setOnClickListener(this);
        qrImgImageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                MainActivity.onPermissionRequests(Manifest.permission.WRITE_EXTERNAL_STORAGE, new  MainActivity.OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            // 长按识别二维码
                            saveCurrentImage();
                        } else {
                            Toast.makeText(getActivity(), "未打开相册权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                return true;
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //处理扫描结果（在界面上显示）
        if (data != null) {
            Log.e("H",requestCode+"-------------"+resultCode);
            resultTextView.setText(data.getStringExtra("result"));
            if(resultCode == 300){
                startActivity(new Intent(getActivity(), FindActivity.class));
            }else {
                startActivity(new Intent(getActivity(), UpLoadActivity.class));
            }

        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.openQrCodeScan:

                //打开扫描界面扫描条形码或二维码
                MainActivity.onPermissionRequests(Manifest.permission.CAMERA, new MainActivity.OnBooleanListener() {
                    @Override
                    public void onClick(boolean bln) {
                        if (bln) {
                            Intent openCameraIntent = new Intent(getActivity(), CaptureActivity.class);
                            startActivityForResult(openCameraIntent, 0);
                        } else {
                            Toast.makeText(getActivity(), "未打开相机权限", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                break;
            case R.id.CreateQrCode:
                //生成二维码
                try {
                    String contentString = qrStrEditText.getText().toString();
                    if (!contentString.equals("")) {
                        int width = getActivity().getWindow().getDecorView().getRootView().getWidth();
                        int scanSize = (width == 0) ? 800 : width;
                        //根据字符串生成二维码图片并显示在界面上，第二个参数为图片的大小（350*350）
                        Bitmap qrCodeBitmap = EncodingHandler.createQRCode(contentString, scanSize);
                        qrImgImageView.setImageBitmap(qrCodeBitmap);
                    } else {
                        //提示文本不能是空的
                        Toast.makeText(getActivity(), "文本不能为空", Toast.LENGTH_SHORT).show();
                    }

                } catch (WriterException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;
        }

    }


    //这种方法状态栏是空白，显示不了状态栏的信息
    private void saveCurrentImage() {
        //获取当前屏幕的大小
        int width = getActivity().getWindow().getDecorView().getRootView().getWidth();
        int height = getActivity().getWindow().getDecorView().getRootView().getHeight();
        //生成相同大小的图片
        Bitmap temBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        //找到当前页面的根布局
        View view = getActivity().getWindow().getDecorView().getRootView();
        //设置缓存
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //从缓存中获取当前屏幕的图片,创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        temBitmap = view.getDrawingCache();
        SimpleDateFormat df = new SimpleDateFormat("yyyymmddhhmmss");
        time = df.format(new Date());
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen", time + ".png");
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                temBitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            new Handler() {
                public void handleMessage(android.os.Message msg) {
                    if (msg.what == 0) {
                        String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/screen/" + time + ".png";
                        final Result result = parseQRcodeBitmap(path);
                        if (null != result) {
                            resultTextView.setText(result.toString());
                        } else {
                            Toast.makeText(mContext, "无法识别", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }.sendEmptyMessageDelayed(0, 10);

            //禁用DrawingCahce否则会影响性能 ,而且不禁止会导致每次截图到保存的是缓存的位图
            view.setDrawingCacheEnabled(false);
        }
    }

    //解析二维码图片,返回结果封装在Result对象中
    private Result parseQRcodeBitmap(String bitmapPath) {
        //解析转换类型UTF-8
        Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
        hints.put(DecodeHintType.CHARACTER_SET, "utf-8");
        //获取到待解析的图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        //如果我们把inJustDecodeBounds设为true，那么BitmapFactory.decodeFile(String path, Options opt)
        //并不会真的返回一个Bitmap给你，它仅仅会把它的宽，高取回来给你
        options.inJustDecodeBounds = true;
        //此时的bitmap是null，这段代码之后，options.outWidth 和 options.outHeight就是我们想要的宽和高了
        Bitmap bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        //我们现在想取出来的图片的边长（二维码图片是正方形的）设置为400像素
        /**
         options.outHeight = 400;
         options.outWidth = 400;
         options.inJustDecodeBounds = false;
         bitmap = BitmapFactory.decodeFile(bitmapPath, options);
         */
        //以上这种做法，虽然把bitmap限定到了我们要的大小，但是并没有节约内存，如果要节约内存，我们还需要使用inSimpleSize这个属性
        options.inSampleSize = options.outHeight / 400;
        if (options.inSampleSize <= 0) {
            options.inSampleSize = 1; //防止其值小于或等于0
        }
        /**
         * 辅助节约内存设置
         *
         * options.inPreferredConfig = Bitmap.Config.ARGB_4444;    // 默认是Bitmap.Config.ARGB_8888
         * options.inPurgeable = true;
         * options.inInputShareable = true;
         */
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(bitmapPath, options);
        //新建一个RGBLuminanceSource对象，将bitmap图片传给此对象
        RGBLuminanceSource rgbLuminanceSource = new RGBLuminanceSource(bitmap);
        //将图片转换成二进制图片
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(rgbLuminanceSource));
        //初始化解析对象
        QRCodeReader reader = new QRCodeReader();
        //开始解析
        Result result = null;
        try {
            result = reader.decode(binaryBitmap, hints);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

}
