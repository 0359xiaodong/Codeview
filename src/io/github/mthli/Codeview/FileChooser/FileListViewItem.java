package io.github.mthli.Codeview.FileChooser;

public class FileListViewItem implements Comparable<FileListViewItem> {
    private String title;
    private String path;
    private String date;
    private String data;
    private boolean folder;
    private boolean parent;

    public FileListViewItem(
            String title,
            String path,
            String date,
            String data,
            boolean folder,
            boolean parent
    ) {
        this.title = title;
        this.path = path;
        this.date = date;
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
