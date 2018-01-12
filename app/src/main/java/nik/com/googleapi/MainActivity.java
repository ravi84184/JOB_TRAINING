package nik.com.googleapi;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import nik.com.googleapi.Canvas.CanvasMainActivity;
import nik.com.googleapi.MAP_API.MapActivity;
import nik.com.googleapi.VideoView.VideoActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final int ERROR_DIALOG_REQUEST = 9001;

    private Button btnMap,btnVideo,btnCanvas,btnIntegration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(isServicesOK()){
            init();
        }
    }

    private void init(){
        btnMap = (Button) findViewById(R.id.btnMap);
        btnVideo = findViewById(R.id.btnVideo);
        btnCanvas = findViewById(R.id.btnCanvas);
        btnIntegration = findViewById(R.id.btnIntegration);
        btnMap.setOnClickListener(this);
        btnVideo.setOnClickListener(this);
        btnCanvas.setOnClickListener(this);
        btnIntegration.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnMap:
                startActivity(new Intent(this, MapActivity.class));
                break;

            case R.id.btnVideo:
                startActivity(new Intent(this, VideoActivity.class));
                break;
            case R.id.btnCanvas:
                startActivity(new Intent(this, CanvasMainActivity.class));
                break;
            case R.id.btnIntegration:
                startActivity(new Intent(this, CanvasMainActivity.class));
                break;
        }

    }
    public boolean isServicesOK(){
        Log.d(TAG, "isServicesOK: checking google services version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(MainActivity.this);

        if(available == ConnectionResult.SUCCESS){
            //everything is fine and the user can make map requests
            Log.d(TAG, "isServicesOK: Google Play Services is working");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //an error occured but we can resolve it
            Log.d(TAG, "isServicesOK: an error occured but we can fix it");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(MainActivity.this, available, ERROR_DIALOG_REQUEST);
            dialog.show();
        }else{
            Toast.makeText(this, "You can't make map requests", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

}






















