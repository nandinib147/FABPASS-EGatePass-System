package com.example.egatepass;

import android.content.Context;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class RequestAdapter
        extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;

    ArrayList<RequestModel> list;

    public RequestAdapter(
            Context context,
            ArrayList<RequestModel> list
    ) {

        this.context = context;

        this.list = list;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(context)
                        .inflate(
                                R.layout.request_item,
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

        RequestModel request =
                list.get(position);

        holder.txtInfo.setText(

                "Request ID : " + request.id +

                        "\n\nStudent : " + request.studentName +

                        "\n\nDepartment : " + request.department +

                        "\n\nYear : " +

                        (
                                request.year != null
                                        ? request.year
                                        : "N/A"
                        )

                        +

                        "\n\nDivision : " +

                        (
                                request.division != null
                                        ? request.division
                                        : "N/A"
                        )

                        +

                        "\n\nReason : " + request.reason +

                        "\n\nDate : " + request.date +

                        "\n\nStatus : " + request.status

        );

        // APPROVE

        holder.btnApprove.setOnClickListener(v -> {

            FirebaseDatabase.getInstance()
                    .getReference("Requests")
                    .child(request.id)
                    .child("status")
                    .setValue("Final Approved");

            Toast.makeText(
                    context,
                    "Approved",
                    Toast.LENGTH_SHORT
            ).show();

        });

        // REJECT

        holder.btnReject.setOnClickListener(v -> {

            FirebaseDatabase.getInstance()
                    .getReference("Requests")
                    .child(request.id)
                    .child("status")
                    .setValue("Final Rejected");

            Toast.makeText(
                    context,
                    "Rejected",
                    Toast.LENGTH_SHORT
            ).show();

        });

    }

    @Override
    public int getItemCount() {

        return list.size();

    }

    public static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtInfo;

        Button btnApprove,
                btnReject;

        public ViewHolder(
                @NonNull View itemView
        ) {

            super(itemView);

            txtInfo =
                    itemView.findViewById(
                            R.id.txtInfo
                    );

            btnApprove =
                    itemView.findViewById(
                            R.id.btnApprove
                    );

            btnReject =
                    itemView.findViewById(
                            R.id.btnReject
                    );

        }

    }

}