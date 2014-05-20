package io.github.mthli.Codeview.FileChooser;

public class FileItem implements Comparable<FileItem> {
    private String title;
    private String content;
    private String path;
    private String data;
    private boolean folder;
    private boolean parent;

    public FileItem(
            String title,
            String content,
            String path,
            String data,
            boolean folder,
            boolean parent
    ) {
        this.title = title;
        this.content = content;
        this.path = path;
        this.data = data;
        this.folder = folder;
        this.parent = parent;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getPath() {
        return path;
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
    public int compareTo(FileItem fileItem) {
        if (this.content != null) {
            return this.content.toLowerCase().compareTo(fileItem.getContent().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
