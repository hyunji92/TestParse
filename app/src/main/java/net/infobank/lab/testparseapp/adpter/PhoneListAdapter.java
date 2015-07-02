package net.infobank.lab.testparseapp.adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseObject;

import net.infobank.lab.testparseapp.IbPhonesMiscUtils;
import net.infobank.lab.testparseapp.R;

import java.util.ArrayList;


/**
 * Describe about this class here...
 *
 * @author ohjongin
 * @since 1.0
 * 15. 7. 1
 */
public class PhoneListAdapter extends ParseListAdapter {
    public PhoneListAdapter(Context context, int layout_res_id, ArrayList<ParseObject> file_list) {
        super(context, layout_res_id, file_list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutResId, null);
            viewHolder = createViewHolder(convertView, position);
            if (convertView != null) {
                convertView.setTag(viewHolder);
            } else {
                Log.e("","convertView is NULL while creating view holder!");
            }
        } else {
            if (convertView.getTag() == null) {
                viewHolder = createViewHolder(convertView, position);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
        }

        ParseObject po = getItem(position);
        if (po == null || viewHolder == null) {
            Log.e("","ParseObject is NULL!!");
            return convertView;
        }

        viewHolder.tv_subtitle.setText("");
        viewHolder.tv_timestamp.setVisibility(View.GONE);
        viewHolder.tv_changelog.setVisibility(View.GONE);

        viewHolder.tv_title.setText(po.getString("modelName"));

        final ImageView iv_icon = viewHolder.iv_icon;
        viewHolder.iv_icon.setParseFile(po.getParseFile("modelImage"));
        viewHolder.iv_icon.loadInBackground(new GetDataCallback() {
            @Override
            public void done(byte[] data, ParseException e) {
                if (e == null) {
                    // Decode the Byte[] into Bitmap
                    if (data != null) {
                        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        if (bitmap != null) {
                        }
                    } else {
                        iv_icon.setImageResource(R.drawable.ic_app_no_icon);
                    }
                }
            }
        });

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // Timestamp
        viewHolder.tv_timestamp.setVisibility(View.VISIBLE);
        viewHolder.tv_timestamp.setText(IbPhonesMiscUtils.getDateTimeString(getContext(), po.getUpdatedAt().getTime()));

        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        // size
        viewHolder.tv_subtitle.setText(po.getString("size"));

        return convertView;
    }
}
