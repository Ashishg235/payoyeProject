package gaur.ashish.com.payoyeproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

import gaur.ashish.com.payoyeproject.model.MovieFields;


public class MainActivity extends AppCompatActivity {

    public String selectedItem;
    MovieFields mf = new MovieFields();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edt = (EditText) findViewById(R.id.search_edt);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        Button btn = (Button) findViewById(R.id.search_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = edt.getText().toString().replace("\\s+", "+");
                if (selectedItem != null) {
                    new ExecuteTask().execute();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    protected String run() {
        try {
            InputStream input = new URL("http://www.omdbapi.com/?t=" + URLEncoder.encode(selectedItem, "UTF-8")).openStream();
            Map<String, String> map = new Gson().fromJson(new InputStreamReader(input, "UTF-8"), new TypeToken<Map<String, String>>() {
            }.getType());

            MovieFields.setTitle(map.get("Title"));
            MovieFields.setYear(map.get("Year"));
            MovieFields.setReleased(map.get("Released"));
            MovieFields.setRuntime(map.get("Runtime"));
            MovieFields.setGenre(map.get("Genre"));
            MovieFields.setActors(map.get("Actors"));
            MovieFields.setPlot(map.get("Plot"));
            MovieFields.setImdbRating(map.get("imdbRating"));
            MovieFields.setPoster(map.get("Poster"));

        } catch (JsonIOException | JsonSyntaxException | IOException e) {
            System.out.println(e);
        }
        return "true";
    }


    class ExecuteTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return run();
        }

        protected void onPostExecute(String jsonObject) {
            progressBar.setVisibility(View.GONE);
            if (!MovieFields.nullOrNot()) {
                Intent intent = new Intent(getApplicationContext(), SearchResultView.class);
                startActivity(intent);
            } else {
                Toast.makeText(getBaseContext(), "No movie data found!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
