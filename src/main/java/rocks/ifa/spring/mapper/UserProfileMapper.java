package rocks.ifa.spring.mapper;

import org.apache.ibatis.annotations.Mapper;
import rocks.ifa.spring.model.UserProfile;

@Mapper
public interface UserProfileMapper {
    boolean checkUserExists(String uid);
    void insertInitUser(UserProfile newProfile);
    void updateLastLogin(String uid);
    int deleteByUid(String uid);
}
