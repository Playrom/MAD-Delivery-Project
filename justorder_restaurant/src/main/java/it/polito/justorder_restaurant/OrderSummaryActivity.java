package it.polito.justorder_restaurant;

import androidx.appcompat.app.AppCompatActivity;
import it.polito.justorder_framework.ActivityAbstractWithSideNav;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemClickListener;


public class OrderSummaryActivity extends ActivityAbstractWithSideNav {

    protected ListView listView;
    protected BaseAdapter adapter;
    //protected ListView itemDetails;

    String[] order_id = {"Order n* 1", "Order n* 2", "Order n* 3", "Order n* 4"};
    String[] address = {"Via Tale 1", "Via Quale 2", "Corso Lecce 1", "Piazza Rivoli"};
    String[] amount = {"17.40 €", "18.00 €", "134.54 €", "1.70 €"};
    String[] timestamp = {"01/04/2019 17:40", "01/04/2019 17:41", "01/04/2019 17:42", "01/04/2019 17:43"};
    //String[] food = {"", "", "", ""};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_summary);
        this.setupActivity();
    }


    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.listView = findViewById(R.id.order_list);
        this.routeHandler = new ResturantActivityWithSideNav();
        this.reloadData();
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();
        this.adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return order_id.length;
            }

            @Override
            public Object getItem(int position) {
                return order_id[position];
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if(convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.order_adapter, parent, false);
                }
                ((TextView)convertView.findViewById(R.id.order_id)).setText(order_id[position]);
                ((TextView)convertView.findViewById(R.id.amount)).setText(amount[position]);
                ((TextView)convertView.findViewById(R.id.address)).setText(address[position]);
                ((TextView)convertView.findViewById(R.id.timestamp)).setText(timestamp[position]);
                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.order_summary_title);
    }

    /*
    listview.setOnItemClickListener(new OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> adapter ,View itemDetails, int position){
            ItemClicked item = adapter.getItem(position);

            Intent intent = new Intent(Activity.this, destinationActivity.class);
            //based on item add info to intent
            startActivity(intent);
        }
    });
    */


}





