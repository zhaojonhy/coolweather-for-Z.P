package com.zhaojonhy.coolweather.fragment;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zhaojonhy.coolweather.activity.MainActivity;
import com.zhaojonhy.coolweather.R;
import com.zhaojonhy.coolweather.activity.WeatherActivity;
import com.zhaojonhy.coolweather.db.City;
import com.zhaojonhy.coolweather.db.County;
import com.zhaojonhy.coolweather.db.Province;
import com.zhaojonhy.coolweather.util.HttpUtil;
import com.zhaojonhy.coolweather.util.Uitlity;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/2/13.
 */
public class ChooseAreaFragment extends Fragment {

    private final static String TAG = ChooseAreaFragment.class.getSimpleName() ;

//    private MainActivity activity ;
    //定义城市级别
    public static final int LEVEL_PRIVINCE = 0 ;
    public static final int LEVEL_CITY = 1 ;
    public static final int LEVEL_COUNTY = 2 ;
    //对话进度条
    private ProgressDialog progressDialog ;
    //标题栏
    private TextView titleText ;
    private Button backButton ;
    //城市菜单
    private ListView listView ;
    //城市的适配器
    private ArrayAdapter<String> arrayAdapter ;
    //城市的集合清单
    private ArrayList<String> dataList = new ArrayList<String>() ;
    /*
    * 省、市、县列表
    * */
    private List<Province> provinceList ;
    private List<City> cityList;
    private List<County> countyList;
    //选中的省份
    private Province selectedProvince ;
    //选中的城市
    private City selectedCity ;
    //当前选中的地域级别,默认为省
    private int currentLevel ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        activity = (MainActivity) getActivity() ;
    }

    //获取控件的实例，并初始化ArrayAdapter作为ListView的适配器
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false) ;
        titleText = (TextView)view.findViewById(R.id.title_text) ;
        backButton = (Button)view.findViewById(R.id.back_button) ;
        listView = (ListView)view.findViewById(R.id.list_view) ;
        arrayAdapter = new ArrayAdapter<>(getActivity(),android.R.layout.simple_list_item_1,dataList) ;
        listView.setAdapter(arrayAdapter);
        return view ;
    }

    //给ListView和Button做点击的监听器(在这个方法中可以找到是否加载双页模式的向导)
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //选中菜单的操作，省级的跳到市级的，市级的跳到县级的
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前的地域级别
                //省级
                if(currentLevel == LEVEL_PRIVINCE){
                    selectedProvince = provinceList.get(position) ;
                    //查询城市
                    queryCities() ;
                }else if(currentLevel == LEVEL_CITY){
                    //市级
                    selectedCity = cityList.get(position) ;
                    //查询县市
                    queryCounties() ;
                }else if(currentLevel == LEVEL_COUNTY){
                    //如果是县级选择的话，则跳如天气界面，传入weather_id
                    String weatherId = countyList.get(position).getWeatherId() ;

                    //做activity判断,1.如果是在开始进入选择城市时2.已经在天气界面切换城市时
                    //1.
                    if(getActivity() instanceof MainActivity){

                        Intent intent = new Intent(getActivity(), WeatherActivity.class) ;
                        intent.putExtra("weather_id",weatherId) ;
                        startActivity(intent) ;
                        getActivity().finish() ;
                    }else if(getActivity() instanceof WeatherActivity) {
                        //2.
                        WeatherActivity activity = (WeatherActivity) getActivity() ;
                        activity.drawLayout.closeDrawers();
                        activity.swipeRefresh.setRefreshing(true);
                        activity.requestWeather(weatherId);
                    }

                }
            }
        });

        //返回键的操作，如果在市级返回省，如果在县级返回市
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(currentLevel == LEVEL_COUNTY){
                    queryCities() ;
                }else if(currentLevel == LEVEL_CITY){
                    queryProvinces() ;
                }
            }
        });
        Log.d(TAG,"onActivityCreated:" + "YES") ;
        queryProvinces() ;
    }

    /*
    * 查询全国所有的省，优先从数据库查询，如果没有从服务器查询
    * */
    private void queryProvinces() {
        //标题栏设置为“中国” ，返回键隐藏
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        //查询省级的数据库，litepal
        provinceList = DataSupport.findAll(Province.class) ;
        Log.d(TAG,"provinceList:" + provinceList.size() ) ;
        //第一次都是从服务器上获取
        if(provinceList.size() > 0) {
            //清空城市集合清单
            dataList.clear();
            for (Province province : provinceList){
                //把省份的名字添加到集合中去
                dataList.add(province.getProvinceName());

            }
            //通知集合更新
            arrayAdapter.notifyDataSetChanged();
            //默认选中第一项
            listView.setSelection(0);
            //当前的区域级别为省份
            currentLevel = LEVEL_PRIVINCE ;
        }else {
            String address = "http://guolin.tech/api/china" ;
            Log.d(TAG,"provinceList:" +"provinceList.size()" +provinceList.size() ) ;
            queryFromServer(address,"province");
        }
    }

    /*
    * 查询全国所有的市，优先从数据库查询，如果没有从服务器查询
    * */
    private void queryCities() {
        //标题栏显示市区域
        titleText.setText(selectedProvince.getProvinceName());
        backButton.setVisibility(View.VISIBLE);
        //根据Id查询城市
        Log.d(TAG,"selectedProvince id : " + selectedProvince.getId()) ;
        cityList = DataSupport.where("provinceid = ?" ,
                String.valueOf(selectedProvince.getId())).find(City.class) ;
        if(cityList.size() > 0 ){
            dataList.clear();
            for(City city : cityList){
                dataList.add(city.getCityName()) ;
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_CITY ;
        }else {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceCode() ;
            queryFromServer(address,"city");
        }
    }

    /*
    * 查询全国所有的县，优先从数据库查询，如果没有从服务器查询
    * */
    private void queryCounties() {
        //标题栏显示县区域
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        //根据Id查询县市
        countyList = DataSupport.where("cityid = ?" ,
                String.valueOf(selectedCity.getId())).find(County.class) ;
        if(countyList.size() > 0 ){
            dataList.clear();
            for(County county : countyList){
                dataList.add(county.getCountyName()) ;
            }
            arrayAdapter.notifyDataSetChanged();
            listView.setSelection(0);
            currentLevel = LEVEL_COUNTY ;
        }else {
            String address = "http://guolin.tech/api/china/" + selectedProvince.getProvinceCode()
                    + "/" + selectedCity.getCityCode() ;
            queryFromServer(address,"county") ;
        }
    }

    //根据区域级别类型返回数据
    private void queryFromServer(String address, final String type) {
        //显示加载网络数据的对话框
        showProgressDialog() ;
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false ;
                //根据数据请求的类别请求服务器
                Log.d(TAG,"onResponse-->" + "sucessful!") ;
                Log.d(TAG,"onResponse-->" + "type:" + type ) ;
                Log.d(TAG,"onResponse-->" + "responseText:" + responseText ) ;

                if("province".equals(type)){
                    result = Uitlity.handleProvinceResponse(responseText) ;
                    Log.d(TAG,"onResponse-->" + "result to province :" + result ) ;
                }else if("city".equals(type)){
                    result = Uitlity.handleCityResponse(responseText,selectedProvince.getId()) ;
                    Log.d(TAG,"onResponse-->" + "result to city :" + result ) ;
                }else if("county".equals(type)){
                    result = Uitlity.handleCountyResponse(responseText,selectedCity.getId()) ;
                    Log.d(TAG,"onResponse-->" + "result to county :" + result ) ;
                }
                //在主线程中关闭进度条
                if(result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            //请求完服务器就存入数据库，然后再从数据库提取，服务器和数据库两两循环
                            if("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCities();
                            }else if("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                //通过runOnUiThread() 方法回到主线程
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG,"onResponse-->" + "fail !") ;
                        closeProgressDialog();
                        Toast.makeText(getActivity(),"加载失败..." ,Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    /*
    *  显示进度对话框
    * */
    private void showProgressDialog() {
        if(progressDialog == null){
            progressDialog = new ProgressDialog(getActivity()) ;
            progressDialog.setMessage("正在加载...");
            //点击外部消失为false
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    /*
    *  关闭进度对话框
    * */
        private void closeProgressDialog() {
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }

}
