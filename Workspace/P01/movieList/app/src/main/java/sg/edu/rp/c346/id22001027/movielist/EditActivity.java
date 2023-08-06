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

public class EditActivity extends AppCompatActivity {

    // Declare UI elements
    private EditText editId, editTitle, editGenre, editYear;
    private Spinner editRating;
    private Button btnCancel, btnDelete, btnUpdate;
    private Movie movieData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize UI elements
        editId = findViewById(R.id.editEtId);
        editTitle = findViewById(R.id.editEtTitle);
        editGenre = findViewById(R.id.editEtGenre);
        editYear = findViewById(R.id.editEtYear);
        editRating = findViewById(R.id.editRating);
        btnCancel = findViewById(R.id.btnCancel);
        btnUpdate = findViewById(R.id.btnUpdate);
        btnDelete = findViewById(R.id.btnDelete);

        // Disable editing of ID field
        disableEditing(editId);

        // Define array of possible ratings
        String[] ratings = {"U", "PG", "12A", "12", "15", "18", "R18"};

        // Create ArrayAdapter for the spinner
        ArrayAdapter ratingAdapter = new ArrayAdapter<>(EditActivity.this, android.R.layout.simple_list_item_1, ratings);
        editRating.setAdapter(ratingAdapter);

        // Retrieve movie data from intent
        Intent receivedIntent = getIntent();
        movieData = (Movie) receivedIntent.getSerializableExtra("data");

        // Populate UI fields with movie data
        populateFieldsWithMovieData(movieData, editId, editTitle, editGenre, editYear);

        // Set the spinner selection based on the movie's rating
        setSpinnerToRating(editRating, ratingAdapter, movieData.getRating());

        // Set up listener for update button
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve input values
                String title = getInput(editTitle);
                String genre = getInput(editGenre);
                int year = Integer.parseInt(getInput(editYear));
                String rating = editRating.getSelectedItem().toString();

                // Update the movie data
                updateMovieData(movieData, title, genre, year, rating);

                // Update data in the database
                sg.edu.rp.c346.id22001027.movielist.DBHelper dbHelper = new sg.edu.rp.c346.id22001027.movielist.DBHelper(EditActivity.this);
                dbHelper.updateMovie(movieData);
                dbHelper.close();

                // Navigate back to the main activity
                navigateTo(MainActivity.class);

                // Display a toast message
                showToast("Movie updated");
            }
        });

        // Set up listener for delete button
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Delete movie data from the database
                sg.edu.rp.c346.id22001027.movielist.DBHelper dbHelper = new sg.edu.rp.c346.id22001027.movielist.DBHelper(EditActivity.this);
                dbHelper.deleteMovie(movieData.getId());

                // Navigate back to the main activity
                navigateTo(MainActivity.class);

                // Display a toast message
                showToast("Movie deleted");
            }
        });

        // Set up listener for cancel button
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back to the main activity
                navigateTo(MainActivity.class);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here.
        int id = item.getItemId();

        if (id == R.id.insert) {
            // Navigate to the insert movie activity
            navigateTo(InsertMovieActivity.class);
            return true;
        } else if (id == R.id.showList) {
            // Navigate to the main activity
            navigateTo(MainActivity.class);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Utility methods

    private void disableEditing(EditText editText) {
        editText.setEnabled(false);
        editText.setClickable(false);
    }

    private void populateFieldsWithMovieData(Movie movie, EditText id, EditText title, EditText genre, EditText year) {
        id.setText(Integer.toString(movie.getId()));
        title.setText(movie.getTitle());
        genre.setText(movie.getGenre());
        year.setText(Integer.toString(movie.getYear()));
    }

    private void setSpinnerToRating(Spinner spinner, ArrayAdapter adapter, String rating) {
        for (int i = 0; i < adapter.getCount(); i++) {
            if (adapter.getItem(i).equals(rating)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private String getInput(EditText editText) {
        return editText.getText().toString();
    }

    private void updateMovieData(Movie movie, String title, String genre, int year, String rating) {
        movie.setTitle(title);
        movie.setGenre(genre);
        movie.setYear(year);
        movie.setRating(rating);
    }

    private void navigateTo(Class<?> destination) {
        Intent intent = new Intent(EditActivity.this, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void showToast(String message) {
        Toast.makeText(EditActivity.this, message, Toast.LENGTH_LONG).show();
    }
}
