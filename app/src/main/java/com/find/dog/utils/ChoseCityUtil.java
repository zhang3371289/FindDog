package com.find.dog.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.find.dog.main.MyApplication;
import com.lljjcoder.citypickerview.widget.CityPicker;

/**
 * Created by ZhangV on 2017/8/16.
 */

public class ChoseCityUtil {
    public static void showDialog(Context mContext,final EditText textview){
        CityPicker cityPicker = new CityPicker.Builder(mContext)
                .textSize(20)
                .title("丢失地址")
                .backgroundPop(0xa0000000)
                .titleBackgroundColor("#CCCCCC")
                .titleTextColor("#CCCCCC")
                .confirTextColor("#234Dfa")
                .cancelTextColor("#234Dfa")
                .province(MyManger.getCity(1,"北京市"))
                .city(MyManger.getCity(2,"北京市"))
                .district(MyManger.getCity(3,"朝阳区"))
                .textColor(Color.parseColor("#000000"))
                .provinceCyclic(true)
                .cityCyclic(false)
                .districtCyclic(false)
                .visibleItemsCount(7)
                .itemPadding(10)
                .onlyShowProvinceAndCity(false)
                .build();
        cityPicker.show();

        //监听方法，获取选择结果
        cityPicker.setOnCityItemClickListener(new CityPicker.OnCityItemClickListener() {
            @Override
            public void onSelected(String... citySelected) {
                //省份
                String province = citySelected[0];
                //城市
                String city = citySelected[1];
                //区县（如果设定了两级联动，那么该项返回空）
                String district = citySelected[2];
                //邮编
                String code = citySelected[3];

                textview.setText(province+city+district);
            }

            @Override
            public void onCancel() {
//                Toast.makeText(IssueActivity.this, "已取消", Toast.LENGTH_LONG).show();
            }
        });
    }
}
