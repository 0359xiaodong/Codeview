package io.github.mthli.Codeview.Main;

import android.graphics.drawable.Drawable;

public class MainItem {
    Drawable icon;
    String title;
    String content;

    public MainItem(
            Drawable icon,
            String title,
            String content
    ) {
        super();
        this.icon = icon;
        this.title = title;
        this.content = content;
    }

    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
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
}
