package jacques.raul.uv.es;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class CampingsFavoritos extends AppCompatActivity implements CampingsViewInterface{

    FavDB db;
    private ArrayList<Camping> campings;
    private ArrayList<Camping> originalCampings;
    private CampingsAdapter adapter;
    RecyclerView recyclerView;
    private TextView noResults;
    private TextView nofav;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campings_favoritos);
        Toolbar toolbar = findViewById(R.id.new_map_toolbar);
        setSupportActionBar(toolbar);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, toolbar.getMenu());
        db = new FavDB(getBaseContext());
        campings = new ArrayList<>();
        originalCampings = new ArrayList<>();
        nofav = findViewById(R.id.no_fav);
        noResults = findViewById(R.id.no_results);
        recyclerView = findViewById(R.id.recyclerview_campingsfav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
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

    @Override
    protected void onResume() {
        super.onResume();
        campings.clear();
        getData();
    }

    public void getData(){
        Cursor cursor = db.read_all_data();
        while (cursor.moveToNext()) {
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(db.KEY_ID));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_NAME));
            String categoria = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_CATEGORY));
            String municipio = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_MUNICIPIO));
            String estado = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_ESTADO));
            String provincia = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_PROVINCIA));
            String cp = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_CP));
            String direccion = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_DIRECCION));
            String email = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_EMAIL));
            String web = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_WEB));
            String numParcelas = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_NUMPARCELAS));
            String plazasParcela = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_PLAZASPARCELA));
            String plazasLibreAcampada = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_PLAZASLIBREACAMPADA));
            String periodo = cursor.getString(cursor.getColumnIndexOrThrow(db.CAMPING_PERIODO));

            campings.add(new Camping(id, nombre, categoria, municipio, estado, provincia, cp, direccion, email, web, numParcelas, plazasParcela, plazasLibreAcampada, periodo));
            originalCampings.add(new Camping(id, nombre, categoria, municipio, estado, provincia, cp, direccion, email, web, numParcelas, plazasParcela, plazasLibreAcampada, periodo));
        }
        cursor.close();

        setupData(campings);
    }

    private void setupData(ArrayList<Camping> campings) {
        this.campings = campings;
        if(campings.isEmpty()){
            nofav.setVisibility(View.VISIBLE);
        }
        adapter = new CampingsAdapter(this, campings, getApplicationContext());
        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(CampingsFavoritos.this, CampingDetallesActivity.class);

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
