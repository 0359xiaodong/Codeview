package io.github.mthli.Codeview.FileChooser;

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

public class FileAdapter extends ArrayAdapter<FileItem> {
    private Context context;
    private int layoutResId;
    private List<FileItem> fileItems;

    public FileAdapter(
            Context context,
            int layoutResId,
            List<FileItem> fileItems
    ) {
        super(context, layoutResId, fileItems);
        this.context = context;
        this.layoutResId = layoutResId;
        this.fileItems = fileItems;
    }

    public FileItem getItem(int i) {
        return fileItems.get(i);
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
            holder.icon = (ImageView) view.findViewById(R.id.fc_item_icon);
            holder.title = (TextView) view.findViewById(R.id.fc_item_title);
            holder.content = (TextView) view.findViewById(R.id.fc_item_content);

            view.setTag(holder);
        } else {
            holder = (Holder) view.getTag();
        }

        FileItem anItem = fileItems.get(position);
        if (anItem != null) {
            if (anItem.getData().equalsIgnoreCase(Constants.FOLDER)) {
                holder.icon.setImageResource(R.drawable.ic_filetype_folder);
            } else {
                /* 忽略大小写，便于判断 */
                String name = anItem.getTitle().toLowerCase();
                if (name.endsWith(Constants.C)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_c);
                } else if (name.endsWith(Constants.CPP)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_cpp);
                } else if (name.endsWith(Constants.CS)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_cs);
                } else if (name.endsWith(Constants.CSS)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_css);
                } else if (name.endsWith(Constants.H)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_h);
                } else if (name.endsWith(Constants.HS)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_haskell);
                } else if (name.endsWith(Constants.HTML)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_html);
                } else if (name.endsWith(Constants.JAVA)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_java);
                } else if (name.endsWith(Constants.JS)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_js);
                } else if (name.endsWith(Constants.PDF)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_pdf);
                } else if (name.endsWith(Constants.PHP)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_php);
                } else if (name.endsWith(Constants.PM) || name.endsWith(Constants.PL) || name.endsWith(Constants.PERL)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_perl);
                } else if (name.endsWith(Constants.PY)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_py);
                } else if (name.endsWith(Constants.RB)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_rb);
                } else if (name.endsWith(Constants.RTF)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_rtf);
                } else if (name.endsWith(Constants.SH)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_sh);
                } else if (name.endsWith(Constants.SQL)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_sql);
                } else if (name.endsWith(Constants.TEX)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_tex);
                } else if (name.endsWith(Constants.XML)) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_xml);
                } else if (name.endsWith(Constants.SZ) ||
                        name.endsWith(Constants.ZIP) ||
                        name.endsWith(Constants.TAR) ||
                        name.endsWith(Constants.TARGZ) ||
                        name.endsWith(Constants.TARBZ) ||
                        name.endsWith(Constants.TARBZ2) ||
                        name.endsWith(Constants.RAR)
                        ) {
                    holder.icon.setImageResource(R.drawable.ic_filetype_zip);
                } else {
                    /* 所有的未知类型都显示为一个内容为空白的图标 */
                    holder.icon.setImageResource(R.drawable.ic_filetype_plain);
                }
            }
            holder.title.setText(anItem.getTitle());
            holder.content.setText(anItem.getContent());
        }
        return view;
    }
}
