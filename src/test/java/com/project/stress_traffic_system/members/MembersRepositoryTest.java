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

import static org.assertj.core.api.Assertions.assertThat;

//@DataJpaTest //h2 DB가 default
@SpringBootTest
public class MembersRepositoryTest {

    @Autowired
    private MembersRepository membersRepository;

    @Mock(name = "USERS")
    private Members MockMember;

    @Mock(name = "USERS")
    private Members savedMockMember;

    //Mock 엔티티를 사용하기 때문에 실제DB에 저장되지 않는다.
    @DisplayName("멤버가 DB에 저장이 잘 되는지 확인")
    @Test
    public void testSave() {
        //given
        String username = "usertest";
        String password = "abcde123?";
        String address = "서울";
        MembersRoleEnum role = MembersRoleEnum.MEMBER;

        MockMember = new Members(username, password, address, role);

        // when
        Members savedMember = membersRepository.save(MockMember);

        // then
        assertThat(savedMember.getId()).isNotNull();
        assertThat(savedMember.getUsername()).isEqualTo(MockMember.getUsername());
        assertThat(savedMember.getPassword()).isEqualTo(MockMember.getPassword());
        assertThat(savedMember.getAddress()).isEqualTo(MockMember.getAddress());
        assertThat(savedMember.getRole()).isEqualTo(MockMember.getRole());
    }

    @Test
    @DisplayName("저장된 멤버가 제대로 조회되는지 확인")
    void findMember() {
        //given
        String username = "asdf1234";
        String password = "abcde123?";
        String address = "서울";
        MembersRoleEnum role = MembersRoleEnum.MEMBER;

        savedMockMember = membersRepository.save(new Members(username, password, address, role));

        // when
        Members findMember = membersRepository.findById(savedMockMember.getId())
                .orElseThrow(() -> new IllegalArgumentException("Wrong MemberId:<" + savedMockMember.getId() + ">"));

        // then
        assertThat(findMember.getUsername()).isEqualTo(savedMockMember.getUsername());
        assertThat(findMember.getPassword()).isEqualTo(savedMockMember.getPassword());
        assertThat(findMember.getAddress()).isEqualTo(savedMockMember.getAddress());
        assertThat(findMember.getRole()).isEqualTo(savedMockMember.getRole());
    }
}