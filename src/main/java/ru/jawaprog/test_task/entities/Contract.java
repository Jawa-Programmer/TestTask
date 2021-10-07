package ru.jawaprog.test_task.entities;

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
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "client_id")
    private Client client;

    @Getter
    @Setter
    @Column(nullable = false)
    private long contract_number;

    @Getter
    @Setter
    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER)
    private Collection<Account> account;

}
