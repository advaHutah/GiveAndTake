package com.example.win10.giveandtake.Logic;

import java.util.Map;

/**
 * Created by win10 on 12/19/2018.
 */

public class Tag {

    private String tag;
    private Map<String, Request.RequestType> tagsChildren;

    public Tag(String tag, Map<String, Request.RequestType> tagsChildren) {
        this.tag = tag;
        this.tagsChildren = tagsChildren;
    }

    public String getTag() {
        return tag;
    }

    public Map<String, Request.RequestType> getTagsChildren() {
        return tagsChildren;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public void setTagsChildren(Map<String, Request.RequestType> tagsChildren) {
        this.tagsChildren = tagsChildren;
    }

}
