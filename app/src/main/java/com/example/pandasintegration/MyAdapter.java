package com.example.pandasintegration;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private List<String> applicationDetails;
    private OnItemClickListener onItemClickListener;

    public MyAdapter(Context context, List<String> applicationDetails, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.applicationDetails = applicationDetails;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] details = applicationDetails.get(position).split(",");

        holder.textViewStudentName.setText("Student Name: " + details[0]);
        holder.textViewXMarks.setText("X Marks: " + details[1]);
        holder.textViewXIIMarks.setText("XII Marks: " + details[2]);
        holder.textViewCurricularActivities.setText("Curricular Activities: " + details[3]);
        holder.email.setText("Email id: " + details[4]);
    }

    @Override
    public int getItemCount() {
        return applicationDetails.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textViewStudentName;
        TextView textViewXMarks;
        TextView textViewXIIMarks;
        TextView textViewCurricularActivities;
        TextView email;
        OnItemClickListener onItemClickListener;

        public ViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            this.onItemClickListener = onItemClickListener;
            itemView.setOnClickListener(this);
            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            textViewXMarks = itemView.findViewById(R.id.textViewXMarks);
            textViewXIIMarks = itemView.findViewById(R.id.textViewXIIMarks);
            textViewCurricularActivities = itemView.findViewById(R.id.textViewCurricularActivities);
            email = itemView.findViewById(R.id.emailID);
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}
