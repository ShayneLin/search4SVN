package com.shark.search4SVN.dao;

import com.shark.search4SVN.pojo.Document;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DocumentDao {

        String TABLE_NAME = " t_documemt ";
        String INSERT_FIELD = " entity_flag,name,doc_url,modify_time,description ";
        String SELECT_FIELD = " id,entity_flag,name,doc_url,modify_time,description ";

    @Select({"delete  from ",TABLE_NAME," where id=#{id}"})
    int deleteById(Integer id);


    @Insert({"insert into ",TABLE_NAME,"(",INSERT_FIELD,") values ","(#{userId},#{ticket},#{expired},#{status})"})
    int insert(Document record);


    @Select({"select ",SELECT_FIELD," from ",TABLE_NAME," where id=#{id}"})
    Document selectById(Integer id);


    @Update({"update ",TABLE_NAME," set name=#{record.name} "," where id=#{record.id}"})
    int updateDocument(Document record);

}