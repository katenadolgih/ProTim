package org.hse.protim.pages;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.hse.protim.R;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SelectionAdapter extends RecyclerView.Adapter<SelectionAdapter.SelectionViewHolder> {

    private static final int TYPE_DEFAULT = 0;
    private static final int TYPE_POPUP = 1;

    private List<Selection> selectionList;
    private boolean usePopupLayout;
    private Set<Integer> selectedPositions = new HashSet<>(); // Для хранения отмеченных чекбоксов

    public SelectionAdapter(List<Selection> selectionList, boolean usePopupLayout) {
        this.selectionList = selectionList;
        this.usePopupLayout = usePopupLayout;
    }

    @Override
    public int getItemViewType(int position) {
        return usePopupLayout ? TYPE_POPUP : TYPE_DEFAULT;
    }

    @NonNull
    @Override
    public SelectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutId = (viewType == TYPE_POPUP)
                ? R.layout.item_selection_for_popup
                : R.layout.item_selection;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new SelectionViewHolder(view, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectionViewHolder holder, int position) {
        Selection selection = selectionList.get(position);
        holder.title.setText(selection.getTitle());

        if (holder.viewType == TYPE_DEFAULT && holder.description != null) {
            holder.description.setText(selection.getDescription());
            holder.itemView.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, SelectionDetailsPage.class);
                context.startActivity(intent);
            });
        } else if (holder.viewType == TYPE_POPUP && holder.checkBox != null) {
            holder.checkBox.setChecked(selectedPositions.contains(position));
            holder.checkBox.setOnClickListener(v -> {
                if (holder.checkBox.isChecked()) {
                    selectedPositions.add(position);
                } else {
                    selectedPositions.remove(position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return selectionList.size();
    }

    public Set<Integer> getSelectedPositions() {
        return selectedPositions;
    }

    static class SelectionViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView description;
        CheckBox checkBox;
        int viewType;

        public SelectionViewHolder(@NonNull View itemView, int viewType) {
            super(itemView);
            this.viewType = viewType;
            title = itemView.findViewById(R.id.selectionName);
            if (viewType == TYPE_DEFAULT) {
                description = itemView.findViewById(R.id.selectionDescription);
            } else {
                checkBox = itemView.findViewById(R.id.checkbox_inspiration);
            }
        }
    }
}
