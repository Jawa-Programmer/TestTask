package ru.jawaprog.test_task.dao.repositories;

import org.apache.ibatis.annotations.*;
import ru.jawaprog.test_task.dao.entities.PhoneNumberDTO;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface PhoneNumbersRepository {

    @Select("SELECT * FROM phone_numbers WHERE number = #{number}")
    @Results({
            @Result(column = "account_id", property = "accountId")
    })
    List<PhoneNumberDTO> findAllByNumber(String number);

    @Select("SELECT * FROM phone_numbers")
    @Results({
            @Result(column = "account_id", property = "accountId")
    })
    List<PhoneNumberDTO> findAll();

    @Select("SELECT * FROM phone_numbers WHERE id = #{id}")
    @Results({
            @Result(column = "account_id", property = "accountId")
    })
    PhoneNumberDTO findById(long id);

    @Insert("<script>INSERT INTO phone_numbers (number, account_id) VALUES(#{number}, #{accountId}) RETURNING *</script>")
    @Results({
            @Result(column = "account_id", property = "accountId")
    })
    PhoneNumberDTO insert(@NotNull String number, long accountId);

    @Select("<script>\n" +
            "  update accounts\n" +
            "    <set>\n" +
            "      <if test=\"number != null\">number=#{number},</if>\n" +
            "      <if test=\"accountId != null\">account_id=#{accountId}</if>\n" +
            "    </set>\n" +
            "  where id=#{id} RETURNING *\n" +
            "</script>")
    @Results({
            @Result(column = "account_id", property = "accountId")
    })
    PhoneNumberDTO update(long id, String number, Long accountId);

    @Select("SELECT * FROM phone_numbers WHERE account_id = #{id}")
    @Results({
            @Result(column = "account_id", property = "accountId")
    })
    List<PhoneNumberDTO> findByAccountId(long id);

    @Delete("DELETE FROM phone_numbers WHERE id = #{id}")
    int deleteById(long id);
}
