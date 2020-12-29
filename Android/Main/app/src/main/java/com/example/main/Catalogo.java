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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Catalogo extends AppCompatActivity {

    ListView listView;
    TextView txv_trad;
    Button bot_gridV;
    String[] productos;
    String[] traduccRapida;
   /* int[] imagenes = {R.drawable.esp, R.drawable.eus, R.drawable.ing, R.drawable.ita,
            R.drawable.nor, R.drawable.ale, R.drawable.chn, R.drawable.jap,
            R.drawable.isl, R.drawable.fin, R.drawable.rus, R.drawable.est,
            R.drawable.sue, R.drawable.haw, R.drawable.tur, R.drawable.pol,
            R.drawable.gre, R.drawable.kor, R.drawable.cat};*/
    int mSelectedItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogo);

        listView = findViewById(R.id.lv1_lista);
        productos = getResources().getStringArray(R.array.productos);
        //traduccRapida = getResources().getStringArray(R.array.frases);

        //txv_trad = (TextView) findViewById(R.id.txv_traduccion);
        //bot_gridV = (Button) findViewById(R.id.btn_grid);

        MyAdapter adapter = new MyAdapter(this, productos);
        listView.setAdapter(adapter);

/*        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSelectedItem = position;
                txv_trad.setText(traduccRapida[position]);

            }
        });*/
    }

    class MyAdapter extends ArrayAdapter<String> {
        Context context;
        String rTitle[];
        String rDescription[];
        int img[];

/*
        public MyAdapter(Context c, String[] title, String[] description, int[] _img) {
            super(c, R.layout.row, R.id.textView1, title);
            this.context = c;
            this.rTitle = title;
            this.rDescription = description;
            this.img = _img;
        }
*/

        public MyAdapter(Context c, String[] productos) {
            super(c, R.layout.row, R.id.textView1, productos);
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
            //viewHolder.tv2.setText(rDescription[position]);
            //viewHolder.imv1.setImageResource(img[position]);

            return row;
        }
    }

    public class ViewHolder {
        TextView tv1;
        //TextView tv2;
        //ImageView imv1;

        public ViewHolder(View itemView) {
            tv1 = itemView.findViewById(R.id.textView1);
            //tv2 = itemView.findViewById(R.id.textView2);
            //imv1 = itemView.findViewById(R.id.imagenes);
        }
    }

}