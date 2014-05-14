package io.github.mthli.Codeview.Activity;

import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class MainListViewItem {
    Drawable item_image;
    String item_title;
    String item_content;
    String item_date;
    ImageButton item_mark;

    public MainListViewItem(
            Drawable item_image,
            String item_title,
            String item_content,
            String item_date,
            ImageButton item_mark
    ) {
        super();

        this.item_image = item_image;
        this.item_title = item_title;
        this.item_content = item_content;
        this.item_date = item_date;
        this.item_mark = item_mark;
    }

    public Drawable getItemImage() {
        return item_image;
    }

    public void setItemImage(Drawable item_image) { //
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

    public String getItemDate() {
        return item_date;
    }

    public void setItemDate(String item_date) {
        this.item_content = item_date;
    }

    public ImageButton getItemMark() {
        return item_mark;
    }

    public void setItemMark(ImageButton item_mark) { //
        this.item_mark = item_mark;
    }
}
