package com.turkcell.staj.business.concretes;

import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.GetResponseUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.UserMapper;
import com.turkcell.staj.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserManager implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public ResponseAddUserDTO addUser(RequestAddUserDTO requestAddUserDTO) {
        User user = this.userMapper.requestAddUserDtoToUser(requestAddUserDTO);
        User savedUser = this.userRepository.save(user);
        return this.userMapper.userToResponseAddUserDto(savedUser);
    }

    @Override
    public ResponseUpdateUserDTO updateUser(int userId, RequestUpdateUserDTO requestUpdateUserDTO) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User doesn't exist"));

        this.userMapper.updateUserFromRequestUpdateUserDTO(requestUpdateUserDTO, user);

        User updatedUser = this.userRepository.save(user);

        return this.userMapper.userToResponseUpdateUserDto(updatedUser);
    }

    @Override
    public GetResponseUserDTO getUserById(int userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User doesn't exist"));

        return this.userMapper.userToResponseGetUserDto(user);
    }

    @Override
    public void deleteUser(int userId) {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException("User doesn't exist"));

        this.userRepository.delete(user);
    }
}
