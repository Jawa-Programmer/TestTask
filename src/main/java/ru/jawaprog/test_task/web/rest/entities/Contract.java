package ru.jawaprog.test_task.web.rest.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Описание контракта с клиентом МТС")
public class Contract {

    public Contract() {
    }

    public Contract(Long id) {
        this.id = id;
    }

    public Contract(Long id, Long number, Long clientId) {
        this.id = id;
        this.number = number;
        if (clientId != null)
            client = new Client(clientId, null, null);
    }

    public Contract(Long id, Long number, Client client) {
        this.id = id;
        this.number = number;
        this.client = client;
    }

    @ApiModelProperty(value = "Идентификатор контракта. Первичный ключ в БД", example = "1")
    private Long id;

    @ApiModelProperty(value = "Номер контракта.", example = "111222333444")
    private Long number;

    @ApiModelProperty(value = "Клиент, с которым заключен контракт")
    Client client;


}
