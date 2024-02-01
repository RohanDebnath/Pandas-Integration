package com.example.pandasintegration;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements Filterable {

    private Context context;
    private List<String> applicationDetails;
    private List<String> filteredApplicationDetails; // Original list for filtering
    private OnItemClickListener onItemClickListener;
    private Filter filter;

    public MyAdapter(Context context, List<String> applicationDetails, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.applicationDetails = applicationDetails;
        this.filteredApplicationDetails = new ArrayList<>(applicationDetails);
        this.onItemClickListener = onItemClickListener;
        this.filter = new NameFilter();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String[] details = filteredApplicationDetails.get(position).split(",");

        holder.textViewStudentName.setText("Student Name: " + details[0]);
        holder.textViewXMarks.setText("X Marks: " + details[1]);
        holder.textViewXIIMarks.setText("XII Marks: " + details[2]);
        holder.textViewCurricularActivities.setText("Curricular Activities: " + details[3]);
        holder.email.setText("Email id: " + details[4]);
    }

    @Override
    public int getItemCount() {
        return filteredApplicationDetails.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    // Custom Filter implementation
    private class NameFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<String> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                // No filter applied, show all items
                filteredList.addAll(applicationDetails);
            } else {
                // Filter based on constraint (student name)
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (String detail : applicationDetails) {
                    if (detail.toLowerCase().contains(filterPattern)) {
                        filteredList.add(detail);
                    }
                }
            }

            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredApplicationDetails.clear();
            filteredApplicationDetails.addAll((List<String>) results.values);
            notifyDataSetChanged();
        }
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
