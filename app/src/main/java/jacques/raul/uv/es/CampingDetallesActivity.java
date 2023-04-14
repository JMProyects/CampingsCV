package jacques.raul.uv.es;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;

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
    TextView txt_distancia;
    FavDB db;

    Menu menu;
    MenuItem botonFav;

    boolean isFav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.campings_detalles_activity);

        Toolbar toolbar = findViewById(R.id.new_map_toolbar);
        setSupportActionBar(toolbar);

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
        txt_distancia = findViewById(R.id.detalle_distancia);

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

        txt_web.setOnClickListener(view -> {
            Intent browserIntent = new Intent(Intent.ACTION_VIEW);
            browserIntent.setData(Uri.parse(txt_web.getText().toString().replace(" ", "+")));
            startActivity(browserIntent);
        });

        // Solicitar permiso de ubicación si no se ha concedido
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                calcularDistancia();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            try {
                calcularDistancia();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Actualizar la distancia cada vez que se reanude la actividad
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            try {
                calcularDistancia();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detalle_menu, menu);
        this.menu = menu;

        botonFav = menu.findItem(R.id.new_fav_camping);

        long id = getIntent().getLongExtra("id", 0);

        Cursor mCursor = db.isFav(id);
        if (!(mCursor.moveToFirst()) || mCursor.getCount() == 0) {
            this.isFav = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                botonFav.setIconTintList(ColorStateList.valueOf(Color.rgb(0, 0, 0)));
            }
        } else {
            this.isFav = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                botonFav.setIconTintList(ColorStateList.valueOf(Color.rgb(255, 0, 0)));
            }
        }
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
                long id = getIntent().getLongExtra("id", 0);
                if (!isFav) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        botonFav.setIconTintList(ColorStateList.valueOf(Color.rgb(255, 0, 0)));
                    }
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

                    db.insertCamping(id, name, categoria, municipio, estado, provincia, cp, direccion, email, web, numParcela, plazasParcela, plazasLibreAcampada, periodo);
                    return true;
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        botonFav.setIconTintList(ColorStateList.valueOf(Color.rgb(0, 0, 0)));
                    }
                    db.deleteCamping(id);

                }

                // Do something when the user clicks on the help item
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void calcularDistancia() throws IOException {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Obtener la ubicación actual del dispositivo
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                // Obtener la localización del camping a partir del nombre
                String campingName = txt_muni.getText().toString();
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses = geocoder.getFromLocationName(campingName, 1);
                if (addresses.size() > 0) {
                    Address campingAddress = addresses.get(0);
                    double campingLatitude = campingAddress.getLatitude();
                    double campingLongitude = campingAddress.getLongitude();

                    // Crear una instancia de Location para la ubicación del camping
                    Location campingLocation = new Location("");
                    campingLocation.setLatitude(campingLatitude);
                    campingLocation.setLongitude(campingLongitude);

                    // Calcular la distancia entre las dos ubicaciones
                    float distance = location.distanceTo(campingLocation);

                    // Actualizar el contenido del TextView con la distancia en kilómetros
                    float distanceInKm = distance / 1000;
                    txt_distancia.setText("Distancia: " + distanceInKm + " km");
                } else {
                    // No se encontró la localización del camping
                    Toast.makeText(this, "No se encontró la localización del camping", Toast.LENGTH_SHORT).show();
                }
            } else {
                // No se pudo obtener la ubicación actual, mostrar un mensaje de error
                Toast.makeText(this, "No se pudo obtener la ubicación actual", Toast.LENGTH_SHORT).show();
            }
        } else {
            // El usuario no ha concedido permiso para acceder a la ubicación, solicitar el permiso
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
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
