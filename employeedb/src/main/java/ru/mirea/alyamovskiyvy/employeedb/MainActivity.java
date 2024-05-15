package ru.mirea.alyamovskiyvy.employeedb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import java.util.List;

import ru.mirea.alyamovskiyvy.employeedb.DB.Employee;
import ru.mirea.alyamovskiyvy.employeedb.DB.EmployeeDao;
import ru.mirea.alyamovskiyvy.employeedb.DB.EmployeeDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmployeeDatabase db = App.getInstance().getDatabase();
        EmployeeDao employeeDao = db.employeeDao();

        Employee employee = new Employee();
        employee.id = 1;
        employee.name = "John Smith";
        employee.salary = 10000;

        employeeDao.insert(employee);

        List<Employee> employees = employeeDao.getAll();
        employee = employeeDao.getById(1);
        employee.salary = 20000;
        employeeDao.update(employee);
        Log.d(MainActivity.class.getSimpleName(), employee.name + " " + employee.salary);
    }
}