package com.example.zhihudailycopy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Calendar;
import java.util.TimeZone;


public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Map<String,Object>> list =new ArrayList<>();
    private Adapter news;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //今日日期
        {
            Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int time = calendar.get(Calendar.HOUR_OF_DAY);
            String dayString = String.valueOf(day);

            final TextView dayNow = findViewById(R.id.dayNow);
            dayNow.setText(dayString);
            final TextView monthNow = findViewById(R.id.monthNow);

            //今日日期显示
            {
                if (month == 1) {
                    monthNow.setText("一月");
                } else if (month == 2) {
                    monthNow.setText("二月");
                } else if (month == 3) {
                    monthNow.setText("三月");
                } else if (month == 4) {
                    monthNow.setText("四月");
                } else if (month == 5) {
                    monthNow.setText("五月");
                } else if (month == 6) {
                    monthNow.setText("六月");
                } else if (month == 7) {
                    monthNow.setText("七月");
                } else if (month == 8) {
                    monthNow.setText("八月");
                } else if (month == 9) {
                    monthNow.setText("九月");
                } else if (month == 10) {
                    monthNow.setText("十月");
                } else if (month == 11) {
                    monthNow.setText("十一月");
                } else {
                    monthNow.setText("十二月");
                }

                final TextView headTitle = findViewById(R.id.headTitle);
                if (time > 2 && time <= 9) {
                    headTitle.setText("早上好~");
                } else if (time > 9 && time <= 19) {
                    headTitle.setText("知乎日报");
                } else if (time > 19 && time <= 22) {
                    headTitle.setText("晚上好~");
                } else {
                    headTitle.setText("早点休息~");
                }
            }
        }

        //
        recyclerView=findViewById(R.id.recyclerview);
        // 线性布局管理器
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        news = new Adapter(MainActivity.this,list);
        recyclerView.setAdapter(news);



        //今日新闻线程
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;

                try {
                    URL url = new URL("https://news-at.zhihu.com/api/3/stories/latest");
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
                }
                catch (Exception e) {
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



        //昨天新闻线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                BufferedReader reader = null;
                try {

                    //日期
                    DateFormat df = new SimpleDateFormat("yyyyMMdd");
                    DateFormat df2 = new SimpleDateFormat("MM月d日");
                    Calendar calendarAgo = Calendar.getInstance();
                    String dateName = df.format(calendarAgo.getTime());
                    String netBefore = "https://news-at.zhihu.com/api/3/news/before/";
                    String netAll = netBefore + dateName;
                    calendarAgo.add(Calendar.DATE,-1);
                    String dateName2 = df2.format(calendarAgo.getTime());
                    URL url = new URL(netAll);
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

                    //
                    showResponse2(dateName2);

                    showResponse(response.toString());//转到解析json的函数


                }
                catch (Exception e) {
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


        }).start();


        //刷新
        RefreshLayout refreshLayout = findViewById(R.id.refreshLayout);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);
                list.clear();
                //今日新闻
                Thread thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader reader = null;

                        try {
                            URL url = new URL("https://news-at.zhihu.com/api/3/stories/latest");
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
                        }
                        catch (Exception e) {
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

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader reader = null;
                        try {

                            //日期
                            DateFormat df = new SimpleDateFormat("yyyyMMdd");
                            DateFormat df2 = new SimpleDateFormat("MM月dd日");
                            Calendar calendarAgo = Calendar.getInstance();
                            String dateName = df.format(calendarAgo.getTime());
                            String netBefore = "https://news-at.zhihu.com/api/3/news/before/";
                            String netAll = netBefore + dateName;
                            calendarAgo.add(Calendar.DATE,-1);
                            String dateName2 = df2.format(calendarAgo.getTime());
                            URL url = new URL(netAll);
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
                            showResponse2(dateName2);
                            showResponse(response.toString());//转到解析json的函数
                        }
                        catch (Exception e) {
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


                }).start();

            }
        });

        final Calendar calendarAgo = Calendar.getInstance();
        calendarAgo.add(Calendar.DATE,-1);

        //加载
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000/*,false*/);

                //以前新闻
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        BufferedReader reader = null;
                        try {
                            //n天前日期
                            DateFormat df = new SimpleDateFormat("yyyyMMdd");
                            DateFormat df2 = new SimpleDateFormat("MM月d日");
                            //final Calendar calendarAgo = Calendar.getInstance();
                            String dateName = df.format(calendarAgo.getTime());
                            String netBefore = "https://news-at.zhihu.com/api/3/news/before/";
                            final String netAll = netBefore + dateName;
                            calendarAgo.add(Calendar.DATE,-1);
                            String dateName2 = df2.format(calendarAgo.getTime());
                            URL url = new URL(netAll);
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
                            showResponse4(dateName2);
                            showResponse3(response.toString());//转到解析json的函数
                        }
                        catch (Exception e) {
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
                }).start();

            }
        });
    }

    //新闻解析
    private void showResponse(final String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String hint = jsonObject1.getString("hint");
                String images= jsonObject1.getString("images");
                String webview= jsonObject1.getString("url");
                String id = jsonObject1.getString("id");
                Map<String,Object> map=new HashMap<>();
                map.put("type","1");
                map.put("title",title);
                map.put("hint",hint);
                map.put("images",images);
                map.put("webview",webview);
                map.put("id",id);
                list.add(map);

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    news.setData(list);
                    news.notifyDataSetChanged();

                };
        });
    } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //日期解析
    private void showResponse2(final String dateName2) {
        Map<String,Object> map=new HashMap<>();
        map.put("type","2");
        map.put("dateName",dateName2);
        list.add(map);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                news.setData(list);
                news.notifyDataSetChanged();
            }
        });
    }
    //
    private void showResponse3(final String JsonData) {
        try {
            JSONObject jsonObject = new JSONObject(JsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("stories");
            for (int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                String title = jsonObject1.getString("title");
                String hint = jsonObject1.getString("hint");
                String images= jsonObject1.getString("images");
                String webview= jsonObject1.getString("url");
                String id = jsonObject1.getString("id");
                Map<String,Object> map=new HashMap<>();
                map.put("type","3");
                map.put("title",title);
                map.put("hint",hint);
                map.put("images",images);
                map.put("webview",webview);
                map.put("id",id);
                list.add(map);

            }


            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    news.setData(list);
                    news.notifyDataSetChanged();

                };
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showResponse4(final String dateName2) {
        Map<String,Object> map=new HashMap<>();
        map.put("type","4");
        map.put("dateName",dateName2);
        list.add(map);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                news.setData(list);
                news.notifyDataSetChanged();
            }
        });
    }
}





