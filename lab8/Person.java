package lab8;

/**
 * Класс Person - это как анкета человека с основной информацией
 *
 * Представь, что это карточка сотрудника с его именем, возрастом и зарплатой.
 * Мы будем использовать эти карточки для обработки данных о людях.
 */
public class Person {
    private String name;    // Имя человека (например, "Иван")
    private int age;        // Возраст (например, 25)
    private double salary;  // Зарплата (например, 50000.0)

    /**
     * Конструктор - это как заполнение новой анкеты
     * Когда создаем нового человека, сразу записываем его данные
     */
    public Person(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    // Геттеры - это как "окошки", через которые можно посмотреть данные
    // Сеттеры - это как "дверцы", через которые можно изменить данные

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    /**
     * Метод toString() - это как "представиться"
     * Когда мы хотим показать информацию о человеке в читаемом виде
     */
    @Override
    public String toString() {
        return String.format("Person{name='%s', age=%d, salary=%.2f}",
                           name, age, salary);
    }
}
