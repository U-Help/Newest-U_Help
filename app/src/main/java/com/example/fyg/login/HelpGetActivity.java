package com.example.fyg.login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import java.util.*;

public class HelpGetActivity extends Activity implements SwipeRefreshLayout.OnRefreshListener {
    private SwipeRefreshLayout mSwipeLayout;
    private ListView mListView;
    private ArrayList<String> list = new ArrayList<String>();
    private ArrayAdapter<String> adapter;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_get);

        getInfo();

        mListView = (ListView) findViewById(R.id.listview);
        /**
         * listview绑定adapter
         */
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, getData());
        mListView.setAdapter(adapter);

        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //绑定刷新时间
        mSwipeLayout.setOnRefreshListener(this);
        //设置颜色
        mSwipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(HelpGetActivity.this, i+1+"被单击了", Toast.LENGTH_LONG).show();
                startActivity(new Intent(HelpGetActivity.this, DetailActivity.class));
            }
        });
    }

    public ArrayList<String> getInfo(){
        OkHttpClient client = new OkHttpClient();
        User user=new User();
        int i=user.id;
        String id=Integer.toString(i);
        String token=user.token;

        HashMap<String, String> map = new HashMap<>();
        map.put("offset","0");
        map.put("id", id);
        map.put("token", token);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/item/hall")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(HelpGetActivity.this, "未连接到服务器", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    boolean flag = false;
                    System.out.println("1");
                    List<Order> orders=new ArrayList<Order>();
                    try {
                        String str = response.body().string();
                        System.out.println("1");

                        JSONObject jsonObject = new JSONObject(str);
                        System.out.println("1");
                        String state=jsonObject.getString("state");
                        System.out.println("1");

                        if (state.equals("success")) {
                            flag=true;
                            JSONArray jsonArray=jsonObject.getJSONArray("data");
                            list.add("  收货时间      取货地点     收货地点");
                            for(int i = 0;i < jsonArray.length();i++){
                                JSONObject jsonObject1 = (JSONObject) jsonArray.get(i);
                                System.out.println("1");
                                Order order=new Order();
                                System.out.println("1");
                                order.setDstplace(jsonObject1.getString("dstplace"));
                                System.out.println(order.dstplace);
                                order.setIs_available(jsonObject1.getString("is_available"));
                                System.out.println(order.is_available);
                                order.setItem_id(jsonObject1.getString("item_id"));
                                System.out.println(order.item_id);
                                order.setPhone(jsonObject1.getString("phone"));
                                System.out.println(order.phone);
                                order.setPrice(jsonObject1.getString("price"));
                                System.out.println(order.price);
                                order.setProp_time(jsonObject1.getString("prop_time"));
                                System.out.println(order.prop_time);
                                order.setRece_time(jsonObject1.getString("rece_time"));
                                System.out.println(order.rece_time);
                                order.setRev_password(jsonObject1.getString("rev_password"));
                                System.out.println(order.rev_password);
                                order.setSize(jsonObject1.getString("size"));
                                System.out.println(order.size);
                                order.setSrcplace(jsonObject1.getString("srcplace"));
                                System.out.println(order.srcplace);
                                orders.add(order);
                                list.add(i+1+"."+orders.get(i).rece_time+"     "+orders.get(i).srcplace+"     "+orders.get(i).dstplace);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        Toast.makeText(HelpGetActivity.this, "获取订单成功", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(HelpGetActivity.this, "获取订单失败", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(HelpGetActivity.this, "服务器未响应" + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
        return list;
    }

    private ArrayList<String> getData() {
        /*list.add("  收货时间      取货地点     收货地点");
        list.add("1."+orders.get(1).rece_time+"     "+orders.get(1).srcplace+"     "+orders.get(1).dstplace);
        list.add("An Android Developer");
        list.add("http://weibo.com/mcxiaobing");
        list.add("http://git.oschina.net/MCXIAOBING");
        list.add("https://github.com/QQ986945193");
        list.add("An Android Developer");
        list.add("http://weibo.com/mcxiaobing");
        list.add("http://git.oschina.net/MCXIAOBING");
        list.add("https://github.com/QQ986945193");
        list.add("An Android Developer");
        list.add("http://weibo.com/mcxiaobing");
        list.add("http://git.oschina.net/MCXIAOBING");
        list.add("https://github.com/QQ986945193");*/
        return list;
    }

    public void onRefresh() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //停止刷新
                mSwipeLayout.setRefreshing(false);

            }
        }, 3000);
    }
}
