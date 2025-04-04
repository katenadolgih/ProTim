//package org.hse.protim.pages;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {
//    private List<Course> courses;
//
//    public CoursesAdapter(List<Course> courses) {
//        this.courses = courses;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        Course course = courses.get(position);
//        holder.title.setText(course.getTitle());
//        holder.tags.setText(course.getTags());
//        holder.date.setText(course.getDate());
//        holder.price.setText(course.getPrice());
//    }
//
//    @Override
//    public int getItemCount() {
//        return courses.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView title, tags, date, price;
//
//        public ViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tags = itemView.findViewById(R.id.courseTags);
//            title = itemView.findViewById(R.id.courseTitle);
//            date = itemView.findViewById(R.id.courseDate);
//            price = itemView.findViewById(R.id.coursePrice);
//        }
//    }
//}
//