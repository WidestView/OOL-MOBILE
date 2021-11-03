package com.example.ool_mobile.ui.util.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.util.UiDate;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PendingPhotoshootAdapter extends RecyclerView.Adapter<PendingPhotoshootAdapter.ViewHolder> {

    public static final int DUE_PHOTOSHOOT = 1;
    public static final int AHEAD_PHOTOSHOOT = 0;
    @NonNull
    private final List<Photoshoot> photoshootList;
    @NonNull
    private final Consumer<Photoshoot> clickListener;

    public PendingPhotoshootAdapter(
            @NonNull List<Photoshoot> photoshootList,
            @NonNull Consumer<Photoshoot> clickListener
    ) {

        Objects.requireNonNull(photoshootList, "photoshootList is null");
        Objects.requireNonNull(clickListener, "clickListener is null");

        this.photoshootList = photoshootList;
        this.clickListener = clickListener;
    }

    @Override
    public int getItemViewType(int position) {

        Photoshoot photoshoot = photoshootList.get(position);

        return photoshoot.startTime().before(new Date())
                ? DUE_PHOTOSHOOT
                : AHEAD_PHOTOSHOOT;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        int layout = viewType == DUE_PHOTOSHOOT
                ? R.layout.row_pending_photoshoot
                : R.layout.row_due_pending_photoshoot;

        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(photoshootList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoshootList.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleTextView;

        private final TextView dateTextView;

        private final Button viewButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleTextView = itemView.findViewById(R.id.pendingPhotoshootRow_titleTextView);
            dateTextView = itemView.findViewById(R.id.pendingPhotoshootRow_dateTextView);
            viewButton = itemView.findViewById(R.id.pendingPhotoshootRow_viewButton);
        }

        public void bind(@NonNull Photoshoot photoshoot) {

            // todo: replace address here with photoshoot -> order -> package -> name
            titleTextView.setText(photoshoot.address());

            bindPhotoshootDate(photoshoot.startTime());

            viewButton.setOnClickListener(listener -> clickListener.accept(photoshoot));
        }

        private void bindPhotoshootDate(Date date) {
            dateTextView.setText(new UiDate(itemView.getContext()).formatDateName(date));
        }
    }
}
