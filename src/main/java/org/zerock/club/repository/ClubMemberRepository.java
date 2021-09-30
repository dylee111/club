package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.ClubMember;

import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, String> {

    // FETCH: entity graph에 명시한 attribute는 EAGER로 패치하고, 나머지 attribute는 LAZY로 패치
    // LOAD: entity graph에 명시한 attribute는 EAGER로 패치하고,
    // 나머지 attribute는 entity에 명시한 fetch type이나 디폴트 FetchType으로 패치
    // (e.g. @OneToMany는 LAZY, @ManyToOne은 EAGER 등이 디폴트이다.)
    @EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT m " +
            " FROM ClubMember m " +
            " WHERE m.fromSocial=:social AND m.email=:email ")
    Optional<ClubMember> findByEmail(String email, Boolean social);
}
