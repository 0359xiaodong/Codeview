package io.github.mthli.Codeview;

public class SwipeListItem {
    String item_title;
    String item_content;

    public SwipeListItem(String item_title, String item_content) {
        super();

        this.item_title = item_title;
        this.item_content = item_content;
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
