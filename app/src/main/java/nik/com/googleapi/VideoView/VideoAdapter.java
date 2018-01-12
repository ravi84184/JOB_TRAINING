package nik.com.googleapi.VideoView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import nik.com.googleapi.R;

/**
 * Created by nikpatel on 04/01/18.
 */

public class VideoAdapter extends ArrayAdapter<VideoModel>{


    Context context;
    LayoutInflater inflater;
    List<VideoModel> videomodelList;
    private SparseBooleanArray mSelectedItemsIds;

    public VideoAdapter(Context context, int resourceId,
                           List<VideoModel> videomodelList) {
        super(context, resourceId, videomodelList);
        mSelectedItemsIds = new SparseBooleanArray();
        this.context = context;
        this.videomodelList = videomodelList;
        inflater = LayoutInflater.from(context);
    }


    public View getView(int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.single_video_layout, null);
            // Locate the TextViews in listview_item.xml
            holder.displayName = (TextView) view.findViewById(R.id.video_single_name);
            holder.size = (TextView) view.findViewById(R.id.video_single_size);
            // Locate the ImageView in listview_item.xml
            holder.thumbImage = (ImageView) view.findViewById(R.id.video_single_image);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        // Capture position and set to the TextViews
        holder.displayName.setText(videomodelList.get(position).getName());
        holder.size.setText(videomodelList.get(position).getSize());
        holder.thumbImage.setImageBitmap(StringToBitMap(videomodelList.get(position).getImg()));
        // Capture position and set to the ImageView
//        holder.thumbImage.setImageResource(videomodelList.get(position)
//                ());
        return view;
    }

    @Override
    public void remove(VideoModel object) {
        videomodelList.remove(object);
        notifyDataSetChanged();
    }

    public List<VideoModel> getWorldPopulation() {
        return videomodelList;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }
    private class ViewHolder {
        TextView displayName;
        TextView size;
        ImageView thumbImage;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
