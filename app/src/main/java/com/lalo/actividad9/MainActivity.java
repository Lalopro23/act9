package com.lalo.actividad9;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lalo.actividad9.data.EmployeeDao;
import com.lalo.actividad9.model.Employee;
import com.lalo.actividad9.ui.EmployeeAdapter;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EmployeeDao dao;
    private EmployeeAdapter adapter;
    private RecyclerView rvEmployees;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // Configurar RecyclerView y eventos

        dao = new EmployeeDao(this);

        rvEmployees = findViewById(R.id.rvEmployees);
        rvEmployees.setLayoutManager(new LinearLayoutManager(this));

        adapter = new EmployeeAdapter(new ArrayList<>(), this::showEmployeeDialog, this::deleteEmployee);
        rvEmployees.setAdapter(adapter);

        FloatingActionButton fabAdd = findViewById(R.id.fabAdd);
        fabAdd.setOnClickListener(view -> showEmployeeDialog(null));

        loadEmployees();
    }

    private void loadEmployees() {
        adapter.updateData(dao.getAllEmployees());
    }

    private void deleteEmployee(Employee employee) {
        new AlertDialog.Builder(this)
                .setTitle("Eliminar Empleado")
                .setMessage("¿Estás seguro de que quieres eliminar a " + employee.getName() + "?")
                .setPositiveButton("Sí", (dialog, which) -> {
                    dao.delete(employee.getId());
                    loadEmployees();
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void showEmployeeDialog(final Employee employee) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_employee, null);
        builder.setView(dialogView);

        final EditText etName = dialogView.findViewById(R.id.etName);
        final EditText etPosition = dialogView.findViewById(R.id.etPosition);
        final EditText etSalary = dialogView.findViewById(R.id.etSalary);

        if (employee != null) {
            builder.setTitle("Editar Empleado");
            etName.setText(employee.getName());
            etPosition.setText(employee.getPosition());
            etSalary.setText(String.valueOf(employee.getSalary()));
        } else {
            builder.setTitle("Agregar Empleado");
        }

        builder.setPositiveButton(employee == null ? "Agregar" : "Guardar", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String position = etPosition.getText().toString().trim();
            String salaryStr = etSalary.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(position) || TextUtils.isEmpty(salaryStr)) {
                Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            double salary;
            try {
                salary = Double.parseDouble(salaryStr);
            } catch (NumberFormatException e) {
                Toast.makeText(this, "El sueldo debe ser un número válido", Toast.LENGTH_SHORT).show();
                return;
            }

            if (employee == null) {
                Employee newEmployee = new Employee(0, name, position, salary);
                dao.insert(newEmployee);
            } else {
                employee.setName(name);
                employee.setPosition(position);
                employee.setSalary(salary);
                dao.update(employee);
            }
            loadEmployees();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.create().show();
    }
}