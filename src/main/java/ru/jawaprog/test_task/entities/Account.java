package ru.jawaprog.test_task.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @ManyToOne(optional = false)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @Getter
    @Setter
    @Column(nullable = false)
    private int number;

    @JsonIgnore // что бы не возникало рекурсии. В будущем можно сделать свой JSON сериализатор
    @Getter
    @Setter
    @OneToMany(mappedBy = "account", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private Collection<PhoneNumber> phoneNumbers;

}
