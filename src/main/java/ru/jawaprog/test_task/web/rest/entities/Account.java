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
    Long id;

    @ApiModelProperty(value = "Номер счёта.", example = "111222333444")
    Long number;

    @ApiModelProperty(value = "Контракт, по которому открыт данный счёт")
    Contract contract;

    public Account() {
    }

    public Account(Long id) {
        this.id = id;
    }

    public Account(Long id, Long number, Long contractId) {
        this.id = id;
        this.number = number;
        if (contractId != null)
            this.contract = new Contract(contractId);
    }
    public Account(Long id, Long number, Contract contract) {
        this.id = id;
        this.number = number;
        this.contract = contract;
    }
}
