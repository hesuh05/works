package com.example.localstorage;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private EditText etId;
    private EditText etName;
    private EditText etLastname;
    private EditText etGrade;
    private EditText etGroup;
    private EditText etAverage;
    private EditText etCareer;
    private Button btnSave;
    private Button btnSearch;
    private Button btnEdit;
    private Button btnDelete;
    private ListView lvStudents;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> dataStudents;
    private SQLiteDatabase db;
    private SchoolControlDbHelper dbHelper;
    private int selected;
    public void getStudent(String id){
        db=dbHelper.getReadableDatabase();
        String selection = SchoolControlContract.Student.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs={etId.getText().toString()};
        Cursor cursor =db.query(
                SchoolControlContract.Student.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        if (!cursor.moveToNext()){
            Toast.makeText(this, "No existe el alumno", Toast.LENGTH_SHORT).show();
            clearFields();
        } else{
            etName.setText(cursor.getString(1));
            etLastname.setText(cursor.getString(2));
            etGrade.setText(String.valueOf(cursor.getInt(3)));
            etGroup.setText(cursor.getString(4));
            etAverage.setText(String.valueOf(cursor.getFloat(5)));
            etCareer.setText(cursor.getString(6));
        }
    }
    public void getStudents(){
        dbHelper = new SchoolControlDbHelper(this);
        db = dbHelper.getReadableDatabase();
        Cursor cursor= db.query(
                SchoolControlContract.Student.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                SchoolControlContract.Student.COLUMN_NAME_NAME+", "+SchoolControlContract.Student.COLUMN_NAME_LASTNAME
        );
        // Crear mi repositorio de datos que seran mostrados en la listView.
        dataStudents = new ArrayList<>();
        // Recorrer cursor para llenar mi repositorio
        while(cursor.moveToNext()==true){
            dataStudents.add(cursor.getString(0)+"::"+cursor.getString(1)+"::"+cursor.getString(2)+"::"+cursor.getFloat(5));
        }
        adapter = new ArrayAdapter<>(this,com.google.android.material.R.layout.support_simple_spinner_dropdown_item,dataStudents);
        lvStudents.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
    public void clearFields(){
        etId.setText("");
        etName.setText("");
        etLastname.setText("");
        etGroup.setText("");
        etGrade.setText("");
        etAverage.setText("");
        etCareer.setText("");
        etId.requestFocus();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        etId = findViewById(R.id.et_id);
        etName = findViewById(R.id.et_name);
        etLastname = findViewById(R.id.et_lastname);
        etGrade = findViewById(R.id.et_grade);
        etGroup = findViewById(R.id.et_group);
        etAverage = findViewById(R.id.et_average);
        etCareer = findViewById(R.id.et_career);
        btnSave = findViewById(R.id.btn_save);
        btnSearch = findViewById(R.id.btn_search);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
        lvStudents = findViewById(R.id.lv_students);
        getStudents();
        dbHelper = new SchoolControlDbHelper(this);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etId.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "No hay ", Toast.LENGTH_SHORT).show();
                } else {
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(SchoolControlContract.Student.COLUMN_NAME_ID, etId.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_NAME, etName.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_LASTNAME, etLastname.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_GRADE, Integer.parseInt(etGrade.getText().toString()));
                    values.put(SchoolControlContract.Student.COLUMN_NAME_GROUP, etGroup.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_AVERAGE, etAverage.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_CAREER, etCareer.getText().toString());
                    String selection = SchoolControlContract.Student.COLUMN_NAME_ID+" = ?";
                    String [] selectionArgs = {etId.getText().toString()};
                    int updateRows = db.update(
                            SchoolControlContract.Student.TABLE_NAME,
                            values,
                            selection,
                            selectionArgs
                    );
                    if (updateRows>0) {
                        Toast.makeText(MainActivity.this, "Alumno actualziado", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(MainActivity.this, "Alumno no encontrado", Toast.LENGTH_SHORT).show();
                    }
                    getStudents();
                }
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = dbHelper.getWritableDatabase();
                String selection = SchoolControlContract.Student.COLUMN_NAME_ID+" = ?";
                String [] selectionArgs = {etId.getText().toString()};
                int deleteRows = db.delete(SchoolControlContract.Student.TABLE_NAME,selection,selectionArgs);
                if (deleteRows>0){
                    Toast.makeText(MainActivity.this, "Estudiante eliminado", Toast.LENGTH_SHORT).show();
                    clearFields();
                    getStudents();
                } else {
                    Toast.makeText(MainActivity.this, "Estudiante no encontrado", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etId.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this, "Introduce una matrícula", Toast.LENGTH_SHORT).show();
                } else {
                    getStudent(etId.getText().toString());
                }
                /*String id=etId.getText().toString();
                Cursor cursor;
                String query="SELECT * FROM "+ SchoolControlContract.Student.TABLE_NAME;
                if (db!=null){
                    cursor = db.rawQuery(query,null);
                    while (cursor.moveToNext()){
                        if(String.valueOf(cursor.getInt(0)).equals(id)){
                            Toast.makeText(MainActivity.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
                        }
                    }
                    adapter.notifyDataSetChanged();
                }*/
            }
        });
        adapter = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,dataStudents);
        lvStudents.setAdapter(adapter);
        lvStudents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selected=position;
            }
        });
        /*btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selected != -1) {
                    Toast.makeText(MainActivity.this, dataStudents.get(selected).toString(), Toast.LENGTH_SHORT).show();
                    dataStudents.remove(selected);
                    adapter.notifyDataSetChanged();
                    selected = -1;
                } else {
                    Toast.makeText(MainActivity.this, "No hay ningun elemento seleccionado", Toast.LENGTH_SHORT).show();
                }
            }
        });*/
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etId.getText().toString().equals("") || etName.getText().toString().equals("") || etLastname.getText().toString().equals("") || etGrade.getText().toString().equals("") || etGroup.getText().toString().equals("") || etAverage.getText().toString().equals("") || etCareer.getText().toString().equals("")){
                    Toast.makeText(MainActivity.this, "Todos los campos deben ir llenos!", Toast.LENGTH_SHORT).show();
                } else {
                    db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(SchoolControlContract.Student.COLUMN_NAME_ID, etId.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_NAME, etName.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_LASTNAME, etLastname.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_GRADE, Integer.parseInt(etGrade.getText().toString()));
                    values.put(SchoolControlContract.Student.COLUMN_NAME_GROUP, etGroup.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_AVERAGE, etAverage.getText().toString());
                    values.put(SchoolControlContract.Student.COLUMN_NAME_CAREER, etCareer.getText().toString());
                    long studentsInserted = db.insert(SchoolControlContract.Student.TABLE_NAME, null, values);
                    if (studentsInserted > 0) {
                        Toast.makeText(MainActivity.this, "Estudiante almacenado con éxito", Toast.LENGTH_SHORT).show();
                        getStudents();
                        clearFields();
                        /*Cursor cursor;
                        db=dbHelper.getWritableDatabase();
                        if (db!=null){
                            String queryNew = "SELECT * FROM "+ SchoolControlContract.Student.TABLE_NAME+" WHERE id = ?";
                            cursor = db.rawQuery(queryNew, new String[]{etId.getText().toString()});
                            while (cursor.moveToNext()){
                                dataStudents.add(
                                        String.valueOf(cursor.getInt(0))
                                );
                            }
                            adapter.notifyDataSetChanged();
                        }*/
                    }
                }
            }
        });
        /*Cursor cursor;
        db=dbHelper.getWritableDatabase();
        String query =  "SELECT * FROM "+ SchoolControlContract.Student.TABLE_NAME;
        if (db!=null){
            cursor = db.rawQuery(query,null);
            while (cursor.moveToNext()){
                dataStudents.add(
                        String.valueOf(cursor.getInt(0))
                );
            }
            adapter.notifyDataSetChanged();
        }*/
    }
}