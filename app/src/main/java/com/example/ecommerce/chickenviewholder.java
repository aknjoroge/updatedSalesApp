package com.example.ecommerce;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class chickenviewholder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtpname,txtpdescription;
    public ImageView pimage;
    itemclicklistener listener;

    public chickenviewholder(@NonNull View itemView) {
        super(itemView);
        txtpname = itemView.findViewById(R.id.chickenanme);
        pimage = itemView.findViewById(R.id.chickendp);
        txtpdescription=itemView.findViewById(R.id.dproductdescription);


    }
    public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

    }
}