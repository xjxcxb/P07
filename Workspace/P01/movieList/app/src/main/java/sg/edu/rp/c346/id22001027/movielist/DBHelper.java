package sg.edu.rp.c346.id22001027.movielist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
    // Start version with 1
    // increment by 1 whenever db schema changes.
    private static final int DATABASE_VER = 1;
    private static final String DATABASE_NAME = "movies.db";
    private static final String TABLE_MOVIE = "movie";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_GENRE = "genre";
    private static final String COLUMN_YEAR = "year";
    private static final String COLUMN_RATING = "rating";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE " + TABLE_MOVIE +  "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_GENRE + " TEXT,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_RATING + " TEXT )";
        db.execSQL(createTableSql);
        Log.i("info" ,"created tables");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIE);
        // Create table(s) again
        onCreate(db);
    }

    public void insertMovie(String title, String genre, int year, String rating){

        // Get an instance of the database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // We use ContentValues object to store the values for
        //  the db operation
        ContentValues values = new ContentValues();
        // Store the column name as key and the title as value
        values.put(COLUMN_TITLE, title);
        // Store the column name as key and the singer/s as value
        values.put(COLUMN_GENRE, genre);
        // Store the column name as key and the year as value
        values.put(COLUMN_YEAR, year);
        // Store the column name as key and the star rating as value
        values.put(COLUMN_RATING, rating);
        // Insert the row into the TABLE_TASK
        db.insert(TABLE_MOVIE, null, values);
        // Close the database connection
        db.close();
    }

    public ArrayList<Movie> getMovies() {
        ArrayList<Movie> movies = new ArrayList<Movie>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_GENRE, COLUMN_YEAR, COLUMN_RATING};
        Cursor cursor = db.query(TABLE_MOVIE, columns, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                int year = cursor.getInt(3);
                String rating = cursor.getString(4);

                Movie obj = new Movie(id, title, genre, year, rating);
                movies.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movies;
    }

    public int updateMovie(Movie data){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_TITLE, data.getTitle());
        values.put(COLUMN_GENRE, data.getGenre());
        values.put(COLUMN_YEAR, data.getYear());
        values.put(COLUMN_RATING, data.getRating());

        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(data.getId())};
        int result = db.update(TABLE_MOVIE, values, condition, args);
        db.close();
        return result;
    }

    public int deleteMovie(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String condition = COLUMN_ID + "= ?";
        String[] args = {String.valueOf(id)};
        int result = db.delete(TABLE_MOVIE, condition, args);
        recreateTable(db, id);
        db.close();
        return result;
    }

    public void recreateTable(SQLiteDatabase db, int id) {
        String createTableSql = "CREATE TABLE TEMP_TABLE" +  "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_GENRE + " TEXT,"
                + COLUMN_YEAR + " INTEGER,"
                + COLUMN_RATING + " TEXT )";
        db.execSQL(createTableSql);

        db.execSQL("INSERT INTO TEMP_TABLE (title, genre, year, rating) " +
                "SELECT title, genre, year, rating FROM " + TABLE_MOVIE +
                " WHERE _id != " + id);

        db.execSQL("DROP TABLE " + TABLE_MOVIE);

        db.execSQL("ALTER TABLE TEMP_TABLE RENAME TO " + TABLE_MOVIE);
    }

    public ArrayList<Movie> getFilteredMovies() {
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_GENRE, COLUMN_YEAR, COLUMN_RATING};
        String selection = COLUMN_RATING + " = ?";
        String[] selectionArgs = {String.valueOf("PG")};
        Cursor cursor = db.query(TABLE_MOVIE, columns, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                int year = cursor.getInt(3);
                String rating = cursor.getString(4);

                Movie obj = new Movie(id, title, genre, year, rating);
                movies.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movies;
    }

    public ArrayList<Movie> getFilteredMovies(String rating) {
        ArrayList<Movie> movies = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_ID, COLUMN_TITLE, COLUMN_GENRE, COLUMN_YEAR, COLUMN_RATING};
        String selection = COLUMN_RATING + " = ?";
        String[] selectionArgs = {String.valueOf(rating)};
        Cursor cursor = db.query(TABLE_MOVIE, columns, selection, selectionArgs, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String genre = cursor.getString(2);
                int year = cursor.getInt(3);
                String movieRating = cursor.getString(4);

                Movie obj = new Movie(id, title, genre, year, movieRating);
                movies.add(obj);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return movies;
    }

    public ArrayList<String> getUniqueRatings() {
        ArrayList<String> uniqueRatings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {COLUMN_RATING};
        Cursor cursor = db.query(true, TABLE_MOVIE, columns, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                String rating = cursor.getString(0);
                if (!uniqueRatings.contains(rating)) {
                    uniqueRatings.add(rating);
                }
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return uniqueRatings;
    }
}
