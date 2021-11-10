package ru.jawaprog.test_task.dao.repositories;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import ru.jawaprog.test_task.dao.entities.ContractDTO;

import java.util.List;

public interface ContractsRepository {

    @Select("SELECT * FROM contracts WHERE id = #{id}")
    @Results({
            @Result(column = "client_id", property = "clientId")
    })
    ContractDTO findById(Long contractId);

    @Delete("DELETE FROM contracts WHERE id = #{id}")
    int deleteById(long id);

    @Select("<script>INSERT INTO contracts (number, client_id) VALUES(#{number}, #{clientId}) RETURNING *</script>")
    @Results({
            @Result(column = "client_id", property = "clientId")
    })
    ContractDTO insert(long number, long clientId);

    @Select("<script>\n" +
            "  update contracts\n" +
            "    <set>\n" +
            "      <if test=\"number != null\">number=#{number},</if>\n" +
            "      <if test=\"clientId != null\">client_id=#{clientId}</if>\n" +
            "    </set>\n" +
            "  where id=#{id} RETURNING *\n" +
            "</script>")
    @Results({
            @Result(column = "client_id", property = "clientId")
    })
    ContractDTO update(long id, Long number, Long clientId);

    @Select("SELECT * FROM contracts")
    @Results({
            @Result(column = "client_id", property = "clientId")
    })
    List<ContractDTO> findAll();

    @Select("SELECT * FROM contracts WHERE client_id = #{id}")
    @Results({
            @Result(column = "client_id", property = "clientId")
    })
    List<ContractDTO> findByClientId(long id);
}
