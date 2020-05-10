package com.csedevs.covid_riskmangement;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

class CentersViewHolder extends RecyclerView.ViewHolder {
    public TextView tv_center;
    public CentersViewHolder(@NonNull View itemView) {
        super(itemView);
        tv_center=itemView.findViewById(R.id.center);
    }
}
