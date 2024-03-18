package com.info_hub.services.auth;

import com.info_hub.components.GetPageableUtil;
import com.info_hub.constant.EnvironmentConstant;
import com.info_hub.dtos.ResponseMessage;
import com.info_hub.exceptions.BadRequestException;
import com.info_hub.models.Role;
import com.info_hub.models.User;
import com.info_hub.repositories.RoleRepository;
import com.info_hub.repositories.user.UserRepository;
import com.info_hub.dtos.responses.SimpleResponse;
import com.info_hub.dtos.responses.user.UserDetailDTO;
import com.info_hub.dtos.responses.user.UserListResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final RoleRepository roleRepository;

    /**
     * Find by condition
     *
     * @param params: email, status, full_name, role_code
     * @return list user
     */
    public SimpleResponse<UserListResponse> getAllUser(Map<String, String> params) {
        Pageable pageable = GetPageableUtil.getPageable(params);
        SimpleResponse<User> entitiesResponse = userRepository.findByCondition(params, pageable);

        List<UserListResponse> results = new ArrayList<>();
        for (User entity : entitiesResponse.getData()) {
            UserListResponse item = convertEntityToDTO(entity);
            results.add(item);
        }

        return SimpleResponse.<UserListResponse>builder()
                .data(results)
                .page(entitiesResponse.getPage())
                .limit(entitiesResponse.getLimit())
                .totalItems(entitiesResponse.getTotalItems())
                .totalPage(entitiesResponse.getTotalPage())
                .build();
    }

    private UserListResponse convertEntityToDTO(User entity) {
        return UserListResponse.builder()
                .id(entity.getId())
                .email(entity.getEmail())
                .fullName(entity.getFullName())
                .roleName(entity.getRole().getName())
                .isEnable(entity.isEnabled())
                .build();
    }

    public UserDetailDTO getUserById(Integer userId) {
        User exsistingUser = findUserById(userId);
        return modelMapper.map(exsistingUser, UserDetailDTO.class);
    }

    public ResponseMessage updateUserById(UserDetailDTO params, Integer userId) {
        // if not exist throw an exception
        User existingUser = findUserById(userId);
        existingUser.setFullName(params.getFullName());

//        String userEmail = existingUser.getEmail();
//        if (!params.getEmail().equals(userEmail)) {
//            boolean isExistEmail = userRepository.existsByEmail(params.getEmail());
//            if (!isExistEmail) {
//                existingUser.setEmail(params.getEmail());
//            } else {
//                throw new BadRequestException("An email existed in another user");
//            }
//        }
        existingUser.setEnabled(params.isEnabled());

        // not allow to update role admin.
        if (params.getRoleId() != EnvironmentConstant.ROLE_ADMIN_ID) {
            Optional<Role> role = roleRepository.findById(params.getRoleId());
            existingUser.setRole(role.get());
        }

        // update
        userRepository.save(existingUser);
        return ResponseMessage.success();
    }

    private User findUserById(Integer userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found with id: " + userId));
    }


}
