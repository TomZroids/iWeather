package com.feige.iweather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

public class Image3dView extends View {

	private View sourceView;

	private Bitmap sourceBitmap;
	private float sourceWidth;

	private Matrix matrix = new Matrix();
	private Camera camera = new Camera();
	public Image3dView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setSourceView(View view) {
		sourceView = view;
		sourceWidth = sourceView.getWidth();
	}

	public void clearSourceBitmap() {
		if (sourceBitmap != null) {
			sourceBitmap = null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		if (sourceBitmap == null) {
			getSourceBitmap();
		}
		float degree = 90 - (90 / sourceWidth) * getWidth();
		camera.save();
		camera.rotateY(degree);
		camera.getMatrix(matrix);
		camera.restore();
		matrix.preTranslate(0, -getHeight() / 2);
		matrix.postTranslate(0, getHeight() / 2);
		canvas.drawBitmap(sourceBitmap, matrix, null);
	}

	private void getSourceBitmap() {
		if (sourceView != null) {
			sourceView.setDrawingCacheEnabled(true);
			sourceView.layout(0, 0, sourceView.getWidth(), sourceView.getHeight());
			sourceView.buildDrawingCache();
			sourceBitmap = sourceView.getDrawingCache();
		}
	}

}
