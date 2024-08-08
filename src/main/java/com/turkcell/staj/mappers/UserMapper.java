package com.turkcell.staj.mappers;


import com.turkcell.staj.dtos.user.requests.RequestAddUserDTO;
import com.turkcell.staj.dtos.user.requests.RequestUpdateUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseAddUserDTO;
import com.turkcell.staj.dtos.user.responses.GetResponseUserDTO;
import com.turkcell.staj.dtos.user.responses.ResponseUpdateUserDTO;
import com.turkcell.staj.entities.User;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "userId", ignore = true)  // userId otomatik oluşturulacak, bu yüzden ignore ettik.
    User requestAddUserDtoToUser(RequestAddUserDTO requestAddUserDTO);

    @Mapping(source = "userId", target = "userId")
    ResponseAddUserDTO userToResponseAddUserDto(User user);

    @Mapping(source = "userId", target = "userId")
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromRequestUpdateUserDTO(RequestUpdateUserDTO requestUpdateUserDTO, @MappingTarget User user);

    @Mapping(source = "userId", target = "userId")
    GetResponseUserDTO userToResponseGetUserDto(User user);

    @Mapping(source = "userId", target = "userId")
    ResponseUpdateUserDTO userToResponseUpdateUserDto(User user);
}
