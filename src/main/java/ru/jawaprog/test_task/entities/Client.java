package ru.jawaprog.test_task.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;


@Entity
@Table(name = "clients")
public class Client {
    public enum ClientType {INDIVIDUAL, ENTITY}

    @Getter
    @Setter
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String fullName;

    @Getter
    @Setter
    @Column(nullable = false)
    @Enumerated(EnumType.ORDINAL)
    private ClientType type;

    @Getter
    @Setter
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Collection<Contract> contracts;

}
