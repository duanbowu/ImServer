package com.duan.imserver.service;

import com.duan.imserver.vo.UserVO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface GroupService {

    List<UserVO> getMembers(String groupId);

}
