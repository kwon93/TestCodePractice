package smaple.cafekiosk.spring.api.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import smaple.cafekiosk.spring.client.mail.MailSendClient;
import smaple.cafekiosk.spring.domain.history.mail.MailSendHistory;
import smaple.cafekiosk.spring.domain.history.mail.MailSendHistoryRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 단위 테스트 진행.
 */

@ExtendWith(MockitoExtension.class) //@Mock 을 사용하기위한 Annotation
class MailServiceTest {

    @Mock
    private MailSendClient mailSendClient;
    //Annotation을 통한 Mock 객체 생성

//    @Spy
//    private MailSendClient mailSendClient;

    @Mock
    private MailSendHistoryRepository mailSendHistoryRepository;

    @InjectMocks
    private MailService mailService;
    //@InjectMocks 의존하고있는 Mock이 있다면 의존주입을 받게해줌.

    @Test
    @DisplayName("메일 전송 테스트")
    void sendMail() throws Exception {
        //given
//        mock에서의 문법이지만 given절에서 when()이라는 메서드가 어색하다...
//        when(mailSendClient.sendEmail(
//                any(String.class),
//                any(String.class),
//                any(String.class),
//                any(String.class)
//        )).thenReturn(true);

        //BDD에서 더 자연스러운 문법
        BDDMockito.given(mailSendClient.sendEmail(anyString(), anyString(), anyString(), anyString()))
                .willReturn(true);

        //@Spy 활용시 문법.
        //@Spy : 일부는 stub 객체의 메서드를 사용하고 싶고, [sendMail()] / 일부는 실제 객체의 메서드를 사용하고 싶을때 [a(), b(), c()]
//        doReturn(true)
//                .when(mailSendClient)
//                .sendEmail(anyString(),anyString(),anyString(),anyString());


        // when
        boolean result = mailService.sendMail("", "", "", "");
        //then
        assertThat(result).isTrue();
        //mock이 몇번 불렸는지 테스트
        verify(mailSendHistoryRepository, times(1)).save(any(MailSendHistory.class));

    }
}