package io.github.mthli.Codeview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

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
        ImageButton button_1;
        ImageButton button_2;
        ImageButton button_3;
        ImageButton button_4;
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
            holder.button_1 = (ImageButton) view.findViewById(R.id.list_view_main_back_button_delete);
            holder.button_2 = (ImageButton) view.findViewById(R.id.list_view_main_back_button_refresh);
            holder.button_3 = (ImageButton) view.findViewById(R.id.list_view_main_back_button_mark);
            holder.button_4 = (ImageButton) view.findViewById(R.id.list_view_main_back_button_log);

            view.setTag(holder);
        } else {
            holder = (NewHolder) view.getTag();
        }

        ((SwipeListView)parent).recycle(view, position); // Maybe useless.

        final SwipeListItem an_item =  swipe_list_item.get(position);
        holder.item_image.setImageDrawable(an_item.getItemImage());
        holder.item_title.setText(an_item.getItemTitle());
        holder.item_content.setText(an_item.getItemContent());

        /* Now back button setting */
        holder.button_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "Delete needs to be implemented.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.button_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Refresh needs to be implemented.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.button_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Mark needs to be implemented.", Toast.LENGTH_SHORT).show();
            }
        });

        holder.button_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toast.makeText(context, "Log needs to be implemented.", Toast.LENGTH_SHORT).show();
                // Intent intent_log = new Intent(context, LogActivity.class);
                // context.startActivity(intent_log);

                String get_title = an_item.getItemTitle();

                Intent intent_log = new Intent(context, LogActivity.class);
                intent_log.putExtra("item_title", get_title);
                context.startActivity(intent_log);
            }
        });

        return view;
    }
}