package dev.saha.otpauthserverweb.domain;

import lombok.*;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "token")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Token {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    @OneToOne
    private Employee employee;
    private LocalTime createdAt;
}
