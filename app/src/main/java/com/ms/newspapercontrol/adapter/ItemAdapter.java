package com.ms.newspapercontrol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.R;
import com.ms.newspapercontrol.entities.Item;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {

    private List<Item> itemList;
    private final OnItemListener ON_ITEM_LISTENER;

    public ItemAdapter(List<Item> itemList, OnItemListener onItemListener) {
        this.itemList = itemList;
        this.ON_ITEM_LISTENER = onItemListener;
    }

    public void setItemList(List<Item> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view, parent, false);
        return new ViewHolder(view, this.ON_ITEM_LISTENER);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if (position == 0) {
//            holder.etReturnItemView.requestFocus();
//        }
        String name = this.itemList.get(position).getItemName();
        String type = this.itemList.get(position).getItemCollectable() == 1 ? "Coleccionable" : "";

        holder.tvItemName.setText(name);
        holder.tvItemCollectable.setText(type);
        if (type.isEmpty()) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.tvItemCollectable.getLayoutParams();
            layoutParams.height = 1;
            holder.tvItemCollectable.setLayoutParams(layoutParams);
        }

//        if (this.itemList.get(position).getItemCollectable() == 0) {
//            holder.btReturnCollectable.setText("");
//            holder.btReturnCollectable.setEnabled(false);
//            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.btReturnCollectable.getLayoutParams();
//            layoutParams.width = 1;
//            holder.btReturnCollectable.setLayoutParams(layoutParams);
//        }

//        holder.etReturnItemView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                String quantity = holder.etReturnItemView.getText().toString();
//                if (!quantity.isEmpty()) {
//                    itemList.get(holder.getAdapterPosition()).setItemQuantityDelivered(Integer.parseInt(quantity));
//                } else {
//
//                }
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final OnItemListener ON_ITEM_LISTENER;
        private final TextView tvItemName, tvItemCollectable;

        public ViewHolder(@NonNull View itemView, OnItemListener onItemListener) {
            super(itemView);
            this.ON_ITEM_LISTENER = onItemListener;
            this.tvItemName = itemView.findViewById(R.id.tvItemName);
            this.tvItemCollectable = itemView.findViewById(R.id.tvItemCollectable);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.ON_ITEM_LISTENER.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemListener {
        void onItemClick(int position);
    }
}
