package com.example.ecommerce;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cartviewholder extends RecyclerView.ViewHolder implements View.OnClickListener{

public TextView txtpname,txtpprice,txtpamount;
        itemclicklistener listener;
        Button remove;

public cartviewholder(@NonNull View itemView) {
        super(itemView);

        txtpname = itemView.findViewById(R.id.cartproductname);
        txtpprice = itemView.findViewById(R.id.cartproductprice);
        remove=itemView.findViewById(R.id.cartremovebtn);

        txtpamount=itemView.findViewById(R.id.cartproductquantity);


        }
public void setItemClickListener(itemclicklistener listener){
        this.listener =listener;
        }

@Override
public void onClick(View v) {
        listener.onclick(v,getAdapterPosition(),false);

        }
        }
