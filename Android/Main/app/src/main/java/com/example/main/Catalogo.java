package com.example.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Catalogo extends AppCompatActivity {

    ListView listView;
    TextView txv_descrip, txv_prod, txv_prec;
    ImageView iv_imgProducto;
    String[] productos;
    String[] descripciones;
    String[] precios;
    int[] imagenes = {R.drawable.falcons_local, R.drawable.falcons_visit,
            R.drawable.patriots_local, R.drawable.patriots_visit};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        listView = findViewById(R.id.lv1_lista);
        iv_imgProducto = (ImageView) findViewById(R.id.iv_foto);
        txv_prod = (TextView) findViewById(R.id.tv_prod);
        txv_descrip = (TextView) findViewById(R.id.tv_descrip);
        txv_prec = (TextView) findViewById(R.id.tv_precio);

        productos = getResources().getStringArray(R.array.productos);
        precios = getResources().getStringArray(R.array.precios);

        MyAdapter adapter = new MyAdapter(this, productos);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //txv_descrip.setText(descripciones[position]);
                txv_prod.setText(productos[position]);
                try {
                    iv_imgProducto.setImageResource(imagenes[position]);
                } catch (Exception e) {
                    System.out.println("No hay imagen para ese producto");
                    iv_imgProducto.setImageResource(R.drawable.draft);
                }

                try {
                    txv_prec.setText("Precio: " + precios[position]);
                } catch (Exception e) {
                    System.out.println("No se ha establecido un precio");
                    txv_prec.setText("Precio: N/D");
                }
            }
        });
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];

        public MyAdapter(Context c, String[] productos) {
            super(c, R.layout.row, R.id.tv_titpartner, productos);
            this.context = c;
            this.rTitle = productos;
        }

        @NonNull
        @Override
        public View getView(int position, @androidx.annotation.Nullable View convertView, @androidx.annotation.NonNull ViewGroup parent) {
            View row = convertView;
            ViewHolder viewHolder;

            if (row == null) {
                row = LayoutInflater.from(getContext()).inflate(R.layout.row, parent, false);

                viewHolder = new ViewHolder(row);

                row.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) row.getTag();
            }
            viewHolder.tv1.setText(rTitle[position]);
            return row;
        }
    }

    public class ViewHolder {
        TextView tv1;

        public ViewHolder(View itemView) {
            tv1 = itemView.findViewById(R.id.tv_titpartner);
        }
    }

}