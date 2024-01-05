package com.example.SystemAlerte.Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.*;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user", uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@DynamicUpdate
public class User implements Serializable {
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(updatable = false)
        private Long id;
        private String firstname;
        private String lastname;
        private String username;
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String password;
        private int phone;
        private String email;
        @ManyToMany(fetch = FetchType.EAGER, targetEntity = Role.class, cascade = CascadeType.ALL )
        @JoinTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
                        @JoinColumn(name = "role_id") })
        private Collection<Role> roles = new ArrayList<>();

}
