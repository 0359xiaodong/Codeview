package io.github.mthli.Codeview;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class SwipeListItemAdapter extends ArrayAdapter<SwipeListItem> {
    List<SwipeListItem> swipe_list_item;
    Context context;
    int layout_res_id;

    public SwipeListItemAdapter(
            Context context,
            int layout_res_id,
            List<SwipeListItem> swipe_list_item
            ) {
        super(context, layout_res_id, swipe_list_item);

        this.swipe_list_item = swipe_list_item;
        this.context = context;
        this.layout_res_id = layout_res_id;
    }

    static class NewHolder {
        ImageView item_image;
        TextView item_title;
        TextView item_content;
        Button button_1;
        Button button_2;
        Button button_3;
        Button button_4;
    }

    @Override
    public View getView(
            int position,
            View convert_view,
            ViewGroup parent
    ) {
        NewHolder holder = null;
        View view = convert_view;
        holder = null;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layout_res_id, parent, false);

            holder = new NewHolder();

            holder.item_image = (ImageView) view.findViewById(R.id.list_view_main_front_image);
            holder.item_title = (TextView) view.findViewById(R.id.list_view_main_front_text_title);
            holder.item_content = (TextView) view.findViewById(R.id.list_view_main_front_text_content);
            holder.button_1 = (Button) view.findViewById(R.id.list_view_main_back_button_delete);
            holder.button_2 = (Button) view.findViewById(R.id.list_view_main_back_button_refresh);
            holder.button_3 = (Button) view.findViewById(R.id.list_view_main_back_button_mark);
            holder.button_4 = (Button) view.findViewById(R.id.list_view_main_back_button_log);

            view.setTag(holder);
        } else {
            holder = (NewHolder) view.getTag();
        }

        SwipeListItem an_item =  swipe_list_item.get(position);
        holder.item_image.setImageDrawable(an_item.getItemImage());
        holder.item_title.setText(an_item.getItemTitle());
        holder.item_content.setText(an_item.getItemContent());

        return view;
    }
}
