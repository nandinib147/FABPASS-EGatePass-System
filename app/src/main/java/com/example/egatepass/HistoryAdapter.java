package com.example.egatepass;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    ArrayList<HistoryModel> list;

    public HistoryAdapter(ArrayList<HistoryModel> list) {

        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(
                parent.getContext()
        ).inflate(

                R.layout.history_item,

                parent,

                false

        );

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        HistoryModel model =
                list.get(position);

        holder.reason.setText(
                model.getReason()
        );

        holder.date.setText(
                model.getDate()
        );

        holder.status.setText(
                model.getStatus()
        );

        String status =
                model.getStatus();

        if(status.contains("Approved")) {

            holder.status.setBackgroundColor(

                    android.graphics.Color.parseColor(
                            "#DCFCE7"
                    )
            );

            holder.status.setTextColor(

                    android.graphics.Color.parseColor(
                            "#166534"
                    )
            );

        }

        else if(status.contains("Rejected")) {

            holder.status.setBackgroundColor(

                    android.graphics.Color.parseColor(
                            "#FEE2E2"
                    )
            );

            holder.status.setTextColor(

                    android.graphics.Color.parseColor(
                            "#991B1B"
                    )
            );

        }

        else {

            holder.status.setBackgroundColor(

                    android.graphics.Color.parseColor(
                            "#FEF3C7"
                    )
            );

            holder.status.setTextColor(

                    android.graphics.Color.parseColor(
                            "#92400E"
                    )
            );

        }

    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView reason,
                date,
                status;

        public ViewHolder(@NonNull View itemView) {

            super(itemView);

            reason = itemView.findViewById(
                    R.id.historyReason
            );

            date = itemView.findViewById(
                    R.id.historyDate
            );

            status = itemView.findViewById(
                    R.id.historyStatus
            );

        }

    }

}