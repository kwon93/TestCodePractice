package smaple.cafekiosk.unit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import smaple.cafekiosk.unit.beverage.Americano;

import static org.junit.jupiter.api.Assertions.*;

class CafeKioskTest {

    @Test
    @DisplayName("음료의 개수를 더하기에 성공한다")
    void test1() throws Exception{
        //given
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano());

        System.out.println("키오스크에 담긴 음료의 개수 : "+cafeKiosk.getBeverages());
        System.out.println("키오스크에 담긴 음료의 개수 : "+cafeKiosk.getBeverages().get(0).getName());
        //when

        //then
    }

}