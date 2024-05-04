package com.santeut.party.entity;

import com.santeut.party.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Geometry;

import java.time.Duration;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Table(name = "party_user")
public class PartyUser extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "party_user_id", nullable = false)
    private int partyUserId;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "party_id", nullable = false)
    private int partyId;

    @Column(name = "party_user_status", length = 1, nullable = false)
    private char status;

    @Column(name = "party_user_distance")
    private Integer distance;

    @Column(name = "party_user_best_height")
    private Integer bestHeight;

    @Column(name = "party_user_move_time")
    private Integer moveTime;

    @Column(name = "party_user_is_success"/*, nullable = false*/)//후순위
    private Boolean isSuccess;

    @Column(name = "party_user_points", columnDefinition = "geometry")
    private Geometry points;

    @Column(name = "party_user_started_at")
    private LocalDateTime started_at;

    /** 비즈니스 로직 **/
    public void setStatus(char status){
        if(status=='P'){
            this.started_at=LocalDateTime.now();
        }
        else if(status=='E'){
            setDeleted(true);
            this.moveTime= (int)Duration.between(getDeletedAt(),this.started_at).getSeconds()/60;
        }
        this.status=status;
    }

    public void addTrackPoints(Geometry points){
        this.points=points;
    }
}