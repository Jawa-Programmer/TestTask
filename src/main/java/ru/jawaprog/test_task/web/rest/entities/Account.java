package ru.jawaprog.test_task.web.rest.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Описание лицевого счёта клиента МТС")
public class Account {

    @ApiModelProperty(value = "Идентификатор счёта. Первичный ключ в БД", example = "1")
    long id;

    @ApiModelProperty(value = "Номер счёта.", example = "111222333444")
    long number;

    @ApiModelProperty(value = "Контракт, по которому открыт данный счёт")
    Contract contract;
}
