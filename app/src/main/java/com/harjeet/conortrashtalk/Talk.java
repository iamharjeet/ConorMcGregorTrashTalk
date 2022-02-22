package com.harjeet.conortrashtalk;

public class Talk {
    private String title;
    private String desc;
    private int url;

    public Talk(String title, String desc, int url) {
        this.title = title;
        this.desc = desc;
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }


    public int getUrl() {
        return url;
    }

}
