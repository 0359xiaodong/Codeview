package io.github.mthli.Codeview;

public class Repo {
    private static final long serialVersionUID = 8753374579416116856L;
    public static final String TABLE = "repos";
    public static final String _ID = "_ID";
    public static final String FOLDER = "FOLDER";
    public static final String NAME = "NAME";
    public static final String ADDRESS = "ADDRESS";
    public static final String STATE = "STATE";
    public static final String ERROR = "ERROR";

    public enum State {
        New,
        Error,
        Local,
    }

    public static final String CREATE_SQL = "CREATE TABLE "
            + TABLE
            + " ("
            + "_ID integer primary key autoincrement,"
            + " FOLDER text,"
            + " NAME text,"
            + " ADDRESS text,"
            + " STATE state,"
            + " ERROR text null"
            + ")";

    private int id;
    private String folder;
    private String name;
    private String address;
    private State state;
    private String error;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
