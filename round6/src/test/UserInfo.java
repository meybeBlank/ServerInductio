package test;

import annotation.DBField;

import java.io.Serializable;

/**
 * 用户信息
 *
 * @author fengzhen
 * @version v1.0, 2017/9/14 10:57
 */
public class UserInfo implements Serializable {

    @DBField(value = {"USER_NAME","user_name"})
    private String userName;

    @DBField(value = {"ARCHIVE_ID","archive_id"})
    private String archiveID;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getArchiveID() {
        return archiveID;
    }

    public void setArchiveID(String archiveID) {
        this.archiveID = archiveID;
    }

    @Override
    public String toString() {
        return "bean.test.UserInfo{" +
                "userName='" + userName + '\'' +
                ", archiveID='" + archiveID + '\'' +
                '}';
    }
}
