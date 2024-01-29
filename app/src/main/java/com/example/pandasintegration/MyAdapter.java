package com.example.pandasintegration;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> applicationDetails;

    public MyAdapter(List<String> applicationDetails) {
        this.applicationDetails = applicationDetails;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] details = applicationDetails.get(position).split(",");

        holder.textViewStudentName.setText("Student Name: " + details[0]);
        holder.textViewXMarks.setText("X Marks: " + details[1]);
        holder.textViewXIIMarks.setText("XII Marks: " + details[2]);
        holder.textViewCurricularActivities.setText("Curricular Activities: " + details[3]);
    }

    @Override
    public int getItemCount() {
        return applicationDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName;
        TextView textViewXMarks;
        TextView textViewXIIMarks;
        TextView textViewCurricularActivities;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            textViewXMarks = itemView.findViewById(R.id.textViewXMarks);
            textViewXIIMarks = itemView.findViewById(R.id.textViewXIIMarks);
            textViewCurricularActivities = itemView.findViewById(R.id.textViewCurricularActivities);
        }
    }
}
