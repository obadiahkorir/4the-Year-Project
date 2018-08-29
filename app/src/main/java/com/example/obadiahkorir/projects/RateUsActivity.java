package com.example.obadiahkorir.projects;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.codemybrainsout.ratingdialog.RatingDialog;

/**
 * Created by Obadiah Korir on 8/9/2018.
 */

public class RateUsActivity extends AppCompatActivity {

        private static final String TAG = RateUsActivity.class.getSimpleName();
        private RelativeLayout rlRate;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_rateus);

                rlRate = (RelativeLayout) findViewById(R.id.rlRate);
                rlRate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                showDialog();
                        }
                });
        }

        private void showDialog() {

                final RatingDialog ratingDialog = new RatingDialog.Builder(this)
                        .session(7)
                        .threshold(3)
                        .title("How was your experience with Emergency App?")
                        .titleTextColor(R.color.black)
                        .positiveButtonText("Not Now")
                        .negativeButtonText("Never")
                        .positiveButtonTextColor(R.color.white)
                        .negativeButtonTextColor(R.color.grey_500)
                        .formTitle("Submit Feedback")
                        .formHint("Tell us where we can improve")
                        .formSubmitText("Submit")
                        .formCancelText("Cancel")
                        .ratingBarColor(R.color.yellow)
                        .playstoreUrl("www.fontech.co.ke")
                        .onThresholdCleared(new RatingDialog.Builder.RatingThresholdClearedListener() {
                                @Override
                                public void onThresholdCleared(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                                        //do something
                                        ratingDialog.dismiss();
                                }
                        })
                        .onThresholdFailed(new RatingDialog.Builder.RatingThresholdFailedListener() {
                                @Override
                                public void onThresholdFailed(RatingDialog ratingDialog, float rating, boolean thresholdCleared) {
                                        //do something
                                        ratingDialog.dismiss();
                                }
                        })
                        .onRatingChanged(new RatingDialog.Builder.RatingDialogListener() {
                                @Override
                                public void onRatingSelected(float rating, boolean thresholdCleared) {

                                }
                        })
                        .onRatingBarFormSumbit(new RatingDialog.Builder.RatingDialogFormListener() {
                                @Override
                                public void onFormSubmitted(String feedback) {

                                }
                        }).build();

                ratingDialog.show();
        }
}
