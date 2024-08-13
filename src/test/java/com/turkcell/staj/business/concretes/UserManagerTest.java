package com.turkcell.staj.business.concretes;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
import com.turkcell.staj.core.exceptions.BusinessException;
import com.turkcell.staj.entities.User;
import com.turkcell.staj.mappers.UserMapper;
import com.turkcell.staj.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class UserManagerTest {

    @InjectMocks
    private UserManager userManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddUser() {
        // Arrange
        RequestAddUserDTO requestAddUserDTO = new RequestAddUserDTO();
        User user = new User();
        User savedUser = new User();
        ResponseAddUserDTO responseAddUserDTO = new ResponseAddUserDTO();

        when(userMapper.requestAddUserDtoToUser(requestAddUserDTO)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.userToResponseAddUserDto(savedUser)).thenReturn(responseAddUserDTO);

        // Act
        ResponseAddUserDTO result = userManager.addUser(requestAddUserDTO);

        // Assert
        assertEquals(responseAddUserDTO, result);
        verify(userMapper).requestAddUserDtoToUser(requestAddUserDTO);
        verify(userRepository).save(user);
        verify(userMapper).userToResponseAddUserDto(savedUser);
    }

    @Test
    void shouldUpdateUser() {
        // Arrange
        int userId = 1;
        RequestUpdateUserDTO requestUpdateUserDTO = new RequestUpdateUserDTO();
        User user = new User();
        User updatedUser = new User();
        ResponseUpdateUserDTO responseUpdateUserDTO = new ResponseUpdateUserDTO();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(userRepository.save(user)).thenReturn(updatedUser);
        when(userMapper.userToResponseUpdateUserDto(updatedUser)).thenReturn(responseUpdateUserDTO);

        // Act
        ResponseUpdateUserDTO result = userManager.updateUser(userId, requestUpdateUserDTO);

        // Assert
        assertEquals(responseUpdateUserDTO, result);
        verify(userMapper).updateUserFromRequestUpdateUserDTO(requestUpdateUserDTO, user);
        verify(userRepository).save(user);
    }

    @Test
    void shouldUpdateUserUserNotFound() {
        // Arrange
        int userId = 1;
        RequestUpdateUserDTO requestUpdateUserDTO = new RequestUpdateUserDTO();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userManager.updateUser(userId, requestUpdateUserDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldGetUser() {
        // Arrange
        int userId = 1;
        User user = new User();
        ResponseGetUserDTO responseGetUserDTO = new ResponseGetUserDTO();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));
        when(userMapper.userToResponseGetUserDto(user)).thenReturn(responseGetUserDTO);

        // Act
        ResponseGetUserDTO result = userManager.getUser(userId);

        // Assert
        assertEquals(responseGetUserDTO, result);
        verify(userRepository).findById(userId);
        verify(userMapper).userToResponseGetUserDto(user);
    }

    @Test
    void shouldGetUserUserNotFound() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userManager.getUser(userId));
        verify(userRepository).findById(userId);
        verify(userMapper, never()).userToResponseGetUserDto(any());
    }

    @Test
    void shouldGetUserById() {
        // Arrange
        int userId = 1;
        User user = new User();

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(user));

        // Act
        User result = userManager.getUserById(userId);

        // Assert
        assertEquals(user, result);
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldGetUserByIdUserNotFound() {
        // Arrange
        int userId = 1;

        when(userRepository.findById(userId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(BusinessException.class, () -> userManager.getUserById(userId));
        verify(userRepository).findById(userId);
    }

    @Test
    void shouldSaveUser() {
        // Arrange
        User user = new User();

        // Act
        userManager.saveUser(user);

        // Assert
        verify(userRepository).save(user);
    }
}
