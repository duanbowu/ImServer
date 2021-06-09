package com.duan.imserver.service.impl;

import com.duan.imserver.service.GroupService;
import com.duan.imserver.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class GroupServiceImpl implements GroupService {

    @Override
    public List<UserVO> getMembers(String groupId) {
        List<UserVO> members = new ArrayList<>();
        HashMap<String, UserVO> users = new HashMap<>();
        UserVO user00 = new UserVO();
        user00.setUserId("user00");
        user00.setUserName("段二");
        users.put(user00.getUserId(), user00);
        UserVO user01 = new UserVO();
        user01.setUserId("user01");
        user01.setUserName("张三");
        users.put(user01.getUserId(), user01);
        UserVO user02 = new UserVO();
        user02.setUserId("user02");
        user02.setUserName("李四");
        users.put(user02.getUserId(), user02);
        UserVO user03 = new UserVO();
        user03.setUserId("user03");
        user03.setUserName("王五");
        users.put(user03.getUserId(), user03);

        if (groupId.equals("group01")) {
            members.add(users.get("user00"));
            members.add(users.get("user01"));
            members.add(users.get("user02"));
            members.add(users.get("user03"));
        } else if (groupId.equals("group02")) {
            members.add(users.get("user01"));
            members.add(users.get("user02"));
            members.add(users.get("user03"));
        } else {
            members.add(users.get("user02"));
            members.add(users.get("user03"));
        }

        return members;
    }
}
