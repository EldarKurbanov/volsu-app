package ru.iss.vanil.volsu;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;
import android.widget.LinearLayout;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class AppTools {

    static LinearLayout.LayoutParams getLinearLayoutParams(@NotNull Context context, int dpLeft, int dpTop, int dpRight, int dpBottom) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int leftInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpLeft, displayMetrics);
        int topInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpTop, displayMetrics);
        int rightInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpRight, displayMetrics);
        int bottomInPixels = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpBottom, displayMetrics);
        params.setMargins(leftInPixels, topInPixels, rightInPixels, bottomInPixels);
        return params;
    }

    //It uses in spinners as first element
    static String[] getStringArrayWithFirstUnActive(@NotNull Context context, @NotNull String[] array) {
        String[] arrayWithFirstEmpty = new String[array.length + 1];
        arrayWithFirstEmpty[0] = context.getResources().getString(R.string.choose_string);
        System.arraycopy(array, 0, arrayWithFirstEmpty, 1, array.length);
        return arrayWithFirstEmpty;
    }

    static List<String> getListStringArrayWithFirstUnActive(@NotNull Context context, @NotNull List<String> array) {
        array.add(0, context.getResources().getString(R.string.choose_string));
        return array;
    }

    static void clearListWithHintInSpinner(@NotNull Context context, @NotNull List<String> list) {
        list.clear();
        list.add(context.getString(R.string.choose_string));
    }

    static ArrayMap<String, String> getSwappedKeyValueMap(@NotNull ArrayMap<String, String> map) {
        ArrayMap<String,String> rev = new ArrayMap<>();
        for(Map.Entry<String,String> entry : map.entrySet())
            rev.put(entry.getValue(), entry.getKey());
        return rev;
    }

}
