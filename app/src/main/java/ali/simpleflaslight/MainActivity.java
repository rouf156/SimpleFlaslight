package ali.simpleflaslight;

import android.app.Activity;
import android.content.Context;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class MainActivity extends Activity {
//new
    public Camera camera;
    public boolean hasFlash;
    public String x;
    static ImageButton btnSwitch;
    Parameters params;
    static MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            //================================== First Check =====================================//
            turnFlash();
            btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
            btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnFlash();
                }
            });
            mp = new MediaPlayer();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //====================================== Turning Flash On/Off ================================//
    public void turnFlash() {
        CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try{
            String cameraId = camManager.getCameraIdList()[0]; // Usually front camera is at 0 position.
            camManager.setTorchMode(cameraId, true);
        } catch (Exception e){

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
