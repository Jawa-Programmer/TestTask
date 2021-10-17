package ru.jawaprog.test_task.dao.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "phone_numbers")
public class PhoneNumberDTO {

    @Id
    @GenericGenerator(name = "generator", strategy = "increment")
    @GeneratedValue(generator = "generator")
    private long id;

    @Column(nullable = false)
    private String number;


    @ManyToOne(optional = false)
    @JoinColumn(name = "account_id")
    private AccountDTO account;

}
