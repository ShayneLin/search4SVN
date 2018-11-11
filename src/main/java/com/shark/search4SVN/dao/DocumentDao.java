package com.shark.search4SVN.dao;

import com.shark.search4SVN.pojo.Document;
import org.apache.ibatis.annotations.*;

import java.util.Map;

@Mapper
public interface DocumentDao {

        String TABLE_NAME = " t_document ";
        String INSERT_FIELD = " entity_flag,name,doc_url,modify_time,description ";
        String SELECT_FIELD = " id,entity_flag,name,doc_url,modify_time,description ";

    @Delete({"delete  from ",TABLE_NAME," where id=#{id}"})
    int deleteById(Integer id);


    @Select({"select ", SELECT_FIELD," from ",TABLE_NAME," where entity_flag=#{entityFlag}"})
    public Document getDocumentByEntityFlag(String entityFlag);

    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,") values ","(#{entityFlag},#{name},#{docUrl},#{modifyTime},#{description})"})
    int insert(Document record);


    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where id=#{id}"})
    Document selectById(Integer id);


    @Update({"update ",TABLE_NAME," set modify_time=#{doc.modifyTime} "," where entity_flag=#{entityFlag}"})
    int updateDocument(@Param("doc") Map<String,Object> params, String entityFlag);

}