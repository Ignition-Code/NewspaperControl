package com.ms.newspapercontrol.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ms.newspapercontrol.R;
import com.ms.newspapercontrol.entities.Newsboy;

import java.util.List;

public class NewsboyAdapter extends RecyclerView.Adapter<NewsboyAdapter.ViewHolder> {

    private final NewsboyListener NEWSBOY_LISTENER;
    private List<Newsboy> newsboyList;

    public NewsboyAdapter(List<Newsboy> newsboysList, NewsboyListener newsboyListener) {
        this.newsboyList = newsboysList;
        this.NEWSBOY_LISTENER = newsboyListener;
    }

    public void setNewsboyList(List<Newsboy> newsboyList) {
        this.newsboyList = newsboyList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String name = this.newsboyList.get(position).getNewsboyName();
        holder.tvNewsboyName.setText(name);
    }

    @Override
    public int getItemCount() {
        return this.newsboyList.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(R.layout.newsboy_item, parent, false);

        return new ViewHolder(view, this.NEWSBOY_LISTENER);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView ivNewsboyAvatar;
        private final TextView tvNewsboyName;
        private final NewsboyListener newsboyListener;

        public ViewHolder(@NonNull View itemView, NewsboyListener newsboyListener) {
            super(itemView);
            this.ivNewsboyAvatar = itemView.findViewById(R.id.ivNewsboyAvatar);
            this.tvNewsboyName = itemView.findViewById(R.id.tvNewsboyName);
            this.newsboyListener = newsboyListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            this.newsboyListener.onNewsboyClick(getAdapterPosition());
        }
    }

    public interface NewsboyListener {
        void onNewsboyClick(int position);
    }
}
