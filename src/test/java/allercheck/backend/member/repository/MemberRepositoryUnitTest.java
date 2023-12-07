package allercheck.backend.member.repository;

import static org.assertj.core.api.Assertions.assertThat;

import allercheck.backend.domain.member.entity.Member;
import allercheck.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@SuppressWarnings("NonAsciiCharacters")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = Replace.NONE)
@DataJpaTest
class MemberRepositoryUnitTest {

    @Autowired
    private MemberRepository memberRepository;

//    @Test
//    void 회원을_등록한다() {
//        //given
//        Member member = Member.createMember("kimsb7218@naver.com", "daily1313!", "김승범");
//
//        //when
//        Member createdMember = memberRepository.save(member);
//
//        //then
//        assertThat(createdMember.getUsername()).isEqualTo(member.getUsername());
//    }
}
