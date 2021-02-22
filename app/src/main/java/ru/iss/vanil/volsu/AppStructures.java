package ru.iss.vanil.volsu;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

class AppStructures {
    static class Margins {
        private int dpLeft, dpTop, dpRight, dpBottom;

        public Margins(int dpLeft, int dpTop, int dpRight, int dpBottom) {
            this.dpLeft = dpLeft;
            this.dpTop = dpTop;
            this.dpRight = dpRight;
            this.dpBottom = dpBottom;
        }

        int getDpLeft() {
            return dpLeft;
        }

        int getDpTop() {
            return dpTop;
        }

        int getDpRight() {
            return dpRight;
        }

        int getDpBottom() {
            return dpBottom;
        }
    }

    static class Subject {
        private String subjectName;
        private int point;

        public Subject(String subjectName, int point) {
            this.subjectName = subjectName;
            this.point = point;
        }

        public String getSubjectName() {
            return subjectName;
        }

        public void setSubjectName(String subjectName) {
            this.subjectName = subjectName;
        }

        public int getPoint() {
            return point;
        }

        public void setPoint(int point) {
            this.point = point;
        }
    }

    static class DataClass {
        String data;
        String zach;
        String change_time;
        String status;

        //Cause server returns json in json
        Data dataParsed;

        static class Data {
            String group;
            String[] predmet;
            int[] ball;
            String[] control;
            int position;
            int sum_bal;
        }
    }

    //XML from https://volsu.ru/?rss=y
    static class VolSUFeed {
        @Root
        static class RSS {
            @Attribute String version;
            @Element Channel channel;
            static class Channel {
                @Element String title;
                @Element String link;
                @Element(required = false) String description;
                @Element String lastBuildDate;
                @Element String ttl;
                @ElementList(inline = true) List<Item> items;
                static class Item {
                    @Element String title;
                    @Element String link;
                    @Element String description;
                    @Element String pubDate;
                }
            }
        }
    }

    static class NewsFromVolSU {
        ArrayList<String> newsURLs;
        String newsHTMLCode;

        NewsFromVolSU(ArrayList<String> imageURLs, String newsHTMLCode) {
            this.newsURLs = imageURLs;
            this.newsHTMLCode = newsHTMLCode;
        }
    }
}
