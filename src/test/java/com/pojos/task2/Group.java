package com.pojos.task2;

import java.util.HashMap;
import java.util.List;

public class Group {
    String groupName;
    String groupId;
    HashMap<String,String> ResourceList;

    public Group() {
        ResourceList = new HashMap<>();
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public HashMap<String, String> getResourceList() {
        return ResourceList;
    }

    public void setResourceList(HashMap<String, String> resourceList) {
        ResourceList = resourceList;
    }

    @Override
    public String toString() {
        return "Group{" +
                "groupName='" + groupName + '\'' +
                ", groupId='" + groupId + '\'' +
                ", ResourceList=" + ResourceList +
                '}';
    }
}
