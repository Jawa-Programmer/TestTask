package ru.jawaprog.test_task.dao.repositories;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import ru.jawaprog.test_task.dao.entities.AccountDTO;

import java.util.List;


public interface AccountsRepository {
    @Select("SELECT * FROM accounts")
    @Results({
            @Result(column = "contract_id", property = "contractId")
    })
    List<AccountDTO> findAll();

    @Select("SELECT * FROM accounts WHERE contract_id = #{contractId}")
    @Results({
            @Result(column = "contract_id", property = "contractId")
    })
    List<AccountDTO> findAccountsByContractId(long contractId);

    @Select("SELECT * FROM accounts WHERE id=#{id}")
    @Results({
            @Result(column = "contract_id", property = "contractId")
    })
    AccountDTO findById(long id);

    @Select("<script>INSERT INTO accounts (number, contractId) VALUES(#{number}, #{contract_id}) RETURNING *</script>")
    @Results({
            @Result(column = "contract_id", property = "contractId")
    })
    AccountDTO insert(long number, long contractId);

    @Select("<script>\n" +
            "  update accounts\n" +
            "    <set>\n" +
            "      <if test=\"number != null\">number=#{number},</if>\n" +
            "      <if test=\"contractId != null\">contract_id=#{contractId}</if>\n" +
            "    </set>\n" +
            "  where id=#{id} RETURNING *\n" +
            "</script>")
    @Results({
            @Result(column = "contract_id", property = "contractId")
    })
    AccountDTO update(long id, Long number, Long contractId);

    @Delete("DELETE FROM accounts WHERE id = #{id}")
    int deleteById(long id);
}
