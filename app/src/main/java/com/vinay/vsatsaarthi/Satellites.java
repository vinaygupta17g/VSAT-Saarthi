package com.vinay.vsatsaarthi;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.vinay.vsatsaarthi.Adapters.SatelliteAdapter;
import com.vinay.vsatsaarthi.Models.SatelliteModel;
import com.vinay.vsatsaarthi.databinding.FragmentSatellitesBinding;

import java.util.ArrayList;

public class Satellites extends Fragment {
    FragmentSatellitesBinding binding;
    ArrayList<SatelliteModel> model=new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSatellitesBinding.inflate(inflater, container, false);

        FirebaseDatabase database =FirebaseDatabase.getInstance();
        SatelliteAdapter adapter=new SatelliteAdapter(model,getContext());
        binding.recyclerview.setAdapter(adapter);

        LinearLayoutManager manager =new LinearLayoutManager(getContext());
        binding.recyclerview.setLayoutManager(manager);

        database.getReference().child("Satellite").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model.clear();
                for(DataSnapshot snapshot1 :snapshot.getChildren())
                {
                    SatelliteModel model1=snapshot1.getValue(SatelliteModel.class);
                    model1.setId(snapshot1.getKey());
                    model.add(model1);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return binding.getRoot();
    }
}