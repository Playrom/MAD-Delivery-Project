package it.polito.justorder_restaurant;

import androidx.annotation.Nullable;
import it.polito.justorder_framework.ActivityAbstractWithSideNav;
import it.polito.justorder_framework.ProductEntity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends ActivityAbstractWithSideNav {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    private int tapped;

    ArrayList<ProductEntity> products = new ArrayList<ProductEntity>() {{
        add(new ProductEntity("Name1", null, "45", "", "", "Pizza"));
        add(new ProductEntity("Name2", null, "45", "", "", "Pizza"));
        add(new ProductEntity("Name3", null, "45", "", "", "Pizza"));
        add(new ProductEntity("Name4", null, "45", "", "", "Pizza"));
    }};

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
        this.fab = findViewById(R.id.fab);
        this.routeHandler = new ResturantActivityWithSideNav();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductEntity entry= (ProductEntity) parent.getAdapter().getItem(position);
                Intent intent = new Intent(ThirdActivity.this, ProductActivity.class);
                intent.putExtra("name", entry.getName());
                intent.putExtra("cost", entry.getCost());
                intent.putExtra("notes", entry.getNotes());
                intent.putExtra("ingredients", entry.getIngredients());
                intent.putExtra("imageFileName", entry.getImageFileName());
                intent.putExtra("category", entry.getCategory());
                tapped = position;
                startActivityForResult(intent, 1);
            }
        });
        this.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ThirdActivity.this, ProductEditActivity.class);
                startActivityForResult(intent, 2);
            }
        });
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
                return products.size();
            }

            @Override
            public Object getItem(int position) {
                return products.get(position);
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

                if(products.get(position) != null){
                    ProductEntity product = products.get(position);
                    ((TextView)convertView.findViewById(R.id.content)).setText(product.getName());
                    ((TextView)convertView.findViewById(R.id.description)).setText(product.getCost());

                    if(product.getImageFileName() != null) {
                        try {
                            Bitmap selectedImage = BitmapFactory.decodeStream(getApplicationContext().openFileInput(product.getImageFileName()));
                            ((ImageView)convertView.findViewById(R.id.image)).setImageBitmap(selectedImage);
                        } catch (Exception e) {
                            ((ImageView)convertView.findViewById(R.id.image)).setImageResource(R.drawable.ic_launcher_background);
                        }
                    }else{
                        ((ImageView)convertView.findViewById(R.id.image)).setImageResource(R.drawable.ic_launcher_background);
                    }
                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);
        this.actionBar.setTitle(R.string.product_list_title);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if(requestCode == 1){
            if(resultCode== Activity.RESULT_OK){
                ProductEntity productEntity = products.get(tapped);
                productEntity.setName(data.getStringExtra("name"));
                productEntity.setCost(data.getStringExtra("cost"));
                productEntity.setNotes(data.getStringExtra("notes"));
                productEntity.setIngredients(data.getStringExtra("ingredients"));
                productEntity.setCategory(data.getStringExtra("category"));
                productEntity.setImageFileName(data.getStringExtra("imageFileName"));

                this.reloadViews();
            }
        }else if(requestCode == 2){
            if(resultCode== Activity.RESULT_OK){
                ProductEntity productEntity = new ProductEntity();
                productEntity.setName(data.getStringExtra("name"));
                productEntity.setCost(data.getStringExtra("cost"));
                productEntity.setNotes(data.getStringExtra("notes"));
                productEntity.setIngredients(data.getStringExtra("ingredients"));
                productEntity.setCategory(data.getStringExtra("category"));
                productEntity.setImageFileName(data.getStringExtra("imageFileName"));
                products.add(productEntity);
                this.reloadViews();
            }
        }
    }
}
