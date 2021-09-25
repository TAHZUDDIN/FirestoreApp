package com.all.firestorecounterapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements UpdateAdapter {

    List<CounterModel> counterList;
    RecyclerView recyclerView;
    LinearLayoutManager llm;
    CounterAdapter adapter;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    FirebaseFirestore db;
    UpdateAdapter updateInterface;

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

        db = FirebaseFirestore.getInstance();

        counterList = new ArrayList<>();

        db.collection("LikeCounter")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            counterList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CounterModel cm = document.toObject(CounterModel.class);
                                cm.setDocumentId(document.getId());
                                counterList.add(cm);
                            }

                            adapter = new CounterAdapter(counterList);
                            recyclerView.setAdapter(adapter);

                        } else {
                            Log.d("MainActivity", "Error getting documents: ", task.getException());
                        }
                    }
          });


    }



    @Override
    public void updateRow(CounterModel model, int index) {
        counterList.set(index,model);
        adapter.notifyItemChanged(index);
    }



    class CounterAdapter extends RecyclerView.Adapter<ViewHolder>{
        List<CounterModel> mList;

        public CounterAdapter(List<CounterModel> list) {
            mList = list;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.counter_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
             CounterModel model = mList.get(position);
             holder.bind(model,position);
        }

        @Override
        public int getItemCount() {
            return mList.size();
        }
    }



    class ViewHolder extends RecyclerView.ViewHolder{

        TextView likes;
        ImageView liked;
        ImageView notLiked;
        TextView name;
        CounterModel counterModel;
        int mPosition;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            likes = itemView.findViewById(R.id.likes);
            liked = itemView.findViewById(R.id.liked);
            notLiked = itemView.findViewById(R.id.notLiked);

            liked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onLikeClick();
                    updateInFirestore(counterModel,false);
                }
            });

            notLiked.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onNotLikedClick();
                    updateInFirestore(counterModel,true);
                }
            });

        }

        public void bind(CounterModel model,int position){

             mPosition = position;

            counterModel = model;

            name.setText(model.getName());
            likes.setText(model.getLikes()+"");

            boolean isLiked = sp.getBoolean(model.getDocumentId(),false);
            if(isLiked){
                onNotLikedClick();
            }else{
                onLikeClick();
            }

        }

        public void onLikeClick(){
            notLiked.setVisibility(View.VISIBLE);
            liked.setVisibility(View.GONE);
        }

        public void onNotLikedClick(){
            notLiked.setVisibility(View.GONE);
            liked.setVisibility(View.VISIBLE);
        }


        public void updateInFirestore(CounterModel model,Boolean liked){
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

            // Update one field, creating the document if it does not already exist.
            Map<String, Object> data = new HashMap<>();
            data.put("likes", likes);
            db.collection("LikeCounter").document(model.getDocumentId())
                    .set(data, SetOptions.merge());

            updateInterface.updateRow(model,mPosition);
        }


    }

}


interface UpdateAdapter{
    void updateRow(CounterModel  model, int index);
}