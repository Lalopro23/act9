package com.lalo.actividad9.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lalo.actividad9.model.Employee;
import com.lalo.actividad9.ui.DBHelper;

import java.util.ArrayList;
import java.util.List;

public class EmployeeDao {
    private final DBHelper helper;

    public EmployeeDao(Context context) {
        helper = new DBHelper(context);
    }

    public long insert(Employee e) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.EmployeeEntry.COL_NAME, e.getName());
        values.put(DBContract.EmployeeEntry.COL_POSITION, e.getPosition());
        values.put(DBContract.EmployeeEntry.COL_SALARY, e.getSalary());
        return db.insert(DBContract.EmployeeEntry.TABLE_NAME, null, values);
    }

    public int update(Employee e) {
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.EmployeeEntry.COL_NAME, e.getName());
        values.put(DBContract.EmployeeEntry.COL_POSITION, e.getPosition());
        values.put(DBContract.EmployeeEntry.COL_SALARY, e.getSalary());
        String where = DBContract.EmployeeEntry._ID + " = ?";
        String[] args = {"" + e.getId()};
        return db.update(DBContract.EmployeeEntry.TABLE_NAME, values, where, args);
    }

    public int delete(long id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        String where = DBContract.EmployeeEntry._ID + " = ?";
        String[] args = {"" + id};
        return db.delete(DBContract.EmployeeEntry.TABLE_NAME, where, args);
    }

    public List<Employee> getAllEmployees() {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.query(DBContract.EmployeeEntry.TABLE_NAME, null, null, null, null, null, null);
        List<Employee> list = new ArrayList<>();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry._ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry.COL_NAME));
            String position = cursor.getString(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry.COL_POSITION));
            double salary = cursor.getDouble(cursor.getColumnIndexOrThrow(DBContract.EmployeeEntry.COL_SALARY));
            list.add(new Employee(id, name, position, salary));
        }
        cursor.close();
        return list;
    }
}
