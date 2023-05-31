package sg.edu.rp.c346.id22001027.simpletodo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> taskList;
    private ArrayAdapter<String> taskAdapter;
    private EditText etTask;
    private Spinner spinner;
    private Button btnAdd;
    private Button btnDelete;
    private Button btnClear;
    private ListView lvTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etTask = findViewById(R.id.etTask);
        spinner = findViewById(R.id.spinner);
        btnAdd = findViewById(R.id.btnAdd);
        btnDelete = findViewById(R.id.btnDelete);
        btnClear = findViewById(R.id.btnClear);
        lvTasks = findViewById(R.id.lvTasks);

        taskList = new ArrayList<>();
        taskAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, taskList);
        lvTasks.setAdapter(taskAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                handleSpinnerSelection(position); // Call method to handle spinner selection
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Empty implementation
            }
        });



        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = etTask.getText().toString().trim();
                if (!task.isEmpty()) {
                    taskList.add(task); // Add task to the task list
                    taskAdapter.notifyDataSetChanged(); // Update the ListView
                    etTask.setText(""); // Clear the EditText field
                }
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the index of the task to be deleted from the EditText
                int index = Integer.parseInt(etTask.getText().toString().trim());
                if (index >= 0 && index < taskList.size()) {
                    taskList.remove(index); // Remove task at the specified index
                    taskAdapter.notifyDataSetChanged(); // Update the ListView
                    etTask.setText(""); // Clear the EditText field
                } else {
                    Toast.makeText(MainActivity.this, "Wrong index number", Toast.LENGTH_SHORT).show();
                    // Show toast message for invalid index
                }
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                taskList.clear(); // Clear all tasks from the list
                taskAdapter.notifyDataSetChanged(); // Update the ListView
                etTask.setText(""); // Clear the EditText field
            }
        });
    }

    private void handleSpinnerSelection(int position) {
        if (position == 0) {
            etTask.setHint(R.string.hint_add_task); // Set hint for adding a new task
            btnAdd.setEnabled(true); // Enable Add button
            btnDelete.setEnabled(false); // Disable Delete button
        } else if (position == 1) {
            etTask.setHint(R.string.hint_remove_task); // Set hint for removing a task
            btnAdd.setEnabled(false); // Disable Add button
            btnDelete.setEnabled(true); // Enable Delete button
        }
    }
}
