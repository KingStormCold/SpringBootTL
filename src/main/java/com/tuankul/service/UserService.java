package com.tuankul.service;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.tuankul.entities.UserEntityJWT;

@Service
public class UserService {
    public static List<UserEntityJWT> listUser = new ArrayList<UserEntityJWT>();
    static {
        UserEntityJWT userKai = new UserEntityJWT(1, "kai", "123456");
        userKai.setRoles(new String[] { "ROLE_ADMIN" });
        UserEntityJWT userSena = new UserEntityJWT(2, "sena", "123456");
        userSena.setRoles(new String[] { "ROLE_USER" });
        listUser.add(userKai);
        listUser.add(userSena);
    }

    public List<UserEntityJWT> findAll() {
        return listUser;
    }

    public UserEntityJWT findById(int id) {
        for (UserEntityJWT user : listUser) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    public boolean add(UserEntityJWT user) {
        for (UserEntityJWT userExist : listUser) {
            if (user.getId() == userExist.getId() || StringUtils
                    .equals(user.getUsername(), userExist.getUsername())) {
                return false;
            }
        }
        listUser.add(user);
        return true;
    }

    public void delete(int id) {
        listUser.removeIf(user -> user.getId() == id);
    }

    public UserEntityJWT loadUserByUsername(String username) {
        for (UserEntityJWT user : listUser) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean checkLogin(UserEntityJWT user) {
        for (UserEntityJWT userExist : listUser) {
            if (StringUtils.equals(user.getUsername(), userExist.getUsername())
                    && StringUtils.equals(user.getPassword(),
                            userExist.getPassword())) {
                return true;
            }
        }
        return false;
    }
}
