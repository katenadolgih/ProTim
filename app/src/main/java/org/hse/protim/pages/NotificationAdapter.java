package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.hse.protim.DTO.notification.NotificationDTO;
import org.hse.protim.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationDTO> notificationList;

    public NotificationAdapter(List<NotificationDTO> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImage;
        TextView nameText, timeText;

        public ViewHolder(View view) {
            super(view);
            avatarImage = view.findViewById(R.id.avatar);
            nameText = view.findViewById(R.id.notification_text);
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
        NotificationDTO item = notificationList.get(position);
        Context currContext = holder.itemView.getContext();

        Glide.with(currContext)
                .load(item.photoPath())
                .error(R.drawable.ic_profile)
                .placeholder(R.drawable.ic_profile)
                .into(holder.avatarImage);

        String[] s = item.text().split(" ");
        String fullName = String.format("%s %s", s[0], s[1]);

        SpannableString spannable = new SpannableString(item.text());

        // Обработчик клика по имени
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
                Intent intent = new Intent(currContext, ProfilePage.class);
                intent.putExtra("fromPage", "specialist");
                intent.putExtra("userId", item.userId());
                currContext.startActivity(intent); 
            }
            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.parseColor("#1A73E8"));
                ds.setUnderlineText(true);
            }
        };

        spannable.setSpan(clickableSpan, 0, fullName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        holder.nameText.setMovementMethod(LinkMovementMethod.getInstance());
        holder.nameText.setHighlightColor(Color.TRANSPARENT);
        holder.nameText.setText(spannable);

        holder.timeText.setText(item.time());
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

