package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Optional;

@Mapper
public interface FileMapper {

    // 获取所有文件记录
    @Select("SELECT * FROM resources")
    List<File> getAllFiles();

    // 获取文件记录详情
    @Select("SELECT * FROM resources WHERE id = #{id}")
    File getFileById(int id);

    // 添加文件记录
    @Insert("INSERT INTO resources (name, file_url, file_type, owner_type, owner_id, created_at, updated_at) VALUES (#{name}, #{fileUrl}, #{fileType}, #{ownerType}, #{ownerId}, #{createdAt}, #{updatedAt})")
    int addFile(File file);

    // 更新文件记录信息
    @Update("UPDATE resources SET name = #{name}, file_url = #{fileUrl}, file_type = #{fileType}, owner_type = #{ownerType}, owner_id = #{ownerId}, created_at = #{createdAt}, uploaded_at = #{uploadedAt} WHERE id = #{id}")
    int updateFile(File file);

    // 删除文件记录
    @Delete("DELETE FROM resources WHERE id = #{id}")
    int deleteFile(int id);

    // 查询文件记录
    @Select("SELECT * FROM resources WHERE id = #{id}")
    Optional<File> findById(Long resourceId);

}