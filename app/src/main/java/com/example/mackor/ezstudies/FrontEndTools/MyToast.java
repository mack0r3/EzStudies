package com.example.mackor.ezstudies.FrontEndTools;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.content.ContextCompat;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mackor.ezstudies.R;

/**
 * Created by Korzonkie on 2016-02-12.
 * Ogarnąć bo poki co chujowo działa troche, zostanę przy zwyklych toastach
 */
public class MyToast {
    private Context context;
    private Activity activity;

    public MyToast(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    public void makeToast(String message, boolean success)
    {
        int bottomMargin = 50; // in dp

        Resources r = activity.getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, bottomMargin, r.getDisplayMetrics());

        LayoutInflater layoutInflater = (LayoutInflater.from(context));
        View toastView = layoutInflater.inflate(R.layout.my_toast, (ViewGroup)activity.findViewById(R.id.myToast_linear_layout));
        TextView toastText = (TextView)toastView.findViewById(R.id.toastText);
        toastText.setText(message);
        if(success)toastText.setBackgroundResource(R.color.myToastBgColorSuccess);
        else toastText.setBackgroundResource(R.color.myToastBgColorError);
        toastText.setTextColor(ContextCompat.getColor(context, R.color.myToastTextColor));
        Toast toast = new Toast(context);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setGravity(Gravity.BOTTOM, 0, (int) px);
        toast.setView(toastView);
        showAToast(toast, message, context);
    }


    public void showAToast (Toast toast, String message, Context context){ //"Toast toast" is declared in the class
        try{ toast.getView().isShown();     // true if visible
            toast.setText(message);
        } catch (Exception e) {
          e.printStackTrace();
        }
        toast.show();  //finally display it
    }
}
