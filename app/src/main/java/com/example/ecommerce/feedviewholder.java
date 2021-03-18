package com.example.ecommerce;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.like.LikeButton;

public class feedviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{
    ImageView profile;
    Button viewmore;
    TextView name,likesno;
    LikeButton like;

    ImageButton share,download;
    itemclicklistener listener;

    public feedviewholder(@NonNull View itemView) {
        super(itemView);

        profile = itemView.findViewById(R.id.imageView6);
        download=itemView.findViewById(R.id.downloadimg);
        viewmore = itemView.findViewById(R.id.feedmorebtn);
        likesno=(itemView).findViewById(R.id.feedlikeno);
        name=(itemView).findViewById(R.id.feednametxt);

        share=itemView.findViewById(R.id.feedshare);
        like=itemView.findViewById(R.id.feedlike);


    }
    public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

    }


}
