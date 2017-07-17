package ali.simpleflaslight;

import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.widget.RemoteViews;
import android.widget.Toast;

public class FlashlightWidgetReceiver extends BroadcastReceiver {
    private static boolean isLightOn = false;
    private static Camera camera;
    MediaPlayer mp;
    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("COM_FLASHLIGHT")) {
            turnFlash(context);
        }
    }
    private void turnFlash(Context context) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.appwidget_layout);

        if(isLightOn) {
            views.setImageViewResource(R.id.imageButton, R.drawable.btn_switch_off);
            mp = MediaPlayer.create(context, R.raw.light_switch_off);
        } else {
            views.setImageViewResource(R.id.imageButton, R.drawable.btn_switch_on);
            mp = MediaPlayer.create(context, R.raw.light_switch_on);
        }
        try {
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {
                    // TODO Auto-generated method stub
                    mp.release();
                }
            });
            mp.start();
        }
        catch (Exception e) {}
        if (isLightOn) {
            if (camera != null) {
                camera.stopPreview();
                camera.release();
                camera = null;
                isLightOn = false;
            }
        } else {
            camera = Camera.open();
            if(camera == null) {
                Toast.makeText(context, "Flashlight already in use!", Toast.LENGTH_LONG).show();
            } else {
                Camera.Parameters param = camera.getParameters();
                param.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                try {
                    camera.setParameters(param);
                    camera.startPreview();
                    isLightOn = true;
                } catch (Exception e) {
                    Toast.makeText(context, "Flashlight already in use!", Toast.LENGTH_LONG).show();
                }
            }
        }
        views.setOnClickPendingIntent(R.id.imageButton, WidgetProvider.buildButtonPendingIntent(context));
        WidgetProvider.pushWidgetUpdate(context.getApplicationContext(), views);
    }
}