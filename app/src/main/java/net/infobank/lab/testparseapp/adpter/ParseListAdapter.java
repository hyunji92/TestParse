package net.infobank.lab.testparseapp.adpter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.parse.ParseImageView;
import com.parse.ParseObject;

import net.infobank.lab.testparseapp.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ohjongin on 13. 6. 16.
 */
public class ParseListAdapter extends ArrayAdapter<ParseObject> {
    protected LayoutInflater mInflater;
    protected int mLayoutResId = -1;
    protected static HashMap<String, Drawable> mDrawableMap = new HashMap<String, Drawable>();

    public ParseListAdapter(Context context, int layout_res_id, ArrayList<ParseObject> file_list) {
        super(context, layout_res_id, file_list);
        mInflater = LayoutInflater.from(context);
        mLayoutResId = layout_res_id;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    protected Context getBaseContext() {
        return getContext();
    }

    protected ViewHolder createViewHolder(View convertView, int position) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        viewHolder.tv_changelog = (TextView) convertView.findViewById(R.id.tv_changelog);
        viewHolder.tv_subtitle = (TextView) convertView.findViewById(R.id.tv_subtitle);
        viewHolder.tv_timestamp = (TextView) convertView.findViewById(R.id.tv_timestamp);
        viewHolder.iv_icon = (ParseImageView) convertView.findViewById(R.id.iv_icon);

        return viewHolder;
    }

    protected static final class ViewHolder {
        public TextView tv_title;
        public TextView tv_changelog;
        public TextView tv_subtitle;
        public TextView tv_timestamp;
        public ParseImageView iv_icon;
    }
}
