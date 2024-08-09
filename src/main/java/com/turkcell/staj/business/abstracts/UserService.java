package com.turkcell.staj.business.abstracts;

import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import com.turkcell.staj.entities.User;

public interface UserService {
    ResponseAddUserDTO addUser(RequestAddUserDTO requestAddUserDTO);
    ResponseUpdateUserDTO updateUser(int userId, RequestUpdateUserDTO requestUpdateUserDTO);
    ResponseGetUserDTO getUser(int userId);
    User getUserById(int userId);

}
