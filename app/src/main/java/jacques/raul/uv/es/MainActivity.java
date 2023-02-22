package jacques.raul.uv.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Camping> campings;
    private CampingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_campings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        campings = new ArrayList<Camping>();
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setupData(ArrayList<Camping> campings){
        adapter = new CampingsAdapter(campings, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }
    public void getData() throws JSONException {
        InputStream is =
                getApplicationContext().getResources().openRawResource(R.raw.datastore_search);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        //The String writer.toString() must be parsed in the campings ArrayList by using JSONArray and JSONObject
        JSONObject object = new JSONObject(writer.toString());
        JSONArray array = object.getJSONObject("result").getJSONArray("records");

        int length = array.length();
        for(int i = 0; i < length; i++){
            JSONObject jsonCamping = array.getJSONObject(i);

            Camping camping = new Camping(jsonCamping.getString("Nombre"));
            //String name = jsonCamping.getString("nombre");
            campings.add(camping);

            System.out.println(jsonCamping.getString("Nombre"));
        }
        //TODO: read the data of each camping, create a new Camping object and insert it in the campings arraylist.
        setupData(campings);
    }
}