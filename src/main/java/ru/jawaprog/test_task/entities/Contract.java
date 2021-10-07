package ru.jawaprog.test_task.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "contracts")
public class Contract {
    @Getter
    @Setter
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @Getter
    @Setter
    @Column(nullable = false, name = "contract_number")
    private long contractNumber;

    @JsonIgnore // что бы не возникало рекурсии. В будущем можно сделать свой JSON сериализатор
    @Getter
    @Setter
    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<Account> accounts;

}
