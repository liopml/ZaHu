package com.wsy.geeknewstest.model.bean.zhihu;

import java.util.List;

/**
 * Created by hasee on 2016/11/9.
 */

public class SectionListBean {

    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 1
         * thumbnail : http://p2.zhimg.com/10/b8/10b8193dd6a3404d31b2c50e1e232c87.jpg
         * name : 深夜食堂
         * description : 睡前宵夜，用别人的故事下酒
         */

        private int id;
        private String thumbnail;
        private String name;
        private String description;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }
}
