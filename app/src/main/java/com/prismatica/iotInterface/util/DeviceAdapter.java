package com.prismatica.iotInterface.util;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.prismatica.iotInterface.R;
import com.prismatica.iotInterface.data.Item;
import java.util.List;

public class DeviceAdapter extends RecyclerView.Adapter<DeviceAdapter.VH> {
    public interface Listener {
        void onEdit(Item item);
        void onDelete(Item item);
    }

    private List<Item> data;
    private final Listener listener;

    public DeviceAdapter(List<Item> data, Listener listener) {
        this.data = data;
        this.listener = listener;
    }

    public void setData(List<Item> newData) {
        this.data = newData;
        notifyDataSetChanged();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView tvName;
        TextView tvQty;
        ImageButton btnEdit;
        ImageButton btnDelete;
        VH(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvQty = v.findViewById(R.id.tvQty);
            btnEdit = v.findViewById(R.id.btnEdit);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }

    @NonNull @Override public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false);
        return new VH(v);
    }

    @Override public void onBindViewHolder(@NonNull VH h, int pos) {
        Item it = data.get(pos);
        h.tvName.setText(it.name);
        h.tvQty.setText(String.valueOf(it.qty));
        h.btnEdit.setOnClickListener(v -> listener.onEdit(it));
        h.btnDelete.setOnClickListener(v -> listener.onDelete(it));
    }

    @Override public int getItemCount() { return data == null ? 0 : data.size(); }
}
