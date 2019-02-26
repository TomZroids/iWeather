package com.feige.animatetest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.feige.iweather.Weather;
import com.feige.iweather.Welcome;
import com.feige.sweather.R;

public class MainActivity_Sp extends Activity implements
		OnGlobalLayoutListener, OnScrollChangedListener {
	private ObservableScrollView mScrollView;
	private View mAnimView;
	private View mAnimView2;
	private int mScrollViewHeight;
	private int mStartAnimateTop;
	ImageButton button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(com.feige.sweather.R.layout.activitymain);

		button = (ImageButton) findViewById(R.id.imageView14);
		button.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Intent intent = new Intent();
				intent.setClass(MainActivity_Sp.this, Weather.class);
				startActivity(intent);
				finish();

			}
		});

		mScrollView = (ObservableScrollView) this
				.findViewById(R.id.scrollView1);
		mScrollView.getViewTreeObserver().addOnGlobalLayoutListener(this);
		mScrollView.setOnScrollChangedListener(this);

		mAnimView2 = this.findViewById(R.id.imageView1);
		mAnimView = this.findViewById(R.id.imageView0);
		mAnimView2.setVisibility(View.INVISIBLE);
		mAnimView.setVisibility(View.INVISIBLE);

		initView();

		initData();
	}

	@Override
	public void onGlobalLayout() {
		mScrollViewHeight = mScrollView.getHeight();
		mStartAnimateTop = mScrollViewHeight / 3 * 2;
	}

	boolean hasStart = false;

	@Override
	public void onScrollChanged(int top, int oldTop) {
		int animTop1 = mAnimView2.getTop() - top;
		int animTop = mAnimView.getTop() - top;

		if (top > oldTop) {
			if (animTop < mStartAnimateTop && !hasStart) {
				Animation anim2 = AnimationUtils.loadAnimation(this,
						R.anim.feature_anim2scale_in);
				anim2.setAnimationListener(new FeatureAnimationListener(
						mAnimView2, true));
				mAnimView2.startAnimation(anim2);
				Animation anim1 = AnimationUtils.loadAnimation(this,
						R.anim.feature_anim2scale_in);
				anim1.setAnimationListener(new FeatureAnimationListener(
						mAnimView, true));
				mAnimView.startAnimation(anim1);

				hasStart = true;
			}

		} else {
			if (animTop1 < mStartAnimateTop && hasStart) {
				Animation anim2 = AnimationUtils.loadAnimation(this,
						R.anim.feature_alpha_out);
				anim2.setAnimationListener(new FeatureAnimationListener(
						mAnimView2, false));
				mAnimView.startAnimation(anim2);
				hasStart = false;
			}
		}
	}

	private ImageView ivGuidePicture;

	private Drawable[] pictures;

	private Animation[] animations;

	private int currentItem = 0;

	private void initView() {
		ivGuidePicture = (ImageView) findViewById(R.id.iv_guide_picture);

		pictures = new Drawable[] {
				getResources().getDrawable(R.drawable.tutorial3_bottom_text),
				getResources().getDrawable(R.drawable.tutorial2_icon2) };
		animations = new Animation[] {
				AnimationUtils.loadAnimation(this, R.anim.guide_fade_in),
				AnimationUtils.loadAnimation(this, R.anim.guide_fade_in_scale),
				AnimationUtils.loadAnimation(this, R.anim.guide_fade_out) };
	}

	private void initData() {

		animations[0].setDuration(3000);
		animations[1].setDuration(2500);

		animations[0].setAnimationListener(new GuideAnimationListener(0));

		ivGuidePicture.setImageDrawable(pictures[currentItem]);
		ivGuidePicture.startAnimation(animations[0]);
	}

	class GuideAnimationListener implements AnimationListener {
		private int index;

		public GuideAnimationListener(int index) {
			this.index = index;
		}

		@Override
		public void onAnimationStart(Animation animation) {
		}

		@Override
		public void onAnimationEnd(Animation animation) {
			if (index < (animations.length - 1)) {
				ivGuidePicture.startAnimation(animations[index + 1]);
			} else {
				currentItem++;
				if (currentItem > (pictures.length - 1)) {
					currentItem = 0;
				}
				ivGuidePicture.startAnimation(animations[0]);
			}
		}

		@Override
		public void onAnimationRepeat(Animation animation) {

		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
