package com.example.gunmunity.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gunmunity.R;
import com.example.gunmunity.model.Result;

import java.util.ArrayList;
import java.util.List;

public class CommunityMainAdapter extends RecyclerView.Adapter<CommunityMainAdapter.ViewHoler> {
    List<Result> results;
    Context context;

    public CommunityMainAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Result> results) {
        this.results = new ArrayList<>();
        this.results = results;
    }

    @NonNull
    @Override
    public ViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_community,parent,false);

        return new ViewHoler(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHoler holder, int position) {
        Result list = results.get(position);
        holder.title.setText("title : " + list.getTitle());
        holder.content.setText("content : " + list.getBody());
        holder.time.setText("time : " + list.getId());
        holder.comment.setText("comment : " + list.getUserId());
    }

    @Override
    public int getItemCount() {
        return results !=null ? results.size() : 0;
    }

    class ViewHoler extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView time;
        TextView comment;

        public ViewHoler(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_title);
            content = itemView.findViewById(R.id.item_content);
            time = itemView.findViewById(R.id.item_time);
            comment = itemView.findViewById(R.id.item_comment);
        }
    }
}
