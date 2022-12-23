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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.R;
import com.ms.newspapercontrol.entities.Delivery;

import java.util.List;

public class ReturnAdapter extends RecyclerView.Adapter<ReturnAdapter.ViewHolder> {
    private List<Delivery> deliveryList;

    public ReturnAdapter(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public void setDeliveryList(List<Delivery> deliveryList) {
        this.deliveryList = deliveryList;
    }

    public List<Delivery> getDeliveryList() {
        return this.deliveryList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == 0) {
            holder.etItemNumberReturn.requestFocus();
        }
        String name = this.deliveryList.get(position).getItemName();
        String number = this.deliveryList.get(position).getDeliveryItemQuantityDelivered().toString();
        String type = this.deliveryList.get(position).getItemCollectable() == 1 ? "Coleccionable" : "";

        System.out.println("----------------");
        System.out.println(this.deliveryList.get(position).toString());

        holder.tvItemNameReturn.setText(name);
        holder.tvItemNumberReturn.setText(number);

        if (type.isEmpty()) {
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.btReturnCollectable.getLayoutParams();
            layoutParams.height = 1;
            holder.btReturnCollectable.setLayoutParams(layoutParams);
        }

        if (this.deliveryList.get(position).getItemCollectable() == 0) {
            holder.btReturnCollectable.setText("");
            holder.btReturnCollectable.setEnabled(false);
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) holder.btReturnCollectable.getLayoutParams();
            layoutParams.width = 1;
            holder.btReturnCollectable.setLayoutParams(layoutParams);
        }

        holder.etItemNumberReturn.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String quantity = holder.etItemNumberReturn.getText().toString();
                if (!quantity.isEmpty()) {
                    deliveryList.get(holder.getAdapterPosition()).setDeliveryItemAmountRefunded(Integer.parseInt(quantity));
                } else {
                    deliveryList.get(holder.getAdapterPosition()).setDeliveryItemAmountRefunded(null);
                }
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_return, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return deliveryList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private final Button btReturnCollectable;
        private final EditText etItemNumberReturn;
        private final TextView tvItemNameReturn, tvItemNumberReturn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.btReturnCollectable = itemView.findViewById(R.id.btReturnCollectable);
            this.etItemNumberReturn = itemView.findViewById(R.id.etItemNumberReturn);
            this.tvItemNameReturn = itemView.findViewById(R.id.tvItemNameReturn);
            this.tvItemNumberReturn = itemView.findViewById(R.id.tvItemNumberReturn);
        }
    }
}
