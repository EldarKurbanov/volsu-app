package ru.iss.vanil.volsu;

final class AppR {
    static class id {
        static final char spinner_university = 1;
        static final char spinner_course = 2;
        static final char spinner_name_group = 3;
        static final char spinner_record_book = 4;
        static final char spinner_semester = 5;
        static final char button_save = 6;
    }

    static class constants {
        static final String ROOT_URL = "http://217.149.179.107/rating/api"; //also in res/xml/network_security_config.xml
        static final String[] INSTITUTES_CONSTANTS = {
                "ИИМОСТ", "ИУРЭ", "ИМИТ", "ИФиМКК", "ИП", "ИМЭИФ", "ИЕН", "ИПТ"
        };
        static final String ALL_STUDENTS_IN_GROUP = "Всей группы";
        static final String ERROR_SERVER_BUSY = "Невозможно подключиться к MySQL: Too many connections";
        static final String ERROR_SERVER_BAD_REQUEST = "Bad request";
    }

    static class constantsSharedPreferences {
        static final String STUDENT_DATA = "StudentData";
        static final String STUDENT_DATA_INSTITUTE_NAME = "InstituteName";
        static final String STUDENT_DATA_COURSE_NAME = "CourseName";
        static final String STUDENT_DATA_GROUP_NAME = "GroupName";
        static final String STUDENT_DATA_RECORD_BOOK = "RecordBook";
        static final String STUDENT_DATA_CURRENT_SEMESTER = "CurrentSemester";
        static final String STUDENT_DATA_FIRST_SEMESTER = "FirstSemester";
        static final String STUDENT_DATA_LAST_SEMESTER = "LastSemester";
    }

    static class constantsDatabaseSQLite {
        static final String DATABASE_NAME = "CurrentSemesterData";
        static final String TABLE_NAME = "semester";
        static final String COLUMN_FIRST = "id";
        static final String COLUMN_SECOND = "subject";
        static final String COLUMN_THIRD = "point";
    }

    static class constantsNews{
        static final String LOAD_HTML = "<html><body><div style=\"text-align: center; margin-top: 10em;\"><p>Загрузка...</p></div></body></html>";
        static final String ERROR_HTML = "<html><body><div style=\"text-align: center; margin-top: 10em; color: red;\"><p>Не удаётся загрузить новости</p></div></body></html>";
        static final String HTML_MIME_TYPE = "text/html; charset=UTF-8";
        static final String ENCODING = "UTF-8";

        static class tags {
            static final String FIRST_TAG = "rss";
            static final String SECOND_TAG = "channel";
            static final String ITEM_TAG = "item";
            static final String TITLE_TAG = "title";
            static final String LING_TAG = "link";
            static final String DESCRIPTION_TAG = "description";
            static final String PUB_DATE_TAG = "pubDate";
        }

        static class webViewStates {
            static final char loading_screen = 0;
            static final char news_list = 1;
            static final char news_show = 2;
            static final char internet_page = 3;
        }

        static class HTMLCodes {
            static final String LOADING = "<div class=\"loading-anim\"><div class=\"center\">\n" +
                    "    <div class=\"preloader-wrapper big active center\">\n" +
                    "      <div class=\"spinner-layer spinner-blue\">\n" +
                    "        <div class=\"circle-clipper left\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"gap-patch\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"circle-clipper right\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "\n" +
                    "      <div class=\"spinner-layer spinner-red\">\n" +
                    "        <div class=\"circle-clipper left\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"gap-patch\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"circle-clipper right\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "\n" +
                    "      <div class=\"spinner-layer spinner-yellow\">\n" +
                    "        <div class=\"circle-clipper left\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"gap-patch\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"circle-clipper right\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "\n" +
                    "      <div class=\"spinner-layer spinner-green\">\n" +
                    "        <div class=\"circle-clipper left\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"gap-patch\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div><div class=\"circle-clipper right\">\n" +
                    "          <div class=\"circle\"></div>\n" +
                    "        </div>\n" +
                    "      </div>\n" +
                    "    </div>\n" +
                    "  </div></div>";
            static final String COLLAPCE_JS = "<script>" + //"document.addEventListener('DOMContentLoaded', function() {" +
                    "    var elems = document.querySelectorAll('.collapsible');" +
                    "    var instances = M.Collapsible.init(elems, null);" +
                    "  </script>";
            static final String COLLAPCE_JS_OLD = "<script>" +
                    //"document.addEventListener('DOMContentLoaded', function() {" +
                    "var coll = document.getElementsByClassName(\"collapsible-header\");" +
                    "var i;" +
                    "" +
                    "for (i = 0; i < coll.length; i++) {" +
                    "  coll[i].addEventListener(\"click\", function() {" +
                    "    this.classList.toggle(\"active\");" +
                    "    var content = this.nextElementSibling;" +
                    "    if (content.style.display === \"block\") {" +
                    "      content.style.display = \"none\";" +
                    "    } else {" +
                    "      content.style.display = \"block\";" +
                    "    }" +
                    "  });" +
                    "}" +
                    "</script>";
        }
    }
}
