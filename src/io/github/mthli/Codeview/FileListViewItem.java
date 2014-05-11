package io.github.mthli.Codeview;

public class FileListViewItem implements Comparable<FileListViewItem> {
    private String title;
    private String content;
    private String date;

    private String path;
    private String data;

    private boolean folder;
    private boolean parent;

    public FileListViewItem(
            String title,
            String content,
            String date,

            String path,
            String data,

            boolean folder,
            boolean parent
    ) {
        this.title = title;
        this.content = content;
        this.date = date;
        this.path = path;
        this.data = data;
        this.folder = folder;
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public String getPath() {
        return path;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }

    public String getData() {
        return data;
    }

    public boolean isFolder() {
        return folder;
    }

    public boolean isParent() {
        return parent;
    }

    @Override
    public int compareTo(FileListViewItem item) {
        if (this.title != null) {
            return this.title.toLowerCase().compareTo(item.getTitle().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
