package gaur.ashish.com.payoyeproject;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

import gaur.ashish.com.payoyeproject.modal.MovieFields;


public class MainActivity extends AppCompatActivity {

    public String selectedItem;
    MovieFields mf = new MovieFields();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText edt = (EditText) findViewById(R.id.search_edt);

        Button btn = (Button) findViewById(R.id.search_btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedItem = edt.getText().toString().replace("\\s+", "+");
                if (selectedItem != null) {
                    new ExecuteTask().execute();
                }
            }
        });
    }

    protected String run() {
        try {
            InputStream input = new URL("http://www.omdbapi.com/?t=" + URLEncoder.encode(selectedItem, "UTF-8")).openStream();
            Map<String, String> map = new Gson().fromJson(new InputStreamReader(input, "UTF-8"), new TypeToken<Map<String, String>>() {
            }.getType());

            mf.setTitle(map.get("Title"));
            mf.setYear(map.get("Year"));
            mf.setReleased(map.get("Released"));
            mf.setRuntime(map.get("Runtime"));
            mf.setGenre(map.get("Genre"));
            mf.setActors(map.get("Actors"));
            mf.setPlot(map.get("Plot"));
            mf.setImdbRating(map.get("imdbRating"));
            mf.setPoster(map.get("Poster"));

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
            Intent intent = new Intent(getApplicationContext(), SearchResultView.class);
            startActivity(intent);
        }
    }
}
