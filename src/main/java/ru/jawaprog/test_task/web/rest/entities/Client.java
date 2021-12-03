package ru.jawaprog.test_task.web.rest.entities;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ApiModel(description = "Описание клиента МТС")
public class Client {
    @ApiModelProperty(value = "Идентификатор клиента. Первичный ключ в БД", example = "1")
    private Long id;
    @ApiModelProperty(value = "ФИО физического лица или наименование организации", example = "ИП Иванова")
    private String fullName;
    @ApiModelProperty(value = "Тип клиента. INDIVIDUAL - физическое лицо, ENTITY - юридическое лицо.", example = "ENTITY")
    private ClientType type;

    public Client() {
    }

    public Client(Long id) {
        this.id = id;
    }

    public Client(Long id, String fullName, ClientType type) {
        this.id = id;
        this.fullName = fullName;
        this.type = type;
    }

    public enum ClientType {
        INDIVIDUAL,
        ENTITY
    }

}
