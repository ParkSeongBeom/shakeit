package kr.co.mythings.iot.api.user;

import kr.co.mythings.iot.core.common.ApiStatusCode;
import kr.co.mythings.iot.core.common.exception.ApiException;
import kr.co.mythings.iot.core.common.exception.UnAuthorizedException;
import kr.co.mythings.iot.core.db.entity.LoginDeviceEntity;
import kr.co.mythings.iot.core.db.entity.LoginDeviceTypeEntity;
import kr.co.mythings.iot.core.db.entity.UserAreaEntity;
import kr.co.mythings.iot.core.db.entity.UserEntity;
import kr.co.mythings.iot.core.db.mapper.*;
import kr.co.mythings.iot.core.util.CryptoUtil;
import kr.co.mythings.iot.core.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.utils.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private LoginDeviceMapper loginDeviceMapper;
    @Autowired
    private LoginDeviceTypeMapper loginDeviceTypeMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAreaMapper userAreaMapper;
    @Autowired
    private UserGroupMapper userGroupMapper;

//    @Autowired
//    private EmailUtil emailUtil;

//    public UserResponse addUser(Integer userIdx, AddUserRequest addUserRequest) {
//
//        String userId = addUserRequest.getUserId();
//        String salt = CryptoUtil.makePasswordSalt();
//        String encrypedPassword = CryptoUtil.makeSHA3_256String(salt + addUserRequest.getUserPassword());
//
//        UserEntity userEntity = UserEntity.builder()
//                .createUserIdx(userIdx)
//                .createAt(new Date())
//                .delYn("N")
//                .userId(userId)
//                .userPassword(encrypedPassword)
//                .passwordSalt(salt)
//                .userName(addUserRequest.getUserName())
//                .userEmail(addUserRequest.getUserEmail())
//                .userEmailAuthYn("N")
//                .userPhone(addUserRequest.getUserPhone())
////                .userAddress(addUserRequest.getUserAddress())
//                .build();
//        userMapper.insertUser(userEntity);
//
//        UserResponse.UserInfo userInfo = UserResponse.UserInfo.builder()
//                .userIdx(userEntity.getUserIdx())
//                .userName(userEntity.getUserName())
//                .userEmail(userEntity.getUserEmail())
//                .userPhone(userEntity.getUserPhone())
//                .userAddress(userEntity.getUserAddress())
//                .build();
//
//        return UserResponse.builder()
//                .loginToken(null)
//                .userInfo(userInfo)
//                .build();
//    }
//
//    public UserResponse editUserByAdmin(UserEntity requestUserEntity, EditUserByAdminRequest editUserByAdminRequest) {
//        String userPassword = editUserByAdminRequest.getUserPassword();
//
//        String salt = null;
//        String encrypedPassword = null;
//        if (userPassword != null) {
//            salt = CryptoUtil.makePasswordSalt();
//            encrypedPassword = CryptoUtil.makeSHA3_256String(salt + userPassword);
//        }
//
//        // Check Role
//        Integer serviceIdx = editUserByAdminRequest.getServiceIdx();
//        UserServiceEntity requestUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, requestUserEntity.getUserIdx());
//        UserServiceEntity targetUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, editUserByAdminRequest.getUserIdx());
//        Integer requestUserRoleIdx = requestUserServuceEntity.getUserRoleIdx();
//        Integer targetUserRoleIdx = targetUserServuceEntity.getUserRoleIdx();
//        if (requestUserRoleIdx > targetUserRoleIdx) {
//            throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//        }
//
//        UserEntity updateUserEntity = UserEntity.builder()
//                .userIdx(editUserByAdminRequest.getUserIdx())
//                .updateAt(new Date())
//                .updateUserIdx(editUserByAdminRequest.getUserIdx())
//                .userPassword(encrypedPassword)
//                .passwordSalt(salt)
//                .userName(editUserByAdminRequest.getUserName())
//                .userEmail(editUserByAdminRequest.getUserEmail())
//                .userEmailAuthYn(editUserByAdminRequest.getUserEmail() != null ? "N" : null)
//                .userPhone(editUserByAdminRequest.getUserPhone())
////                .userAddress(editUserByAdminRequest.getUserAddress())
//                .build();
//        userMapper.updateUser(updateUserEntity);
//
//        UserEntity userEntity = userMapper.findByUserIdx(editUserByAdminRequest.getUserIdx());
//        List<UserServiceEntity> userServiceEntityList = userServiceMapper.findListByUserIdx(userEntity.getUserIdx());
//
//        return UserResponseUtil.makeUserResponse(null, userEntity, userServiceEntityList);
//    }
//
//    public UserResponse editUser(Integer userIdx, EditUserRequest editUserRequest) {
//        String userPassword = editUserRequest.getUserPassword();
//
//        String salt = null;
//        String encrypedPassword = null;
//        if (userPassword != null) {
//            salt = CryptoUtil.makePasswordSalt();
//            encrypedPassword = CryptoUtil.makeSHA3_256String(salt + userPassword);
//        }
//
//        UserEntity updateUserEntity = UserEntity.builder()
//                .userIdx(userIdx)
//                .updateAt(new Date())
//                .updateUserIdx(userIdx)
//                .userPassword(encrypedPassword)
//                .passwordSalt(salt)
//                .userName(editUserRequest.getUserName())
//                .userEmail(editUserRequest.getUserEmail())
//                .userEmailAuthYn(editUserRequest.getUserEmail() != null ? "N" : null)
//                .userPhone(editUserRequest.getUserPhone())
////                .userAddress(editUserRequest.getUserAddress())
//                .build();
//        userMapper.updateUser(updateUserEntity);
//
//        UserEntity userEntity = userMapper.findByUserIdx(userIdx);
//        List<UserServiceEntity> userServiceEntityList = userServiceMapper.findListByUserIdx(userEntity.getUserIdx());
//
//        return UserResponseUtil.makeUserResponse(null, userEntity, userServiceEntityList);
//    }

    public LoginInfo loginUser(LoginUserRequest loginUserRequest) {
        String userId = loginUserRequest.getUserId();
        String userPassword = loginUserRequest.getUserPassword();
        String deviceUuid = loginUserRequest.getLoginDevice().getDeviceUuid();
        String deviceTypeName = loginUserRequest.getLoginDevice().getDeviceType();

        UserEntity userEntity = userMapper.findByUserId(userId);
        if (userEntity == null) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_ID, "Invalid ID");
        }

        String encryptedPassword = CryptoUtil.makeSHA3_256String(userEntity.getPasswordSalt() + userPassword);
        if (Objects.equals(encryptedPassword, userEntity.getUserPassword()) == false) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_PASSWORD, "Invalid Password");
        }

        LoginDeviceTypeEntity loginDeviceTypeEntity = loginDeviceTypeMapper.findByName(deviceTypeName);
        if (loginDeviceTypeEntity == null) {
            throw new ApiException(ApiStatusCode.BAD_REQUEST, "device_type is mismatch");
        }

        String loginToken = JWTUtil.makeLoginToken(userId, deviceUuid);

        LoginDeviceEntity existLoginDeviceEntity = loginDeviceMapper.findByDeviceUuid(deviceUuid);
        if (existLoginDeviceEntity == null) {
            Date now = new Date();
            LoginDeviceEntity loginDeviceEntity = LoginDeviceEntity.builder()
                    .createAt(now)
                    .createUserIdx(userEntity.getUserIdx())
                    .delYn("N")
                    .userIdx(userEntity.getUserIdx())
                    .loginDeviceUuid(deviceUuid)
                    .loginDeviceName(loginUserRequest.getLoginDevice().getDeviceName())
                    .loginDeviceTypeIdx(loginDeviceTypeEntity.getLoginDeviceTypeIdx())
                    .loginDeviceOs(loginUserRequest.getLoginDevice().getDeviceOs())
                    .pushToken(loginUserRequest.getLoginDevice().getPushToken())
                    .loginToken(loginToken)
                    .build();
            loginDeviceMapper.insertLoginDevice(loginDeviceEntity);
        } else {
            Date now = new Date();
            LoginDeviceEntity loginDeviceEntity = LoginDeviceEntity.builder()
                    .loginDeviceIdx(existLoginDeviceEntity.getLoginDeviceIdx())
                    .updateAt(now)
                    .updateUserIdx(userEntity.getUserIdx())
                    .delYn("N")
                    .userIdx(userEntity.getUserIdx())
                    .pushToken(loginUserRequest.getLoginDevice().getPushToken())
                    .loginToken(loginToken)
                    .build();
            loginDeviceMapper.updateLoginDevice(loginDeviceEntity);
        }

        List<UserAreaEntity> userAreaEntityList = userAreaMapper.findListByUserIdx(userEntity.getUserIdx());

        UserInfo userInfo = UserInfo.builder()
                .userIdx(userEntity.getUserIdx())
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .userPhone(userEntity.getUserPhone())
                .userAddress(userEntity.getUserAddress())
                .roleIdx(userEntity.getRoleIdx())
                .userGroupIdx(userEntity.getUserGroupIdx())
                .userAreaInfoList(userAreaEntityList.stream()
                        .map(entity -> UserAreaInfo.builder()
                                .areaIdx(entity.getAreaIdx())
                                .areaName(entity.getAreaName())
                                .areaRoleIdx(entity.getAreaRoleIdx())
                                .areaRoleName(entity.getAreaRoleName())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        return LoginInfo.builder()
                .loginToken(loginToken)
                .userInfo(userInfo)
                .build();
    }

    public LoginInfo validUser(String loginToken) {
        LoginDeviceEntity loginDeviceEntity = loginDeviceMapper.findByLoginToken(loginToken);
        if (loginDeviceEntity == null) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token is not valid");
        }

        Integer userIdx = loginDeviceEntity.getUserIdx();
        UserEntity userEntity = userMapper.findByUserIdx(userIdx);
        if (userEntity == null ) { //|| JWTUtil.verifyLoginToken(loginToken, userEntity.getUserId(), loginDeviceEntity.getLoginDeviceUuid()) == false) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_TOKEN, "login token is not valid");
        }

        List<UserAreaEntity> userAreaEntityList = userAreaMapper.findListByUserIdx(userIdx);

        UserInfo userInfo = UserInfo.builder()
                .userIdx(userEntity.getUserIdx())
                .userId(userEntity.getUserId())
                .userName(userEntity.getUserName())
                .userEmail(userEntity.getUserEmail())
                .userPhone(userEntity.getUserPhone())
                .userAddress(userEntity.getUserAddress())
                .roleIdx(userEntity.getRoleIdx())
                .userGroupIdx(userEntity.getUserGroupIdx())
                .userAreaInfoList(userAreaEntityList.stream()
                        .map(entity -> UserAreaInfo.builder()
                                .areaIdx(entity.getAreaIdx())
                                .areaName(entity.getAreaName())
                                .areaRoleIdx(entity.getAreaRoleIdx())
                                .areaRoleName(entity.getAreaRoleName())
                                .build())
                        .collect(Collectors.toList()))
                .build();

        return LoginInfo.builder()
                .loginToken(loginToken)
                .userInfo(userInfo)
                .build();
    }

    public List<UserInfo> getUserList(Integer userGroupIdx) {
        List<UserEntity> userEntityList = userMapper.findListByUserGroup(userGroupIdx);
        return userEntityList.stream()
                .map(
                        userEntity -> UserInfo.builder()
                                .userIdx(userEntity.getUserIdx())
                                .userId(userEntity.getUserId())
                                .userName(userEntity.getUserName())
                                .userEmail(userEntity.getUserEmail())
                                .userPhone(userEntity.getUserPhone())
                                .userAddress(userEntity.getUserAddress())
                                .roleIdx(userEntity.getRoleIdx())
                                .userGroupIdx(userEntity.getUserGroupIdx())
                                .userAreaInfoList(userAreaMapper.findListByUserIdx(userEntity.getUserIdx()).stream()
                                        .map(entity -> UserAreaInfo.builder()
                                                .areaIdx(entity.getAreaIdx())
                                                .areaName(entity.getAreaName())
                                                .areaRoleIdx(entity.getAreaRoleIdx())
                                                .areaRoleName(entity.getAreaRoleName())
                                                .build())
                                        .collect(Collectors.toList()))
                                .build()
                ).toList();
    }

    public UserEntity getUserByIdx(Integer userIdx){
        return userMapper.findByUserIdx(userIdx);
    }

    public void addUser(Integer requestUserIdx, AddUserRequest request){
        if(checkDuplicateId(request.getUserId())){
            throw new ApiException(ApiStatusCode.DUPLICATE_ID, null);
        }
        String salt = CryptoUtil.makePasswordSalt();
        String encryptedPassword = CryptoUtil.makeSHA3_256String(salt + request.getUserPassword());

        UserEntity userEntity = UserEntity.builder()
                .createAt(new Date())
                .createUserIdx(requestUserIdx)
                .delYn("N")
                .userId(request.getUserId())
                .userPassword(encryptedPassword)
                .passwordSalt(salt)
                .userName(request.getUserName())
                .userEmail(request.getUserEmail())
                .userEmailAuthYn("N")
                .userPhone(request.getUserPhone())
                .userAddress(request.getUserAddress())
                .roleIdx(request.getRoleIdx())
                .userGroupIdx(request.getUserGroupIdx())
                .build();
        userMapper.insertUser(userEntity);

        for(AddUserRequest.UserArea userArea : request.getUserAreaInfoList()){
            userAreaMapper.insert(
                    UserAreaEntity.builder()
                            .userIdx(userEntity.getUserIdx())
                            .areaIdx(userArea.getAreaIdx())
                            .areaRoleIdx(userArea.getAreaRoleIdx())
                            .build()
            );
        }
    }

    public void editUser(Integer requestUserIdx, EditUserRequest request){
        UserEntity userEntity = getUserByIdx(request.getUserIdx());
        if (userEntity == null) {
            throw new UnAuthorizedException(ApiStatusCode.INVALID_ID, "Invalid ID");
        }else if(!userEntity.getUserId().equals(request.getUserId()) && checkDuplicateId(request.getUserId())){
            throw new ApiException(ApiStatusCode.DUPLICATE_ID, null);
        }
        String salt = null;
        String encryptedPassword = null;
        if(StringUtils.trimToNull(request.getUserPassword())!=null){
            salt = CryptoUtil.makePasswordSalt();
            encryptedPassword = CryptoUtil.makeSHA3_256String(salt + request.getUserPassword());
        }
        userMapper.updateUser(
                UserEntity.builder()
                        .updateAt(new Date())
                        .updateUserIdx(requestUserIdx)
                        .userIdx(request.getUserIdx())
                        .userId(request.getUserId())
                        .userPassword(encryptedPassword)
                        .passwordSalt(salt)
                        .userName(request.getUserName())
                        .userEmail(request.getUserEmail())
                        .userPhone(request.getUserPhone())
                        .userAddress(request.getUserAddress())
                        .roleIdx(request.getRoleIdx())
                        .userGroupIdx(request.getUserGroupIdx())
                        .build()
        );

        userAreaMapper.delete(request.getUserIdx());
        for(EditUserRequest.UserArea userArea : request.getUserAreaInfoList()){
            userAreaMapper.insert(
                    UserAreaEntity.builder()
                            .userIdx(userEntity.getUserIdx())
                            .areaIdx(userArea.getAreaIdx())
                            .areaRoleIdx(userArea.getAreaRoleIdx())
                            .build()
            );
        }
    }

    public void deleteUser(Integer requestUserIdx, DeleteUserRequest request){
        userMapper.updateUser(
                UserEntity.builder()
                        .updateAt(new Date())
                        .updateUserIdx(requestUserIdx)
                        .userIdx(request.getUserIdx())
                        .delYn("Y")
                        .build()
        );
    }

    public void findId(String email) {
//        UserEntity userEntity = userMapper.findByUserEmail(email);
//        if (userEntity == null) {
//            throw new ApiException(ApiStatusCode.INVALID_EMAIL, "Email not found");
//        }
//
//        emailUtil.sendFindIdEmail(email, userEntity.getUserId());
    }

    public void findPassword(String email, String userId) {
//        UserEntity userEntity = userMapper.findByUserEmail(email);
//        if (userEntity == null) {
//            throw new ApiException(ApiStatusCode.INVALID_EMAIL, "Email not found");
//        } else if (Objects.equals(userEntity.getUserId(), userId) == false) {
//            throw new ApiException(ApiStatusCode.INVALID_ID, "Invalid ID");
//        }
//
//        String newPassword = RandomUtil.makeRandomPassword();
//
//        String salt = CryptoUtil.makePasswordSalt();
//        String encrypedPassword = CryptoUtil.makeSHA3_256String(salt + newPassword);
//
//        UserEntity updateEntity = UserEntity.builder()
//                .userIdx(userEntity.getUserIdx())
//                .userPassword(encrypedPassword)
//                .passwordSalt(salt)
//                .build();
//        userMapper.updateUser(updateEntity);
//
//        emailUtil.sendFindPasswordEmail(email, newPassword);
    }

//    public List<UserResponse.UserInfo> getUserList(UserEntity requestUserEntity, List<Integer> serviceIdxList, PageInfo pageInfo) {
//        for (Integer serviceIdx : serviceIdxList) {
//            UserServiceEntity requestUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, requestUserEntity.getUserIdx());
//            int requestUserRoleIdx = requestUserServuceEntity.getUserRoleIdx();
//            if (requestUserRoleIdx != 1 && requestUserRoleIdx != 2) {
//                throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//            }
//        }
//
//        List<UserEntity> userEntityList = userMapper.findListByServiceIdx(serviceIdxList, pageInfo);
//
//        List<UserResponse.UserInfo> userInfoList = Lists.newArrayList();
//        for (UserEntity userEntity : userEntityList) {
//            Integer userIdx = userEntity.getUserIdx();
//            List<UserServiceEntity> userServiceEntityList = userServiceMapper.findListByUserIdx(userIdx);
//
//            List<UserResponse.UserInfo.UserServiceInfo> userServiceInfoList = userServiceEntityList.stream()
//                    .map(entity -> UserResponse.UserInfo.UserServiceInfo.builder()
//                            .serviceIdx(entity.getServiceIdx())
//                            .serviceName(entity.getServiceName())
//                            .areaIdx(entity.getAreaIdx())
//                            .areaName(entity.getAreaName())
//                            .userRoleIdx(entity.getUserRoleIdx())
//                            .userRoleName(entity.getUserRoleName())
//                            .build())
//                    .collect(Collectors.toList());
//            UserResponse.UserInfo userInfo = UserResponse.UserInfo.builder()
//                    .userIdx(userEntity.getUserIdx())
//                    .userName(userEntity.getUserName())
//                    .userEmail(userEntity.getUserEmail())
//                    .userPhone(userEntity.getUserPhone())
//                    .userAddress(userEntity.getUserAddress())
//                    .userServiceInfoList(userServiceInfoList)
//                    .build();
//            userInfoList.add(userInfo);
//        }
//
//        return userInfoList;
//    }
//
//    public Integer getUserListCount(List<Integer> serviceIdxList) {
//        return userMapper.findListByServiceIdxCount(serviceIdxList);
//    }
//
//    public void addUserService(Integer requestUserIdx, AddUserProjectRequest addUserServiceRequest) {
//        // Check Role
//        Integer serviceIdx = addUserServiceRequest.getServiceIdx();
//        if (addUserServiceRequest.getAreaIdx() != 0) {
//            UserServiceEntity requestUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, requestUserIdx);
//            UserServiceEntity targetUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, addUserServiceRequest.getUserIdx());
//            Integer requestUserRoleIdx = requestUserServuceEntity.getUserRoleIdx();
//            Integer targetUserRoleIdx = targetUserServuceEntity.getUserRoleIdx();
//            if (requestUserRoleIdx > targetUserRoleIdx) {
//                throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//            }
//        }
//
//        UserServiceEntity userServiceEntity = UserServiceEntity.builder()
//                .userIdx(addUserServiceRequest.getUserIdx())
//                .serviceIdx(addUserServiceRequest.getServiceIdx())
//                .serviceName(addUserServiceRequest.getServiceName())
//                .areaIdx(addUserServiceRequest.getAreaIdx())
//                .areaName(addUserServiceRequest.getAreaName())
//                .userRoleIdx(addUserServiceRequest.getUserRoleIdx())
//                .userRoleName(addUserServiceRequest.getUserRoleName())
//                .build();
//        userServiceMapper.insertUserService(userServiceEntity);
//    }
//
//    public void editUserService(UserEntity requestUserEntity, EditUserProjectRequest editUserServiceRequest) {
//        // Check Role
//        Integer serviceIdx = editUserServiceRequest.getServiceIdx();
//        UserServiceEntity requestUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, requestUserEntity.getUserIdx());
//        UserServiceEntity targetUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, editUserServiceRequest.getUserIdx());
//        Integer requestUserRoleIdx = requestUserServuceEntity.getUserRoleIdx();
//        Integer targetUserRoleIdx = targetUserServuceEntity.getUserRoleIdx();
//        if (requestUserRoleIdx > targetUserRoleIdx) {
//            throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//        }
//
//        UserServiceEntity userServiceEntity = UserServiceEntity.builder()
//                .userIdx(editUserServiceRequest.getUserIdx())
//                .serviceIdx(editUserServiceRequest.getServiceIdx())
//                .areaIdx(editUserServiceRequest.getAreaIdx())
//                .userRoleIdx(editUserServiceRequest.getUserRoleIdx())
//                .userRoleName(editUserServiceRequest.getUserRoleName())
//                .build();
//        userServiceMapper.updateUserService(userServiceEntity);
//    }
//
//    public void clearUserService(UserEntity requestUserEntity, DeleteUserRequest deleteUserRequest) {
//        // Check Role
//        Integer serviceIdx = 2;
//        UserServiceEntity requestUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, requestUserEntity.getUserIdx());
//        UserServiceEntity targetUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, deleteUserRequest.getUserIdx());
//        Integer requestUserRoleIdx = requestUserServuceEntity.getUserRoleIdx();
//        Integer targetUserRoleIdx = targetUserServuceEntity.getUserRoleIdx();
//        if (targetUserRoleIdx == 1) {
//            throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//        } else if (requestUserRoleIdx > targetUserRoleIdx) {
//            throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//        }
//
//        UserServiceEntity userServiceEntity = UserServiceEntity.builder()
//                .userIdx(deleteUserRequest.getUserIdx())
//                .serviceIdx(2)
//                .build();
//        userServiceMapper.deleteUserService(userServiceEntity);
//    }
//
//    public void deleteUserService(UserEntity requestUserEntity, DeleteUserProjectRequest deleteUserServiceRequest) {
//        if (deleteUserServiceRequest.getAreaIdx() == 0) {
//            throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//        }
//
//        // Check Role
//        Integer serviceIdx = deleteUserServiceRequest.getServiceIdx();
//        UserServiceEntity requestUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, requestUserEntity.getUserIdx());
//        UserServiceEntity targetUserServuceEntity = userServiceMapper.findByServiceIdxNAreaIdx(serviceIdx, 0, deleteUserServiceRequest.getUserIdx());
//        Integer requestUserRoleIdx = requestUserServuceEntity.getUserRoleIdx();
//        Integer targetUserRoleIdx = targetUserServuceEntity.getUserRoleIdx();
//        if (requestUserRoleIdx > targetUserRoleIdx) {
//            throw new UnAuthorizedException(ApiStatusCode.BAD_REQUEST, "UnAuthorized");
//        }
//
//        UserServiceEntity userServiceEntity = UserServiceEntity.builder()
//                .userIdx(deleteUserServiceRequest.getUserIdx())
//                .serviceIdx(deleteUserServiceRequest.getServiceIdx())
//                .areaIdx(deleteUserServiceRequest.getAreaIdx())
//                .build();
//        userServiceMapper.deleteUserService(userServiceEntity);
//    }
//
    public boolean checkDuplicateId(String userId) {
        UserEntity userEntity = userMapper.findByUserId(userId);
        return userEntity != null;
    }
}
