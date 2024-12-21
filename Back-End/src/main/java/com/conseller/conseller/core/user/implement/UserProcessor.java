package com.conseller.conseller.core.user.implement;

import com.conseller.conseller.core.user.domain.User;
import org.springframework.stereotype.Component;

@Component
public class UserProcessor {
    public String geneatePartialId(User user) {
        StringBuilder partialEncodeId = new StringBuilder();
        int length = user.getUserId().length();

        partialEncodeId.append("*".repeat(length / 2));
        partialEncodeId.append(user.getUserId().substring(length / 2));

        return partialEncodeId.toString();
    }
}
