package com.example.mackor.ezstudies.FrontEndTools;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Bogus on 2016-02-07.
 */
public class FontManager {

    public static final String ROOT = "fonts/", FONTAWESOME = ROOT + "fontawesome-webfont.ttf";

    public static Typeface getTypeface(Context context, String font){
        return Typeface.createFromAsset(context.getAssets(), font);
    }

}
