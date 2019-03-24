package com.cqut.icode.asss_android.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cqut.icode.asss_android.R;
import com.cqut.icode.asss_android.common.LogUtil;
import com.cqut.icode.asss_android.common.StaticParameters;
import com.cqut.icode.asss_android.util.ImageUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 作者：hwl
 * 时间：2017/7/19:21:31
 * 邮箱：1097412672@qq.com
 * 说明：
 */
public class ViewPhotoActivity extends BaseActivity {
    @BindView(R.id.img_photo)
    ImageView imgPhoto;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rl_layout)
    FrameLayout rlLayout;
    @BindView(R.id.tv_cancel)
    Button tvCancel;
    @BindView(R.id.tv_ok)
    Button tvOk;


    private String imagePath;
    private String screenshotPath;
    private String screenshotName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_view_photo);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        imagePath = intent.getStringExtra(StaticParameters.IMAGE_PATH);
        screenshotName = intent.getStringExtra(StaticParameters.SCREENSHOT_NAME);
        initView();
    }

    private void initView() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        try {
            // 将拍摄的照片显示出来
            Bitmap bitmap = ImageUtil.getPressedBitmap(imagePath,
                    display.getWidth(), display.getHeight());
            LogUtil.d("display.getWidth()", display.getWidth() + "");
            LogUtil.d("display.getHeight()", display.getHeight() + "");
            imgPhoto.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @OnClick({R.id.tv_cancel, R.id.tv_ok,R.id.img_return})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_return:
            case R.id.tv_cancel:
                ImageUtil.deleteImageFromSDCard(imagePath);
                finish();
                break;
            case R.id.tv_ok:
                final Bitmap bitmap = getScreenPhoto(rlLayout);
                screenshotPath = ImageUtil.saveBitmap(null, screenshotName, bitmap);//根据路径保存图片
                ImageUtil.deleteImageFromSDCard(imagePath);
                returnResult();
                finish();
                break;
        }
    }

    public void returnResult() {
        Intent intent = new Intent();
        intent.putExtra(StaticParameters.SCREENSHOT_PATH, screenshotPath);
        setResult(RESULT_OK, intent);
    }


    public Bitmap getScreenPhoto(FrameLayout waterPhoto) {
        View view = waterPhoto;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        int width = view.getWidth();
        int height = view.getHeight();
        Bitmap bitmap1 = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        view.destroyDrawingCache();
        bitmap = null;
        return bitmap1;
    }
}
