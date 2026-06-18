package com.example.egatepass;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HODDashboard extends AppCompatActivity {

    RecyclerView recyclerView;

    Button logoutBtn;

    ArrayList<RequestModel> list;

    RequestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_hoddashboard);

        recyclerView =
                findViewById(R.id.recyclerView);

        logoutBtn =
                findViewById(R.id.logoutBtn);

        list = new ArrayList<>();

        adapter =
                new RequestAdapter(
                        this,
                        list
                );

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this)
        );

        recyclerView.setAdapter(adapter);

        loadRequests();

        // LOGOUT

        logoutBtn.setOnClickListener(v -> {

            startActivity(

                    new Intent(

                            HODDashboard.this,

                            MainActivity.class

                    )

            );

            finish();

        });

    }

    // LOAD REQUESTS

    private void loadRequests() {

        FirebaseHelper.requestsRef

                .addValueEventListener(

                        new ValueEventListener() {

                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot snapshot
                            ) {

                                list.clear();

                                for(DataSnapshot data :
                                        snapshot.getChildren()) {

                                    RequestModel request =
                                            data.getValue(
                                                    RequestModel.class
                                            );

                                    if(request != null
                                            && request.status != null
                                            && request.status.equals(
                                            "GFM Approved"
                                    )) {

                                        list.add(request);

                                    }

                                }

                                adapter.notifyDataSetChanged();

                            }

                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError error
                            ) {

                            }

                        }

                );

    }

}