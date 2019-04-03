package it.polito.justorder_restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ThirdActivity extends AppCompatActivity {

    private ListView lv;
    String[] data = {"Kebab", "Pizza", "Pasta", "Sushi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        lv = findViewById(R.id.food_list);
        lv.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return data.length;
            }

            @Override
            public Object getItem(int position) {
                return data[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_adapter, parent, false);
                }
                ((TextView)convertView.findViewById(R.id.counter)).setText("" + (position + 1));
                ((TextView)convertView.findViewById(R.id.counter)).setText(data[position]);
                return convertView;
            }
        });
    }
}
