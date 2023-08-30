package smaple.cafekiosk.unit.beverage;

public class Americano  implements Beverage{
    @Override
    public String getName() {
        return "Americano";
    }

    @Override
    public int getPrice() {
        return 5000;
    }
}
