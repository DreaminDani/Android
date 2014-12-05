package demo.android.app.hu.camerademo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class MainActivity extends Activity {

    private final String IMAGEPATH =
      Environment.getExternalStorageDirectory().getAbsolutePath() + "/tmp_image.jpg";
    public static final int REQUEST_CODE_PHOTO = 101;

    private ImageView ivPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnPhoto = (Button) findViewById(R.id.btnPhoto);
        btnPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                i.putExtra(MediaStore.EXTRA_OUTPUT,
                    Uri.fromFile(new File(IMAGEPATH)));
                startActivityForResult(i, REQUEST_CODE_PHOTO);
            }
        });

        ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_PHOTO) {
            if (resultCode == Activity.RESULT_OK) {
                FileInputStream fis = null;
                try {
                    BitmapFactory.Options opt = new BitmapFactory.Options();
                    opt.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(IMAGEPATH, opt);
                    int imgWidth = opt.outWidth;
                    int imgHeight = opt.outHeight;

                    int realWidth = ivPhoto.getMeasuredWidth();
                    int scaleFactor = Math.round((float)imgWidth / (float)realWidth);
                    opt.inSampleSize = scaleFactor;
                    opt.inJustDecodeBounds = false;

                    Bitmap img = BitmapFactory.decodeFile(IMAGEPATH,opt);

                    ivPhoto.setImageBitmap(img);
                } catch (Throwable t) {
                    t.printStackTrace();
                } finally {
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }
        }
    }
}
