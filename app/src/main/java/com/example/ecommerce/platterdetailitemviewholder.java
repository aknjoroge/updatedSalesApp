package com.example.ecommerce;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class platterdetailitemviewholder  extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtpname,txtamount;

    itemclicklistener listener;

    public platterdetailitemviewholder(@NonNull View itemView) {
        super(itemView);
        txtpname = itemView.findViewById(R.id.platteritemname);
        txtamount = itemView.findViewById(R.id.platteritemamount);


    }
    public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
    }

    @Override
    public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

    }
}
