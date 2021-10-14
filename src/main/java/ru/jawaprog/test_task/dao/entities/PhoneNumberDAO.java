package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "phone_numbers")
public class PhoneNumberDAO {
    @Getter
    @Setter
    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Getter
    @Setter
    @Column(nullable = false)
    private String number;

    @Getter
    @Setter
    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountDAO account;

}
