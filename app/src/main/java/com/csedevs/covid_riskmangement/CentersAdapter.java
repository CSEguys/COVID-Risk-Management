package com.csedevs.covid_riskmangement;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CentersAdapter extends RecyclerView.Adapter<CentersViewHolder> {

    private List<CenterData> mlist;
    public  CentersAdapter(List<CenterData> mList){

        this.mlist=mList;
    }

    @NonNull
    @Override
    public CentersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view =inflater.inflate(R.layout.list_item,null);


        return new CentersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CentersViewHolder holder, int position) {
        CenterData data= mlist.get(position);
        holder.tv_center.setText(data.getName());

    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }


}
