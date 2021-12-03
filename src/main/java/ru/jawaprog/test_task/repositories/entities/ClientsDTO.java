package ru.jawaprog.test_task.repositories.entities;

import lombok.Getter;
import lombok.Setter;
import ru.jawaprog.test_task_mts.Client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "client"
})
@XmlRootElement(name = "Clients")
@Getter
@Setter
public class ClientsDTO {
    protected List<Client> client;
}