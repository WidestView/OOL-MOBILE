package com.example.ool_mobile.horizontal_package;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;

import java.util.List;
import java.util.Objects;

public class ElementAdapter extends RecyclerView.Adapter<ElementAdapter.ViewHolder> {
    List<ElementItem> items;


    public ElementAdapter(@NonNull List<ElementItem> items) {
        Objects.requireNonNull(items);
        this.items = items;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ElementItem item = items.get(position);
        holder.imageIcon.setImageDrawable(item.imageIcon);
        holder.textView.setText(item.nameOfItem);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageView imageIcon;

        public ViewHolder(View view) {
            super(view);


            textView = (TextView) view.findViewById(R.id.textViewName);
            imageIcon = (ImageView) view.findViewById(R.id.imageIcon);

        }
    }

}
