package jacques.raul.uv.es;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity implements CampingsViewInterface {

    RecyclerView recyclerView;
    private ArrayList<Camping> campings;
    private ArrayList<Camping> originalCampings;
    private CampingsAdapter adapter;
    private TextView noResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        setTheme(R.style.Theme_CampingsCV);

        setContentView(R.layout.activity_main);

        // Personaliza la Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabCampings = findViewById((R.id.fab));
        noResults = findViewById(R.id.no_results);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, toolbar.getMenu());

        recyclerView = findViewById(R.id.recyclerview_campings);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        campings = new ArrayList<>();
        originalCampings = new ArrayList<>();

        fabCampings.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CampingsFavoritos.class)));

        HTTPConnector httpConnector = new HTTPConnector();
        httpConnector.execute();
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

            case R.id.refresh_data:
                refreshCampingsData();
                Toast.makeText(this, "¡Campings actualizados con éxito!", Toast.LENGTH_SHORT).show();
                return true;

            case R.id.buscador:
                // Mostrar un cuadro de diálogo para que el usuario ingrese el texto de búsqueda
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Buscar un camping");
                EditText input = new EditText(this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);

                // Agregar un botón para realizar la búsqueda
                builder.setPositiveButton("Buscar", (dialog, which) -> {
                    // Filtrar los campings según el texto ingresado en el cuadro de búsqueda
                    String searchText = input.getText().toString().trim().toLowerCase();
                    ArrayList<Camping> filteredCampings = new ArrayList<>();
                    for (Camping camping : originalCampings) {
                        if (camping.getNombre().toLowerCase().contains(searchText) || camping.getCategoria().toLowerCase().contains(searchText) || camping.getMunicipio().toLowerCase().contains(searchText)) {
                            filteredCampings.add(camping);
                        }
                    }
                    // Actualizar la lista de campings con los resultados de la búsqueda
                    if (filteredCampings.size() > 0) {
                        runOnUiThread(() -> adapter.setFilteredList(filteredCampings));
                    } else {
                        runOnUiThread(() -> adapter.setFilteredList(filteredCampings));
                    }
                });
                // Mostrar el cuadro de diálogo de búsqueda
                builder.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // Cambiar el color del ícono de mapa a negro
        MenuItem mapItem = menu.findItem(R.id.new_map);
        if (mapItem != null) {
            Drawable mapIcon = mapItem.getIcon();
            if (mapIcon != null) {
                mapIcon = DrawableCompat.wrap(mapIcon);
                DrawableCompat.setTint(mapIcon.mutate(), Color.BLACK);
                mapItem.setIcon(mapIcon);
            }
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.buscador);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                ArrayList<Camping> filteredCampings = new ArrayList<>();
                String searchText = newText.trim().toLowerCase();

                for (Camping camping : originalCampings) {
                    if (camping.getNombre().toLowerCase().contains(searchText) || camping.getCategoria().toLowerCase().contains(searchText) || camping.getMunicipio().toLowerCase().contains(searchText)) {
                        filteredCampings.add(camping);
                    }
                }
                if (filteredCampings.isEmpty()) {
                    noResults.setVisibility(View.VISIBLE);
                } else {
                    noResults.setVisibility(View.GONE);
                }
                adapter.setFilteredList(filteredCampings);
                return true;
            }
        });
        return true;
    }

    private void setupData(ArrayList<Camping> campings) {
        this.campings = campings;
        adapter = new CampingsAdapter(this, campings, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }

    private String getHttpContent(String url) throws IOException {
        HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("user-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/55.0.2883.87 Safari/537.36");
        con.setRequestProperty("accept", "application/json;");
        con.setRequestProperty("accept-language", "es");
        con.connect();
        int responseCode = con.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
        }
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8));
        StringBuilder content = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            content.append(line);
        }
        in.close();
        return content.toString();
    }

    private ArrayList<Camping> getCampingsFromUrl(String url) {
        ArrayList<Camping> campings = new ArrayList<>();
        try {
            String jsonResponse = getHttpContent(url);
            JSONObject object = new JSONObject(jsonResponse);
            JSONArray array = object.getJSONObject("result").getJSONArray("records");

            int length = array.length();
            for (int i = 0; i < length; i++) {
                JSONObject jsonCamping = array.getJSONObject(i);
                Camping camping = new Camping(i, jsonCamping.getString("Nombre"), jsonCamping.getString("Categoria"), jsonCamping.getString("Municipio"), jsonCamping.getString("Estado"), jsonCamping.getString("Provincia"), jsonCamping.getString("CP"), jsonCamping.getString("Direccion"), jsonCamping.getString("Email"), jsonCamping.getString("Web"), jsonCamping.getString("Núm. Parcelas"), jsonCamping.getString("Plazas Parcela"), jsonCamping.getString("Plazas Libre Acampada"), jsonCamping.getString("Días Periodo"));
                campings.add(camping);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return campings;
    }

    private void refreshCampingsData() {
        new Thread(() -> {
            String packageSearchUrl = "https://dadesobertes.gva.es/api/3/action/package_search?q=dades-turisme-campings-comunitat-valenciana";
            try {
                JSONObject packageSearchJson = new JSONObject(getHttpContent(packageSearchUrl));
                JSONArray resources = packageSearchJson.getJSONObject("result").getJSONArray("results").getJSONObject(0).getJSONArray("resources");
                String latestResourceId = null;
                for (int i = 0; i < resources.length(); i++) {
                    JSONObject resource = resources.getJSONObject(i);
                    if (latestResourceId == null || resource.getString("created").compareTo(latestResourceId) > 0) {
                        latestResourceId = resource.getString("id");
                    }
                }
                if (latestResourceId != null) {
                    String newDataUrl = "https://dadesobertes.gva.es/api/3/action/datastore_search?id=" + latestResourceId;
                    ArrayList<Camping> newCampings = getCampingsFromUrl(newDataUrl);
                    runOnUiThread(() -> {
                        originalCampings = newCampings;
                        ArrayList<Camping> filteredCampings = new ArrayList<>(originalCampings);
                        adapter.setFilteredList(filteredCampings);
                    });
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        }).start();
    }

    class HTTPConnector extends AsyncTask<String, Void, ArrayList> {
        @Override
        protected ArrayList doInBackground(String... params) {
            ArrayList<Camping> campings = new ArrayList<>();
            String url = "https://dadesobertes.gva.es/api/3/action/datastore_search?id=2ddaf823-5da4-4459-aa57-5bfe9f9eb474";
            campings = getCampingsFromUrl(url);
            originalCampings.addAll(campings);
            return campings;
        }

        @Override
        protected void onPostExecute(ArrayList campings) {
            setupData(campings);
            Collections.sort(campings, comparator);
        }
    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(MainActivity.this, CampingDetallesActivity.class);

        intent.putExtra("id", campings.get(position).getId());
        intent.putExtra("nombre", campings.get(position).getNombre());
        intent.putExtra("categoria", campings.get(position).getCategoria());
        intent.putExtra("municipio", campings.get(position).getMunicipio());
        intent.putExtra("estado", campings.get(position).getEstado());
        intent.putExtra("provincia", campings.get(position).getProvincia());
        intent.putExtra("cp", campings.get(position).getCp());
        intent.putExtra("direccion", campings.get(position).getDireccion());
        intent.putExtra("email", campings.get(position).getEmail());
        intent.putExtra("web", campings.get(position).getWeb());
        intent.putExtra("núm. Parcela", campings.get(position).getNumParcelas());
        intent.putExtra("Plazas Parcela", campings.get(position).getPlazasParcela());
        intent.putExtra("Plazas Libre Acampada", campings.get(position).getPlazasLibreAcampada());
        intent.putExtra("Días Periodo", campings.get(position).getPeriodo());

        startActivity(intent);
    }
}
