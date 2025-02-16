package com.ronak.expensetracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ronak.expensetracker.Model.listmodel;
import com.ronak.expensetracker.R;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyView> {
    Context context;
    ArrayList<listmodel>list;

    public ListAdapter(Context context, ArrayList<listmodel> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("ResourceType")
        View view= LayoutInflater.from(context).inflate(R.layout.listmodel_desing,null);
        return  new MyView(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        listmodel l=list.get(position);
        date.setText(l.getDate());
        time.setText(l.getTime());
        note.setText(l.getNotes());
        if(l.getType().equals("IN")) {
            amount.setTextColor(ContextCompat.getColor(context, R.color.green));
        }else{
            amount.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        amount.setText(l.getAmount());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
        TextView date,time,note,amount;
    public class MyView extends RecyclerView.ViewHolder {

        public MyView(@NonNull View itemView) {
            super(itemView);
            date=itemView.findViewById(R.id.date_desing);
            time=itemView.findViewById(R.id.time_desing);
            note=itemView.findViewById(R.id.note_desing);
            amount=itemView.findViewById(R.id.amount_desing);
        }
    }
}
