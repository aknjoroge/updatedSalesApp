package com.example.ecommerce;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class checkoutviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

    public TextView txtname,txtprice,txtamount;
    itemclicklistener listener;


    public checkoutviewholder(@NonNull View itemView) {
        super(itemView);

        txtname = itemView.findViewById(R.id.checkoutitemname);
        txtprice = itemView.findViewById(R.id.checkoutitemprice);
        txtamount = itemView.findViewById(R.id.checkoutitemamount);


    }
    public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

    }
}




