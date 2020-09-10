package com.example.learn.ui.youtu;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.common.base.BaseActivity;
import com.example.common.ui.loading.LoadingUtil;
import com.example.learn.R;
import com.example.learn.ui.youtu.common.Config;
import com.example.learn.ui.youtu.util.Util;
import com.example.utils.view.ImageUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author fqxyi
 * @date 2017/7/4
 */
public class YoutuTestActivity extends BaseActivity {

    private final String LOG_TAG = YoutuTestActivity.class.getName();

    private final int UPLOAD_IDCARD_FRONT = 1;
    private final int UPLOAD_IDCARD_BACK = 2;

    private BitmapFactory.Options opts = null;

    private Bitmap theSelectedImage = null;

    private String APP_ID = "";
    private String SECRET_ID = "";
    private String SECRET_KEY = "";
    Youtu faceYoutu;

    private Button identify_local_idcard_front;
    private Button identify_local_idcard_back;
    private Button identify_upload_idcard_front;
    private Button identify_upload_idcard_back;
    private Button identify_remote_idcard_front;
    private Button identify_remote_idcard_back;

    private ImageView frontimage;
    private TextView name;
    private TextView sex;
    private TextView nation;
    private TextView birth;
    private TextView address;
    private TextView id;

    private ImageView backimage;
    private TextView authority;
    private TextView valid_date;

    private TextView exception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yoututest);

        init();
    }

    private void init() {
        APP_ID = Config.APP_ID;
        SECRET_ID = Config.SECRET_ID;
        SECRET_KEY = Config.SECRET_KEY;
        faceYoutu = new Youtu(APP_ID, SECRET_ID, SECRET_KEY, Youtu.API_YOUTU_END_POINT);

        opts = new BitmapFactory.Options();
        opts.inDensity = this.getResources().getDisplayMetrics().densityDpi;
        opts.inTargetDensity = this.getResources().getDisplayMetrics().densityDpi;

        initView();
        initEvent();
    }

    private void initView() {
        identify_local_idcard_front = (Button) findViewById(R.id.identify_local_idcard_front);
        identify_local_idcard_back = (Button) findViewById(R.id.identify_local_idcard_back);
        identify_upload_idcard_front = (Button) findViewById(R.id.identify_upload_idcard_front);
        identify_upload_idcard_back = (Button) findViewById(R.id.identify_upload_idcard_back);
        identify_remote_idcard_front = (Button) findViewById(R.id.identify_remote_idcard_front);
        identify_remote_idcard_back = (Button) findViewById(R.id.identify_remote_idcard_back);

        frontimage = (ImageView) findViewById(R.id.frontimage);
        name = (TextView) findViewById(R.id.name);
        sex = (TextView) findViewById(R.id.sex);
        nation = (TextView) findViewById(R.id.nation);
        birth = (TextView) findViewById(R.id.birth);
        address = (TextView) findViewById(R.id.address);
        id = (TextView) findViewById(R.id.id);

        backimage = (ImageView) findViewById(R.id.backimage);
        authority = (TextView) findViewById(R.id.authority);
        valid_date = (TextView) findViewById(R.id.valid_date);

        exception = (TextView) findViewById(R.id.exception);
    }

    private void initEvent() {
        identify_local_idcard_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingUtil.showLoading(YoutuTestActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap selectedImage = BitmapFactory.decodeResource(getResources(), R.mipmap.idcard_front, opts);
                            final JSONObject response = faceYoutu.IdcardOcr(selectedImage, 0);
                            Log.d(LOG_TAG, response.toString());
                            showFrontData(response);
                            if (null != selectedImage) {
                                selectedImage.recycle();
                            }
                        } catch (Exception e) {
                            exception.setText(e.toString());
                        }
                    }
                }).start();
            }
        });
        identify_local_idcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingUtil.showLoading(YoutuTestActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Bitmap selectedImage = BitmapFactory.decodeResource(getResources(), R.mipmap.idcard_back, opts);
                            final JSONObject response = faceYoutu.IdcardOcr(selectedImage, 1);
                            Log.d(LOG_TAG, response.toString());
                            showBackData(response);
                            if (null != selectedImage) {
                                selectedImage.recycle();
                            }
                        } catch (Exception e) {
                            exception.setText(e.toString());
                        }
                    }
                }).start();
            }
        });
        identify_upload_idcard_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, UPLOAD_IDCARD_FRONT);
            }
        });
        identify_upload_idcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, UPLOAD_IDCARD_BACK);
            }
        });
        identify_remote_idcard_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingUtil.showLoading(YoutuTestActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject response = faceYoutu.IdcardOcrUrl("http://open.youtu.qq.com/content/img/product/ocr/ocr_id_02.jpg", 0);
                            Log.d(LOG_TAG, response.toString());
                            showFrontData(response);
                        } catch (Exception e) {
                            exception.setText(e.toString());
                        }
                    }
                }).start();
            }
        });
        identify_remote_idcard_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingUtil.showLoading(YoutuTestActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            final JSONObject response = faceYoutu.IdcardOcrUrl("http://img2.thebetterchinese.com/beicai2/tmp/33339769f5f3411497a65bb785f42f49.jpg", 1);
                            Log.d(LOG_TAG, response.toString());
                            showBackData(response);
                        } catch (Exception e) {
                            exception.setText(e.toString());
                        }
                    }
                }).start();
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Uri uri = data.getData();
            Log.e("uri", uri.toString());
            try {
                String path = Util.getPath(this, uri);
                theSelectedImage = ImageUtil.getBitmap(path, 1000, 1000);
                LoadingUtil.showLoading(YoutuTestActivity.this);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (theSelectedImage != null) {
                            try {
                                if (requestCode == UPLOAD_IDCARD_FRONT) {
                                    JSONObject response = faceYoutu.IdcardOcr(theSelectedImage, 0);
                                    Log.d(LOG_TAG, response.toString());
                                    showFrontData(response);
                                } else if (requestCode == UPLOAD_IDCARD_BACK) {
                                    JSONObject response = faceYoutu.IdcardOcr(theSelectedImage, 1);
                                    Log.d(LOG_TAG, response.toString());
                                    showBackData(response);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            } catch ( Exception e) {
                exception.setText(e.toString());
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFrontData(final JSONObject response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.hideLoading(YoutuTestActivity.this);
                backimage.setVisibility(View.GONE);
                authority.setVisibility(View.GONE);
                valid_date.setVisibility(View.GONE);
                frontimage.setVisibility(View.VISIBLE);
                name.setVisibility(View.VISIBLE);
                sex.setVisibility(View.VISIBLE);
                nation.setVisibility(View.VISIBLE);
                birth.setVisibility(View.VISIBLE);
                address.setVisibility(View.VISIBLE);
                id.setVisibility(View.VISIBLE);
                frontimage.setImageBitmap(null);
                name.setText("");
                sex.setText("");
                nation.setText("");
                birth.setText("");
                address.setText("");
                id.setText("");
                try {
                    frontimage.setImageBitmap(ImageUtil.base64ToBitmap(response.getString("frontimage")));
                    name.setText(String.valueOf("姓 名 " + response.getString("name")));
                    sex.setText(String.valueOf("性 别 " + response.getString("sex")));
                    nation.setText(String.valueOf("\t民 族 " + response.getString("nation")));
                    if (null != response.getString("birth") && response.getString("birth").split("/").length > 2) {
                        birth.setText(String.valueOf("出 生 " + response.getString("birth").split("/")[0] + "年" + response.getString("birth").split("/")[1] + "月" + response.getString("birth").split("/")[2] + "日"));
                    }
                    address.setText(String.valueOf("住 址 " + response.getString("address")));
                    id.setText(String.valueOf("公民身份号码 " + response.getString("id")));
                } catch (JSONException e) {
                    exception.setText(e.toString());
                }
            }
        });
    }

    private void showBackData(final JSONObject response) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                LoadingUtil.hideLoading(YoutuTestActivity.this);
                frontimage.setVisibility(View.GONE);
                name.setVisibility(View.GONE);
                sex.setVisibility(View.GONE);
                nation.setVisibility(View.GONE);
                birth.setVisibility(View.GONE);
                address.setVisibility(View.GONE);
                id.setVisibility(View.GONE);
                backimage.setVisibility(View.VISIBLE);
                authority.setVisibility(View.VISIBLE);
                valid_date.setVisibility(View.VISIBLE);
                backimage.setImageBitmap(null);
                authority.setText("");
                valid_date.setText("");
                try {
                    backimage.setImageBitmap(ImageUtil.base64ToBitmap(response.getString("backimage")));
                    authority.setText(String.valueOf("签发机关 " + response.getString("authority")));
                    valid_date.setText(String.valueOf("有效期限 " + response.getString("valid_date")));
                } catch (JSONException e) {
                    exception.setText(e.toString());
                }
            }
        });
    }
}
