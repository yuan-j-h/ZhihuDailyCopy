package com.example.zhihudailycopy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity3 extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Map<String,Object>> list =new ArrayList<>();
    private int longComment;
    private int shortComment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        recyclerView = findViewById(R.id.recyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        //数据传输

        Intent intent = getIntent();
        final String id = intent.getStringExtra("id");
        Log.i(id,"myid");

        //评论数量
            Thread thread1 = new Thread(new Runnable() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    String idUrl = "https://news-at.zhihu.com/api/3/story-extra/" + id;
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(idUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        showResponse1(response.toString());//转到解析json的函数
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
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

        //长评
        if (longComment!=0) {
            Thread thread2 = new Thread(new Runnable() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    String longUrl = "https://news-at.zhihu.com/api/4/story/" + id + "/long-comments";
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(longUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        showResponse3(String.valueOf(longComment));
                        showResponse2(response.toString());//转到解析json的函数
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }


            });
            thread2.start();
            try {
                thread2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }



        //短评
        if (shortComment!=0) {
            Thread thread3 = new Thread(new Runnable() {
                @SuppressLint("LongLogTag")
                @Override
                public void run() {
                    String longUrl = "https://news-at.zhihu.com/api/4/story/" + id + "/short-comments";
                    BufferedReader reader = null;
                    try {
                        URL url = new URL(longUrl);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("GET");
                        connection.setConnectTimeout(8000);
                        connection.setReadTimeout(8000);
                        InputStream in = connection.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        showResponse4(String.valueOf(shortComment));
                        showResponse2(response.toString());//转到解析json的函数
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }


            });
            thread3.start();
            try {
                thread3.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }






        //返回
        Button button2 = findViewById(R.id.last);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }

        });
    }

    //解析评论数量
    private void showResponse1(final String JsonData) {
        try {
                JSONObject jsonObject = new JSONObject(JsonData);
                final String number = jsonObject.getString("comments");
                longComment = jsonObject.getInt("long_comments");
                shortComment = jsonObject.getInt("short_comments");

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView commentNumber = findViewById(R.id.commentNumber);
                    String commentNumberS = number+"条评论";
                    commentNumber.setText(commentNumberS);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity3.this,"",Toast.LENGTH_LONG);
        }
    }
    //解析评论
    private void showResponse2(final String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("comments");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String author = jsonObject1.getString("author");
                String content = jsonObject1.getString("content");
                String avatar = jsonObject1.getString("avatar");
                String time = jsonObject1.getString("time");
                String id = jsonObject1.getString("id");
                String likes = jsonObject1.getString("likes");
                Map<String,Object> map=new HashMap<>();
                map.put("type","2");
                map.put("author",author);
                map.put("content",content);
                map.put("avatar",avatar);
                map.put("time",time);
                list.add(map);
            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Adapter2 news = new Adapter2(MainActivity3.this,list);
                    recyclerView.setAdapter(news);

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(MainActivity3.this,"",Toast.LENGTH_LONG);
        }
    }
    //长评数量
    private void showResponse3(final String JsonData) {
        String longComment = JsonData+"条长评";
        Map<String,Object> map=new HashMap<>();
        map.put("type","1");
        map.put("number",longComment);
        list.add(map);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Adapter2 news = new Adapter2(MainActivity3.this,list);
                recyclerView.setAdapter(news);
            }
        });
    }
    //短评数量
    private void showResponse4(final String JsonData) {
        String shortComment = JsonData+"条短评";
        Map<String,Object> map=new HashMap<>();
        map.put("type","1");
        map.put("number",shortComment);
        list.add(map);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Adapter2 news = new Adapter2(MainActivity3.this,list);
                recyclerView.setAdapter(news);
            }
        });
    }
}