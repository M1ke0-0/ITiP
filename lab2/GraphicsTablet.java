package LABA_2;

public class GraphicsTablet extends Peripheral{
    private String activeArea;
    private int penPressure;

    public GraphicsTablet() {
        this("Unknown", 0.0, "USB", "10x6 дюймов", 2048);
    }

    public GraphicsTablet(String brand, double price, String connectionType, String activeArea, int penPressure) {
        super(brand, price, connectionType);
        this.activeArea = activeArea;
        this.penPressure = penPressure;
    }

    @Override
    public String deviceInfo() {
        return "Графический планшет: " + getBrand() + ", Цена: " + getPrice() + " руб., Подключение: "
                + getConnectionType() + ", Рабочая область: " + activeArea + ", Уровни давления пера: " + penPressure;
    }

    @Override
    public String use() {
        return "Графический планшет работает";
    }

    public String getActiveArea() {
        return activeArea;
    }

    public int getPenPressure() {
        return penPressure;
    }

    public void setActiveArea(String activeArea) {
        this.activeArea = activeArea;
    }

    public void setPenPressure(int penPressure) {
        this.penPressure = penPressure;
    }

}
