package com.vinay.vsatsaarthi;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.FirebaseDatabase;
import com.vinay.vsatsaarthi.Models.SatelliteModel;
import com.vinay.vsatsaarthi.databinding.FragmentAddSatelliteBinding;
public class AddSatellite extends Fragment {
    FragmentAddSatelliteBinding binding;
    FirebaseDatabase database;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding=FragmentAddSatelliteBinding.inflate(inflater,container,false);
        database=FirebaseDatabase.getInstance();
        binding.addsatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String satname=binding.satname.getText().toString();
                String satlatitude=binding.satlatitude.getText().toString();
                String satlongitude=binding.satlongitude.getText().toString();
                String sataltitude=binding.sataltitude.getText().toString();
                if(satname.isEmpty())
                    binding.satname.setError("");
                else if (satlatitude.isEmpty())
                    binding.satlatitude.setError("");
                else if(satlongitude.isEmpty())
                    binding.satlongitude.setError("");
                else if(sataltitude.isEmpty())
                    binding.sataltitude.setError("");
                else {
                    SatelliteModel model=new SatelliteModel(satname,satlongitude,satlatitude,sataltitude);
                    database.getReference().child("Satellite").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(getContext(),"Satellite "+satname+" Inserted", Toast.LENGTH_SHORT).show();
                        }
                    });
                    binding.satname.setText("");
                    binding.satlatitude.setText("");
                    binding.satlongitude.setText("");
                    binding.sataltitude.setText("");
                }
            }
        });


        return binding.getRoot();
    }
}