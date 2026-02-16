package LABA_2;

abstract class Peripheral {
    private String brand;
    private  Double price;
    private String connectionType;

    private static int objectCounter = 0;

    public Peripheral() {
        this("Unknown", 0.0, "USB");
    }

    public Peripheral(String brand, Double price, String connectionType){
        this.brand = brand;
        this.price = price;
        this.connectionType = connectionType;
        objectCounter++;
    }

    public abstract String deviceInfo();
    public abstract String use();

    public String getBrand(){
        return brand;
    }

    public Double getPrice(){
        return price;
    }

    public String getConnectionType(){
        return connectionType;
    }

    public void setBrand(String brand){
        this.brand = brand;
    }

    public void setPrice(Double price){
        if (price > 0){
            this.price = price;

        } else {
            System.out.println("ЦЕНА ДОЛЖНА БЫТЬ ПОЛОЖИТЕЛЬНОЙ");
        }
    }

    public void setConnectionType(String connectionType){
        this.connectionType = connectionType;
    }

    public static void showObjectCounter() {
        System.out.println("Всего создано объектов периферии: " + objectCounter);
    }

}






