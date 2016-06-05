package com.testing.simplesp.domain;

import java.util.List;

public class DocumentItem {

    private List<DocumentItem.Data> data;
    private boolean flag;

    public static class Data{
        private   int id;
        private  String title;
        private  String unit;
        private  String time;
        private  String content;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<DocumentItem.Data> getData() {
        return data;
    }

    public void setData(List<DocumentItem.Data> data) {
        this.data = data;
    }


}