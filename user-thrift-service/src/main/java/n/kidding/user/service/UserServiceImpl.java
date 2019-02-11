package n.kidding.user.service;

import org.apache.thrift.TException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import n.kidding.thrift.user.UserInfo;
import n.kidding.thrift.user.UserService;
import n.kidding.user.mapper.UserMapper;

@Service
public class UserServiceImpl implements UserService.Iface {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserInfo getUserById(int id) throws TException {
        return userMapper.getUserById(id);
    }

    @Override
    public UserInfo getTeacherById(int id) throws TException {
        return userMapper.getTeacherById(id);
    }

    @Override
    public UserInfo getUserByName(String username) throws TException {
        return userMapper.getUserByName(username);
    }

    @Override
    public void regiserUser(UserInfo userInfo) throws TException {
        userMapper.registerUser(userInfo);
    }
}
