package com.example.quevvse150165_lab10.Model;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.quevvse150165_lab10.MainActivity;
import com.example.quevvse150165_lab10.R;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class TraineeAdapter extends RecyclerView.Adapter<TraineeAdapter.ViewHolder> {

    ArrayList<Trainee> traineeList;
    private MainActivity mainActivity;

    public TraineeAdapter(ArrayList<Trainee> traineeList, MainActivity mainActivity) {
        this.traineeList = traineeList;
        this.mainActivity = mainActivity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);

        View view = layoutInflater.inflate(R.layout.trainee_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Trainee trainee = traineeList.get(position);

        holder.tvName.setText(trainee.getName());
        holder.tvGender.setText(trainee.getGender());
        holder.tvPhone.setText(trainee.getPhone());
        holder.tvEmail.setText(trainee.getEmail());
        holder.updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.DialogTrainee(MainActivity.DialogType.UPDATE, trainee);
            }
        });
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity.deleteTrainee(trainee.getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return traineeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGender, tvPhone, tvEmail;
        ImageView updateBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvGender = (TextView) itemView.findViewById(R.id.tvGender);
            tvPhone = (TextView) itemView.findViewById(R.id.tvPhone);
            tvEmail = (TextView) itemView.findViewById(R.id.tvEmail);
            updateBtn = (ImageView) itemView.findViewById(R.id.updateBtn);
            deleteBtn = (ImageView) itemView.findViewById(R.id.deleteBtn);
        }
    }
}
