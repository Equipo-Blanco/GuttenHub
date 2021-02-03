package com.example.main;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class tablasSQLHelper extends SQLiteOpenHelper {

    //Sentencia SQL para crear la tabla de Usuarios
    String sqlCreatePartners = "CREATE TABLE IF NOT EXISTS PARTNERS (ID_PARTNER INTEGER PRIMARY KEY," +
            "              ID_COMERCIAL INTEGER," +
            "                      EMPRESA TEXT," +
            "                      DIRECCION TEXT," +
            "                      CONTACTO TEXT," +
            "                      TELEFONO INTEGER," +
            "                      EMAIL TEXT," +
            "                      FOREIGN KEY (ID_COMERCIAL) REFERENCES COMERCIALES(ID_COMERCIAL) ON UPDATE SET NULL ON DELETE SET NULL);";
    String sqlCreateComerciales = "CREATE TABLE IF NOT EXISTS COMERCIALES(ID_COMERCIAL INTEGER PRIMARY KEY," +
            "             NOMBRE TEXT," +
            "                         APELLIDOS TEXT," +
            "                         EMPRESA TEXT," +
            "                         DIRECCION_PRIVADA TEXT," +
            "                         DIRECCION_TRABAJO TEXT," +
            "                         TELEFONO_MOVIL INTEGER," +
            "                         CODIGO TEXT," +
            "                         EMAIL TEXT)";

    String sqlCreateLogin = "CREATE TABLE LOGINUSUARIOS(ID_COMERCIAL INTEGER PRIMARY KEY, " +
                             " NOMBRE TEXT," +
                             " CONTRASEN TEXT, " +
                             " EMAIL TEXT, " +
                             " FOREIGN KEY (ID_COMERCIAL) REFERENCES COMERCIALES(ID_COMERCIAL))";


    String sqlCreateCategorias = "CREATE TABLE IF NOT EXISTS CATEGORIAS (ID_CATEGORIA INTEGER NOT NULL UNIQUE PRIMARY KEY," +
            "CODIGO TEXT NOT NULL," +
            "DESCRIPCION TEXT)";

    String sqlCreateArticulos = "CREATE TABLE IF NOT EXISTS ARTICULOS (" +
            "ID_ARTICULO INTEGER NOT NULL UNIQUE PRIMARY KEY," +
            "NOMBRE TEXT NOT NULL," +
            "ID_CATEGORIA INT NOT NULL," +
            "PRECIO_VENTA FLOAT," +
            "PRECIO_COSTE FLOAT," +
            "EXISTENCIAS INTEGER," +
            "STOCK_MINIMO INTEGER," +
            "FOTOS TEXT," +
            "FOREIGN KEY (ID_CATEGORIA) REFERENCES CATEGORIAS(ID_CATEGORIA))";

    String sqlCreateCabeceraAlbaranes = "CREATE TABLE IF NOT EXISTS CABECERA_ALBARANES (" +
            "ID_ALBARANCABECERA INTEGER NOT NULL UNIQUE PRIMARY KEY," +
            "ID_PARTNER INTEGER NOT NULL," +
            "ID_COMERCIAL INTEGER NOT NULL," +
            "FECHA_ALBARAN DATE," +
            "FECHA_ENVIO DATE," +
            "FECHA_ENTREGA DATE," +
            "DIRECCION_ENVIO TEXT," +
            "TOTAL_FACTURA FLOAT," +
            "FOREIGN KEY (ID_PARTNER) REFERENCES PARTNERS(ID_PARTNER)," +
            "FOREIGN KEY (ID_COMERCIAL) REFERENCES COMERCIALES(ID_COMERCIAL))";

    String sqlCreateLineasAlbaran = "CREATE TABLE IF NOT EXISTS LINEAS_ALBARAN (" +
            "ID_ALBARANLINEA INTEGER NOT NULL UNIQUE PRIMARY KEY," +
            "IVA INTEGER," +
            "ID_ARTICULO INT NOT NULL," +
            "CANTIDAD INTEGER," +
            "PRECIO FLOAT," +
            "ID_ALBARANCABECERA INTEGER NOT NULL," +
            "IMPORTE FLOAT," +
            "FOREIGN KEY (ID_ARTICULO) REFERENCES ARTICULOS(ID_ARTICULO)," +
            "FOREIGN KEY (ID_ALBARANCABECERA) REFERENCES CABECERA_ALBARANES(ID_ALBARANCABECERA))";

    public tablasSQLHelper(Context contexto, String nombre,
                           CursorFactory factory, int version) {
        super(contexto, nombre, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Se ejecuta la sentencia SQL de creación de la tabla en el orden correcto para que se
        //respeten las creaciones de claves extranjeras
        db.execSQL(sqlCreateCategorias);
        db.execSQL(sqlCreateComerciales);
        db.execSQL(sqlCreateArticulos);
        db.execSQL(sqlCreatePartners);
        db.execSQL(sqlCreateCabeceraAlbaranes);
        db.execSQL(sqlCreateLineasAlbaran);
        db.execSQL(sqlCreateLogin);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAnterior, int versionNueva) {
        //NOTA: Por simplicidad del ejemplo aquí utilizamos directamente la opción de
        //      eliminar la tabla anterior y crearla de nuevo vacía con el nuevo formato.
        //      Sin embargo lo normal será que haya que migrar datos de la tabla antigua
        //      a la nueva, por lo que este método debería ser más elaborado.

        //Se elimina la versión anterior de la tabla
        db.execSQL("DROP TABLE IF EXISTS LINEAS_ALBARAN");
        db.execSQL("DROP TABLE IF EXISTS CABECERA_ALBARANES");
        db.execSQL("DROP TABLE IF EXISTS PARTNERS");
        db.execSQL("DROP TABLE IF EXISTS ARTICULOS");
        db.execSQL("DROP TABLE IF EXISTS COMERCIALES");
        db.execSQL("DROP TABLE IF EXISTS CATEGORIAS");


        //Se crean las nuevas versiones de las tablas
        db.execSQL(sqlCreateCategorias);
        db.execSQL(sqlCreateComerciales);
        db.execSQL(sqlCreateArticulos);
        db.execSQL(sqlCreatePartners);
        db.execSQL(sqlCreateCabeceraAlbaranes);
        db.execSQL(sqlCreateLineasAlbaran);
        db.execSQL(sqlCreateLogin);
    }
}