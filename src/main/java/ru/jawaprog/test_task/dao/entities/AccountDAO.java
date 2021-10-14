package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class AccountDAO {
    @Getter
    @Setter
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id")
    private ContractDAO contract;

    @Getter
    @Setter
    @Column(nullable = false)
    private long number;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<PhoneNumberDAO> phoneNumbers;

}
