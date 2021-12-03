package com.example.zhihudailycopy;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  int NEW_VIEW = 1;
    private  int DATE_VIEW = 2;
    private  int NEW_VIEW2 = 3;
    private  int DATE_VIEW2 = 4;


    private Context context;
    private List<Map<String,Object>> list;

    public List<Map<String,Object>> getData (){
        return list;
    }
    public  void  setData (List<Map<String,Object>> list){
        this.list=list;
    }


    public Adapter(Context context, List<Map<String,Object>> list){
        this.context=context;
        this.list=list;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int getItemViewType(int position) {
        if (Objects.requireNonNull(list.get(position).get("type")).toString()=="1") {
            return NEW_VIEW;
        } else if (Objects.requireNonNull(list.get(position).get("type")).toString()=="2"){
            return DATE_VIEW;
        }else if (Objects.requireNonNull(list.get(position).get("type")).toString()=="3"){
            return NEW_VIEW2;
        }
        else {
            return DATE_VIEW2;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        if (i==NEW_VIEW){
            view = LayoutInflater.from(context).inflate(R.layout.item_new, viewGroup, false);
            viewHolder= new ViewHolder(view);
        }
        else if (i==DATE_VIEW){
            view = LayoutInflater.from(context).inflate(R.layout.item_date, viewGroup, false);
            viewHolder= new ViewHolder2(view);
        }
        else if (i==NEW_VIEW2){
            view = LayoutInflater.from(context).inflate(R.layout.item_new, viewGroup, false);
            viewHolder= new ViewHolder3(view);
        }
        else if(i==DATE_VIEW2){
            view = LayoutInflater.from(context).inflate(R.layout.item_date, viewGroup, false);
            viewHolder= new ViewHolder4(view);
        }
        return viewHolder;

    }




    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {

        if (holder instanceof  ViewHolder){

        ((ViewHolder) holder).title.setText(Objects.requireNonNull(list.get(i).get("title")).toString());
        ((ViewHolder) holder).hint.setText(Objects.requireNonNull(list.get(i).get("hint")).toString());
        String imagesUrl1 = Objects.requireNonNull(list.get(i).get("images")).toString();
        final String webview = Objects.requireNonNull(list.get(i).get("webview")).toString();
        final String id = Objects.requireNonNull(list.get(i).get("id")).toString();
        String imagesUrl2 = imagesUrl1.replace("[\"","");
        String imagesUrl3 = imagesUrl2.replace("\\","");
        String imagesUrl = imagesUrl3.replace("]\"","");
        Glide.with(context).load(imagesUrl).into(((ViewHolder) holder).picture);
        //跳转
        ((ViewHolder) holder).next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,MainActivity2.class);
                intent.putExtra("webview",webview);
                intent.putExtra("id",id);
                context.startActivity(intent);
            }
        });
        }
        else if (holder instanceof  ViewHolder2){
            ((ViewHolder2) holder).dateBefore.setText(Objects.requireNonNull(list.get(i).get("dateName")).toString());
        }
        else if (holder instanceof  ViewHolder3){

            ((ViewHolder3) holder).title.setText(Objects.requireNonNull(list.get(i).get("title")).toString());
            ((ViewHolder3) holder).hint.setText(Objects.requireNonNull(list.get(i).get("hint")).toString());
            String imagesUrl1 = Objects.requireNonNull(list.get(i).get("images")).toString();
            final String webview = Objects.requireNonNull(list.get(i).get("webview")).toString();
            final String id = Objects.requireNonNull(list.get(i).get("id")).toString();
            String imagesUrl2 = imagesUrl1.replace("[\"","");
            String imagesUrl3 = imagesUrl2.replace("\\","");
            String imagesUrl = imagesUrl3.replace("]\"","");
            Glide.with(context).load(imagesUrl).into(((ViewHolder3) holder).picture);
            //跳转
            ((ViewHolder3) holder).next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context,MainActivity2.class);
                    intent.putExtra("webview",webview);
                    intent.putExtra("id",id);
                    context.startActivity(intent);
                }
            });
        }
        else if (holder instanceof  ViewHolder4){
            ((ViewHolder4) holder).dateBefore.setText(Objects.requireNonNull(list.get(i).get("dateName")).toString());
        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title,hint;
        ImageView picture;
        RelativeLayout next;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title1);
            hint = itemView.findViewById(R.id.hint);
            picture = itemView.findViewById(R.id.picture);
            next = itemView.findViewById(R.id.next);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView dateBefore;

        public ViewHolder2(View itemView) {
            super(itemView);
            dateBefore = itemView.findViewById(R.id.dateBefore);

        }
    }
    static class ViewHolder3 extends RecyclerView.ViewHolder {

        TextView title,hint;
        ImageView picture;
        RelativeLayout next;
        public ViewHolder3(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title1);
            hint = itemView.findViewById(R.id.hint);
            picture = itemView.findViewById(R.id.picture);
            next = itemView.findViewById(R.id.next);
        }
    }
    static class ViewHolder4 extends RecyclerView.ViewHolder {

        TextView dateBefore;

        public ViewHolder4(View itemView) {
            super(itemView);
            dateBefore = itemView.findViewById(R.id.dateBefore);

        }
    }


}
