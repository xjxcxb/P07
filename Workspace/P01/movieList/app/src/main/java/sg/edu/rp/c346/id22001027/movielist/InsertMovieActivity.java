package sg.edu.rp.c346.id22001027.movielist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import sg.edu.rp.c346.id22001027.movielist.R;


public class InsertMovieActivity extends AppCompatActivity {

    private EditText etTitle, etGenre, etYear;
    private Spinner insertRating;
    private Button btnInsert, btnShowList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_movie);

        // Initialize UI elements
        etTitle = findViewById(R.id.etTitle);
        etGenre = findViewById(R.id.etGenre);
        etYear = findViewById(R.id.etYear);
        insertRating = findViewById(R.id.insertRating);
        btnInsert = findViewById(R.id.btnInsert);
        btnShowList = findViewById(R.id.btnShowList);

        // Define array of possible ratings
        String[] ratingArray = {"U", "PG", "12A", "12", "15", "18", "R18"};

        // Create ArrayAdapter for the spinner
        ArrayAdapter ratingAdapter = new ArrayAdapter<>(InsertMovieActivity.this, android.R.layout.simple_list_item_1, ratingArray);
        insertRating.setAdapter(ratingAdapter);
        insertRating.setSelection(0, false);

        // Set up listener for insert button
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get input values
                String title = etTitle.getText().toString();
                String genre = etGenre.getText().toString();
                String yearStr = etYear.getText().toString();
                String rating = insertRating.getSelectedItem().toString();

                // Check for empty fields
                if (!title.isEmpty() && !genre.isEmpty() && !yearStr.isEmpty() && !rating.isEmpty()) {
                    int year = Integer.parseInt(yearStr);

                    // Insert movie into database
                    sg.edu.rp.c346.id22001027.movielist.DBHelper db = new sg.edu.rp.c346.id22001027.movielist.DBHelper(InsertMovieActivity.this);
                    db.insertMovie(title, genre, year, rating);
                    db.close();

                    // Display success message
                    showToast("Movie added!");

                    // Reset input fields
                    resetInputFields();
                } else {
                    // Display error message for empty fields
                    showToast("Error. Please fill in all fields.");
                }
            }
        });

        // Set up listener for show list button
        btnShowList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateTo(MainActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.btnShowList) {
            navigateTo(MainActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Utility methods

    private void showToast(String message) {
        Toast.makeText(InsertMovieActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void resetInputFields() {
        etTitle.setText("");
        etGenre.setText("");
        etYear.setText("");
        insertRating.setSelection(0, false);
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(InsertMovieActivity.this, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
