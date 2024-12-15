package com.conceptile.mapper;

import com.conceptile.dto.response.UserDTO;
import com.conceptile.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FlowChartMgmtGlobalMapper {
    UserDTO fromUserEntityToUserDTO(User userEntity);
}
