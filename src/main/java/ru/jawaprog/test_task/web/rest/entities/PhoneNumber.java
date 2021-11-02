package ru.jawaprog.test_task.web.rest.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Описание номера телефона в сети МТС")
public class PhoneNumber {

    @ApiModelProperty(value = "Идентификатор номера телефона. Первичный ключ в БД", example = "1")
    private long id;

    @ApiModelProperty(value = "Номер телефона в формате +7 (xxx) xxx-xx-xx", example = "+7 (800) 555-35-35")
    private String number;

    @ApiModelProperty(value = "Лицевой счёт, на котором хранится баланс телефона.")
    private Account account;
}
