package smaple.cafekiosk.unit.orders;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import smaple.cafekiosk.unit.beverage.Beverage;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Getter
public class Order {

    private LocalDateTime orderDate;
    private List<Beverage> beverages;
}
