package br.com.squarebits.cameratest;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.edmodo.cropper.CropImageView;
import com.mindorks.paracamera.Camera;

public class MainActivity extends AppCompatActivity {
    private final static int REQUEST_PERMISSIONS_CODE = 128;
//    protected CameraView cameraView;
    Camera camera;
    private ImageView picFrame;
    private Button cropper;
    private CropImageView cropImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_main);


        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_PERMISSIONS_CODE);
        }

        initView();

       initCamera();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.deleteImage();
    }

    public void initCamera(){
        camera = new Camera.Builder()
                .resetToCorrectOrientation(true)// it will rotate the camera bitmap to the correct orientation from meta data
                .setTakePhotoRequestCode(1)
                .setDirectory("pics")
                .setName("ali_" + System.currentTimeMillis())
                .setImageFormat(Camera.IMAGE_JPEG)
                .setCompression(75)
                .setImageHeight(1000)// it will try to achieve this height as close as possible maintaining the aspect ratio;
                .build(this);


        try {
            camera.takePicture();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void initView() {
        picFrame = (ImageView) findViewById(R.id.picFrame);
        cropper = (Button) findViewById(R.id.cropper);

        cropImageView = (CropImageView) findViewById(R.id.CropImageView);
        cropImageView.setAspectRatio(5, 10);
        cropImageView.setFixedAspectRatio(true);
        cropImageView.setGuidelines(1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Camera.REQUEST_TAKE_PHOTO){
            Bitmap bitmap = camera.getCameraBitmap();
            if(bitmap != null) {
                picFrame.setImageBitmap(bitmap);
            }else{
                Toast.makeText(this.getApplicationContext(),"Picture not taken!",Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CODE:
                for (int i = 0; i < permissions.length; i++) {

                    if (permissions[i].equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    } else if (permissions[i].equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)
                            && grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        Toast.makeText(this, "Favor habilitar a permissão para usar o aplicativo!", Toast.LENGTH_LONG).show();
                        finishAffinity();
                        return;
                    }
                }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
