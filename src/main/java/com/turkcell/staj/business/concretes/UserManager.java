package com.turkcell.staj.business.concretes;


import com.turkcell.staj.business.abstracts.UserService;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
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
            User user = userMapper.requestAddUserDtoToUser(requestAddUserDTO);
            User savedUser = userRepository.save(user);
            return userMapper.userToResponseAddUserDto(savedUser);
        }

        @Override
        public ResponseUpdateUserDTO updateUser(int userId, RequestUpdateUserDTO requestUpdateUserDTO) {
            User user = getUserById(userId);
            userMapper.updateUserFromRequestUpdateUserDTO(requestUpdateUserDTO, user);
            User updatedUser = userRepository.save(user);
            return userMapper.userToResponseUpdateUserDto(updatedUser);
        }

        @Override
        public ResponseGetUserDTO getUser(int userId) {
            User user = getUserById(userId);
            return userMapper.userToResponseGetUserDto(user);
        }

        @Override
        public User getUserById(int userId) {
            return userRepository.findById(userId).orElseThrow(()-> new BusinessException("User can't be null"));
        }

        @Override
        public void saveUser(User user) {
            userRepository.save(user);
        }

    }
