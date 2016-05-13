package gaur.ashish.com.payoyeproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import gaur.ashish.com.payoyeproject.modal.MovieFields;

/**
 * Created by Ashish Gaur on 5/13/2016.
 */
public class SearchResultView extends AppCompatActivity {

    MovieFields mf = new MovieFields();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);

        ImageView img = (ImageView) findViewById(R.id.result_img);
        new ImageLoadTask(mf.getPoster(), img).execute();

        TextView name = (TextView) findViewById(R.id.result_title_txt);
        TextView year = (TextView) findViewById(R.id.result_year_txt);
        TextView actors = (TextView) findViewById(R.id.result_actors_txt);
        TextView runtime = (TextView) findViewById(R.id.result_runtime_txt);
        TextView imdbRating = (TextView) findViewById(R.id.result_rating_txt);
        TextView plot = (TextView) findViewById(R.id.result_plot_txt);
        TextView genre = (TextView) findViewById(R.id.result_genre_txt);
        TextView released = (TextView) findViewById(R.id.result_released_txt);

        name.setText(mf.getTitle());
        year.setText(mf.getYear());
        actors.setText(mf.getActors());
        runtime.setText(mf.getRuntime());
        imdbRating.setText(mf.getImdbRating());
        plot.setText(mf.getPlot());
        genre.setText(mf.getGenre());
        released.setText(mf.getReleased());
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}
