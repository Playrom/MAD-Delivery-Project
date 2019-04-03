package it.polito.justorder_restaurant;

import it.polito.justorder_framework.ActivityAbstractWithSideNav;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ThirdActivity extends ActivityAbstractWithSideNav {

    protected ListView listView;
    protected BaseAdapter adapter;

    String[] data = {"Kebab", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi"
            , "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi", "Pizza", "Pasta", "Sushi"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.listView = findViewById(R.id.food_list);
        this.routeHandler = new ResturantActivityWithSideNav();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        this.adapter = new BaseAdapter() {
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
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.product_list_title);
    }
}
