package com.ms.newspapercontrol.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.R;
import com.ms.newspapercontrol.entities.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> itemList;

    public ItemAdapter(List<Item> itemList) {
        this.itemList = itemList;
    }

    public List<Item> getItemList() {
        return this.itemList;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = this.itemList.get(position).getItemName();
        String number = this.itemList.get(position).getItemQuantityDelivered().toString();

        holder.tvItemNameItemView.setText(name);
        holder.tvItemNumberItemView.setText(number);

        if (this.itemList.get(position).getItemCollectable() == 0) {
            holder.btReturnCollectable.setVisibility(View.INVISIBLE);
        }

        holder.etReturnItemView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantity = holder.etReturnItemView.getText().toString();
                if (!quantity.isEmpty()) {
                    itemList.get(holder.getAdapterPosition()).setItemQuantityDelivered(Integer.parseInt(quantity));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private final Button btReturnCollectable;
        private final EditText etReturnItemView;
        private final TextView tvItemNameItemView, tvItemNumberItemView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.btReturnCollectable = itemView.findViewById(R.id.btReturnCollectable);
            this.etReturnItemView = itemView.findViewById(R.id.etReturnItemView);
            this.tvItemNameItemView = itemView.findViewById(R.id.tvItemNameItemView);
            this.tvItemNumberItemView = itemView.findViewById(R.id.tvItemNumberItemView);
        }
    }
}
