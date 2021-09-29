package com.all.firestorecounterapp.adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.all.firestorecounterapp.R;
import com.all.firestorecounterapp.interfaces.UpdateAdapter;
import com.all.firestorecounterapp.models.CounterModel;

import java.util.List;



public class CounterAdapter  extends RecyclerView.Adapter<CounterAdapter.ViewHolder>{
    List<CounterModel> mList;
    SharedPreferences sp;
    UpdateAdapter updateInterface;

    public CounterAdapter(List<CounterModel> list, SharedPreferences sp, UpdateAdapter updateInterface) {
        mList = list;
        this.sp = sp;
        this.updateInterface = updateInterface;
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
            updateInterface.updateRow(model,mPosition,liked);
        }


    }




}