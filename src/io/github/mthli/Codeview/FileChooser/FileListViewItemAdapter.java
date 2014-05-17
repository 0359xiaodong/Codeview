package io.github.mthli.Codeview.FileChooser;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import io.github.mthli.Codeview.R;

import java.io.File;
import java.util.List;

public class FileListViewItemAdapter extends ArrayAdapter<FileListViewItem> {
    private Context context;
    private int layout_res_id;
    private List<FileListViewItem> items;

    public FileListViewItemAdapter(
            Context context,
            int layout_res_id,
            List<FileListViewItem> items
    ) {
        super(context, layout_res_id, items);

        this.context = context;
        this.layout_res_id = layout_res_id;
        this.items = items;
    }

    public FileListViewItem getItem(int i) {
        return items.get(i);
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
            View convert_view,
            ViewGroup parent
    ) {
        Holder holder = null;
        View view = convert_view;
        holder = null; //

        if (view == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            view = inflater.inflate(layout_res_id, parent, false);

            holder = new Holder();

            holder.item_image = (ImageView) view.findViewById(R.id.list_view_item_fc_image);
            holder.item_title = (TextView) view.findViewById(R.id.list_view_item_fc_title);
            holder.item_content = (TextView) view.findViewById(R.id.list_view_item_fc_content);
            holder.item_date = (TextView) view.findViewById(R.id.list_view_item_fc_date);
            holder.item_mark = (ImageButton) view.findViewById(R.id.list_view_item_fc_mark);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        /* Now set list */
        FileListViewItem an_item = items.get(position);
        if (an_item != null) {
            /* first we set image */
            if (an_item.getData().equalsIgnoreCase(FileConstants.FOLDER)) {
                holder.item_image.setImageResource(R.drawable.ic_filetype_folder);
            } else if (an_item.getData().equalsIgnoreCase(FileConstants.PARENT)) {
                holder.item_image.setImageResource(R.drawable.ic_action_back);
                holder.item_image.setVisibility(View.INVISIBLE);
                holder.item_date.setVisibility(View.INVISIBLE);
                holder.item_mark.setVisibility(View.INVISIBLE);
            } else {
                String name = an_item.getTitle().toLowerCase();
                if (name.endsWith(FileConstants.C)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_c);
                } else if (name.endsWith(FileConstants.CPP)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_cpp);
                } else if (name.endsWith(FileConstants.CS)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_cs);
                } else if (name.endsWith(FileConstants.CSS)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_css);
                } else if (name.endsWith(FileConstants.JAVA)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_java);
                } else if (name.endsWith(FileConstants.JS)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_js);
                } else if (name.endsWith(FileConstants.PHP)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_php);
                } else if (name.endsWith(FileConstants.PY)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_py);
                } else if (name.endsWith(FileConstants.RB)) {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_rb);
                } else {
                    holder.item_image.setImageResource(R.drawable.ic_filetype_unknown);
                }
            }
            holder.item_title.setText(an_item.getTitle());
            holder.item_content.setText(an_item.getPath());
            holder.item_date.setText(an_item.getDate());
            holder.item_mark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, "GaGa.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        return view;
    }
}