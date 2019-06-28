package com.example.dailysmarts.recyclerView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailysmarts.R;
import com.example.dailysmarts.database.SmartEntity;

public class SmartViewHolder extends RecyclerView.ViewHolder {

    private TextView smart;
    private TextView author;
    private SmartEntity smartEntity;


    public SmartViewHolder(@NonNull View itemView, final ItemListener listener) {
        super(itemView);
        this.smart = itemView.findViewById(R.id.txt_smart);
        this.author = itemView.findViewById(R.id.txt_author);
        initLikeListener(listener);
        initShareListener(listener);
    }

    public TextView getSmart() {
        return smart;
    }

    public void setSmart(TextView smart) {
        this.smart = smart;
    }

    public TextView getAuthor() {
        return author;
    }

    public void setAuthor(TextView author) {
        this.author = author;
    }

    public SmartEntity getSmartEntity() {
        return smartEntity;
    }

    public void setSmartEntity(SmartEntity smartEntity) {
        this.smartEntity = smartEntity;
    }

    private void initLikeListener(final ItemListener listener) {
        ImageButton imageButton = itemView.findViewById(R.id.btn_like);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(smartEntity, imageButton);
            }
        });
    }

    private void initShareListener(final ItemListener listener) {
        ImageButton imageButton = itemView.findViewById(R.id.btn_share);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClicked(smartEntity, imageButton);
            }
        });
    }

    public interface ItemListener {
        void onItemClicked(SmartEntity entity, ImageButton imageButton);
    }
}
