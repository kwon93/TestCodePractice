package smaple.cafekiosk.unit;

import smaple.cafekiosk.unit.beverage.Americano;
import smaple.cafekiosk.unit.beverage.Latte;
import smaple.cafekiosk.unit.orders.Order;

import java.time.LocalDateTime;

/**
 * 수동 테스트
 * 메인함수로 콘촐창으로 프로그램의 실형결과를 눈으로 직접 확인
 */
public class CafeKioskRunner {
    public static void main(String[] args) {
        CafeKiosk cafeKiosk = new CafeKiosk();
        cafeKiosk.add(new Americano(),1);
        System.out.println("Americano add-----");

        cafeKiosk.add(new Latte(),1);
        System.out.println("Latte add-----");

        int totalPrice = cafeKiosk.calculateTotalPrice();
        System.out.println("Total Price : " + totalPrice);

        Order order = cafeKiosk.createOrder(LocalDateTime.now());

    }
}
