package com.conceptile.service.innterfaceLayer;

import com.conceptile.dto.request.RegisterUserRequest;
import com.conceptile.dto.response.FlowchartDTO;
import com.conceptile.dto.response.UserDTO;
import com.conceptile.exception.FlowChartMgmtException;
import com.conceptile.exception.NoDataFoundException;

import java.util.List;

public interface UserService {
    UserDTO registerUser(RegisterUserRequest request) throws FlowChartMgmtException, IllegalArgumentException;

    UserDTO findUserUsingEmail(String email) throws NoDataFoundException;

    UserDTO findUserUsingId(Long userId) throws NoDataFoundException;

    void deleterUserAndCorrespondingFlowchartData(Long userId);

    List<FlowchartDTO> getAllFlowchartsForUser(Long userId) throws NoDataFoundException;
}
