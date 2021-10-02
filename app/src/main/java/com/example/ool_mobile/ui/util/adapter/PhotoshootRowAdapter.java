package com.example.ool_mobile.ui.util.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ool_mobile.R;
import com.example.ool_mobile.model.Photoshoot;
import com.example.ool_mobile.ui.component.ContentRow;
import com.example.ool_mobile.ui.component.ContentRowField;
import com.example.ool_mobile.ui.util.UiDate;

import java.util.List;
import java.util.Objects;

public class PhotoshootRowAdapter extends RecyclerView.Adapter<PhotoshootRowAdapter.ViewHolder> {

    @NonNull
    private final List<Photoshoot> photoshootList;

    public PhotoshootRowAdapter(@NonNull List<Photoshoot> photoshootList) {
        this.photoshootList = photoshootList;
    }

    @NonNull
    @Override
    public PhotoshootRowAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_photoshoot, parent, false);

        return new PhotoshootRowAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoshootRowAdapter.ViewHolder holder, int position) {
        holder.bind(photoshootList.get(position));
    }

    @Override
    public int getItemCount() {
        return photoshootList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final ContentRow contentRow;

        private final ContentRowField idField;
        private final ContentRowField orderIdField;
        private final ContentRowField addressField;
        private final ContentRowField startTimeField;
        private final ContentRowField durationField;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            contentRow = itemView.findViewById(R.id.photoshootRow_contentRow);
            idField = itemView.findViewById(R.id.photoshootRow_idField);
            orderIdField = itemView.findViewById(R.id.photoshootRow_orderIdField);
            addressField = itemView.findViewById(R.id.photoshootRow_addressField);
            startTimeField = itemView.findViewById(R.id.photoshootRow_startTimeField);
            durationField = itemView.findViewById(R.id.photoshootRow_durationField);
        }

        public void bind(@NonNull Photoshoot photoshoot) {

            Context context = Objects.requireNonNull(
                    itemView.getContext(),
                    "itemViewContext must not be null"
            );

            // todo: use order name instead of address

            contentRow.getTitleTextView().setText(
                    photoshoot.address()
            );

            contentRow.getStatusTextView().setText(
                    new UiDate(context).formatShortDate(photoshoot.startTime())
            );

            idField.getValueTextView().setText(photoshoot.resourceId().toString());

            orderIdField.getValueTextView().setText(
                    String.format(
                            context.getString(R.string.format_id_name), photoshoot.orderId(),
                            "beep"
                    )
            );

            addressField.getValueTextView().setText(photoshoot.address());

            startTimeField.getValueTextView().setText(
                    new UiDate(context).formatDateTime(photoshoot.startTime())
            );

            // todo: replace "unknown" with proper photoshoot order id
            durationField.getValueTextView().setText(
                    String.format(context.getString(R.string.format_duration_minutes),
                            photoshoot.durationMinutes()
                    )
            );
        }

    }
}
