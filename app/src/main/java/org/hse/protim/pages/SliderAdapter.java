package org.hse.protim.pages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.List;

public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private final List<Integer> sliderItems;

    public SliderAdapter(List<Integer> sliderItems) {
        this.sliderItems = sliderItems;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_slider, parent, false);
        return new SliderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {
        holder.imageView.setImageResource(sliderItems.get(position));
    }

    @Override
    public int getItemCount() {
        return sliderItems.size();
    }

    static class SliderViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        SliderViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageViewSlide);
        }
    }
}
