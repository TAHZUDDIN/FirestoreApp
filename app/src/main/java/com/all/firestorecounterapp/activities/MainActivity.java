package com.all.firestorecounterapp.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.all.firestorecounterapp.R;
import com.all.firestorecounterapp.adapters.CounterAdapter;
import com.all.firestorecounterapp.interfaces.UpdateAdapter;
import com.all.firestorecounterapp.models.CounterModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements UpdateAdapter {

    List<CounterModel> counterList;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    CounterAdapter adapter;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    UpdateAdapter updateInterface;
    private DatabaseReference db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(llm);

        sp = this.getPreferences(this.MODE_PRIVATE);
        spe = sp.edit();

        updateInterface = this;

        db = FirebaseDatabase.getInstance().getReference("LikeCounter");

        counterList = new ArrayList<>();

        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    DataSnapshot ds = task.getResult();
                    counterList.clear();

                    for(DataSnapshot d : ds.getChildren()){
                        String key = d.getKey();
                        CounterModel cm = d.getValue(CounterModel.class);
                        cm.setDocumentId(key);
                        counterList.add(cm);
                    }
                    Log.i("MainActivity","counterList "+counterList);
                    adapter = new CounterAdapter(counterList,sp,updateInterface);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }



    @Override
    public void updateRow(CounterModel model, int index, boolean liked) {
        long likes = model.getLikes();
        if(liked){
            spe.putBoolean(model.getDocumentId(),true);
            likes++;
        }else{
            spe.putBoolean(model.getDocumentId(),false);
            likes--;
        }
        spe.commit();

        model.setLikes(likes);

        db.child(model.getDocumentId()).child("likes").setValue(likes);

        counterList.set(index,model);
        adapter.notifyItemChanged(index);
    }

}

