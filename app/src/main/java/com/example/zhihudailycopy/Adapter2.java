package com.example.zhihudailycopy;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Adapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private  int NUMBER = 1;
    private  int COMMENT = 2;

    private Context context;
    private List<Map<String,Object>> list;

    public Adapter2(Context context, List<Map<String,Object>> list){
        this.context=context;
        this.list=list;
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int getItemViewType(int position) {
        if (Objects.requireNonNull(list.get(position).get("type")).toString()=="1") {
            return NUMBER;
        } else {
            return COMMENT;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType==NUMBER) {
            view = LayoutInflater.from(context).inflate(R.layout.item_number, parent, false);
            viewHolder = new ViewHolder1(view);
        }
        else if (viewType == COMMENT){
            view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
            viewHolder =new ViewHolder2(view);
        }
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int i) {
        if (holder instanceof ViewHolder1) {
            ((ViewHolder1) holder).number.setText(Objects.requireNonNull(list.get(i).get("number")).toString());
        }
        else if (holder instanceof ViewHolder2){

            ((ViewHolder2) holder).author.setText(Objects.requireNonNull(list.get(i).get("author")).toString());
            ((ViewHolder2) holder).commentContent.setText(Objects.requireNonNull(list.get(i).get("content")).toString());
            //时间
            String time1 = Objects.requireNonNull(list.get(i).get("time")).toString();
            String time2 = DateFormatUtil2.timeStampDate2(time1);
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Calendar calendarAgo = Calendar.getInstance();
            String dateName = df.format(calendarAgo.getTime());
            if (time2.equals(dateName)) {
                String time = DateFormatUtil3.timeStampDate3(time1);
                ((ViewHolder2) holder).commentTime.setText("今天 "+time);
            }
            else {
                String time = DateFormatUtil.timeStampDate(time1);
                ((ViewHolder2) holder).commentTime.setText(time);
            }

            String avatar = Objects.requireNonNull(list.get(i).get("avatar")).toString();
            RequestOptions requestOptions = RequestOptions.circleCropTransform()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true);
            Glide.with(context).load(avatar).apply(requestOptions).into(((ViewHolder2) holder).avatar);

        }

    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    static class ViewHolder1 extends RecyclerView.ViewHolder {
        TextView number;
        public ViewHolder1(View itemView) {
            super(itemView);
            number = itemView.findViewById(R.id.number);
        }
    }

    static class ViewHolder2 extends RecyclerView.ViewHolder {

        TextView author,commentContent,commentTime;
        ImageView avatar;
        public ViewHolder2(View itemView) {
            super(itemView);
            author = itemView.findViewById(R.id.author);
            commentContent = itemView.findViewById(R.id.commentContent);
            avatar = itemView.findViewById(R.id.avatar);
            commentTime = itemView.findViewById(R.id.commentTime);
        }
    }

    public static class DateFormatUtil {
        public  static String timeStampDate (String string){
            if (string==null||string.isEmpty()||string.equals("null")){
                return "";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm");
            return simpleDateFormat.format((new Date(Long .valueOf(string+"000"))));
        }
    }
    public static class DateFormatUtil2 {
        public  static String timeStampDate2 (String string){
            if (string==null||string.isEmpty()||string.equals("null")){
                return "";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            return simpleDateFormat.format((new Date(Long .valueOf(string+"000"))));
        }
    }
    public static class DateFormatUtil3 {
        public  static String timeStampDate3 (String string){
            if (string==null||string.isEmpty()||string.equals("null")){
                return "";
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            return simpleDateFormat.format((new Date(Long .valueOf(string+"000"))));
        }
    }
}
