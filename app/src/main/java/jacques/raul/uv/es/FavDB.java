package jacques.raul.uv.es;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class FavDB extends SQLiteOpenHelper {
    private static int DB_VERSION = 1;
    private static String DATABASE_NAME = "CampingsDB";
    public static String TABLE_NAME = "favoriteTable";
    public static String KEY_ID = "id";
    public static String CAMPING_NAME = "campingName";
    public static String CAMPING_CATEGORY = "Categoria";
    public static String CAMPING_MUNICIPIO = "Municipio";
    public static String CAMPING_ESTADO = "Estado";
    public static String CAMPING_PROVINCIA = "Provincia";
    public static String CAMPING_CP = "CP";
    public static String CAMPING_DIRECCION = "Direccion";
    public static String CAMPING_EMAIL = "Email";
    public static String CAMPING_WEB = "Web";
    public static String CAMPING_NUMPARCELAS = "NumParcelas";
    public static String CAMPING_PLAZASPARCELA = "PlazasParcela";
    public static String CAMPING_PLAZASLIBREACAMPADA = "PlazasLibreAcampada";
    public static String CAMPING_PERIODO = "Periodo";

    private static String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY NOT NULL," + CAMPING_NAME + " TEXT," + CAMPING_CATEGORY + " TEXT," +
            CAMPING_MUNICIPIO + " TEXT," + CAMPING_ESTADO + " TEXT," + CAMPING_PROVINCIA + " TEXT,"
            + CAMPING_CP + " TEXT," + CAMPING_DIRECCION + " TEXT," + CAMPING_EMAIL + " TEXT," + CAMPING_WEB + " TEXT,"
            + CAMPING_NUMPARCELAS + " TEXT," + CAMPING_PLAZASPARCELA + " TEXT," + CAMPING_PLAZASLIBREACAMPADA + " TEXT,"
            + CAMPING_PERIODO + " TEXT" + ")";
    private static String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public FavDB(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }

    public void insertCamping(int id, String nombre, String categoria, String municipio, String estado, String provincia, String cp, String direccion, String email, String web, String numParcelas, String plazasParcela, String plazasLibreAcampada, String periodo) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, id);
        values.put(CAMPING_NAME, nombre);
        values.put(CAMPING_CATEGORY, categoria);
        values.put(CAMPING_MUNICIPIO, municipio);
        values.put(CAMPING_ESTADO, estado);
        values.put(CAMPING_PROVINCIA, provincia);
        values.put(CAMPING_CP, cp);
        values.put(CAMPING_DIRECCION, direccion);
        values.put(CAMPING_EMAIL, email);
        values.put(CAMPING_WEB, web);
        values.put(CAMPING_NUMPARCELAS, numParcelas);
        values.put(CAMPING_PLAZASPARCELA, plazasParcela);
        values.put(CAMPING_PLAZASLIBREACAMPADA, plazasLibreAcampada);
        values.put(CAMPING_PERIODO, periodo);

        db.insert(TABLE_NAME, null, values);
    }

    public void deleteCamping(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        String where = KEY_ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};

        db.delete(TABLE_NAME, where, whereArgs);
    }

    public Cursor read_all_data() {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] colums = {
                KEY_ID,
                CAMPING_NAME,
                CAMPING_CATEGORY,
                CAMPING_MUNICIPIO,
                CAMPING_ESTADO,
                CAMPING_PROVINCIA,
                CAMPING_CP,
                CAMPING_DIRECCION,
                CAMPING_EMAIL,
                CAMPING_WEB,
                CAMPING_NUMPARCELAS,
                CAMPING_PLAZASPARCELA,
                CAMPING_PLAZASLIBREACAMPADA,
                CAMPING_PERIODO
        };

        String sortOrder = KEY_ID + " DESC";
        Cursor cursor = db.query(
                TABLE_NAME,
                colums,
                null,
                null,
                null,
                null,
                sortOrder
        );
        return cursor;
    }

    public Cursor isFav(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] colums = {
                KEY_ID,
                CAMPING_NAME,
                CAMPING_CATEGORY,
                CAMPING_MUNICIPIO
        };

        String sortOrder = KEY_ID + " DESC";
        String where = KEY_ID + " = ?";
        String[] whereArgs = {Integer.toString(id)};
        Cursor cursor = db.query(
                TABLE_NAME,
                colums,
                where,
                whereArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }
}
