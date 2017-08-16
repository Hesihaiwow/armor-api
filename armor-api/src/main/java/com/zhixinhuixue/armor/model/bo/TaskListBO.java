package com.zhixinhuixue.armor.model.bo;

import com.zhixinhuixue.armor.model.pojo.Tag;
import com.zhixinhuixue.armor.model.pojo.Task;

import java.util.List;

/**
 * Created by Tate on 2017/8/15.
 */
public class TaskListBO extends Task {

    private List<Tag> tags;

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
