package com.wsy.geeknewstest.app;

import android.os.Environment;

import com.wsy.geeknewstest.common.HostType;

import java.io.File;

/**
 * Created by hasee on 2016/11/10.
 */

public class Constants {

    //================= TYPE ====================

    public static final int TYPE_ZHIHU = 101;

    public static final int TYPE_ANDROID = 102;

    public static final int TYPE_IOS = 103;

    public static final int TYPE_WEB = 104;

    public static final int TYPE_GIRL = 105;

    public static final int TYPE_WECHAT = 106;

    public static final int TYPE_GANK = 107;

    public static final int TYPE_GOLD = 108;

    public static final int TYPE_SETTING = 109;

    public static final int TYPE_LIKE = 110;

    public static final int TYPE_ABOUT = 111;

    public static final int TYPE_VIDEO = 112;

    public static final int TYPE_NEWS = 113;

    //================= KEY ====================

    public static final String KEY_API = "7954cabb94e7ba4a653da6dbfd8dc432"; //需要APIKEY请去 http://www.tianapi.com/#wxnew 申请,复用会减少访问可用次数

    public static final String KEY_ALIPAY = "aex07566wvayrgxicnaraae";

    //================= PATH ====================

    public static final String PATH_DATA = App.getInstance().getCacheDir().getAbsolutePath() + File.separator + "data";

    public static final String PATH_CACHE = PATH_DATA + "/NetCache";

    public static final String PATH_SDCARD = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "codeest" + File.separator + "GeekNews";

    //================= UMENG ====================

    public static final String EVENT_TAB_HOME = "tab_home";

    //================= PREFERENCE ====================

    public static final String SP_NIGHT_MODE = "night_mode";

    public static final String SP_NO_IMAGE = "no_image";

    public static final String SP_AUTO_CACHE = "auto_cache";

    public static final String SP_CURRENT_ITEM = "current_item";

    public static final String SP_LIKE_POINT = "like_point";

    public static final String SP_VERSION_POINT = "version_point";

    public static final String SP_MANAGER_POINT = "manager_point";

    public static final String SP_NEWS_MANAGER_POINT = "news_manager_point";

    //================= INTENT ====================

    public static final String IT_GANK_TYPE = "type";

    public static final String IT_GANK_TYPE_CODE = "type_code";

    public static final String IT_DETAIL_TITLE = "title";

    public static final String IT_DETAIL_URL = "url";

    public static final String IT_DETAIL_IMG_URL = "img_url";

    public static final String IT_DETAIL_ID = "id";

    public static final String IT_DETAIL_TYPE = "type";

    public static final String IT_GOLD_TYPE = "type";

    public static final String IT_GOLD_TYPE_STR = "type_str";

    public static final String IT_GOLD_MANAGER = "manager";

    public static final String IT_NEWS_MANAGER = "news_manager";

    public static final String NEWS_ID = "news_id";

    public static final String NEWS_TYPE = "news_type";

    public static final String CHANNEL_POSITION = "channel_position";

    public static final String IT_PHOTO_DETAIL = "photo_detail";

    public static final String IT_PHOTO_DETAIL_IMGSRC = "photo_detail_imgsrc";

    public static final String IT_NEWS_POST_ID = "news_post_id";

    public static final String IT_NEWS_IMG_RES = "news_img_res";

    public static final String TRANSITION_ANIMATION_NEWS_PHOTOS = "transition_animation_news_photos";

    //=========================news==================================

    public static final String NETEAST_HOST = "http://c.m.163.com/";
    public static final String END_URL = "-20.html";
    public static final String ENDDETAIL_URL = "/full.html";

    // 新闻详情
    public static final String NEWS_DETAIL = NETEAST_HOST + "nc/article/";

    // 头条TYPE
    public static final String HEADLINE_TYPE = "headline";
    // 房产TYPE
    public static final String HOUSE_TYPE = "house";
    // 其他TYPE
    public static final String OTHER_TYPE = "list";

    //    // 北京
    //    public static final String LOCAL_TYPE = "local";
    //    // 北京的Id
    //    public static final String BEIJING_ID = "5YyX5Lqs";

    // 头条id
    public static final String HEADLINE_ID = "T1348647909107";
    // 房产id
    public static final String HOUSE_ID = "5YyX5Lqs";
    // 足球
    public static final String FOOTBALL_ID = "T1399700447917";
    // 娱乐
    public static final String ENTERTAINMENT_ID = "T1348648517839";
    // 体育
    public static final String SPORTS_ID = "T1348649079062";
    // 财经
    public static final String FINANCE_ID = "T1348648756099";
    // 科技
    public static final String TECH_ID = "T1348649580692";
    // 电影
    public static final String MOVIE_ID = "T1348648650048";
    // 汽车
    public static final String CAR_ID = "T1348654060988";
    // 笑话
    public static final String JOKE_ID = "T1350383429665";
    // 游戏
    public static final String GAME_ID = "T1348654151579";
    // 时尚
    public static final String FASHION_ID = "T1348650593803";
    // 情感
    public static final String EMOTION_ID = "T1348650839000";
    // 精选
    public static final String CHOICE_ID = "T1370583240249";
    // 电台
    public static final String RADIO_ID = "T1379038288239";
    // nba
    public static final String NBA_ID = "T1348649145984";
    // 数码
    public static final String DIGITAL_ID = "T1348649776727";
    // 移动
    public static final String MOBILE_ID = "T1351233117091";
    // 彩票
    public static final String LOTTERY_ID = "T1356600029035";
    // 教育
    public static final String EDUCATION_ID = "T1348654225495";
    // 论坛
    public static final String FORUM_ID = "T1349837670307";
    // 旅游
    public static final String TOUR_ID = "T1348654204705";
    // 手机
    public static final String PHONE_ID = "T1348649654285";
    // 博客
    public static final String BLOG_ID = "T1349837698345";
    // 社会
    public static final String SOCIETY_ID = "T1348648037603";
    // 家居
    public static final String FURNISHING_ID = "T1348654105308";
    // 暴雪游戏
    public static final String BLIZZARD_ID = "T1397016069906";
    // 亲子
    public static final String PATERNITY_ID = "T1397116135282";
    // CBA
    public static final String CBA_ID = "T1348649475931";
    // 消息
    public static final String MSG_ID = "T1371543208049";
    // 军事
    public static final String MILITARY_ID = "T1348648141035";

    /**
     * 新浪图片新闻
     */
    public static final String SINA_PHOTO_HOST = "http://gank.io/api/";


    /**
     * 新闻id获取类型
     *
     * @param id 新闻id
     * @return 新闻类型
     */
    public static String getType(String id) {
        switch (id) {
            case HEADLINE_ID:
                return HEADLINE_TYPE;
            case HOUSE_ID:
                return HOUSE_TYPE;
            default:
                break;
        }
        return OTHER_TYPE;
    }

    /**
     * 获取对应的host
     *
     * @param hostType host类型
     * @return host
     */
    public static String getHost(int hostType) {
        String host;
        switch (hostType) {
            case HostType.NETEASE_NEWS_VIDEO:
                host = NETEAST_HOST;
                break;
            case HostType.GANK_GIRL_PHOTO:
                host = SINA_PHOTO_HOST;
                break;
            case HostType.NEWS_DETAIL_HTML_PHOTO:
                host = "http://kaku.com/";
                break;
            default:
                host = "";
                break;
        }
        return host;
    }
}
