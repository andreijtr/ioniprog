package com.ja.ioniprog.model.entity.audit;

import com.ja.ioniprog.model.entity.Location;
import com.ja.ioniprog.model.entity.User;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "location_audit")
@Getter @Setter @Builder @NoArgsConstructor @AllArgsConstructor @ToString
public class LocationAudit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_location_audit")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_location")
    private Location locationEntity;

    @Column(name = "action")
    private String action;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user_action")
    private User userAction;

    @Column(name = "date_Action")
    private LocalDateTime dateAction;

    //folosesti ObjectMapper din libraria Jackson pt conversia JSON - object si Object JSON
    @Column(name = "location_version")
    private String locationVersion;
}
