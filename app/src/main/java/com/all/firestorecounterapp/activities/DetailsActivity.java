package com.all.firestorecounterapp.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.all.firestorecounterapp.R;
import com.all.firestorecounterapp.models.CounterModel;

public class DetailsActivity extends AppCompatActivity {


    TextView name;
    TextView desc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        name = findViewById(R.id.name);
        desc = findViewById(R.id.desc);

        CounterModel cm = (CounterModel) getIntent().getSerializableExtra("model");

        name.setText(cm.getName());
        desc.setText(cm.getDesc());


    }
}