package com.example.smartpetrolcalculator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

public class AboutFragment extends Fragment {

    public AboutFragment() {
        // Required empty constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(
                R.layout.fragment_about,
                container,
                false
        );

        Button btnGithub = view.findViewById(R.id.btnGithub);

        btnGithub.setOnClickListener(v -> {
            String githubUrl = "https://github.com/emawinz/SmartPetrolCalculator";

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(githubUrl));
            startActivity(intent);
        });

        return view;
    }
}