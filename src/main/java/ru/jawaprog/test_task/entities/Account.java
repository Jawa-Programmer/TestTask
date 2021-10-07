package ru.jawaprog.test_task.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "accounts")
public class Account {
    @Getter
    @Setter
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Getter
    @Setter
    @ManyToOne(optional = false, cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Getter
    @Setter
    @Column(nullable = false)
    private int number;

    @Getter
    @Setter
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER)
    private Collection<PhoneNumber> phoneNumbers;

}
