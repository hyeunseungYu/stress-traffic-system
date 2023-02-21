package com.project.stress_traffic_system.members;

import com.project.stress_traffic_system.members.entity.Members;
import com.project.stress_traffic_system.members.entity.MembersRoleEnum;
import com.project.stress_traffic_system.members.repository.MembersRepository;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest //h2 DB가 default 값
@SpringBootTest
public class MembersRepositoryTest {

    @Autowired
    private MembersRepository membersRepository;

    @Mock(name = "USERS")
    private Members member;

    @DisplayName("멤버가 DB에 저장이 잘 되는지 확인")
    @Test
    public void testSave() {
        //given
        String username = "qwertdf2";
        String password = "abcde123?";
        String address = "서울";
        MembersRoleEnum role = MembersRoleEnum.MEMBER;

        member = new Members(username, password, address, role);

        // when
        Members savedMember = membersRepository.save(member);

        // then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getUsername()).isEqualTo(member.getUsername());
        assertThat(savedMember.getPassword()).isEqualTo(member.getPassword());
        assertThat(savedMember.getAddress()).isEqualTo(member.getAddress());
        assertThat(savedMember.getRole()).isEqualTo(member.getRole());
    }

    @Test
    @DisplayName("저장된 멤버가 제대로 조회되는지 확인")
    void findMember() {
        //given
        String username = RandomStringUtils.randomAlphanumeric(10);
        String password = "abcde123?";
        String address = "서울";
        MembersRoleEnum role = MembersRoleEnum.MEMBER;

        Members savedMember = membersRepository.save(new Members(username, password, address, role));

        // when
        Members findMember = membersRepository.findById(savedMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<" + savedMember.getId() + ">"));

        // then
        assertThat(findMember.getUsername()).isEqualTo(savedMember.getUsername());
        assertThat(findMember.getPassword()).isEqualTo(savedMember.getPassword());
        assertThat(findMember.getAddress()).isEqualTo(savedMember.getAddress());
        assertThat(findMember.getRole()).isEqualTo(savedMember.getRole());
    }
}