package io.github.mthli.Codeview;

import android.graphics.drawable.Drawable;

public class SwipeListItem {
    Drawable item_image;
    String item_title;
    String item_content;

    public SwipeListItem(
            Drawable item_image,
            String item_title,
            String item_content
    ) {
        super();

        this.item_image = item_image;
        this.item_title = item_title;
        this.item_content = item_content;
    }

    public Drawable getItemImage() {
        return item_image;
    }

    public void setItemImage() {
        this.item_image = item_image;
    }

    public String getItemTitle() {
        return item_title;
    }

    public void setItemTitle(String item_title) {
        this.item_title = item_title;
    }

    public String getItemContent() {
        return item_content;
    }

    public void setItemContent(String item_content) {
        this.item_content = item_content;
    }
}
