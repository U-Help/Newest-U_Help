package com.example.fyg.login;

import android.content.Intent;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.view.View;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private Button btnRegister;
    private Button btnSetting;
    private EditText editUsername;
    private EditText editPassword;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editUsername = findViewById(R.id.editUsername);
        editPassword = findViewById(R.id.editPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                postLogin();
            }
        });

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
            }
        });
        btnSetting = findViewById(R.id.btnSetting);
        btnSetting.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, GetActivity.class));
            }
        });
    }

    public void postLogin() {
        OkHttpClient client = new OkHttpClient();
        String username = editUsername.getText().toString();
        String password = editPassword.getText().toString();

        HashMap<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("password", password);
        JSONObject jsonObject = new JSONObject(map);
        String jsonStr = jsonObject.toString();
        RequestBody body = RequestBody.create(JSON, jsonStr);
        Request request = new Request.Builder()
                .url("http://47.100.116.160:5000/user/login")
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Looper.prepare();
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_LONG).show();
                Looper.loop();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    boolean flag = false;
                    try {
                        String str = response.body().string();

                        JSONObject jsonStr = new JSONObject(str);

                        if (jsonStr.getString("state").equals("success")) {
                            flag = true;
                            User user=new User();
                            user.id=jsonStr.getInt("id");
                            user.token=jsonStr.getString("token");
                            System.out.println(User.id);
                            System.out.println(user.token);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (flag) {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(MainActivity.this, GetActivity.class);
                        startActivity(intent);
                        Looper.loop();
                    } else {
                        Looper.prepare();
                        Toast.makeText(MainActivity.this, "Login Failure", Toast.LENGTH_LONG).show();
                        Looper.loop();
                    }
                } else {
                    Looper.prepare();
                    Toast.makeText(MainActivity.this, "Login Response Failed " + response.body().string(), Toast.LENGTH_LONG).show();
                    Looper.loop();
                }
            }
        });
    }
}
