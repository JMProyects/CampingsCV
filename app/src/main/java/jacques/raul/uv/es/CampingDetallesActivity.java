package jacques.raul.uv.es;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.w3c.dom.Text;

public class CampingDetallesActivity extends AppCompatActivity {

    TextView txt_nombre;
    TextView txt_cat;
    TextView txt_muni;

    TextView txt_estado;
    TextView txt_provincia;
    TextView txt_cp;
    TextView txt_direccion;
    TextView txt_email;
    TextView txt_web;
    TextView txt_numParcela;
    TextView txt_plazasParcela;
    TextView txt_plazasLibreAcampada;
    TextView txt_periodo;

    FavDB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campings_detalles_activity);


        Toolbar toolbar = findViewById(R.id.new_map_toolbar);
        setSupportActionBar(toolbar);

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, toolbar.getMenu());


        String name = getIntent().getStringExtra("nombre");
        String categoria = getIntent().getStringExtra("categoria");
        String municipio = getIntent().getStringExtra("municipio");
        String estado = getIntent().getStringExtra("estado");
        String provincia = getIntent().getStringExtra("provincia");
        String cp = getIntent().getStringExtra("cp");
        String direccion = getIntent().getStringExtra("direccion");
        String email = getIntent().getStringExtra("email");
        String web = getIntent().getStringExtra("web");
        String numParcela = getIntent().getStringExtra("núm. Parcela");
        String plazasParcela = getIntent().getStringExtra("Plazas Parcela");
        String plazasLibreAcampada = getIntent().getStringExtra("Plazas Libre Acampada");
        String periodo = getIntent().getStringExtra("Días Periodo");


        txt_nombre = findViewById(R.id.detalle_nombre);
        txt_cat = findViewById(R.id.detalle_categoria);
        txt_muni = findViewById(R.id.detalle_municipio);

        txt_estado = findViewById(R.id.detalle_estado);
        txt_provincia = findViewById(R.id.detalle_provincia);
        txt_cp = findViewById(R.id.detalle_cp);
        txt_direccion = findViewById(R.id.detalle_direccion);
        txt_email = findViewById(R.id.detalle_email);
        txt_web = findViewById(R.id.detalle_web);
        txt_numParcela = findViewById(R.id.detalle_numparcela);
        txt_plazasParcela = findViewById(R.id.detalle_plazaparcela);
        txt_plazasLibreAcampada = findViewById(R.id.detalle_plazalibreacampada);
        txt_periodo = findViewById(R.id.detalle_periodo);


        txt_nombre.setText(name);
        txt_cat.setText(categoria);
        txt_muni.setText(municipio);
        txt_estado.setText(estado);
        txt_provincia.setText(provincia);
        txt_cp.setText(cp);
        txt_direccion.setText(direccion);
        txt_email.setText(email);
        txt_web.setText(web);
        txt_numParcela.setText(numParcela);
        txt_plazasParcela.setText(plazasParcela);
        txt_plazasLibreAcampada.setText(plazasLibreAcampada);
        txt_periodo.setText(periodo);
        System.out.println(txt_estado);

        db = new FavDB(getBaseContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detalle_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
// Handle item selection
        switch (item.getItemId()) {
            case R.id.new_map:
                Uri uri = Uri.parse("geo:0,0?q=" + txt_nombre.getText().toString().replace(" ", "+"));
                Uri uri2 = Uri.parse("http://www.google.es");
                // Do something when the user clicks on the new game
                showMap(uri);
                return true;
            case R.id.new_fav_camping:
                String name = getIntent().getStringExtra("nombre");
                String categoria = getIntent().getStringExtra("categoria");
                String municipio = getIntent().getStringExtra("municipio");

                //db.insertCamping(name, categoria, municipio);
                return true;

            // Do something when the user clicks on the help item
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showMap(Uri geoLocation) {
        Intent intent = new Intent(Intent.ACTION_VIEW, geoLocation);
        intent.setData(geoLocation);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }
}
