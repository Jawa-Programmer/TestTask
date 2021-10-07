package ru.jawaprog.test_task.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore // что бы не возникало рекурсии. В будущем можно сделать свой JSON сериализатор
    @Getter
    @Setter
    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Contract> contracts;

}
