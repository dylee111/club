package org.zerock.club.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.zerock.club.entity.Note;

import java.util.List;
import java.util.Optional;

public interface NoteRepository extends JpaRepository<Note, Long> {

    // attributePaths에 지정된 필드만 EAGER로 조회, 나머지는 Entity에 지정된 방식대로 조회.
    @EntityGraph(attributePaths = "writer", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT n FROM Note n WHERE n.num=:num ")
    Optional<Note> getWithWriter(Long num);

    @EntityGraph(attributePaths = {"writer"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT n FROM Note n WHERE n.writer.email=:email ") // Note -> ClubMember -> email
    List<Note> getList(String email);

}
