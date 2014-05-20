package io.github.mthli.Codeview.Database;

public class Mark {
    public static final String TABLE = "mark";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";

    public static final String CREATE_SQL = "CREATE TABLE "
            + TABLE
            + " ("
            + " TITLE text,"
            + " CONTENT text"
            + ")";

    private String title;
    private String content;

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
