package com.example.zhihudailycopy;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity2 extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //数据传输
        Intent intent = getIntent();
        String webview = intent.getStringExtra("webview");
        final String id = intent.getStringExtra("id");

        //内容页面
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(webview);


        //评论按钮
        RelativeLayout button = findViewById(R.id.comment);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MainActivity2.this,MainActivity3.class);
                intent2.putExtra("id",id);
                startActivity(intent2);
            }
        });

        //返回按钮
        Button button2 = findViewById(R.id.last);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }

        });

        //评论数量
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                String idUrl = "https://news-at.zhihu.com/api/3/story-extra/" + id;
                BufferedReader reader = null;
                try {
                    URL url = new URL(idUrl);
                    HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    InputStream in = connection.getInputStream();
                    reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line=reader.readLine())!=null){
                        response.append(line);
                    }
                    showResponse(response.toString());//转到解析json的函数
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally {
                    if (reader != null){
                        try{
                            reader.close();
                        }catch(IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }


        });
        thread1.start();
        try {
            thread1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    private void showResponse(final String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            final String number = jsonObject.getString("comments");


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView numberImage = findViewById(R.id.numberImage);
                    numberImage.setText(number);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity2.this,"",Toast.LENGTH_LONG);
        }
    }
}