package com.all.firestorecounterapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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

    public String TAG = "MainActivity";
    List<CounterModel> counterList;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    CounterAdapter adapter;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    UpdateAdapter updateInterface;
    private DatabaseReference db;
    AlertDialog dialog;
    ConstraintLayout emptyView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        llm = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(llm);
        recyclerView.setItemAnimator(null);

        emptyView = findViewById(R.id.emptyView);

        sp = this.getPreferences(this.MODE_PRIVATE);
        spe = sp.edit();
        updateInterface = this;

        db = FirebaseDatabase.getInstance().getReference("LikeCounter");

        counterList = new ArrayList<>();

        showProgressDialog();

        db.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {

                hideProgressDialog();

                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                    showEmptyView();
                }
                else {
                    hideEmptyView();
                    DataSnapshot ds = task.getResult();
                    counterList.clear();

                    for(DataSnapshot d : ds.getChildren()){

                        String key = d.getKey();
                        CounterModel cm = d.getValue(CounterModel.class);
                        cm.setDocumentId(key);
                        int idOfString = getResources().getIdentifier(key+"_desc","string",getPackageName());
                        String desc = getString(idOfString);
                        cm.setDesc(desc);
                        int idOfName = getResources().getIdentifier(key+"_name","string",getPackageName());
                        String name = getString(idOfName);
                        cm.setName(name);
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

    @Override
    public void onRowClick(CounterModel model) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra("model",model);
        startActivity(intent);
    }


    public void showProgressDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View v = LayoutInflater.from(this).inflate(R.layout.progress_layout,null,false);
        builder.setView(v);
        dialog = builder.create();
        dialog.show();
    }


    public void hideProgressDialog(){
        Log.i(TAG,"hideProgressDialog() called");
        dialog.dismiss();
    }


    public void showEmptyView(){
        emptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }

    public void hideEmptyView(){
        emptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

}

