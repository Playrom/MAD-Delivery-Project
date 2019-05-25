package it.polito.justorder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import it.polito.justorder_framework.db.Database;
import it.polito.justorder_framework.model.Product;
import it.polito.justorder_framework.model.Restaurant;
import it.polito.justorder_framework.model.Review;
import it.polito.justorder_framework.model.User;

public class ReviewActivity extends AppCompatActivity {

    private RatingBar mRatingBar;
    private TextView mRatingScale;
    private EditText mFeedback;
    private Button mSendFeedback;
    private Restaurant restaurant;
    private Review review;
    private User user;
    private int stars = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        this.restaurant = (Restaurant) intent.getSerializableExtra("restaurant");
        this.user = Database.INSTANCE.getCurrent_User();
        setContentView(R.layout.activity_review);
        mRatingBar = findViewById(R.id.ratingBar);
        mRatingScale = findViewById(R.id.tvRatingScale);
        mFeedback = findViewById(R.id.reviewField);
        mSendFeedback = findViewById(R.id.btnSubmit);

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

        mSendFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mFeedback.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in feedback text box", Toast.LENGTH_LONG).show();
                } else {
                    review = new Review();
                    review.setRestaurantKey(restaurant.getKeyId());
                    review.setComment(mFeedback.getText().toString());
                    review.setStars(stars);
                    review.setReviewerKey(user.getKeyId());
                    Database.INSTANCE.getReviews().save(review);
                    Toast.makeText(getApplicationContext(), "Thank you for sharing your feedback", Toast.LENGTH_SHORT).show();
                    finish();

                }
            }
        });

    }
}
