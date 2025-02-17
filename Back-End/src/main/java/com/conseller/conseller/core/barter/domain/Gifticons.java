package com.conseller.conseller.core.barter.domain;

import com.conseller.conseller.core.gifticon.domain.Gifticon;
import com.conseller.conseller.core.user.domain.User;

import java.util.List;

public interface Gifticons {
    public List<Gifticon> getGifticons();
    public void deleteAll();
    public void modifyUserAll(User suer);
}
