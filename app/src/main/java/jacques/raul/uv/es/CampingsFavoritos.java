package jacques.raul.uv.es;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CampingsFavoritos extends AppCompatActivity implements CampingsViewInterface{

    FavDB db;
    private ArrayList<Camping> campings;
    private CampingsAdapter adapter;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campings_favoritos);
        db = new FavDB(getBaseContext());
        campings = new ArrayList<Camping>();

        recyclerView = findViewById(R.id.recyclerview_campingsfav);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
        }
        cursor.close();

        setupData(campings);
    }

    private void setupData(ArrayList<Camping> campings) {
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
