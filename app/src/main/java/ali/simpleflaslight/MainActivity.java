package ali.simpleflaslight;

import android.app.Activity;
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
    ImageButton btnSwitch;
    Parameters params;
    MediaPlayer mp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            //================================== First Check =====================================//
            hasFlash = getApplicationContext().getPackageManager()
                    .hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

            if (!hasFlash) {
                AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                        .create();
                alert.setTitle("Error");
                alert.setMessage("Sorry, your device doesn't support a flashlight!");
                alert.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });
                alert.show();
                return;
            }
            turnFlash();
            btnSwitch = (ImageButton) findViewById(R.id.btnSwitch);
            btnSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    turnFlash();
                }
            });
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    //====================================== Getting Camera Parameters ===========================//
    public void getCamera() {
        try {
            if (camera == null) {
                try {
                    camera = Camera.open();
                    params = camera.getParameters();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void checkLight() {
        try {
            if (camera == null || params == null) {
                getCamera();
            }
            x = params.getFlashMode();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    //====================================== Turning On Flash ====================================//
    public void turnFlash() {
        checkLight();
        try {
            if (x.equals("off")) {
                if (camera == null || params == null) {
                    getCamera();
                }
                try {
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    camera.setParameters(params);
                    camera.startPreview();
                }catch (java.lang.RuntimeException f) {
                    Toast.makeText(this, "Flashlight already in use!", Toast.LENGTH_LONG).show();
                    //finish();
                }
                catch (Exception e) {e.printStackTrace();}
                try {
                    btnSwitch.setImageResource(R.drawable.btn_switch_on);
                    mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_on);
                } catch (Exception e) {e.printStackTrace();}
            }
            else if (x.equals("torch")) {
                if (camera == null || params == null) {
                    getCamera();
                }
                try {
                    params = camera.getParameters();
                    params.setFlashMode(Parameters.FLASH_MODE_OFF);
                    camera.setParameters(params);
                    camera.stopPreview();
                    if (camera != null) {
                        camera.release();
                        camera = null;
                    }
                }catch (java.lang.RuntimeException f) {
                    Toast.makeText(this, "Flashlight already in use!", Toast.LENGTH_LONG).show();
                    //finish();
                }
                catch (Exception e) {e.printStackTrace();}
                try {
                    btnSwitch.setImageResource(R.drawable.btn_switch_off);
                    mp = MediaPlayer.create(MainActivity.this, R.raw.light_switch_off);
                } catch (Exception e) {e.printStackTrace();}
            }
            mp.setOnCompletionListener(new OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                }
            });
            mp.start();
        } catch (java.lang.RuntimeException f) {
            Toast.makeText(this, "Flashlight already in use!", Toast.LENGTH_LONG).show();
            //finish();
        }
    catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getCamera();
        checkLight();
        if (x.equals("off")) {
            turnFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
}
