package com.example.dailysmarts.recyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dailysmarts.R;
import com.example.dailysmarts.database.SmartEntity;

import java.util.ArrayList;
import java.util.List;

public class SmartAdapter extends RecyclerView.Adapter<SmartViewHolder> {

    private List<SmartEntity> smartEntities;
    private SmartViewHolder.ItemListener listener;

    public SmartAdapter(SmartViewHolder.ItemListener listener) {
        this.listener = listener;
        this.smartEntities = new ArrayList<>();
    }

    public List<SmartEntity> getSmartEntities() {
        return smartEntities;
    }

    public void setSmartEntities(List<SmartEntity> smartEntities) {
        this.smartEntities = smartEntities;
    }

    public SmartViewHolder.ItemListener getListener() {
        return listener;
    }

    public void setListener(SmartViewHolder.ItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public SmartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dayli_quote, parent, false);
        v.findViewById(R.id.btn_like).setBackgroundResource(R.drawable.ic_favorite_green_48px);

        SmartViewHolder svh = new SmartViewHolder(v, getListener());
        return svh;
    }

    @Override
    public void onBindViewHolder(@NonNull SmartViewHolder holder, int position) {

        SmartEntity smartEntity = smartEntities.get(position);
        holder.getSmart().setText(smartEntity.getQuoteText());
        holder.getAuthor().setText(smartEntity.getQuoteAuthor());
        holder.setSmartEntity(smartEntity);
    }

    @Override
    public int getItemCount() {
        return this.smartEntities.size();
    }
}
