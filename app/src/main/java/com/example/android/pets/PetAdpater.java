package com.example.android.pets;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class PetAdpater extends ArrayAdapter<PetDetail> {
    public PetAdpater(Context context, ArrayList<PetDetail> petDetails) {
        super(context, 0, petDetails);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View root = convertView;
        if (root == null)
            root = LayoutInflater.from(getContext()).inflate(R.layout.pet_layout, parent, false);
        PetDetail petDetail = getItem(position);
        TextView petName = root.findViewById(R.id.pet_name);
        TextView petBreed = root.findViewById(R.id.pet_breed);

        petBreed.setText(petDetail.getBreed());
        petName.setText(petDetail.getName());
        return root;
    }
}
