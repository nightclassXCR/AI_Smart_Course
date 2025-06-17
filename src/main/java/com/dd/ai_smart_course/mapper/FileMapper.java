package com.dd.ai_smart_course.mapper;

import com.dd.ai_smart_course.entity.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {

    // 获取所有文件记录
    @Select("SELECT * FROM file")
    List<File> getAllFiles();

    // 获取文件记录详情
    @Select("SELECT * FROM file WHERE id = #{id}")
    File getFileById(int id);

    // 添加文件记录
    @Insert("INSERT INTO file (name, file_url, file_type, owner_type, owner_id, uploaded_at) VALUES (#{name}, #{fileUrl}, #{fileType}, #{ownerType}, #{ownerId}, #{uploadedAt})")
    int addFile(File file);

    // 更新文件记录信息
    @Update("UPDATE file SET name = #{name}, file_url = #{fileUrl}, file_type = #{fileType}, owner_type = #{ownerType}, owner_id = #{ownerId}, uploaded_at = #{uploadedAt} WHERE id = #{id}")
    int updateFile(File file);

    // 删除文件记录
    @Delete("DELETE FROM file WHERE id = #{id}")
    int deleteFile(int id);
}