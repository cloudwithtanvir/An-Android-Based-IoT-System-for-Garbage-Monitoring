package com.fahimshahrierrasel.collectionnotifier.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fahimshahrierrasel.collectionnotifier.R;
import com.fahimshahrierrasel.collectionnotifier.model.Bin;

import java.util.List;
import java.util.Locale;

public class BinAdapter extends RecyclerView.Adapter<BinAdapter.ViewHolder> {

    private List<Bin> bins;
    private Listener listener;

    public BinAdapter(List<Bin> bins, Listener listener) {
        this.bins = bins;
        this.listener = listener;
    }

    public interface Listener {
        void onItemClick(Bin bin);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_bin, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(bins.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return bins.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Android Views
         **/
        ImageView imageViewBinLogo;
        TextView textViewBinName;
        TextView textViewBinCleanCount;
        TextView textViewBinStatus;

        /**
         * Android Views
         **/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindViews(itemView);
        }

        public void bind(Bin bin, final Listener listener) {
            textViewBinName.setText(bin.getName());
            textViewBinCleanCount.setText(String.format(Locale.US, "Cleaned %d times", bin.getCount()));
            textViewBinStatus.setText(String.format("Status: %s", bin.getStatus().toUpperCase()));
            itemView.setOnClickListener(view -> listener.onItemClick(bin));
        }

        /**
         * Binds XML views
         * Call this function after layout is ready.
         **/
        private void bindViews(View rootView) {
            imageViewBinLogo = rootView.findViewById(R.id.image_view_bin_logo);
            textViewBinName = rootView.findViewById(R.id.text_view_bin_name);
            textViewBinCleanCount = rootView.findViewById(R.id.text_view_bin_clean_count);
            textViewBinStatus = rootView.findViewById(R.id.text_view_bin_status);
        }
    }
}
