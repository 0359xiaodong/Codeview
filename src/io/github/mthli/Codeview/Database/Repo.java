package io.github.mthli.Codeview.Database;

public class Repo {
    public static final String TABLE = "repos";
    public static final String ID = "ID";
    public static final String TITLE = "TITLE";
    public static final String CONTENT = "CONTENT";
    public static final String DATE = "DATE";
    public static final String STATE = "STATE";

    public enum State {
        Mark,
        Unmark
    }

    public static final String CREATE_SQL = "CREATE TABLE "
            + TABLE
            + " ("
            + " ID integer primary key autoincrement,"
            + " TITLE text,"
            + " CONTENT text,"
            + " DATE text,"
            + " STATE integer"
            + ")";

    private int id;
    private String title;
    private String content;
    private String date;
    private State state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }
}
