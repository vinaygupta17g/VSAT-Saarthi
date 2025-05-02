package com.vinay.vsatsaarthi.utils;

import android.content.Context;
import android.widget.Toast;

public class res {
    public static void res(Context context,String message)
    {
        Toast.makeText(context,message, Toast.LENGTH_LONG).show();
    }
}
