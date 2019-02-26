package com.example.feige;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
public class Typefaces {
	private static final String TAG = "Typefaces";
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();

	public static Typeface get(Context c, String assetPath) {
		synchronized (cache) {
			if (!cache.containsKey(assetPath)) {
				try {
					Typeface t = Typeface.createFromAsset(c.getAssets(),
							assetPath);
					cache.put(assetPath, t);
				} catch (Exception e) {
					return null;
				}
			}

			return cache.get(assetPath);
		}
	}
}
