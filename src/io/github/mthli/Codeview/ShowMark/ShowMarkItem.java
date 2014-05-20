package io.github.mthli.Codeview.ShowMark;

public class ShowMarkItem implements Comparable<ShowMarkItem> {
    String title;
    String content;
    String path;

    public ShowMarkItem(
            String title,
            String content,
            String path
    ) {
        super();
        this.title = title;
        this.content = content;
        this.path = path;
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

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public int compareTo(ShowMarkItem showMarkItem) {
        if (this.path != null) {
            return this.path.toLowerCase().compareTo(showMarkItem.getPath().toLowerCase());
        } else {
            throw new IllegalArgumentException();
        }
    }
}
