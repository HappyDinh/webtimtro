package com.haui.web_demo.entities;

import jakarta.persistence.*;


@Entity
@Table(name = "users_roles")
public class User_Role {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userID")
    private Userobject user;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private Role role;

    public User_Role() {
        super();
        // TODO Auto-generated constructor stub
    }

    public User_Role(Userobject user, Role role) {
        this.user = user;
        this.role = role;
    }

    public User_Role(Long id, Userobject user, Role role) {
        super();
        this.id = id;
        this.user = user;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Userobject getUser() {
        return user;
    }

    public void setUser(Userobject user) {
        this.user = user;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
