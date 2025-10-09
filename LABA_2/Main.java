package LABA_2;

public class Main {

    public static void main(String[] args) {
        Keyboard k1 = new Keyboard("Logitech", 3500, "USB", 104, true);
        Headphones h1 = new Headphones("Sony", 5000, "Bluetooth", true, true);
        GraphicsTablet g1 = new GraphicsTablet("Wacom", 12000, "USB-C", "12x8 дюймов", 8192);

        System.out.println(k1.deviceInfo());
        System.out.println(k1.use());

        System.out.println(h1.deviceInfo());
        System.out.println(h1.use());

        System.out.println(g1.deviceInfo());
        System.out.println(g1.use());

        Peripheral.showObjectCounter();
    }
}
