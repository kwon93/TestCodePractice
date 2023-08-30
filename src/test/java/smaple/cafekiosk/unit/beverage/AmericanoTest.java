package smaple.cafekiosk.unit.beverage;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smaple.cafekiosk.unit.CafeKiosk;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    @DisplayName("Americano Class Test")
    void test1() throws Exception{
         //given
        Americano americano = new Americano();
        //when

         //then
        assertEquals(americano.getName(), "Americano");
        assertThat(americano.getName()).isEqualTo("Americano");

    }

    @Test
    @DisplayName("아메리카노 가격 검증하기")
    void test2() throws Exception{
         //given
        Americano americano = new Americano();
        //when

         //then
        assertThat(americano.getPrice()).isEqualTo(5000 );

    }

    //자동화된 테스트 사람이 눈으로 직접 확인하지않고 프로그램을 통해 검증
    @Test
    @DisplayName("Cafe Kiosk add Test")
    void test3() throws Exception{
         //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(),1);
        //when

         //then
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(1);
        assertThat(cafeKiosk.getBeverages().get(0).getName()).isEqualTo("Americano");
    }

    @Test
    @DisplayName("Cafe Kiosk remove test")
    void test4() throws Exception{
         //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano,1);

        //when
        assertThat(cafeKiosk.getBeverages()).hasSize(1);
        cafeKiosk.remove(americano);

         //then
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }

    @Test
    @DisplayName("Cafe Kiosk Clear Test")
    void test5() throws Exception{
         //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        Latte latte = new Latte();
        //when
        cafeKiosk.add(americano,1);
        cafeKiosk.add(latte,1);
        assertThat(cafeKiosk.getBeverages()).hasSize(2);

        cafeKiosk.clear();
         //then
        assertThat(cafeKiosk.getBeverages()).isEmpty();

    }

    @Test
    @DisplayName("Cafe Kiosk 를 통해 여러잔 주문하기")
    void test6() throws Exception{
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();
        cafeKiosk.add(americano, 2); //경계값 : 2
        //when

        //then
        assertThat(cafeKiosk.getBeverages().size()).isEqualTo(2);
        assertThat(cafeKiosk.getBeverages().get(0)).isEqualTo(americano);
        assertThat(cafeKiosk.getBeverages().get(1)).isEqualTo(americano);
    }

    @Test
    @DisplayName("Cafe kiosk add Exception Test")
    void test7() throws Exception{
         //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        Americano americano = new Americano();

        //when

        //then 경계값이 0일때에 예외가 터져야한다.
        assertThatThrownBy(() -> cafeKiosk.add(americano, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("음료는 1잔 이상 주문하셔야 합니다.");

    }
}