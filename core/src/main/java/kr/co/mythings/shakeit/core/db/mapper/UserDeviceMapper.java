package kr.co.mythings.shackit.core.db.mapper;

import kr.co.mythings.shackit.core.db.entity.UserDeviceEntity;
import kr.co.mythings.shackit.core.db.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserDeviceMapper {

    UserDeviceEntity find(UserDeviceEntity userDeviceEntity);

    void upsert(UserDeviceEntity userDeviceEntity);
}
