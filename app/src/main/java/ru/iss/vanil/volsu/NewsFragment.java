package ru.iss.vanil.volsu;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewsFragment extends Fragment {

    WebView webView;
    AppStructures.NewsFromVolSU news;
    String currentURL;
    char currentStateWebView;
    volatile boolean picturesLoading = false;
    Activity activity;

    public NewsFragment() {
        //required empty constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        activity = getActivity();
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        webView = view.findViewById(R.id.newsWebView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new AppNetTools.AppWebViewInterface(), "AndroidErrorReporter");
        //webView.loadUrl("https://volsu.ru");
        webView.loadData(AppR.constantsNews.LOAD_HTML, AppR.constantsNews.HTML_MIME_TYPE, AppR.constantsNews.ENCODING);
        currentStateWebView = AppR.constantsNews.webViewStates.loading_screen;
        webView.setWebViewClient(new AppViews.AppWebViewClient());
        startLoadNews();
        return view;
    }

    public void onSuperClick(View v) {
        switch (v.getId()) {
            case R.id.refreshButton:
                switch (currentStateWebView){
                    case AppR.constantsNews.webViewStates.loading_screen:
                        break;
                    case AppR.constantsNews.webViewStates.news_list:
                        webView.goBack();
                        startLoadNews();
                        break;
                    case AppR.constantsNews.webViewStates.news_show:
                        break;
                    case AppR.constantsNews.webViewStates.internet_page:
                        break;
                    default:
                        Log.wtf("VolSU App Log", "What a terrible failure????!!!!");
                }
                break;
            default:
                Log.wtf(getString(R.string.error_string), "How you did it????");
        }
    }

    //calls from MainActivity
    public boolean onBackPressed() {
        if (webView.canGoBackOrForward(-2)) {
            webView.goBack();
            return true;
        } else {
            return false;//else it loading screen
        }
    }

    private void startLoadNews() {
        currentStateWebView = AppR.constantsNews.webViewStates.loading_screen;
        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (picturesLoading) {
                        Thread.sleep(50);
                    }
                    news = AppNetTools.getNews();
                    showPage(news.newsHTMLCode);
                } catch (Exception e) {
                    showError(e);
                }
            }
        });
        thread.start();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    thread.join();
                    while (picturesLoading) {
                        Thread.sleep(50);
                    }
                    picturesLoading = true;
                    final ArrayList<String> imageURLs = new ArrayList<>();
                    for (int i = 0; i < news.newsURLs.size(); i++) {
                        if (getActivity() == null) return;
                        final int j = i;
                        currentURL = news.newsURLs.get(i);
                        imageURLs.add(AppNetTools.getUrlOfImageOfNews(news.newsURLs.get(i)));
                        if (getActivity() == null) return;
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Unfortunately, Chrome forgiving this var and we must define it again and again
                                AppNetTools.AppWebViewInterface.callJavaScript(webView,
                                        "var newsImages = document.getElementsByClassName",
                                        "imageNews");
                                AppNetTools.AppWebViewInterface.callJavaScript(webView,
                                        "var loadingAnimations = document.getElementsByClassName",
                                        "loading-anim");
                                AppNetTools.AppWebViewInterface.callJavaScriptCode(webView,
                                        "newsImages[" + j + "].setAttribute(\"src\", \""
                                                + imageURLs.get(j) + "\");");
                                AppNetTools.AppWebViewInterface.callJavaScriptCode(webView,
                                        "loadingAnimations[" + j + "].style.display = \"none\";");
                            }
                        });
                        //if we don't sleep, volsu site will block us
                        Thread.sleep(50);
                        if (getActivity() == null) return;
                    }
                    currentStateWebView = AppR.constantsNews.webViewStates.news_list;
                } catch (Exception e) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(e.getMessage()).append("<br>").append(currentURL).append("<br>");
                    for (StackTraceElement stackTraceElement : e.getStackTrace()) {
                        stringBuilder.append(stackTraceElement.toString()).append("<br>");
                    }
                    showMessageError(stringBuilder.toString());
                } finally {
                    picturesLoading = false;
                }
            }
        });
        thread1.start();
    }

    private void showPage(final String pageCode) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.loadData(pageCode, AppR.constantsNews.HTML_MIME_TYPE,
                        AppR.constantsNews.ENCODING);
            }
        });
    }

    private void showError(final Exception e) {
        picturesLoading = false;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.goBack();
                webView.loadData(AppR.constantsNews.ERROR_HTML, AppR.constantsNews.HTML_MIME_TYPE, AppR.constantsNews.ENCODING);
                Log.e(getString(R.string.log_tag), e.getMessage());
            }
        });
    }

    private void showMessageError(final String message) {
        picturesLoading = false;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.goBack();
                webView.loadData(message, AppR.constantsNews.HTML_MIME_TYPE, AppR.constantsNews.ENCODING);
                Log.e("VolSU App Log", message);
            }
        });
    }

}
