package com.feige.iweather;

import com.gitonway.lee.niftymodaldialogeffects.lib.effects.BaseEffects;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FadeIn;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.Fall;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipH;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.FlipV;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.NewsPaper;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.RotateLeft;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.Shake;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SideFall;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.RotateBottom;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideBottom;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideLeft;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideRight;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.SlideTop;
import com.gitonway.lee.niftymodaldialogeffects.lib.effects.Slit;

public enum  Effectstype {

    Fadein(FadeIn.class),
    Slideleft(SlideLeft.class),
    Slidetop(SlideTop.class),
    Fall(Fall.class),
    Newspager(NewsPaper.class),
    Fliph(FlipH.class),
    Flipv(FlipV.class),
    RotateBottom(RotateBottom.class),
    RotateLeft(RotateLeft.class),
    Slit(Slit.class),
    Shake(Shake.class),
    Sidefill(SideFall.class);
    private Class<? extends BaseEffects> effectsClazz;

    private Effectstype(Class<? extends BaseEffects> mclass) {
        effectsClazz = mclass;
    }

    public BaseEffects getAnimator() {
        BaseEffects bEffects=null;
	try {
		bEffects = effectsClazz.newInstance();
	} catch (ClassCastException e) {
		throw new Error("Can not init animatorClazz instance");
	} catch (InstantiationException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	} catch (IllegalAccessException e) {
		// TODO Auto-generated catch block
		throw new Error("Can not init animatorClazz instance");
	}
	return bEffects;
    }
}
