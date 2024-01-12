package com.example.neweditor.newa;

//public class NewAdapter {
//}

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.neweditor.R;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
    private ArrayList<User> languageRVModalArrayList;
    ItemClickListener itemClickListener;
    Context context ;

    public NewAdapter(ArrayList<User> languageRVModalArrayList, Context context,ItemClickListener itemClickListener) {
        this.languageRVModalArrayList = languageRVModalArrayList;
        this.itemClickListener = itemClickListener ;
        this.context = context ;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.new_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // on below line we are setting text to our text view.
        holder.lngTV.setText(languageRVModalArrayList.get(position).getName());
        holder.imageview.setImageResource(languageRVModalArrayList.get(position).getNameofpainter());

        Glide.with(context)
                .load(languageRVModalArrayList.get(position).getNameofpainter())
                .into(holder.imageview);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(holder.itemView, position);

            }
        });

    }

    @Override
    public int getItemCount() {
        return languageRVModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // on below line we are creating variable.
        private TextView lngTV;
        private ImageView imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // on below line we are initialing our variable.
            lngTV = itemView.findViewById(R.id.textView);
            imageview = itemView.findViewById(R.id.imageview);
        }
    }
}
