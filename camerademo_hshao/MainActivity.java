package com.example.shaohao.camerademo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final int TAKE_PIC = 1;
    private Button mTakePic;
    private ImageView mPic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        initView();
        initEvent();
    }

    private void initView() {

        setContentView(R.layout.activity_main);

        mTakePic = (Button) this.findViewById(R.id.btn_take_pci);
        mPic = (ImageView) findViewById(R.id.iv_image);
    }

    private void initEvent() {
        mTakePic.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v == mTakePic) {
            //跳转到拍照界面
            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
            startActivityForResult(intent, TAKE_PIC);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAKE_PIC) {
            if (resultCode == RESULT_OK) {
                //说明成功咯
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                //让照片显示在imageView上
                mPic.setImageBitmap(bitmap);

                //判断外置内存卡是否存在
                if (!ExCardIsMount()) {
                    Toast.makeText(MainActivity.this, "没有挂载外置内存卡!", Toast.LENGTH_SHORT).show();
                    return;
                }
                //把照片存放到照相机目录里头
                File picFile = new File(Environment.getExternalStorageDirectory(), "" + System.currentTimeMillis() + ".jpg");
                try {
                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(picFile));

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);

                    //关流
                    bos.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private boolean ExCardIsMount() {
        String isMount = Environment.getExternalStorageState();
        if (isMount.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
}