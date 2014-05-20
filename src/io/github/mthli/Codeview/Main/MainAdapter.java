package io.github.mthli.Codeview.Main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import io.github.mthli.Codeview.R;

import java.util.List;

public class MainAdapter extends ArrayAdapter<MainItem> {
    private Context context;
    private int layoutResId;
    private List<MainItem> mainItems;

    public MainAdapter(
            Context context,
            int layoutResId,
            List<MainItem> mainItems
    ) {
        super(context, layoutResId, mainItems);
        this.context = context;
        this.layoutResId = layoutResId;
        this.mainItems = mainItems;
    }

    private class Holder {
        ImageView icon;
        TextView title;
        TextView content;
    }

    @Override
    public View getView(
            int position,
            View convertView,
            ViewGroup viewGroup
    ) {
        Holder holder = null;
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layoutResId, viewGroup, false);

            holder = new Holder();
            holder.icon = (ImageView) view.findViewById(R.id.main_item_icon);
            holder.title = (TextView) view.findViewById(R.id.main_item_title);
            holder.content = (TextView) view.findViewById(R.id.main_item_content);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        MainItem anItem = mainItems.get(position);
        holder.icon.setImageDrawable(anItem.getIcon());
        holder.title.setText(anItem.getTitle());
        holder.content.setText(anItem.getContent());

        return view;
    }
}
