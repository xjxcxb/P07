package sg.edu.rp.c346.id22001027.movielist;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.Collections;
import sg.edu.rp.c346.id22001027.movielist.R;


public class MainActivity extends AppCompatActivity {

    private ToggleButton btnFilterRatings;
    private Spinner spinnerRatings;
    private ListView listViewMovies;
    private CustomAdapter adapter;
    private ArrayAdapter<String> spinnerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI elements
        btnFilterRatings = findViewById(R.id.btnFilterRatings);
        spinnerRatings = findViewById(R.id.spinnerRatings);
        listViewMovies = findViewById(R.id.listViewMovies);

        // Initialize database helper
        DBHelper dbHelper = new DBHelper(MainActivity.this);

        // Retrieve movie list and unique ratings from the database
        ArrayList<Movie> movieList = dbHelper.getMovies();
        ArrayList<String> ratingArr = dbHelper.getUniqueRatings();
        String defVal = "Filter by - Default";
        ratingArr.add(defVal);
        Collections.reverse(ratingArr);
        dbHelper.close();

        // Initialize spinner adapter
        spinnerAdapter = new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, ratingArr);
        spinnerRatings.setAdapter(spinnerAdapter);

        // Initialize custom adapter for movie list
        adapter = new CustomAdapter(MainActivity.this, R.layout.row, movieList);
        listViewMovies.setAdapter(adapter);

        // Set click listener for items in the movie list
        listViewMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie data = movieList.get(position);  // This line uses the Movie class
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra("data", data);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        // Set click listener for the filter toggle button
        btnFilterRatings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = btnFilterRatings.isChecked();
                modifyMovieListByRating(movieList, isChecked);
            }
        });

        // Set item selected listener for the ratings spinner
        spinnerRatings.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                btnFilterRatings.setEnabled(position == 0);
                btnFilterRatings.setChecked(false);
                modifyMovieListBySelectedRating(movieList, selectedItem);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    // Method to modify the movie list based on the filter toggle button
    public void modifyMovieListByRating(ArrayList<Movie> movieList, boolean isChecked) {
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        movieList.clear();

        if (isChecked) {
            movieList.addAll(dbHelper.getFilteredMovies());
        } else {
            movieList.addAll(dbHelper.getMovies());
        }

        adapter.notifyDataSetChanged();
        dbHelper.close();
    }

    // Method to modify the movie list based on the selected spinner item
    public void modifyMovieListBySelectedRating(ArrayList<Movie> movieList, String selectedItem) {
        DBHelper dbHelper = new DBHelper(MainActivity.this);
        movieList.clear();

        if (selectedItem.equalsIgnoreCase("Filter by - Default")) {
            movieList.addAll(dbHelper.getMovies());
        } else {
            movieList.addAll(dbHelper.getFilteredMovies(selectedItem));
        }

        adapter.notifyDataSetChanged();
        dbHelper.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.insert) {
            Intent intent = new Intent(MainActivity.this, InsertMovieActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
