package com.turkcell.staj.business.abstracts;


import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.GetResponseUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;

public interface UserService {
    ResponseAddUserDTO addUser(RequestAddUserDTO requestAddUserDTO);
    ResponseUpdateUserDTO updateUser(int userId, RequestUpdateUserDTO requestUpdateUserDTO);
    GetResponseUserDTO getUserById(int userId);
    void deleteUser(int userId);
    ResponseAddUserDTO updateUser(int id, RequestAddUserDTO requestUpdateUserDTO);
}
