package ru.jawaprog.test_task.web.rest.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Описание контракта с клиентом МТС")
public class Contract {

    @ApiModelProperty(value = "Идентификатор контракта. Первичный ключ в БД", example = "1")
    private long id;

    @ApiModelProperty(value = "Номер контракта.", example = "111222333444")
    private long number;

    @ApiModelProperty(value = "Клиент, с которым заключен контракт")
    Client client;
}
