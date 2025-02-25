package it.polito.justorder;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import it.polito.justorder_framework.R;
import it.polito.justorder_framework.abstract_activities.AbstractListViewWithSidenavSave;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Order;
import it.polito.justorder_framework.model.OrderProduct;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class CartProductsUserListActivity extends AbstractListViewWithSidenavSave {

    protected ListView listView;
    protected BaseAdapter adapter;
    protected FloatingActionButton fab;
    protected int tapped;
    protected User user;
    protected Restaurant restaurant;
    protected List<String> productKeys = new ArrayList<>();
    protected List<Product> productList = new ArrayList<>();
    protected Map<String, Product> products = new HashMap<>();
    protected Product product12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_list_activity);
        this.setupActivity();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_confirm_delete_menu, menu);
        return true;
    }

    @Override
    protected void setupActivity() {

        super.setupActivity();
        this.initDataSource();
        this.listView = findViewById(R.id.food_list);
        this.fab = findViewById(R.id.fab);
        this.fab.hide();
        this.adapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return productList.size();
            }

            @Override
            public Object getItem(int position) {
                return productList.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.product_item_adapter, parent, false);
                }

                if (productList.get(position) != null) {

                    Product product = productList.get(position);
                    Integer qty = user.getProducts().get(product.getKeyId());
                    String x = String.format("%.02f", new Float(product.getCost()));
                    ((TextView) convertView.findViewById(R.id.content)).setText(product.getName());
                    ((TextView) convertView.findViewById(R.id.description)).setText("cost: " + x + " € ,quantity: " + qty);
                    Glide.with(CartProductsUserListActivity.this).load(product.getImageUri()).into((ImageView) convertView.findViewById(R.id.image));

                }

                return convertView;
            }
        };
        this.listView.setAdapter(this.adapter);

        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Product entry = (Product) parent.getAdapter().getItem(position);
                Intent intent = new Intent(CartProductsUserListActivity.this, CartProductActivity.class);

                intent.putExtra("product", entry);
                intent.putExtra("qty", user.getProducts().get(entry.getKeyId()));
                tapped = position;
                CartProductsUserListActivity.this.startActivityForResult(intent, 1);
            }
        });

        this.reloadData();
    }

    protected void initDataSource() {

        this.user = Database.INSTANCE.getCurrent_User();
        if (this.user != null && this.user.getProducts() != null) {
            if(this.user.getProducts().size()!=0) {
                this.productKeys = new ArrayList<>(this.user.getProducts().keySet());
                products.clear();
                Database.INSTANCE.getRestaurants().get(user.getCurrentRestaurant(), (restaurant1 -> {
                    this.restaurant = restaurant1;
                    for (String prod : productKeys) {
                        this.products.put(prod, this.restaurant.getProducts().get(prod));
                    }
                    this.productList = new ArrayList<>(products.values());
                    this.reloadData();
                    return Unit.INSTANCE;
                }));
            }
        }
    }

    @Override
    protected void reloadData() {
        super.reloadData();
        this.reloadViews();
    }

    @Override
    protected void reloadViews() {
        super.reloadViews();

        if(this.user != null && productList.size()!=0 && user.getProducts()!= null) {
            if(user.getProducts().size()!=0){
                Integer i = user.getProducts().size();
                Double total = 0.0;
                while(i>0){
                    Product product = productList.get(i-1);
                    Integer qty = user.getProducts().get(product.getKeyId());
                    total = total + qty * product.getCost();
                    i--;
                }
                String x = String.format("%.02f", new Float(total));
                this.actionBar.setTitle("Cart total: " + x + " €");
                findViewById(R.id.empty).setVisibility(View.GONE);
            }
        } else {
            if(this.user!=null){
                this.actionBar.setTitle(this.user.getName() + "'s Cart");  findViewById(R.id.empty).setVisibility(View.VISIBLE);
            } else {
                this.actionBar.setTitle( "Cart");  findViewById(R.id.empty).setVisibility(View.VISIBLE);
            }
        }





        this.adapter.notifyDataSetChanged();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == it.polito.justorder_framework.R.id.saveUserData) {


            Order order = new Order();
            order.setState("pending");

            if (user.getCurrentRestaurant() != null && products != null && user != null && user.getProducts() != null) {

                if (user.getProducts().size() != 0) {
                    Database.INSTANCE.getRestaurants().get(user.getCurrentRestaurant(), true, (restaurant -> {
                        this.restaurant = restaurant;
                        this.reloadData();
                        return Unit.INSTANCE;
                    }));


                    order.setRestaurantName(restaurant.getName());
                    order.setRestaurantAddress(restaurant.getAddress());
                    order.setRestaurant(restaurant.getKeyId());

                    Map<String, OrderProduct> mm = order.getProducts();
                    Double orderPrice = 0.0;

                    Iterator<Map.Entry<String, Integer>> iterator = user.getProducts().entrySet().iterator();
                    while (iterator.hasNext()) {

                        Map.Entry<String, Integer> entry = iterator.next();
                        product12 = products.get(entry.getKey());
                        Double price = entry.getValue() * product12.getCost();
                        orderPrice = orderPrice + price;
                        order.setPrice(orderPrice);
                        OrderProduct op = new OrderProduct();
                        op.setProductKey(product12.getKeyId());
                        op.setRestaurantKey(user.getCurrentRestaurant());
                        op.setQuantity(entry.getValue());
                        mm.put(product12.getKeyId(), op);

                    }

                    order.setProducts(mm);
                    order.setUserName(user.getName());
                    order.setUserAddress(user.getAddress());
                    order.setUser(user.getKeyId());

                    Database.INSTANCE.getOrders().save(order);

                    Map<String, Boolean> m = restaurant.getOrders();
                    m.put(order.getKeyId(), true);
                    restaurant.setOrders(m);
                    Database.INSTANCE.getRestaurants().save(restaurant);

                    Map<String, Boolean> m1 = user.getOrders();
                    m1.put(order.getKeyId(), true);
                    user.setOrders(m1);
                    user.setCurrentRestaurant("");
                    user.setProducts(null);
                    productList.clear();
                    productKeys.clear();
                    Database.INSTANCE.getUsers().save(user);

                    Toast toast = Toast.makeText(getApplicationContext(), "Order successfully received by the restaurant, soon a deliveryman will deliver your order", Toast.LENGTH_SHORT);
                    toast.show();
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "Please select first some products to order", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        }
        if (item.getItemId() == R.id.delete && user != null) {

            user.setCurrentRestaurant("");
            user.setProducts(null);
            productList.clear();
            productKeys.clear();
            Database.INSTANCE.getUsers().save(user);

            Toast toast = Toast.makeText(getApplicationContext(), "Cart cleared", Toast.LENGTH_SHORT);
            toast.show();
            this.reloadViews();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("user", user);
        outState.putSerializable("rest", restaurant);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        user = (User) savedInstanceState.getSerializable("user");
        restaurant = (Restaurant) savedInstanceState.getSerializable("rest");
        reloadViews();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // check that it is the SecondActivity with an OK result
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                // get String data from Intent
               Integer qty = (Integer) data.getSerializableExtra("qty");

               Product prod= (Product) data.getSerializableExtra("product");

               if (qty == 0){
                   user.getProducts().remove(prod.getKeyId());
                   Integer i = this.productList.size();
                   while(i>0){
                     Product p =  this.productList.get(i-1);
                     if(p.getKeyId().compareTo(prod.getKeyId())==0){
                         this.productList.remove(i-1);
                     }
                     i--;
                   }
               } else {
                   user.getProducts().put(prod.getKeyId(), qty);
               }
               if(user.getProducts().size()==0 ) {
                   user.setCurrentRestaurant("");
                   user.setProducts(null);
                   productList.clear();
                   productKeys.clear();
               }
                Database.INSTANCE.getUsers().save(user);
                this.setupActivity();

            }
        }
    }
}