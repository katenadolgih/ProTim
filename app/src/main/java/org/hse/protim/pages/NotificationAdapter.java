package org.hse.protim.pages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationItem> notificationList;

    public NotificationAdapter(List<NotificationItem> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nameText, projectText, timeText;

        public ViewHolder(View view) {
            super(view);
            avatarImage = view.findViewById(R.id.avatar);
            nameText = view.findViewById(R.id.notification_text);
            projectText = view.findViewById(R.id.project_name);
            timeText = view.findViewById(R.id.time_text);
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NotificationItem item = notificationList.get(position);
        holder.avatarImage.setImageResource(item.getAvatarResId());

        String name = item.getFullName();
        String fullText = name + " оценил-(а) ваш проект";

        SpannableString spannable = new SpannableString(fullText);
        spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#1A73E8")), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new UnderlineSpan(), 0, name.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.nameText.setText(spannable);
        holder.projectText.setText("“" + item.getProjectName() + "”");
        holder.timeText.setText("11:59");
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

