package jacques.raul.uv.es;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
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
        try {
            getData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        fabCampings.setOnClickListener(v -> {
            //here
            startActivity(new Intent(MainActivity.this, CampingsFavoritos.class));
        });
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
                        if (camping.getNombre().toLowerCase().contains(searchText)
                                || camping.getCategoria().toLowerCase().contains(searchText)
                                || camping.getMunicipio().toLowerCase().contains(searchText)) {
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
                    if (camping.getNombre().toLowerCase().contains(searchText)
                            || camping.getCategoria().toLowerCase().contains(searchText)
                            || camping.getMunicipio().toLowerCase().contains(searchText)) {
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
        adapter = new CampingsAdapter(this, campings, getApplicationContext());
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

            Camping camping = new Camping(i, jsonCamping.getString("Nombre"), jsonCamping.getString("Categoria"), jsonCamping.getString("Municipio"),
                    jsonCamping.getString("Estado"), jsonCamping.getString("Provincia"), jsonCamping.getString("CP"), jsonCamping.getString("Direccion"),
                    jsonCamping.getString("Email"), jsonCamping.getString("Web"), jsonCamping.getString("Núm. Parcelas"), jsonCamping.getString("Plazas Parcela"),
                    jsonCamping.getString("Plazas Libre Acampada"), jsonCamping.getString("Días Periodo"));


            //String name = jsonCamping.getString("nombre");
            campings.add(camping);
            originalCampings.add(camping);

            System.out.println(jsonCamping.getString("Nombre"));
        }
        //TODO: read the data of each camping, create a new Camping object and insert it in the campings arraylist.
        setupData(campings);

        Collections.sort(campings, comparator);

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
