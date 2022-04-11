package com.tkn.cloud.mall.practice.user.service.impl;


import com.tkn.cloud.mall.practice.common.exception.ImoocMallException;
import com.tkn.cloud.mall.practice.common.exception.ImoocMallExceptionEnum;
import com.tkn.cloud.mall.practice.common.util.MD5Utils;
import com.tkn.cloud.mall.practice.user.model.dao.UserMapper;
import com.tkn.cloud.mall.practice.user.model.pojo.User;
import com.tkn.cloud.mall.practice.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 描述：     UserService实现类
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Value("${icode}")
    public String icode;

    @Override
    public void register(String userName, String password) throws ImoocMallException {
        //查询用户名是否存在，不允许重名
        User result = userMapper.selectByName(userName);
        if (result != null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.NAME_EXISTED);
        }

        //写到数据库
        User user = new User();
        user.setUsername(userName);
        user.setPassword(MD5Utils.getMD5Str(password, icode));
        int count = userMapper.insertSelective(user);
        if (count == 0) {
            throw new ImoocMallException(ImoocMallExceptionEnum.INSERT_FAILED);
        }
    }

    @Override
    public User login(String userName, String password) throws ImoocMallException {
        String md5Password = null;
        md5Password = MD5Utils.getMD5Str(password, icode);
        User user = userMapper.selectLogin(userName, md5Password);
        if (user == null) {
            throw new ImoocMallException(ImoocMallExceptionEnum.WRONG_PASSWORD);
        }
        return user;
    }

    @Override
    public void updateInformation(User user) throws ImoocMallException {
        //更新个性签名
        int updateCount = userMapper.updateByPrimaryKeySelective(user);
        if (updateCount > 1) {
            throw new ImoocMallException(ImoocMallExceptionEnum.UPDATE_FAILED);
        }
    }

    @Override
    public boolean checkAdminRole(User user) {
        //1是普通用户，2是管理员，3是测试用户
        return user.getRole().equals(2);
    }
}
