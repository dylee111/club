package org.zerock.club.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.zerock.club.entity.ClubMember;
import org.zerock.club.entity.ClubMemberRole;

import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClubMemberRepositoryTests {
    @Autowired
    private ClubMemberRepository clubMemberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            ClubMember clubMember = ClubMember.builder()
                    .email("user" + i + "@ds.org")
                    .name("사용자" + i)
                    .fromSocial(false)
                    .password(passwordEncoder.encode("1111"))
                    .build();
            /*
            *  1 - 80 : USER
            *  81 - 90 : USER, MANAGER
            *  91 - 100 : USER, MANAGER, ADMIN
            **/

            clubMember.addMemberRole(ClubMemberRole.USER);

            if (i > 80) { // 80보다 큰 번호는 Manager 권한
                clubMember.addMemberRole(ClubMemberRole.MANAGER);
            }
            if (i > 90) { // 90보다 큰 번호는 Admin 권한
               clubMember.addMemberRole(ClubMemberRole.ADMIN);
            }

            clubMemberRepository.save(clubMember);
        });
    } // insertDummies()

    @Test
    public void testRead() {

        Optional<ClubMember> result = clubMemberRepository.findByEmail("user95@ds.org", false);

        ClubMember clubMember = result.get();

        System.out.println(clubMember);
    }
}