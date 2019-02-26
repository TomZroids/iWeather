package com.feige.wireframe;


import com.feige.sweather.R;

import android.R.integer;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnPreDrawListener;

/**
 * 圆弧计分
 * @author Administrator
 *
 */
public class HomeArc extends View { 

	private Paint paint_black, paint_white;
	private RectF rectf;
	private float tb;
	private int blackColor = 0x70000000; // 底黑色
	private int Color = 0xff00ffff; // 蓝
	private int score;
	private float arc_y = 0f;
	private int score_text;
    String  my_number  = com.feige.iweather.Weather.My_temperature ;
    int number  = Integer.parseInt(my_number);
    
	public HomeArc(Context context, int score) {
		super(context);
		score = number ;
		
		init(score);
	}

	public void init(int score) {
	
		
		System.out.println(score+"sbsbsbsbsbbsbsbsbsbbss");
		this.score = score;
		Resources res = getResources();
		tb = res.getDimension(R.dimen.historyscore_tb);

		paint_black = new Paint();
		paint_black.setAntiAlias(true);
		paint_black.setColor(blackColor);
		paint_black.setStrokeWidth(tb * 0.2f);
		paint_black.setStyle(Style.STROKE);

		paint_white = new Paint();
		paint_white.setAntiAlias(true);
		paint_white.setColor(Color);
		paint_white.setTextSize(tb*8.0f);// 文字大小
		paint_white.setStrokeWidth(tb * 0.2f);// 文字 宽度
		paint_white.setTextAlign(Align.CENTER);
		paint_white.setStyle(Style.STROKE);

		rectf = new RectF();
		rectf.set(tb * 0.5f, tb * 0.5f, tb * 18.5f, tb * 18.5f);

		setLayoutParams(new LayoutParams((int) (tb * 19.5f), (int) (tb * 19.5f))); // 布局

		this.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {
					public boolean onPreDraw() {
						new thread();
						getViewTreeObserver().removeOnPreDrawListener(this);
						return false;
					}
				});
	}

	protected void onDraw(Canvas c) {
		super.onDraw(c);
		c.drawArc(rectf, -90, 360, false, paint_black);
		c.drawArc(rectf, -90, arc_y, false, paint_white);
		c.drawText("" + score_text, tb * 9.7f, tb * 11.0f, paint_white);
	}

	class thread implements Runnable {
		private Thread thread;
		private int statek;
		int count;

		public thread() {
			thread = new Thread(this);
			thread.start();
		}

		public void run() {
			while (true) {
				switch (statek) {
				case 0:
					try {
						Thread.sleep(200);
						statek = 1;
					} catch (InterruptedException e) {
					}
					break;
				case 1:
					try {
						Thread.sleep(50);
						arc_y += 8.0f;
						score_text++;
						count++;
						postInvalidate();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					break;
				}
				if (count >= score)
					break;
			}
		}
	}

}
