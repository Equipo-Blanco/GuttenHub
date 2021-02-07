package com.example.main;

import android.content.Context;
import android.content.res.Resources;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

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
            "ID_ALBARANLINEA INTEGER NOT NULL," +
            "IVA INTEGER," +
            "ID_ARTICULO INT NOT NULL," +
            "CANTIDAD INTEGER," +
            "PRECIO FLOAT," +
            "ID_ALBARANCABECERA INTEGER NOT NULL," +
            "IMPORTE FLOAT," +
            "PRIMARY KEY (ID_ALBARANLINEA, ID_ALBARANCABECERA)," +
            "FOREIGN KEY (ID_ARTICULO) REFERENCES ARTICULOS(ID_ARTICULO)," +
            "FOREIGN KEY (ID_ALBARANCABECERA) REFERENCES CABECERA_ALBARANES(ID_ALBARANCABECERA))";

    //Inserción de los comerciales predefinidos
    String sqlComercial1 = "INSERT INTO COMERCIALES " +
            "              VALUES(1, 'Sergio', 'Luhía Davila', 'DraftSL', 'Calle principal', 'Poligono 27', 696747474, 1, 'sergio@draft.com')";
    String sqlComercial2 = "INSERT INTO COMERCIALES " +
            "              VALUES(2, 'Hodei', 'Aguirre Barros', 'DraftSL', 'Calle Mayor', 'Poligono 27', 696121212, 2, 'hodei@draft.com')";
    String sqlComercial3 = "INSERT INTO COMERCIALES " +
            "              VALUES(3, 'Javi', 'Javi Seara', 'Cebanc', 'Calle 1', 'Paseo 2', 696111111, 3, 'javi@cebanc.com')";

    //Inserción de las categorías
    String sqlCategoria1 = "INSERT INTO CATEGORIAS VALUES (1, 'C', 'CAMISETAS')";
    String sqlCategoria2 = "INSERT INTO CATEGORIAS VALUES (2, 'P', 'COMPLEMENTOS')";

    //Inserción de los usuarios y contraseñas
    String sqlNuevoUsuario = "INSERT INTO LOGINUSUARIOS VALUES(2, 'Hodei', 'Hodei727', 'hodei@draft.com')";
    String sqlNuevoUsuario2 = "INSERT INTO LOGINUSUARIOS VALUES(1, 'Sergio', 'luhia', 'sergio@draft.com')";
    String sqlNuevoUsuario3 = "INSERT INTO LOGINUSUARIOS VALUES(3, 'javi', 'seara', 'javi@cebanc.com')";


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

        db.execSQL(sqlComercial1);
        db.execSQL(sqlComercial2);
        db.execSQL(sqlComercial3);

        db.execSQL(sqlCategoria1);
        db.execSQL(sqlCategoria2);

        db.execSQL(sqlNuevoUsuario);
        db.execSQL(sqlNuevoUsuario2);
        db.execSQL(sqlNuevoUsuario3);
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

        db.execSQL(sqlComercial1);
        db.execSQL(sqlComercial2);
        db.execSQL(sqlComercial3);

        db.execSQL(sqlCategoria1);
        db.execSQL(sqlCategoria2);

        db.execSQL(sqlNuevoUsuario);
        db.execSQL(sqlNuevoUsuario2);
        db.execSQL(sqlNuevoUsuario3);
    }
}


