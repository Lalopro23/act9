package com.lalo.actividad9.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lalo.actividad9.R;
import com.lalo.actividad9.model.Employee;

import java.util.List;
import java.util.function.Consumer;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.ViewHolder> {

    private List<Employee> employeeList;
    private final Consumer<Employee> onEditListener;
    private final Consumer<Employee> onDeleteListener;


    public EmployeeAdapter(List<Employee> employeeList, Consumer<Employee> onEditListener, Consumer<Employee> onDeleteListener) {
        this.employeeList = employeeList;
        this.onEditListener = onEditListener;
        this.onDeleteListener = onDeleteListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_employee, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Employee employee = employeeList.get(position);
        holder.tvName.setText(employee.getName());
        holder.tvPosition.setText(employee.getPosition());
        holder.tvSalary.setText(String.valueOf(employee.getSalary()));

        holder.itemView.setOnClickListener(v -> onEditListener.accept(employee));
        holder.btnDelete.setOnClickListener(v -> onDeleteListener.accept(employee));
    }

    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public void updateData(List<Employee> newEmployeeList) {
        this.employeeList.clear();
        this.employeeList.addAll(newEmployeeList);
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPosition, tvSalary;
        ImageButton btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvSalary = itemView.findViewById(R.id.tvSalary);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
