package com.example.fourchelin.domain.waiting.entity;

import com.example.fourchelin.common.baseentity.Timestamped;
import com.example.fourchelin.domain.store.entity.Store;
import com.example.fourchelin.domain.waiting.enums.WaitingMealType;
import com.example.fourchelin.domain.waiting.enums.WaitingStatus;
import com.example.fourchelin.domain.waiting.enums.WaitingType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Waiting extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;*/

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Enumerated(EnumType.STRING)
    private WaitingType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private WaitingMealType mealType;

    @Enumerated(EnumType.STRING)
    private WaitingStatus status;

    @Column(name = "waiting_number")
    private int waitingNumber;  // 대기 번호
    private int personnel;  // 인원 수

/*    @Builder
    public Waiting(User user, Store store, WaitingType type, WaitingMealType mealType, WaitingStatus status, int waitingNumber, int personnel) {
        this.user = user;
        this.store = store;
        this.type = type;
        this.mealType = mealType;
        this.status = status;
        this.waitingNumber = waitingNumber;
        this.personnel = personnel;
    }*/
}
