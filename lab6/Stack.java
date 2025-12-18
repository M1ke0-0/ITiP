package lab6;

public class Stack<T> {
    private T[] data;
    private int size;

    @SuppressWarnings("unchecked")
    public Stack(int capacity) {
        data = (T[]) new Object[capacity];
        size = 0;
    }

    public void push(T element) {
        if (size >= data.length) {
            throw new RuntimeException("Стек переполнен: невозможно добавить элемент (размер: " + size + ", вместимость: " + data.length + ")");
        }
        data[size] = element;
        size++;
    }

    public T pop() {
        if (size == 0) {
            throw new RuntimeException("Стек пуст: невозможно извлечь элемент");
        }
        size--;
        T element = data[size];
        data[size] = null;
        return element;
    }

    public T peek() {
        if (size == 0) {
            throw new RuntimeException("Стек пуст: невозможно посмотреть верхний элемент");
        }
        return data[size - 1];
    }

    public static void main(String[] args) {
        Stack<Integer> stack = new Stack<>(10);
        stack.push(1);
        stack.push(2);
        stack.push(3);
        System.out.println(stack.pop());
        System.out.println(stack.peek());
        stack.push(4);
        System.out.println(stack.pop());
    }
}
