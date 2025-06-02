package org.hse.protim.pages;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.hse.protim.DTO.collection.CollectionDTO;
import org.hse.protim.R;
import org.hse.protim.clients.retrofit.RetrofitProvider;
import org.hse.protim.clients.retrofit.collection.CollectionClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SelectionBottomSheet extends BottomSheetDialogFragment {

    private TextView addSelection;
    private RecyclerView selectionsRecycler;
    private List<CollectionDTO> selections = new ArrayList<>();
    private RetrofitProvider retrofitProvider;
    private CollectionClient collectionClient;

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        // 1) Получаем адаптер
        RecyclerView.Adapter adapter = selectionsRecycler.getAdapter();
        if (adapter instanceof SelectionAdapter) {
            SelectionAdapter selectionAdapter = (SelectionAdapter) adapter;

            // 2) Достаём список выбранных DTO
            List<CollectionDTO> chosen = selectionAdapter.getSelectedCollections();

            // 3) Передаём этот список тому, кто слушает (см. следующий шаг)
            if (listener != null) {
                listener.onSelectionConfirmed(chosen);
            }
        }
    }

    private OnSelectionConfirmedListener listener;
    public void setOnSelectionConfirmedListener(OnSelectionConfirmedListener listener) {
        this.listener = listener;
    }

    /**
     * Интерфейс, через который мы сообщаем Activity/Fragment о том,
     * какие коллекции были выбраны пользователем.
     */
    public interface OnSelectionConfirmedListener {
        void onSelectionConfirmed(List<CollectionDTO> selectedCollections);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.selection_popup, container, false);

        init(view);
        handle();
        loadSampleData();

        return view;
    }

    private void init(View view) {
        retrofitProvider = new RetrofitProvider(view.getContext());
        collectionClient = new CollectionClient(retrofitProvider);
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
        collectionClient.getAllCollections(new CollectionClient.GetAllCollectionCallback() {
            @Override
            public void onSuccess(List<CollectionDTO> allCollections) {
                selections.addAll(allCollections);
                setupAdapter();
            }

            @Override
            public void onError(String message) {
                requireActivity().runOnUiThread(() -> Toast.makeText(requireContext(),
                        message, Toast.LENGTH_LONG).show());
            }
        });
    }

    private void setupAdapter() {
        selectionsRecycler.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false));
        selectionsRecycler.setAdapter(new SelectionAdapter(selections, true));
    }
}
