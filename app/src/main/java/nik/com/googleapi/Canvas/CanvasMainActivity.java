package nik.com.googleapi.Canvas;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import nik.com.googleapi.R;
import yuku.ambilwarna.AmbilWarnaDialog;

public class CanvasMainActivity extends AppCompatActivity {

    private PaintView paintView;
    public static int DEFAULT_COLOR = Color.BLUE;
    public static int BRUSH_SIZE = 10;

    Button btnColor,btnStrokewidth;
    DisplayMetrics metrics;

    Dialog dialog;

    private static final String TAG = "CanvasMainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_canvas_main);

        paintView = findViewById(R.id.paintView);

        metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics,DEFAULT_COLOR,BRUSH_SIZE);

        btnColor = findViewById(R.id.btnColor);
        btnStrokewidth = findViewById(R.id.btnStrokewidth);
        btnColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenColorPicker(false);
            }
        });
        // Dialog Function
        dialog = new Dialog(CanvasMainActivity.this);
        // Removing the features of Normal Dialogs
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_signature);
        dialog.setCancelable(true);
        btnStrokewidth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SeekBar seekBar = dialog.findViewById(R.id.seekBar);

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        Log.e(TAG, "onProgressChanged: "+ i );
                        if (i == seekBar.getMax()) {

                            BRUSH_SIZE = i;
//                            paintView.init(metrics,DEFAULT_COLOR,BRUSH_SIZE);
                            paintView.strokeWidth(BRUSH_SIZE);

                        }
                        else {
                            BRUSH_SIZE = i;
//                            paintView.init(metrics,DEFAULT_COLOR,BRUSH_SIZE);
                            paintView.strokeWidth(BRUSH_SIZE);
                        }

                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {

                    }
                });

                dialog.show();

            }
        });

    }


    private void OpenColorPicker(boolean AlphaSupport){
        AmbilWarnaDialog ambilWarnaDialog = new AmbilWarnaDialog(this, DEFAULT_COLOR, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
                Toast.makeText(CanvasMainActivity.this, "Color Picker Close", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                DEFAULT_COLOR = color;
                paintView.init(metrics,DEFAULT_COLOR,BRUSH_SIZE);
            }
        });
        ambilWarnaDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.paint_main,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.eraser:
                paintView.init(metrics,Color.WHITE,BRUSH_SIZE);

                return true;
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.emboss:
                paintView.emBoss();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
