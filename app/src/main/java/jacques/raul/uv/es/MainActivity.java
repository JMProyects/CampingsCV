package jacques.raul.uv.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private ArrayList<Camping> campings;
    private CampingsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Personaliza la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, toolbar.getMenu());

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_campings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        campings = new ArrayList<>();
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // Define el comparador en el nivel de clase
    private final Comparator<Camping> comparator = (camping1, camping2) -> {
        // Comparar por nombre, categoría y municipio, en ese orden
        int comparacion = camping1.getNombre().compareToIgnoreCase(camping2.getNombre());
        if (comparacion == 0) {
            comparacion = camping1.getCategoria().compareToIgnoreCase(camping2.getCategoria());
            if (comparacion == 0) {
                comparacion = camping1.getMunicipio().compareToIgnoreCase(camping2.getMunicipio());
            }
        }
        return comparacion;
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item1:
                // Ordenar por nombre usando el comparador existente
                Collections.sort(campings, comparator);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.menu_item2:
                // Crear un nuevo comparador para ordenar por categoría
                Comparator<Camping> comparadorCategoria = (camping1, camping2) -> camping1.getCategoria().compareToIgnoreCase(camping2.getCategoria());
                Collections.sort(campings, comparadorCategoria);
                adapter.notifyDataSetChanged();
                return true;

            case R.id.menu_item3:
                // Crear un nuevo comparador para ordenar por municipio
                Comparator<Camping> comparadorMunicipio = (camping1, camping2) -> camping1.getMunicipio().compareToIgnoreCase(camping2.getMunicipio());
                Collections.sort(campings, comparadorMunicipio);
                adapter.notifyDataSetChanged();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    private void setupData(ArrayList<Camping> campings) {
        adapter = new CampingsAdapter(campings, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    public void getData() throws JSONException {
        InputStream is = getApplicationContext().getResources().openRawResource(R.raw.datastore_search);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }

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
        for (int i = 0; i < length; i++) {
            JSONObject jsonCamping = array.getJSONObject(i);

            Camping camping = new Camping(jsonCamping.getString("Nombre"), jsonCamping.getString("Categoria"), jsonCamping.getString("Municipio"));
            //String name = jsonCamping.getString("nombre");
            campings.add(camping);

            System.out.println(jsonCamping.getString("Nombre"));
        }
        //TODO: read the data of each camping, create a new Camping object and insert it in the campings arraylist.
        setupData(campings);

        Collections.sort(campings, comparator);

    }

}
