package sg.edu.rp.c346.id22001027.movielist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomAdapter extends ArrayAdapter<Movie> {

    private Context parentContext;
    private int layoutId;
    private ArrayList<Movie> movieList;

    public CustomAdapter(Context context, int resource, ArrayList<Movie> objects) {
        super(context, resource, objects);

        parentContext = context;
        layoutId = resource;
        movieList = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parentContext);
        View rowView = inflater.inflate(layoutId, parent, false);

        TextView listTitle = rowView.findViewById(R.id.rowTitle);
        TextView listGenre = rowView.findViewById(R.id.rowGenre);
        TextView listYear = rowView.findViewById(R.id.rowYear);
        ImageView listRating = rowView.findViewById(R.id.imageRating);

        Movie currentMovie = movieList.get(position);

        listTitle.setText(currentMovie.getTitle());
        listGenre.setText(currentMovie.getGenre());
        listYear.setText(String.valueOf(currentMovie.getYear()));

        // Don't load any image into the ImageView (leave it empty)

        return rowView;
    }
}
