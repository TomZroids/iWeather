package com.gitonway.lee.niftymodaldialogeffects.lib.effects;

import android.view.View;

import com.nineoldandroids.animation.ObjectAnimator;
public class Shake  extends BaseEffects{

    @Override
    protected void setupAnimation(View view) {
        getAnimatorSet().playTogether(
                ObjectAnimator.ofFloat(view, "translationX", 0, .10f, -40, .26f, 40,.42f, -40, .40f, 40,.70f,-40,.90f,1,0).setDuration(mDuration)

        );
    }
}
