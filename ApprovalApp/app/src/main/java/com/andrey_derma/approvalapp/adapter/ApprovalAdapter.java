package com.andrey_derma.approvalapp.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.andrey_derma.approvalapp.R;
import com.andrey_derma.approvalapp.model.ApprovalRequestDO;

import java.util.ArrayList;
import java.util.List;

public class ApprovalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<ApprovalRequestDO> items = new ArrayList<>();

    private Context ctx;
    private ClickListener mClickListener;

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        mClickListener = clickListener;
    }


    public ApprovalAdapter(Context context, List<ApprovalRequestDO> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView name;
        public TextView status;
        public TextView date;

        public OriginalViewHolder(View v) {
            super(v);
            name = v.findViewById(R.id.name);
            date = v.findViewById(R.id.date);
            status = v.findViewById(R.id.status);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_approval, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            OriginalViewHolder view = (OriginalViewHolder) holder;

            ApprovalRequestDO p = items.get(position);
            view.name.setText(p.get_subject());
            view.date.setText(p.get_timestamp());
            view.status.setText(p.getStatus());

        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
