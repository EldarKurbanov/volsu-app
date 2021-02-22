package ru.iss.vanil.volsu;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.util.TypedValue;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Spinner;

import org.jetbrains.annotations.NotNull;

class AppViews {
    static class RegistrationTextView extends android.support.v7.widget.AppCompatTextView {
        public RegistrationTextView(Context context, @NotNull AppStructures.Margins margins) {
            super(context);
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            setTypeface(null, Typeface.BOLD);
            setTextColor(Color.DKGRAY);
            setLayoutParams(AppTools.getLinearLayoutParams(context, margins.getDpLeft(), margins.getDpTop(), margins.getDpRight(), margins.getDpBottom()));
        }
    }

    static class AppWebViewClient extends WebViewClient {
        //For new devices
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if(Uri.parse(request.getUrl().toString()).getHost() == null) {
                return true;
            }
            if (Uri.parse(request.getUrl().toString()).getHost().length() == 0)
                return false;

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(request.getUrl().toString()));
            view.getContext().startActivity(intent);
            return true;
        }

        //For old devices
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (Uri.parse(url).getHost() == null) {
                return true;
            }

            if(Uri.parse(url).getHost().length() == 0) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            view.getContext().startActivity(intent);
            return true;
        }
    }
}
