package com.example.pandasintegration;// StudentListAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StudentListAdapter extends RecyclerView.Adapter<StudentListAdapter.ViewHolder> {

    private List<Student> studentList;

    public StudentListAdapter(List<Student> studentList) {
        this.studentList = studentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Student student = studentList.get(position);
        holder.bind(student);
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView nameTextView;
        private TextView xMarksTextView;
        private TextView xiiMarksTextView;
        private TextView activitiesTextView;
        private TextView emailTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            xMarksTextView = itemView.findViewById(R.id.xMarksTextView);
            xiiMarksTextView = itemView.findViewById(R.id.xiiMarksTextView);
            activitiesTextView = itemView.findViewById(R.id.activitiesTextView);
            emailTextView = itemView.findViewById(R.id.emailTextView);
        }

        public void bind(Student student) {
            nameTextView.setText("Name: " + student.getName());
            xMarksTextView.setText("X Marks: " + student.getXMarks());
            xiiMarksTextView.setText("XII Marks: " + student.getXiiMarks());
            activitiesTextView.setText("Activities: " + student.getActivities());
            emailTextView.setText("Email: " + student.getEmail());
        }
    }
}
