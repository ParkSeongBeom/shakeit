package kr.co.mythings.iot.api.user;

import kr.co.mythings.iot.core.common.ApiStatusCode;
import kr.co.mythings.iot.core.common.RootResponse;
import kr.co.mythings.iot.core.common.exception.UnAuthorizedException;
import kr.co.mythings.iot.core.db.entity.UserEntity;
import kr.co.mythings.iot.core.db.mapper.AreaMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private AreaMapper areaMapper;

    @PostMapping("/login")
    public RootResponse loginUser(
            @Valid @RequestBody LoginUserRequest loginUserRequest
    ) {
        LoginInfo loginInfo =  userService.loginUser(loginUserRequest);
        return new RootResponse<>(ApiStatusCode.OK, loginInfo);
    }

    @PostMapping("/valid")
    public RootResponse validUser(
            @RequestHeader String authorization
    ) {
        String token = authorization.split(" ")[1];
        LoginInfo loginInfo = userService.validUser(token);
        return new RootResponse<>(ApiStatusCode.OK, loginInfo);
    }

    @PostMapping("/list")
    public RootResponse userList(
            @Valid @RequestBody ListUserRequest request
    ) {
        return new RootResponse<>(ApiStatusCode.OK, userService.getUserList(request.getUserGroupIdx()));
    }

    @PostMapping("/notExistGroupList")
    public RootResponse notExistGroupList(
    ) {
        return new RootResponse<>(ApiStatusCode.OK, userService.getUserList(null));
    }

    @PostMapping("/add")
    public RootResponse addUser(
            @RequestAttribute(value = "requestUserEntity") UserEntity requestUserEntity
            , @Valid @RequestBody AddUserRequest request
    ) {
        if(requestUserEntity.getRoleIdx()!=1){
            if(request.getRoleIdx()==1 || requestUserEntity.getRoleIdx() < request.getRoleIdx()){
                throw new UnAuthorizedException(ApiStatusCode.UNAUTHORIZED_REQUEST, "권한이 없습니다.");
            }
        }
        userService.addUser(requestUserEntity.getUserIdx(), request);
        return new RootResponse<>(ApiStatusCode.OK, userService.getUserList(request.getUserGroupIdx()));
    }

    @PostMapping("/edit")
    public RootResponse editUser(
            @RequestAttribute(value = "requestUserEntity") UserEntity requestUserEntity
            , @Valid @RequestBody EditUserRequest request
    ) {
        if(requestUserEntity.getRoleIdx()!=1){
            if(request.getRoleIdx()==1 || requestUserEntity.getRoleIdx() < request.getRoleIdx()){
                throw new UnAuthorizedException(ApiStatusCode.UNAUTHORIZED_REQUEST, "권한이 없습니다.");
            }
        }
        userService.editUser(requestUserEntity.getUserIdx(), request);
        return new RootResponse<>(ApiStatusCode.OK, userService.getUserList(request.getUserGroupIdx()));
    }

    @PostMapping("/delete")
    public RootResponse deleteUser(
            @RequestAttribute(value = "requestUserEntity") UserEntity requestUserEntity
            , @Valid @RequestBody DeleteUserRequest request
    ) {
        UserEntity userEntity = userService.getUserByIdx(request.getUserIdx());
        if (userEntity == null) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_ID, "Invalid ID");
        }

        if(requestUserEntity.getRoleIdx()!=1){
            if(userEntity.getRoleIdx()==1 || requestUserEntity.getRoleIdx() < userEntity.getRoleIdx()){
                throw new UnAuthorizedException(ApiStatusCode.UNAUTHORIZED_REQUEST, "권한이 없습니다.");
            }
        }
        userService.deleteUser(requestUserEntity.getUserIdx(), request);
        return new RootResponse<>(ApiStatusCode.OK, userService.getUserList(userEntity.getUserGroupIdx()));
    }

//    @PostMapping("/find-id")
//    public RootResponse findId(
//            @Valid @RequestBody FindIdRequest findIdRequest
//    ) {
//        return RootApiUtil.findId(findIdRequest);
//    }
//
//    @PostMapping("/find-password")
//    public RootResponse findPassword(
//            @Valid @RequestBody FindPasswordRequest findPasswordRequest
//    ) {
//        return RootApiUtil.findPassword(findPasswordRequest);
//    }

//    @PostMapping("/check/duplicate-id")
//    public RootResponse checkDuplicateId(
//            @Valid @RequestBody CheckDuplicateIdRequest checkDuplicateIdRequest
//    ) {
//        boolean isDuplicate = userService.checkDuplicateId(checkDuplicateIdRequest.getUserId());
//        return new RootResponse<>(ApiStatusCode.OK, isDuplicate);
//    }
//
//    @PostMapping("/signup")
//    public RootResponse signupUser(
//            @RequestHeader String authorization
//            , @RequestAttribute(value = "userInfo") UserResponse.UserInfo userInfo
//            , @Valid @RequestBody AddUserRequest addUserRequest
//    ) {
////        if (UserUtil.isNotAdmin(userInfo)) {
////            throw new ApiException(ApiStatusCode.UNAUTHORIZED_REQUEST, "");
////        }
//
//        int userRoleIdx = addUserRequest.getUserRoleIdx();
//        if ((userRoleIdx == 2 || userRoleIdx == 3) == false) {
//            throw new ApiException(ApiStatusCode.BAD_REQUEST, "user_role_idx is wrong");
//        }
//
//        addUserRequest.setUserEmail(addUserRequest.getUserId());
//        UserResponse userResponse = userService.addUser(userInfo.getUserIdx(), addUserRequest);
//        Integer userIdx = userResponse.getUserInfo().getUserIdx();
//
//        AddUserProjectRequest addUserProjectRequest = AddUserProjectRequest.builder()
//                .userIdx(userIdx)
//                .serviceIdx(2)
//                .serviceName("hsgreen")
//                .areaIdx(0)
//                .areaName("서비스관리")
//                .userRoleIdx(userRoleIdx)
//                .userRoleName(userRoleIdx == 2 ? "운영자" : "사용자")
//                .build();
//        userService.addUserService(userInfo.getUserIdx(), addUserProjectRequest);
//        return new RootResponse<>(ApiStatusCode.OK, userResponse);
//    }
//
//    @PostMapping("/edit")
//    public RootResponse editUser(
//            @RequestAttribute("userEntity") UserEntity requestUserEntity
//            , @Valid @RequestBody EditUserByAdminRequest editUserByAdminRequest
//    ) {
//        // TODO : 권한
//        UserResponse userResponse = userService.editUserByAdmin(requestUserEntity, editUserByAdminRequest);
//        UserResponse.UserInfo userInfo = userResponse.getUserInfo();
//        List<UserResponse.UserInfo.UserServiceInfo> userServiceInfoList = userInfo.getUserServiceInfoList();
//        userServiceInfoList.removeIf(userServiceInfo -> userServiceInfo.getServiceIdx() != 2);
//        Iterator<UserResponse.UserInfo.UserServiceInfo> iter = userServiceInfoList.iterator();
//        while (iter.hasNext()) {
//            var userServiceInfo = iter.next(); // must be called before you can call i.remove()
//
//            Integer areaIdx = userServiceInfo.getAreaIdx();
//            if (areaIdx > 0) {
//                AreaEntity areaEntity = areaMapper.findByAreaId(areaIdx);
//                if (areaEntity == null) {
//                    iter.remove();
//                } else {
//                    userServiceInfo.setAreaName(areaEntity.getAreaName());
//                }
//            }
//        }
//        List<UserResponse.UserInfo.UserServiceInfo> list = userServiceInfoList.stream()
//                .sorted(Comparator.comparing(UserResponse.UserInfo.UserServiceInfo::getAreaName))
//                .collect(Collectors.toList());
//        userInfo.setUserServiceInfoList(list);
//        return new RootResponse<>(ApiStatusCode.OK, userResponse);
//    }
//
//    @PostMapping("/delete")
//    public RootResponse deleteUser(
//            @RequestAttribute("userEntity") UserEntity requestUserEntity
//            , @Valid @RequestBody DeleteUserRequest deleteUserRequest
//    ) {
//        // TODO : 권한체크
//        userService.clearUserService(requestUserEntity, deleteUserRequest);
//        return new RootResponse<>(ApiStatusCode.OK, null);
//    }
//
//    @GetMapping("/list")
//    public RootResponse userList(
//            @RequestAttribute("userEntity") UserEntity requestUserEntity
//            , @RequestAttribute(value = "userInfo") UserResponse.UserInfo userInfo
//            , @RequestParam(value = "page", required = false) Integer page
//            , @RequestParam(value = "limit", required = false) Integer limit
//    ) {
//        if (UserUtil.isNotOperator(userInfo)) {
//            throw new ApiException(ApiStatusCode.UNAUTHORIZED_REQUEST, "");
//        }
//
//        Integer serviceIdx = 2; // 한설그린 service_idx = 2 고정.
//        List<Integer> serviceIdxList = Lists.newArrayList();
//        serviceIdxList.add(serviceIdx);
//        PageInfo pageInfo = PageUtil.setPageParam(page, limit);
//        List<UserResponse.UserInfo> userInfoList = userService.getUserList(requestUserEntity, serviceIdxList, pageInfo);
//        userInfoList.removeIf(info -> UserUtil.isNotAdmin(info) == false);
//        for (var info : userInfoList) {
//            List<UserResponse.UserInfo.UserServiceInfo> userServiceInfoList = info.getUserServiceInfoList();
//            userServiceInfoList.removeIf(userServiceInfo -> userServiceInfo.getServiceIdx() != 2);
//            for (var userServiceInfo : userServiceInfoList) {
//                Integer areaIdx = userServiceInfo.getAreaIdx();
//                if (areaIdx > 0) {
//                    AreaEntity areaEntity = areaMapper.findByAreaId(areaIdx);
//                    if (areaEntity == null) {
//                        userServiceInfoList.remove(userServiceInfo);
//                    } else {
//                        userServiceInfo.setAreaName(areaEntity.getAreaName());
//                        // TODO : Project Status 추가.
//                        userServiceInfo.setStatus(statusService.getAreaStatus(areaIdx));
//                    }
//                }
//            }
//            if (userServiceInfoList.size() == 0) {
//                userInfoList.remove(info);
//            } else {
//                List<UserResponse.UserInfo.UserServiceInfo> list = userServiceInfoList.stream()
//                        .sorted(Comparator.comparing(UserResponse.UserInfo.UserServiceInfo::getAreaName))
//                        .collect(Collectors.toList());
//                info.setUserServiceInfoList(list);
//            }
//        }
//
//        userInfoList.sort((o1, o2) -> {
//            var o1UserServiceInfo = o1.getUserServiceInfoList().stream()
//                    .filter(userServiceInfo -> userServiceInfo.getAreaIdx() == 0)
//                    .findFirst()
//                    .get();
//            var o2UserServiceInfo = o2.getUserServiceInfoList().stream()
//                    .filter(userServiceInfo -> userServiceInfo.getAreaIdx() == 0)
//                    .findFirst()
//                    .get();
//            return o1UserServiceInfo.getUserRoleIdx().compareTo(o2UserServiceInfo.getUserRoleIdx());
//        });
//
//        return new RootResponse<>(ApiStatusCode.OK, userInfoList, pageInfo);
//    }
}
