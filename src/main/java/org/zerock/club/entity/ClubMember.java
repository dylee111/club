package org.zerock.club.entity;

import lombok.*;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class ClubMember extends BaseEntity {

    @Id
    private String email;

    private String password;

    private String name;

    private boolean fromSocial;

    @ElementCollection(fetch = FetchType.LAZY) // One To Many 관계를 다룸, 하지만 Entity가 아닌 단순 객체 집합을 다룰 때 사용
    @Builder.Default
    private Set<ClubMemberRole> roleSet = new HashSet<>(); // 복수의 role을 가질 수 있기 때문에 set 타입으로 정의

    public void addMemberRole(ClubMemberRole clubMemberRole) {
        roleSet.add(clubMemberRole);
    } // addMemberRole()

}
