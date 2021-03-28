package com.example.ecommerce;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class offerviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView txtpname,txtpprice,txtpdescription,txtoldprice;
    public ImageView pimage;
    itemclicklistener listener;

    public offerviewholder(@NonNull View itemView) {
        super(itemView);
        txtpname = itemView.findViewById(R.id.dproductname);
        txtoldprice = itemView.findViewById(R.id.dproductoldprice);
        txtpprice = itemView.findViewById(R.id.dproductprice);
        pimage = itemView.findViewById(R.id.dproductimage);
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


