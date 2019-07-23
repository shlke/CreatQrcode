package com.tjgx.creatqrcode;
import androidx.appcompat.app.AppCompatActivity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.Hashtable;

public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private ImageView imageView;
    private Button  btn;
    private int QR_WIDTH = 200, QR_HEIGHT = 200; //要生成的二维码图片大小

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ininview();

    }
//控件初始化
    private void ininview() {
        editText = findViewById(R.id.edt);
        imageView = findViewById(R.id.imgv);
        btn = findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {//替按钮添加点击事件
            @Override
            public void onClick(View view) {
                String string  = editText.getText().toString();//获得输入的文字
                if (!string.isEmpty()) {   //判断输入是否为空
                createQRImage(string);  //调用方法开始生成
                }
            }
        });
    }

    /**
     * @方法功能说明: 生成二维码图片,实际使用时要初始化sweepIV,不然会报空指针错误
     * @作者:tjgx
     * @时间:2019-7-23
     * @参数: @param url 要转换的地址或字符串,可以是中文
     * @return void
     * @throws
     */

    //要转换的地址或字符串,可以是中文
    public void createQRImage(String url)
    {
        try
        {
            //判断URL合法性
            if (url == null || "".equals(url) || url.length() < 1)
            {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            //图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(url, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            //下面这里按照二维码的算法，逐个生成二维码的图片，
            //两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++)
            {
                for (int x = 0; x < QR_WIDTH; x++)
                {
                    if (bitMatrix.get(x, y))
                    {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    }
                    else
                    {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            //生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            //显示到一个ImageView上面
            imageView.setImageBitmap(bitmap);
        }
        catch (WriterException e)
        {
            e.printStackTrace();
        }
    }
}
