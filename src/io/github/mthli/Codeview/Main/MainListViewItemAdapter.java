package io.github.mthli.Codeview.Main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import io.github.mthli.Codeview.R;

import java.util.List;

public class MainListViewItemAdapter extends ArrayAdapter<MainListViewItem> {
    private Context context;
    private int layout_res_id;
    private List<MainListViewItem> items;

    public MainListViewItemAdapter(
            Context context,
            int layout_res_id,
            List<MainListViewItem> items
    ) {
        super(context, layout_res_id, items);

        this.context = context;
        this.layout_res_id = layout_res_id;
        this.items = items;
    }

    private class Holder {
        ImageView item_image;
        TextView item_title;
        TextView item_content;
        TextView item_date;
        ImageButton item_mark;
    }

    @Override
    public View getView(
            int position,
            View content_view,
            ViewGroup parent
    ) {
        Holder holder = null;
        View view = content_view;
        holder = null; //

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layout_res_id, parent, false);

            holder = new Holder();

            holder.item_image =
                    (ImageView)
                            view.findViewById(R.id.list_view_item_main_image);
            holder.item_title =
                    (TextView)
                            view.findViewById(R.id.list_view_item_main_title);
            holder.item_content =
                    (TextView)
                            view.findViewById(R.id.list_view_item_main_content);
            holder.item_date =
                    (TextView)
                            view.findViewById(R.id.list_view_item_main_date);
            holder.item_mark =
                    (ImageButton)
                            view.findViewById(R.id.list_view_item_main_mark);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        /* Now set list */
        MainListViewItem an_item = items.get(position);
        holder.item_image.setImageDrawable(an_item.getItemImage());
        holder.item_title.setText(an_item.getItemTitle());
        holder.item_content.setText(an_item.getItemContent());
        holder.item_date.setText(an_item.getItemDate());
        holder.item_mark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "GaGa.", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
