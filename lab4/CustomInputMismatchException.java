package lab4;

public class CustomInputMismatchException extends Exception {
    private String inputValue;

    public CustomInputMismatchException(String message) {
        super(message);
    }

    public CustomInputMismatchException(String message, String inputValue) {
        super(message);
        this.inputValue = inputValue;
    }

    public String getInputValue() {
        return inputValue;
    }

    @Override
    public String toString() {
        return "CustomInputMismatchException: " + getMessage() +
                (inputValue != null ? " (введено: '" + inputValue + "')" : "");
    }
}