package com.vinay.vsatsaarthi.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.vinay.vsatsaarthi.Models.SatelliteModel;
import com.vinay.vsatsaarthi.R;

import java.util.ArrayList;

public class SatelliteAdapter extends RecyclerView.Adapter<SatelliteAdapter.viewHolder>{
    ArrayList<SatelliteModel> list;
    Context context;

    public SatelliteAdapter(ArrayList<SatelliteModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.satellite_demo,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        SatelliteModel model = list.get(position);
        holder.name.setText(model.getSatelliteName());

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                new AlertDialog.Builder(context).setTitle("Delete").setMessage("Are you sure you want to delete this satellite?").setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase.getInstance().getReference().child("Satellite").child(model.getId()).setValue(null).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context,"Satellite Deleted", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                }).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder
    {
        TextView name;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
        }
    }
}
