package nik.com.googleapi.VideoView;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import nik.com.googleapi.R;

public class VideoActivity extends AppCompatActivity {

    private static final String TAG = "VideoActivity";
    private Cursor videocursor;
    private int video_column_index;
    ListView videolist;
    int count;
    List<VideoModel> listItems;
    private VideoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        init();

    }

    private void init() {


        videolist = (ListView) findViewById(R.id.videoList);

        adapter = new VideoAdapter(this,R.layout.single_video_layout,getItem());

        videolist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        videolist.setAdapter(adapter);


        videolist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                video_column_index = videocursor
                        .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
                videocursor.moveToPosition(position);
                String filename = videocursor.getString(video_column_index);
                Toast.makeText(VideoActivity.this, filename, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(VideoActivity.this,
                        ViewVideoActivity.class);
                intent.putExtra("videofilename", filename);
                startActivity(intent);
            }
        });
        videolist.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                final int checkedCount = videolist.getCheckedItemCount();
                // Set the CAB title according to total checked items
                mode.setTitle(checkedCount + " Selected");
                // Calls toggleSelection method from ListViewAdapter Class
                adapter.toggleSelection(position);

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {

                mode.getMenuInflater().inflate(R.menu.list_menu,menu);

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
//                    case R.id.selectAll:
//                        final int checkedCount  = videolist.getCount();
//                        for (int i = 0; i <  checkedCount; i++) {
//                            videolist.setItemChecked(i,   true);
//                            adapter.toggleSelection(i);
//                        }
//                        return true;
                    case R.id.delete:
                        SparseBooleanArray selected = adapter
                                .getSelectedIds();
                        // Captures all selected ids with a loop
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                VideoModel selecteditem = adapter
                                        .getItem(selected.keyAt(i));
                                // Remove selected items following the ids
                                adapter.remove(selecteditem);
                            }
                        }
                        // Close CAB
                        mode.finish();
                        return true;
                    default:
                        return false;

                }
            }
            @Override
            public void onDestroyActionMode(ActionMode mode) {
                adapter.removeSelection();
            }
        });
    }
    private ArrayList getItem(){

        ArrayList<VideoModel> arrayList = new ArrayList<>();

        VideoModel addarray;

        String[] proj = { MediaStore.Video.Media._ID,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.SIZE,
        };


        videocursor = managedQuery(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                proj, null, null, null);
        count = videocursor.getCount();

        for (int i = 0; i<videocursor.getCount(); i++){

            int id1 = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media._ID);
            video_column_index = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            int name = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);

            int size = videocursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);

//            int imge = videocursor.getColumnIndexOrThrow(String.valueOf(MediaStore.Video.Thumbnails.MICRO_KIND));




            videocursor.moveToPosition(i);

            String videoID = videocursor.getString(id1);
            String data = videocursor.getString(video_column_index);
            String displayname = videocursor.getString(name);
            String videoSize = videocursor.getString(size);

            @SuppressWarnings("deprecation")
            Cursor cursor = managedQuery(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
                    MediaStore.Video.Media.DISPLAY_NAME + "=?",
                    new String[] { displayname }, null);
            cursor.moveToFirst();

            long ids = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media._ID));

            ContentResolver crThumb = getContentResolver();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
                    crThumb, ids, MediaStore.Video.Thumbnails.MICRO_KIND,
                    options);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            curThumb.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e(TAG, "init: "+videoID );
            Log.e(TAG, "init: "+data );
            Log.e(TAG, "init: "+displayname );
            Log.e(TAG, "init: "+videoSize );
            Log.e(TAG, "init: "+encodedImage );


            addarray= new VideoModel();
            addarray.setId(videoID);
            addarray.setName(displayname);
            addarray.setData(data);
            addarray.setSize(videoSize);
            addarray.setImg(encodedImage);




            arrayList.add(addarray);
        }
        return arrayList;
    }


//    public class VideoAdapter extends BaseAdapter {
//        private Context vContext;
//
//        public VideoAdapter(Context c) {
//            vContext = c;
//        }
//
//        public int getCount() {
//            return count;
//        }
//
//        public Object getItem(int position) {
//            return position;
//        }
//
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @SuppressLint("SetTextI18n")
//        public View getView(int position, View convertView, ViewGroup parent) {
//            System.gc();
//            ViewHolder holder;
//            String id = null;
//            String duration;
//            long size;
//            convertView = null;
//            if (convertView == null) {
//                convertView = LayoutInflater.from(vContext).inflate(
//                        R.layout.single_video_layout, parent, false);
//                holder = new ViewHolder();
//                holder.txtTitle = (TextView) convertView
//                        .findViewById(R.id.video_single_name);
//                holder.txtSize = (TextView) convertView
//                        .findViewById(R.id.video_single_size);
//                holder.txtDutaion = convertView.findViewById(R.id.video_single_duration);
//                holder.thumbImage = (ImageView) convertView
//                        .findViewById(R.id.video_single_image);
//                holder.relativeLayout = findViewById(R.id.relLayout1);
//
//                video_column_index = videocursor
//                        .getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
//                videocursor.moveToPosition(position);
//                id = videocursor.getString(video_column_index);
//
//                video_column_index = videocursor
//                        .getColumnIndexOrThrow(MediaStore.Video.Media.SIZE);
//                videocursor.moveToPosition(position);
//                size = Long.parseLong(videocursor.getString(video_column_index));
//
////                video_column_index = videocursor
////                        .getColumnIndexOrThrow(MediaStore.Video.Media.);
////                videocursor.moveToPosition(position);
////                duration = videocursor.getString(video_column_index);
//
//                // id += " Size(KB):" +
//                // videocursor.getString(video_column_index);
//                holder.txtTitle.setText(id);
//                holder.txtSize.setText(formatFileSize(size));
////                holder.txtDutaion.setText(duration);
////                holder.txtSize.setText(" Size(KB):"
////                        + videocursor.getString(video_column_index));
//
//
//                String[] proj = { MediaStore.Video.Media._ID,
//                        MediaStore.Video.Media.DISPLAY_NAME,
//                        MediaStore.Video.Media.DATA };
//                @SuppressWarnings("deprecation")
//                Cursor cursor = managedQuery(
//                        MediaStore.Video.Media.EXTERNAL_CONTENT_URI, proj,
//                        MediaStore.Video.Media.DISPLAY_NAME + "=?",
//                        new String[] { id }, null);
//                cursor.moveToFirst();
//                long ids = cursor.getLong(cursor
//                        .getColumnIndex(MediaStore.Video.Media._ID));
//
//                ContentResolver crThumb = getContentResolver();
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 1;
//                Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(
//                        crThumb, ids, MediaStore.Video.Thumbnails.MICRO_KIND,
//                        options);
//                holder.thumbImage.setImageBitmap(curThumb);
//                curThumb = null;
//
//
//
//            } else{
//                holder = (ViewHolder) convertView.getTag();
//            }
//            return convertView;
//        }
//    }

    static class ViewHolder {

        TextView txtTitle;
        TextView txtSize;
        TextView txtDutaion;
        ImageView thumbImage;
        RelativeLayout relativeLayout;
    }

    public static String formatFileSize(long size) {
        String hrSize = null;

        double b = size;
        double k = size/1024.0;
        double m = ((size/1024.0)/1024.0);
        double g = (((size/1024.0)/1024.0)/1024.0);
        double t = ((((size/1024.0)/1024.0)/1024.0)/1024.0);

        DecimalFormat dec = new DecimalFormat("0.00");

        if ( t>1 ) {
            hrSize = dec.format(t).concat(" TB");
        } else if ( g>1 ) {
            hrSize = dec.format(g).concat(" GB");
        } else if ( m>1 ) {
            hrSize = dec.format(m).concat(" MB");
        } else if ( k>1 ) {
            hrSize = dec.format(k).concat(" KB");
        } else {
            hrSize = dec.format(b).concat(" Bytes");
        }

        return hrSize;
    }
}