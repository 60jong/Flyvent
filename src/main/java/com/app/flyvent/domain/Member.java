package com.app.flyvent.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Entity
@NoArgsConstructor
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    private Boolean mailNotificationEnable;

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<Subscription> subscriptions = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = CascadeType.ALL)
    private List<MemberDestination> destinations = new ArrayList<>();

    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Constructor
    public Member(String name, String email, String password, String phoneNumber, Boolean mailNotificationEnable) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.mailNotificationEnable = mailNotificationEnable;
    }

    // Method
    public List<String> getAirlineEnglishNames() {
        return subscriptions.stream()
                .map(subscription -> subscription.getAirline().getEnglishName())
                .collect(Collectors.toList());
    }

    public void subscribe(Subscription subscription) {
        this.subscriptions.add(subscription);
    }

    public void addMemberDestination(MemberDestination memberDestination) {
        this.destinations.add(memberDestination);
    }
}
