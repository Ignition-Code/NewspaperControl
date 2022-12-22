package com.ms.newspapercontrol.adapter;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.R;
import com.ms.newspapercontrol.entities.Delivery;
import com.ms.newspapercontrol.entities.Reception;

import java.util.ArrayList;
import java.util.List;

public class DeliveryAdapter extends RecyclerView.Adapter<DeliveryAdapter.ViewHolder> {

    private List<Reception> receptionList;
    private List<Delivery> deliveryList;

    public DeliveryAdapter(List<Reception> receptionList) {
        this.receptionList = receptionList;
        this.deliveryList = new ArrayList<>();
    }

    public void setReceptionList(List<Reception> receptionList) {
        this.receptionList = receptionList;
        for (int i = 0; i < receptionList.size(); i++) {
            deliveryList.add(
                    new Delivery(
                            null,
                            null,
                            null,
                            null,
                            null,
                            null,
                            receptionList.get(i).getReceptionID()
                    )
            );
        }
    }

    public List<Delivery> getDeliveryList() {
        return this.deliveryList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.delivery_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final int p = position;
        String name = this.receptionList.get(p).getReceptionProductName();
        holder.tvItemNameDeliveryItem.setText(name);

        holder.etDeliveryNumberDeliveryItem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String number = holder.etDeliveryNumberDeliveryItem.getText().toString();
                if (number.isEmpty()) {
                    deliveryList.get(p).setDeliveryItemQuantityDelivered(null);
                } else {
                    deliveryList.get(p).setDeliveryItemQuantityDelivered(Integer.parseInt(number));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return receptionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private EditText etDeliveryNumberDeliveryItem;
        private TextView tvItemNameDeliveryItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            etDeliveryNumberDeliveryItem = itemView.findViewById(R.id.etDeliveryNumberDeliveryItem);
            tvItemNameDeliveryItem = itemView.findViewById(R.id.tvItemNameDeliveryItem);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}
