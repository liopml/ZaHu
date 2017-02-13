package com.wsy.geeknewstest.widget;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Html;
import android.widget.TextView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.App;
import com.wsy.geeknewstest.model.http.RetrofitHelper;
import com.wsy.geeknewstest.util.LogUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.ResponseBody;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by hasee on 2017/2/6.
 */

public class URLImageGetter implements Html.ImageGetter {

    private TextView mTextView;
    private int mPicWidth;
    private String mNewsBody;
    private int mPicCount;
    private int mPicTotal;
    private static final String mFilePath = App.getInstance().getCacheDir().getAbsolutePath();
    private RetrofitHelper mRetrofitHelper;
    public Subscription mSubscription;

    public URLImageGetter(TextView textView, String newsBody, int picTotal) {
        mTextView = textView;
        mPicWidth = mTextView.getWidth();
        mNewsBody = newsBody;
        mPicTotal = picTotal;
        mRetrofitHelper = new RetrofitHelper();
    }

    @Override
    public Drawable getDrawable(String source) {
        Drawable drawable;
        File file = new File(mFilePath, source.hashCode() + "");
        if (file.exists()) {
            mPicCount++;
            drawable = getDrawableFromDisk(file);
        } else {
            drawable = getDrawableFromNet(source);
        }
        return drawable;
    }

    @Nullable
    private Drawable getDrawableFromDisk(File file) {
        Drawable drawable = Drawable.createFromPath(file.getAbsolutePath());
        if (drawable != null) {
            int picHeight = calculatePicHeight(drawable);
            drawable.setBounds(0, 0, mPicWidth, picHeight);
        }
        return drawable;
    }

    private int calculatePicHeight(Drawable drawable) {
        float imgWidth = drawable.getIntrinsicWidth();
        float imgHeight = drawable.getIntrinsicHeight();
        float rate = imgHeight / imgWidth;
        return (int) (mPicWidth * rate);
    }

    @NonNull
    private Drawable getDrawableFromNet(final String source) {
        mSubscription = mRetrofitHelper.fetchNewsBodyHtmlPhoto(source)
                .unsubscribeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<ResponseBody, Boolean>() {
                    @Override
                    public Boolean call(ResponseBody responseBody) {
                        return WritePicToDisk(responseBody, source);
                    }
                })
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        mPicCount++;
                        if (aBoolean && (mPicCount == mPicTotal - 1)) {
                            mTextView.setText(Html.fromHtml(mNewsBody, URLImageGetter.this, null));
                        }
                    }
                });
        return createPicPlaceholder();
    }

    @NonNull
    private Boolean WritePicToDisk(ResponseBody response, String source) {
        File file = new File(mFilePath, source.hashCode() + "");
        InputStream in = null;
        FileOutputStream out = null;
        try {
            in = response.byteStream();
            out = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            while ((len = in.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {

            }
        }
    }

    @SuppressWarnings("deprecation")
    @NonNull
    private Drawable createPicPlaceholder() {
        Drawable drawable;
        drawable = new ColorDrawable(App.getInstance().getResources().getColor(R.color.image_place_holder));
        drawable.setBounds(0, 0, mPicWidth, mPicWidth / 3);
        return drawable;
    }

}
