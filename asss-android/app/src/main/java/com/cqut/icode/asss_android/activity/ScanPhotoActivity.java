package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.StaticParameters;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ScanPhotoActivity extends BaseActivity {

    @BindView(R.id.imgv_photo)
    ImageView imgvPhoto;
    @BindView(R.id.img_return)
    ImageView imgReturn;


    private String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan_photo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra(StaticParameters.IMAGE_PATH);
        initView();
    }

    private void initView() {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imgvPhoto.setImageBitmap(bitmap);
    }

    @OnClick(R.id.img_return)
    public void onClick() {
        this.finish();
    }
}
