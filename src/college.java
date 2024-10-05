import java.util.Scanner;

public class college {
    public static void main(String[] args) {
        MY_SQL mysql = new MY_SQL();
        if (mysql.openConnection()) {
            System.out.println("Задание 1. Случайным образом распределить варианты студентам в диапазоне от 1 до 10 (включительно)\n");
            if (!mysql.sendVariants()) {
                System.out.println("Не удалось распределить варианты. Произошла ошибка!");
                mysql.closeConnection();
                return;
            }

            System.out.println("\n\nЗадание 2. Вывести отсортированный список студентов.\n");
            Scanner scanner = new Scanner(System.in);
            if (!mysql.getSortedStudents()) {
                System.out.println("Не удалось вывести список студентов. Произошла ошибка!");
                mysql.closeConnection();
                return;
            }

            System.out.println("\n\nЗадание 3. Вывести список студентов, сгруппированный по вариантам\n");
            if (!mysql.getGroupedStudents()) {
                System.out.println("Не удалось вывести список студентов. Произошла ошибка!");
                mysql.closeConnection();
                return;
            }

            System.out.println("\n\nЗадание 4. Вывести вариант студента по фамилии.\nВведите фамилию:");
            String surname = scanner.nextLine();
            System.out.println();
            if (!mysql.getVariantBySurname(surname)) {
                System.out.println("Не удалось вывести вариант студента. Произошла ошибка!");
                mysql.closeConnection();
                return;
            }

            System.out.println("\n\nЗадание 5. Вывести список студентов с определённым вариантом\nВведите номер варианта:");
            int variant = scanner.nextInt();
            System.out.println();
            if (!mysql.getStudentsByVariantNumber(variant)) {
                System.out.println("Не удалось вывести список студентов. Произошла ошибка!");
                mysql.closeConnection();
                return;
            }
            mysql.closeConnection();
        }
    }
}