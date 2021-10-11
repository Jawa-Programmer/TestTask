package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "contracts")
public class ContractDAO {
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
    private ClientDAO client;

    @Getter
    @Setter
    @Column(nullable = false, name = "number")
    private long number;

    @Getter
    @Setter
    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AccountADO> accounts;

}
