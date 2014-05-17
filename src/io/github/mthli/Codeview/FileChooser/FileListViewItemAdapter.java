package io.github.mthli.Codeview.FileChooser;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import io.github.mthli.Codeview.R;

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

        /* 现在我们开始设置ListView */
        FileListViewItem an_item = items.get(position);
        if (an_item != null) {
            /* 
             * 首先根据文件类型设置图标，
             * 在这里我们使用后缀名的方式进行判断，
             * 实际上这是一种相当简陋的判断方式，
             * 会给实际的操作带来很多的不方便，
             * 后期将会考虑根据文件头来判断文件类型
             */
            /* 
             * 另外在这里会有一个神奇的Bug，
             * 就是有时候应该显示图标和日期的时候不会显示，
             * 只显示正确的标题和内容，
             * 这个Bug亟待解决
             */
            /* 如果是文件夹，就设置文件夹图标 */
            if (an_item.getData().equalsIgnoreCase(FileConstants.FOLDER)) {
                holder.item_image.setImageResource(R.drawable.ic_filetype_folder);
            /* 
             * 如果是父路径，则隐藏图标和日期，
             * 在这里的行为倒是和Bug相类似，
             * 需要重点考察
             */
            } else if (an_item.getData().equalsIgnoreCase(FileConstants.PARENT)) {
                /* 将文件图标设置为不显示 */
                holder.item_image.setVisibility(View.INVISIBLE);
                /* 将日期设置为不显示 */
                holder.item_date.setVisibility(View.INVISIBLE);
                /* 将mark设置为不显示 */
                holder.item_mark.setVisibility(View.INVISIBLE);
            /* 如果是文件，则根据文件后缀名设置相应的图标 */
            } else {
                /* 忽略大小写，便于判断 */
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
                    /* 所有的未知类型都显示为一个内容为空白的图标 */
                    holder.item_image.setImageResource(R.drawable.ic_filetype_unknown);
                }
            }
            holder.item_title.setText(an_item.getTitle());
            holder.item_content.setText(an_item.getContent());
            holder.item_date.setText(an_item.getDate());
            /* Need to change */
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
