package com.wsy.geeknewstest.ui.main.fragment;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wsy.geeknewstest.R;
import com.wsy.geeknewstest.app.Constants;
import com.wsy.geeknewstest.base.BaseFragment;
import com.wsy.geeknewstest.component.ACache;
import com.wsy.geeknewstest.component.RxBus;
import com.wsy.geeknewstest.model.bean.NightModeEvent;
import com.wsy.geeknewstest.model.bean.VersionBean;
import com.wsy.geeknewstest.presenter.SettingPresenter;
import com.wsy.geeknewstest.presenter.contract.SettingContract;
import com.wsy.geeknewstest.util.ShareUtil;
import com.wsy.geeknewstest.util.SharedPreferenceUtil;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hasee on 2016/12/10.
 */

public class SettingFragment extends BaseFragment<SettingPresenter> implements SettingContract.View, CompoundButton.OnCheckedChangeListener {

    @BindView(R.id.cb_setting_cache)
    AppCompatCheckBox cbSettingCache;
    @BindView(R.id.cb_setting_image)
    AppCompatCheckBox cbSettingImage;
    @BindView(R.id.cb_setting_night)
    AppCompatCheckBox cbSettingNight;
    @BindView(R.id.ll_setting_feedback)
    LinearLayout llSettingFeedback;
    @BindView(R.id.tv_setting_clear)
    TextView tvSettingClear;
    @BindView(R.id.ll_setting_clear)
    LinearLayout llSettingClear;
    @BindView(R.id.tv_setting_update)
    TextView tvSettingUpdate;
    @BindView(R.id.ll_setting_update)
    LinearLayout llSettingUpdate;

    private File cacheFile;
    private String versionName;
    private boolean isNull = true;

    @Override
    protected void initInject() {
        getFragmentComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    protected void initEventAndData() {
        cacheFile = new File(Constants.PATH_CACHE);
        tvSettingClear.setText(ACache.getCacheSize(cacheFile));
        cbSettingCache.setChecked(SharedPreferenceUtil.getAutoCacheState());
        cbSettingImage.setChecked(SharedPreferenceUtil.getNoImageState());
        cbSettingNight.setChecked(SharedPreferenceUtil.getNightModeState());
        cbSettingCache.setOnCheckedChangeListener(this);
        cbSettingImage.setOnCheckedChangeListener(this);
        cbSettingNight.setOnCheckedChangeListener(this);
        try {
            PackageManager pm = getActivity().getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getActivity().getPackageName(), PackageManager.GET_ACTIVITIES);
            versionName = pi.versionName;
            tvSettingUpdate.setText(String.format("当前版本号 v%s", versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        isNull = savedInstanceState == null;
        super.onCreate(savedInstanceState);
    }

    @OnClick(R.id.ll_setting_feedback)
    void doFeedBack() {
        ShareUtil.sendEmail(mContext, "选择邮件客户端:");
    }

    @OnClick(R.id.ll_setting_clear)
    void doClear() {
        ACache.deleteDir(cacheFile);
        tvSettingClear.setText(ACache.getCacheSize(cacheFile));
    }

    @Override
    public void showUpdateDialog(VersionBean bean) {

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.cb_setting_night:
                if (isNull) {  //防止夜间模式MainActivity执行reCreate后重复调用
                    SharedPreferenceUtil.setNightModeState(isChecked);
                    NightModeEvent event = new NightModeEvent();
                    event.setNightMode(isChecked);
                    RxBus.getDefault().post(event);
                }
                break;

            case R.id.cb_setting_image:
                SharedPreferenceUtil.setNoImageState(isChecked);
                break;

            case R.id.cb_setting_cache:
                SharedPreferenceUtil.setAutoCacheState(isChecked);
                break;
        }
    }
}
