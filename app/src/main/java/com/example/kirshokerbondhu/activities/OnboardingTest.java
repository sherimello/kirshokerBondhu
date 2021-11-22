package com.example.kirshokerbondhu.activities;

import android.animation.Animator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.kirshokerbondhu.R;
import com.example.kirshokerbondhu.classes.OnSwipeTouchListener;
import com.example.kirshokerbondhu.classes.TypeWriter;


public class OnboardingTest extends AppCompatActivity {

    private static final String ON_BOARDING_SEEN_STATE = "isOnboardingSeen";
    private CardView cardView, cardView2, cardView3, cardView4;
    private ImageView screw;
    private LinearLayout linearLayout1, linearLayout2, linearLayout3, linearLayout4;
    private View swipe_view1, swipe_view2, swipe_view3, swipe_view4;
    private TypeWriter text_intro_prompt;
    private Button button_continue;
    private ImageView image_bg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding_test);

        //        getWindow().setStatusBarColor(getResources().getColor(R.color.transparent));

        cardView = findViewById(R.id.card1);
        cardView2 = findViewById(R.id.card2);
        cardView3 = findViewById(R.id.card3);
        cardView4 = findViewById(R.id.card4);
        screw = findViewById(R.id.image_screw);

        linearLayout1 = findViewById(R.id.linear1);
        linearLayout2 = findViewById(R.id.linear2);
        linearLayout3 = findViewById(R.id.linear3);
        linearLayout4 = findViewById(R.id.linear4);

        text_intro_prompt = findViewById(R.id.text_intro_prompt);

        button_continue = findViewById(R.id.button_continue);

        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.onboarding_layout4, linearLayout1);
        View view2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.onboarding_layout3, linearLayout2);
        View view3 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.onboarding_layout2, linearLayout3);
        View view4 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.onboarding_layout1, linearLayout4);

        swipe_view1 = view.findViewById(R.id.swipe_view);
        swipe_view2 = view2.findViewById(R.id.swipe_view);
        swipe_view3 = view3.findViewById(R.id.swipe_view);
        swipe_view4 = view4.findViewById(R.id.swipe_view);

        screw.setTranslationX(.05f * getResources().getDisplayMetrics().widthPixels + DpTopx(21));
        screw.setTranslationY(.035f * getResources().getDisplayMetrics().heightPixels + DpTopx(21));
        cardView.setPivotX(.05f * getResources().getDisplayMetrics().widthPixels + DpTopx(21));
        cardView.setPivotY(.035f * getResources().getDisplayMetrics().heightPixels + DpTopx(21));
        cardView2.setPivotX(.05f * getResources().getDisplayMetrics().widthPixels + DpTopx(21));
        cardView2.setPivotY(.035f * getResources().getDisplayMetrics().heightPixels + DpTopx(21));
        cardView3.setPivotX(.05f * getResources().getDisplayMetrics().widthPixels + DpTopx(21));
        cardView3.setPivotY(.035f * getResources().getDisplayMetrics().heightPixels + DpTopx(21));
        cardView4.setPivotX(.05f * getResources().getDisplayMetrics().widthPixels + DpTopx(21));
        cardView4.setPivotY(.035f * getResources().getDisplayMetrics().heightPixels + DpTopx(21));

        swipe_action(swipe_view1);
        swipe_action(swipe_view2);
        swipe_action(swipe_view3);
        swipe_action(swipe_view4);

    }

    public int DpTopx(int dp) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density);
    }

    public void swipe_action(View v) {

        v.setOnTouchListener(new OnSwipeTouchListener(getApplicationContext()) {
            @Override
            public void onSwipeRight() {
                super.onSwipeRight();
                if (cardView.getRotation() > -80) {
                    cardView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItems(cardView, -90);
                    return;
                }
                if (cardView2.getRotation() > -85) {
                    cardView2.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItems(cardView2, -85);
                    return;
                }
                if (cardView3.getRotation() > -80) {
                    cardView3.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItems(cardView3, -80);
                    return;
                }
                cardView4.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                animateMenuItems(cardView4, -75);
            }

            @Override
            public void onSwipeLeft() {
                super.onSwipeLeft();
                if (cardView4.getRotation() < 0) {
                    cardView4.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItemsReverse(cardView4);
                    return;
                }
                if (cardView3.getRotation() < 0) {
                    cardView3.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItemsReverse(cardView3);
                    return;
                }
                if (cardView2.getRotation() < 0) {
                    cardView2.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItemsReverse(cardView2);
                    return;
                }
                cardView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                animateMenuItemsReverse(cardView);
            }

            @Override
            public void onSwipeDown() {
                super.onSwipeDown();
                if (cardView4.getRotation() < 0) {
                    cardView4.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItemsReverse(cardView4);
                    return;
                }
                if (cardView3.getRotation() < 0) {
                    cardView3.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItemsReverse(cardView3);
                    return;
                }
                if (cardView2.getRotation() < 0) {
                    cardView2.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                    animateMenuItemsReverse(cardView2);
                    return;
                }
                cardView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
                animateMenuItemsReverse(cardView);
            }
        });

    }

    private void animateMenuItemsReverse(View view) {
        view.animate().rotation(0).setDuration(700).setInterpolator(new OvershootInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setLayerType(View.LAYER_TYPE_NONE, null);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    private void animateMenuItems(View view, float f) {
        view.animate().rotation(f).setDuration(700).setInterpolator(new OvershootInterpolator()).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setLayerType(View.LAYER_TYPE_NONE, null);
                if (view == cardView4) {

                    text_intro_prompt.setCharacterDelay(21);
                    text_intro_prompt.animateText(getString(R.string.intro_prompt));

                    new Handler().postDelayed(() -> button_continue.setVisibility(View.VISIBLE), getResources().getString(R.string.intro_prompt).length() * 30L);

                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void goto_SignIn(View view) {
        SharedPreferences.Editor editor = getSharedPreferences(ON_BOARDING_SEEN_STATE, MODE_PRIVATE).edit();
        editor.putInt("status", 1);
        editor.apply();
        startActivity(new Intent(getApplicationContext(), SignIn.class));
        finish();
    }
}