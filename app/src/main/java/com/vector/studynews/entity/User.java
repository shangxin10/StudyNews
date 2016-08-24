
package com.vector.studynews.entity;
/**
 * Created by zhang on 2016/8/23.
 */

import com.hyphenate.chat.EMContact;
import com.vector.studynews.utils.CommonUtils;

public class User extends EMContact {

    /**
     * initial letter for nickname
     */
    protected String initialLetter;
    /**
     * avatar of the user
     */
    protected String avatar;

    public User(String username){
        this.username = username;
    }

    public String getInitialLetter() {
        if(initialLetter == null){
            CommonUtils.setUserInitialLetter(this);
        }
        return initialLetter;
    }

    public void setInitialLetter(String initialLetter) {
        this.initialLetter = initialLetter;
    }


    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int hashCode() {
        return 17 * getUsername().hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !(o instanceof User)) {
            return false;
        }
        return getUsername().equals(((User) o).getUsername());
    }

    @Override
    public String toString() {
        return nick == null ? username : nick;
    }
}

