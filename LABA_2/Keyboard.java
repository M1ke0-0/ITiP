package LABA_2;

public class Keyboard extends Peripheral{

    private int keysCount;
    private boolean backlight;

    public Keyboard() {
        this("Unknown", 0.0, "USB", 104, false);
    }

    public Keyboard(String brand, double price, String connectionType, int keysCount, boolean backlight) {
        super(brand, price, connectionType);
        this.keysCount = keysCount;
        this.backlight = backlight;
    }

    @Override
    public String deviceInfo() {
        return "Клавиатура: " + getBrand() + ", Цена: " + getPrice() + " руб., Подключение: " + getConnectionType()
                + ", Клавиш: " + keysCount + ", Подсветка: " + (backlight ? "Да" : "Нет");
    }

    @Override
    public String use() {
        return "Клавиатура рабоатет";
    }

    public int getKeysCount() {
        return keysCount;
    }

    public boolean hasBacklight() {
        return backlight;
    }

    public void setKeysCount(int keysCount) {
        this.keysCount = keysCount;
    }

    public void setBacklight(boolean backlight) {
        this.backlight = backlight;
    }

}
