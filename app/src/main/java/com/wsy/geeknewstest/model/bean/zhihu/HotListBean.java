package com.wsy.geeknewstest.model.bean.zhihu;

import java.util.List;

/**
 * Created by hasee on 2016/11/9.
 */

public class HotListBean {

    private List<RecentBean> recent;

    public List<RecentBean> getRecent() {
        return recent;
    }

    public void setRecent(List<RecentBean> recent) {
        this.recent = recent;
    }

    public static class RecentBean {
        /**
         * news_id : 3748552
         * url : http://daily.zhihu.com/api/2/news/3748552
         * thumbnail : http://p3.zhimg.com/67/6a/676a8337efec71a100eea6130482091b.jpg
         * title : 长得漂亮能力出众性格单纯的姑娘为什么会没有男朋友？
         */

        private int news_id;
        private String url;
        private String thumbnail;
        private String title;
        private boolean readState;

        public boolean getReadState() {
            return readState;
        }

        public void setReadState(boolean readState) {
            this.readState = readState;
        }

        public int getNews_id() {
            return news_id;
        }

        public void setNews_id(int news_id) {
            this.news_id = news_id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
