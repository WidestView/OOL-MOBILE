package com.example.ool_mobile.ui.list.photoshoot;

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
import com.example.ool_mobile.ui.util.adapter.AdapterParameters;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class PhotoshootRowAdapter extends RecyclerView.Adapter<PhotoshootRowAdapter.ViewHolder> {

    @NonNull
    private final AdapterParameters<Photoshoot> parameters;

    public PhotoshootRowAdapter(@NonNull AdapterParameters<Photoshoot> parameters) {
        this.parameters = parameters;
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
        holder.bind(parameters.items().get(position));
    }

    @Override
    public int getItemCount() {
        return parameters.items().size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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

        // todo: use order package name instead of address or "unknown"

        public void bind(@NonNull Photoshoot photoshoot) {

            parameters.bindRowEvents(contentRow, photoshoot);


            contentRow.getTitleTextView().setText(
                    photoshoot.address()
            );

            Context context = requireContext();

            contentRow.getStatusTextView().setText(
                    new UiDate(context).formatShortDate(photoshoot.startTime())
            );

            idField.getValueTextView().setText(photoshoot.resourceId().toString());

            orderIdField.getValueTextView().setText(
                    String.format(
                            context.getString(R.string.format_id_name), photoshoot.orderId(),
                            "unknown"
                    )
            );

            addressField.getValueTextView().setText(photoshoot.address());

            startTimeField.getValueTextView().setText(
                    new UiDate(context).formatDateTime(photoshoot.startTime())
            );

            durationField.getValueTextView().setText(
                    String.format(context.getString(R.string.format_duration_minutes),
                            photoshoot.durationMinutes()
                    )
            );
        }

        @NotNull
        private Context requireContext() {
            return Objects.requireNonNull(
                    itemView.getContext(),
                    "itemViewContext must not be null"
            );
        }

    }
}
