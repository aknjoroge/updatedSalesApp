package com.example.ecommerce;

import android.media.Image;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class categoryviewholder extends  RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView catname;
    public ImageView mainimage;
    itemclicklistener listener;

    public categoryviewholder(@NonNull View itemView) {
        super(itemView);
        catname=itemView.findViewById(R.id.categoryname);
        mainimage = itemView.findViewById(R.id.categoryimg);

    }
    public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

    }
}
