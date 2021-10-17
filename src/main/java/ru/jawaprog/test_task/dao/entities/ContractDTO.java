package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "contracts")
public class ContractDTO {
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "client_id")
    private ClientDTO client;

    @Column(nullable = false, name = "number")
    private long number;

    @OneToMany(mappedBy = "contract", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Set<AccountDTO> accounts;

}
