package ru.jawaprog.test_task.dao.repositories;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import ru.jawaprog.test_task.dao.entities.ClientDTO;

import javax.validation.constraints.NotNull;
import java.util.List;


public interface ClientsDatabaseMapper {
    @Select("SELECT * FROM clients WHERE full_name ILIKE '%'||#{name}||'%'")
    @Results({
            @Result(column = "full_name", property = "fullName")
    })
    List<ClientDTO> findAllByName(String name);

    @Select(value = "SELECT * FROM clients WHERE id = #{id}")
    @Results({
            @Result(column = "full_name", property = "fullName")
    })
    ClientDTO findById(long id);

    @Select("SELECT EXISTS(SELECT 1 FROM clients WHERE id = #{id})")
    boolean exists(long id);


    @Select(value = "SELECT * FROM clients")
    @Results({
            @Result(column = "full_name", property = "fullName")
    })
    List<ClientDTO> findAll();

    @Select("<script>INSERT INTO clients (full_name, type) VALUES (#{fullName}, #{type}) RETURNING *</script>")
    @Results({
            @Result(column = "full_name", property = "fullName")
    })
    ClientDTO insert(@NotNull String fullName, int type);

    @Select("<script>\n" +
            "  update clients\n" +
            "    <set>\n" +
            "      <if test=\"fullName != null\">full_name=#{fullName},</if>\n" +
            "      <if test=\"type != null\">type=#{type}</if>\n" +
            "    </set>\n" +
            "  where id=#{id} RETURNING *\n" +
            "</script>")
    @Results({
            @Result(column = "full_name", property = "fullName")
    })
    ClientDTO update(ClientDTO clientDTO);

    @Delete("DELETE FROM clients WHERE id=#{id}")
    int deleteById(long id);
}
