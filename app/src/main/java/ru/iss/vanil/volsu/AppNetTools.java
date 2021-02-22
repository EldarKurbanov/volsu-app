package ru.iss.vanil.volsu;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class AppNetTools {
    private static Gson gson;
    static {
        gson = new Gson();
    }

    static ArrayMap<String, String> getCoursesByInstitute(int institutePosition) throws Exception {
        //Server accepts a short names of institutes
        String instituteName = AppR.constants.INSTITUTES_CONSTANTS[institutePosition];
        String coursesJson = readUrl(AppR.constants.ROOT_URL
                + "?method=getNamePlan" + "&institut="
                + URLEncoder.encode(instituteName, "UTF-8"));
        String[] coursesArray = gson.fromJson(coursesJson, String[].class);
        if (coursesArray == null) throw new Exception("Can't parse JSON with courses");

        //Server can't accepts courses with spaces and we are store 'keys'
        ArrayMap<String, String> coursesMap = new ArrayMap<>();
        for (String courseName: coursesArray) {
            String courseNameInServer = courseName.replace(" ", "pr");
            coursesMap.put(courseName, courseNameInServer);
        }
        return coursesMap;
    }

    static ArrayMap<String, String> getGroupNamesByCourse(String courseKey) throws Exception {
        String groupNamesJson = readUrl(AppR.constants.ROOT_URL + "?method=getNameGroups"
                + "&name_plan=" + URLEncoder.encode(courseKey, "UTF-8"));
        Type type = new TypeToken<ArrayMap<String, String>>(){}.getType();
        ArrayMap<String, String> groupNamesMap = gson.fromJson(groupNamesJson, type);
        if (groupNamesMap == null) throw new Exception("Can't parse JSON with group names");
        return AppTools.getSwappedKeyValueMap(groupNamesMap);
    }

    //In the next requests we don't need an keys
    static ArrayList<String> getRecordBooksByNameGroup(String nameGroupKey) throws Exception {
        String recordBooksJson = readUrl(AppR.constants.ROOT_URL + "?method=getZachIds"
                + "&name_plan=" + URLEncoder.encode(nameGroupKey, "UTF-8"));
        Type type = new TypeToken<ArrayMap<String, String>>(){}.getType();
        ArrayMap<String, String> recordBooksMap = gson.fromJson(recordBooksJson, type);
        if (recordBooksMap == null) throw new Exception("Can't parse JSON with record books");
        ArrayList<String> arrayList = new ArrayList<>(recordBooksMap.values());
        arrayList.remove(AppR.constants.ALL_STUDENTS_IN_GROUP);
        return arrayList;
    }

    @NonNull
    static ArrayList<String> getSemestersByRecordBook(String recordBook) throws Exception {
        String semestersJson = readUrl(AppR.constants.ROOT_URL + "?method=getSemestrs"
                + "&zach=" + recordBook);
        String[] semesters = gson.fromJson(semestersJson, String[].class);
        if (semesters == null) throw new Exception("Can't parse JSON with semesters");
        return new ArrayList<>(Arrays.asList(semesters));
    }

    static AppStructures.DataClass
    getDataOfStudent(String recordBook, int semester) throws Exception {
        String studentDataJson = readUrl(AppR.constants.ROOT_URL + "/get_data/"
                + "?method=getData" + "&zach=" + recordBook + "&semestr=" + semester);
        AppStructures.DataClass allData = gson.fromJson(studentDataJson, AppStructures.DataClass.class);
        if (allData == null) throw new Exception("Can't parse full data of student " + recordBook);
        if (allData.status.equals(AppR.constants.ERROR_SERVER_BAD_REQUEST))
            throw new Exception("Bad request: " + studentDataJson);
        AppStructures.DataClass.Data data = gson.fromJson(allData.data, AppStructures.DataClass.Data.class);
        if (data == null) throw new Exception("Can't parse data from all data of student");
        allData.dataParsed = data;
        return allData;
    }

    @NonNull
    static AppStructures.NewsFromVolSU getNews() throws Exception {
        ArrayList<String> newsURL = new ArrayList<>();
        String rawXML = readUrl("https://volsu.ru/?rss=y");
        Serializer serializer = new Persister();
        AppStructures.VolSUFeed.RSS rss =
                serializer.read(AppStructures.VolSUFeed.RSS.class, rawXML);
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><head><style>")
                .append("img.imageNews{width: 100%;}")
                .append("</style><link rel=\"stylesheet\" href=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css\">")
                .append("<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">")
                .append("<script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js\"></script>");
        htmlBuilder.append("</head><body><script src=\"https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js\"></script>");
        for (AppStructures.VolSUFeed.RSS.Channel.Item itemNews : rss.channel.items) {
            Document document = Jsoup.parse("<html><head></head><body>" + itemNews.description + "</body></html>", "https://volsu.ru");
            document.select("a.photo").attr("href", "");
            Elements images = document.select("img");
            String src = "";
            for (int i = 0; i < images.size(); i++) {
                src = images.get(i).attr("src");
                if (src.startsWith("/upload/medialibrary/")) {
                    src = "https://volsu.ru" + src;
                    images.get(i).attr("src", src);
                }
            }
            itemNews.description = document.outerHtml();
            htmlBuilder.append("<div style=\"font-weight: bold; width: 100%; font-size: 1.5em;\">")
                    //.append(itemNews.title).append("<br>")
                    .append(AppR.constantsNews.HTMLCodes.LOADING)
                    .append("<img class=\"imageNews\" src = \"//:0\"></img><br>")
                    .append("<ul class=\"collapsible\"><li><div class=\"collapsible-header\"><i class=\"material-icons\">expand_more</i>")
                    .append(itemNews.title)
                    .append("</div><div class=\"collapsible-body\" style=\"font-size: 0.75em; padding: 0 1em;\"><div><span class=\"center-align\">")
                    .append(itemNews.description)
                    .append("</div></span></div></li></ul>")
                    //.append("<br><a class=\"waves-effect waves-light btn\">Подробнее...</a></div><hr style=\"margin-bottom: 1em;\">");
                    .append("</div>"); //<hr style="margin-bottom: 1em;">
            newsURL.add(itemNews.link);
        }
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            htmlBuilder.append(AppR.constantsNews.HTMLCodes.COLLAPCE_JS);
        else htmlBuilder.append(AppR.constantsNews.HTMLCodes.COLLAPCE_JS_OLD);*/
        htmlBuilder.append(AppR.constantsNews.HTMLCodes.COLLAPCE_JS_OLD);
        htmlBuilder.append("</body></html>");
        return new AppStructures.NewsFromVolSU(newsURL, htmlBuilder.toString());
    }

    @NonNull
    static String getUrlOfImageOfNews(String urlNews) throws Exception {
        Document document = Jsoup.connect(urlNews).get();
        for (Element element : document.select("a")) {
            String rel = element.attr("rel");
            String href = element.attr("href");
            if (rel.equals("photo")) {
                return "https://volsu.ru" + href;
            }
        }
        return "https://volsu.ru/upload/iblock/16c/logo_volsu_260x260.jpg";
    }

    /*private static String getUrlOfImageOfNews(String urlNews) throws Exception {
        String rawHTML = readUrl(urlNews);
        Pattern pattern = Pattern.compile(".*<a rel=\"photo\" href=\"(.*?)\">.*");
        Matcher matcher = pattern.matcher(rawHTML);
        String urlOfImage = "https://volsu.ru";
        if (matcher.find()) {
            urlOfImage += matcher.group(1);
        } else {
            urlOfImage += "favicon.ico";
        }
        return urlOfImage;
    }*/

    @NonNull
    private static String readUrl(String urlString) throws Exception {
        URL url = new URL(urlString);
        URLConnection request = url.openConnection();
        request.connect();
        InputStream inputStream = (InputStream) request.getContent();
        final int bufferSize = 1024;
        final char[] buffer = new char[bufferSize];
        final StringBuilder out = new StringBuilder();
        Reader in = new InputStreamReader(inputStream, "UTF-8");
        while (true){
            int rsz = in.read(buffer, 0, buffer.length);
            if (rsz < 0)
                break;
            out.append(buffer, 0, rsz);
        }
        String completeString = out.toString();
        if (completeString.contains(AppR.constants.ERROR_SERVER_BUSY))
            throw new Exception(completeString);
        if (completeString.equals("null")) {
            throw new Exception("Server returns error 'null' from url " + url);
        }
        if (completeString.isEmpty()) {
            throw new Exception("Server return nothing from url " + url);
        }
        return completeString;
    }

    static class AppWebViewInterface {

        @JavascriptInterface
        public void onError(String error){
            throw new Error(error);
        }

        static void callJavaScript(WebView webView, String methodName, @NotNull Object...params){
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("javascript:try{");
            stringBuilder.append(methodName);
            stringBuilder.append("(");
            for (int i = 0; i < params.length; i++) {
                Object param = params[i];
                if(param instanceof String){
                    stringBuilder.append("'");
                    stringBuilder.append(param);
                    stringBuilder.append("'");
                }
                if(i < params.length - 1){
                    stringBuilder.append(",");
                }
            }
            stringBuilder.append(");}catch(error){Android.onError(error.message);}");
            webView.loadUrl(stringBuilder.toString());
        }

        static void callJavaScriptCode(@NotNull WebView webView, String code) {
            webView.loadUrl(
                    "javascript:(function() {try{" + code + "}catch(error){Android.onError(error.message);}})()");
        }
    }
}
