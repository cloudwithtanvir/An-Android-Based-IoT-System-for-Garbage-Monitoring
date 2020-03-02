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
import com.fahimshahrierrasel.collectionnotifier.model.Option;

import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private List<Option> options;
    private Listener listener;

    public OptionAdapter(List<Option> options, Listener listener) {
        this.options = options;
        this.listener = listener;
    }

    public interface Listener {
        void onOptionClick(Option option);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_option, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.bind(options.get(i), listener);
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        /**
         * Android Views
         **/
        ImageView textViewOptionLogo;
        TextView textViewOptionTitle;
        TextView textViewOptionText;

        /**
         * Android Views
         **/

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            bindViews(itemView);
        }

        public void bind(Option option, Listener listener) {
            textViewOptionLogo.setImageDrawable(itemView.getContext()
                    .getResources()
                    .getDrawable(option.getImage()));
            textViewOptionTitle.setText(option.getTitle());
            textViewOptionText.setText(option.getText());

            itemView.setOnClickListener(view -> listener.onOptionClick(option));
        }

        /**
         * Binds XML views
         * Call this function after layout is ready.
         **/
        private void bindViews(View rootView) {
            textViewOptionLogo = rootView.findViewById(R.id.text_view_option_logo);
            textViewOptionTitle = rootView.findViewById(R.id.text_view_option_title);
            textViewOptionText = rootView.findViewById(R.id.text_view_option_text);
        }
    }
}
