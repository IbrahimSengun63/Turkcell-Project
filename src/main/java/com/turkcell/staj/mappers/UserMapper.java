package com.turkcell.staj.mappers;


import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseGetUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import com.turkcell.staj.entities.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User requestAddUserDtoToUser(RequestAddUserDTO requestAddUserDTO);

    @Mapping(source = "id", target = "userId")
    ResponseAddUserDTO userToResponseAddUserDto(User user);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequestUpdateUserDTO(RequestUpdateUserDTO requestUpdateUserDTO, @MappingTarget User user);

    @Mapping(source = "id", target = "userId")
    ResponseGetUserDTO userToResponseGetUserDto(User user);

    @Mapping(source = "id", target = "userId")
    ResponseUpdateUserDTO userToResponseUpdateUserDto(User user);
}
