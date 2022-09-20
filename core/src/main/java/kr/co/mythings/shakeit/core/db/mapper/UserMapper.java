package kr.co.mythings.shackit.core.db.mapper;

import kr.co.mythings.shackit.core.db.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    UserEntity find(UserEntity userEntity);

    List<UserEntity> list(UserEntity userEntity);

    void insert(UserEntity userEntity);

    void update(UserEntity userEntity);
}
