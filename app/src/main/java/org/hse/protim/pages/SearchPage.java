//package org.hse.protim.pages;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.List;
//
//public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.ViewHolder> {
//    private Context context;
//    private List<Integer> images;
//
//    public SliderAdapter(Context context, List<Integer> images) {
//        this.context = context;
//        this.images = images;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(context).inflate(R.layout.item_slider, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.imageView.setImageResource(images.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return images.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        ImageView imageView;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            imageView = itemView.findViewById(R.id.image);
//        }
//    }
//}
//
