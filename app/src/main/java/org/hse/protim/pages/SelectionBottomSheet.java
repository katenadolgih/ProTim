package org.hse.protim.pages;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.hse.protim.R;

import java.util.ArrayList;
import java.util.List;

public class SelectionBottomSheet extends BottomSheetDialogFragment {

    private TextView addSelection;
    private RecyclerView selectionsRecycler;
    private List<Selection> selections = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selection_popup, container, false);

        init(view);
        handle();
        loadSampleData();
        setupAdapter();

        return view;
    }

    private void init(View view) {
        addSelection = view.findViewById(R.id.add_selection);
        selectionsRecycler = view.findViewById(R.id.selectionsRecycler);
    }

    private void handle() {
        addSelection.setOnClickListener(v -> {
            Intent intent = new Intent(requireContext(), SelectionCreatingPage.class);
            startActivity(intent);
            dismiss();
        });
    }

    private void loadSampleData() {
        selections.add(new Selection("Подборка 1", "Описание 1"));
        selections.add(new Selection("Подборка 2", "Описание 2"));
        selections.add(new Selection("Подборка 3", "Описание 3"));
    }

    private void setupAdapter() {
        selectionsRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        selectionsRecycler.setAdapter(new SelectionAdapter(selections, true));
    }
}
