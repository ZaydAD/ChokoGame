open module Appname {
    requires javafx.controls;
    requires javafx.media;
    requires javafx.graphics;
    requires javafx.base;
    requires java.sql;

    exports JavaMVP.view.board;
    exports JavaMVP.model;
    exports enums;
    exports JavaMVP;
    exports JavaMVP.view.startscreen;
    exports JavaMVP.view.endscreen;
    exports JavaMVP.view.mainscreen;
}