package it.polito.justorder;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import it.polito.justorder_framework.Utils;
import it.polito.justorder_framework.abstract_activities.AbstractEditor;
import it.polito.justorder_framework.common_activities.UserSettingsEditorActivity;
import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Deliverer;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.Review;
import it.polito.justorder_framework.model.User;
import kotlin.Unit;

public class ReviewActivity extends AbstractEditor {

    private RatingBar mRatingBar;
    private TextView mRatingScale;
    private EditText mFeedback;
    private Restaurant restaurant;
    private Product product;
    private User user_deliverer;
    private Deliverer deliverer;
    private Review review;
    private User user;
    private int stars = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        this.setupActivity();
    }

    @Override
    protected void setupActivity() {
        super.setupActivity();
        this.restaurant = (Restaurant) i.getSerializableExtra("restaurant");
        this.product = (Product) i.getSerializableExtra( "product");
        this.user_deliverer = (User) i.getSerializableExtra("deliverer");

        this.user = Database.INSTANCE.getCurrent_User();
        mRatingBar = findViewById(R.id.ratingBar);
        mRatingScale = findViewById(R.id.tvRatingScale);
        mFeedback = findViewById(R.id.reviewField);

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                mRatingScale.setText(String.valueOf(v));
                stars = (int) v;
                switch ((int) ratingBar.getRating()) {
                    case 1:
                        mRatingScale.setText("Very bad");
                        break;
                    case 2:
                        mRatingScale.setText("Need some improvement");
                        break;
                    case 3:
                        mRatingScale.setText("Good");
                        break;
                    case 4:
                        mRatingScale.setText("Great");
                        break;
                    case 5:
                        mRatingScale.setText("Awesome. I love it");
                        break;
                    default:
                        mRatingScale.setText("");
                }
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.saveUserData) {
            if (restaurant != null && product == null) {
                review = new Review();
                review.setRestaurantKey(restaurant.getKeyId());
                review.setComment(mFeedback.getText().toString());
                review.setStars(stars);
                review.setReviewerKey(user.getKeyId());
                Database.INSTANCE.getReviews().save(review);
                Toast.makeText(getApplicationContext(), "Thank you for sharing your feedback about this restaurant", Toast.LENGTH_SHORT).show();
                finish();
            }else if (product != null && restaurant!=null) {
                review = new Review();
                review.setProductKey(product.getKeyId());
                review.setRestaurantKey(restaurant.getKeyId());
                review.setComment(mFeedback.getText().toString());
                review.setStars(stars);
                review.setReviewerKey(user.getKeyId());
                Database.INSTANCE.getReviews().save(review);
                Toast.makeText(getApplicationContext(), "Thank you for sharing your feedback about this product", Toast.LENGTH_SHORT).show();
                finish();
            }else if (user_deliverer != null) {
                review = new Review();
                Database.INSTANCE.getDeliverers().get(user_deliverer.getDelivererKey(), deliverer1 -> {
                    deliverer = deliverer1;
                    review.setDelivererKey(deliverer.getKeyId());
                    review.setComment(mFeedback.getText().toString());
                    review.setStars(stars);
                    review.setReviewerKey(user.getKeyId());
                    Database.INSTANCE.getReviews().save(review);
                    Toast.makeText(getApplicationContext(), "Thank you for sharing your feedback about this deliverer", Toast.LENGTH_SHORT).show();
                    finish();
                    return Unit.INSTANCE;
                });

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_second_menu, menu);
        return true;
    }
}
