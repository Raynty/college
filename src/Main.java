import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/college?useSSL=false&serverTimezone=Europe/Moscow", "root", "12345678")) {
            // ЗАДАНИЕ 1
            System.out.println("Задание 1. Запрос по фамилии студента, какие у него оценки.\nВведите фамилию студента:");
            Scanner scanner = new Scanner(System.in);
            String surname = scanner.nextLine();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_student FROM student WHERE surname='"+surname+"';");
            int columnCount = resultSet.getMetaData().getColumnCount();
            int student_id = 0;
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    student_id += resultSet.getInt(i);
                }
            }
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT id_grades FROM record WHERE id_student="+student_id+";");
            columnCount = resultSet.getMetaData().getColumnCount();
            System.out.println("Оценки ученика с фамилией " + surname + ":");
            while (resultSet.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    Statement statement1 = connection.createStatement();
                    ResultSet resultSet1 = statement1.executeQuery("SELECT grades FROM `grades` WHERE id_grades="+resultSet.getInt(i)+";");
                    int columnCount1 = resultSet1.getMetaData().getColumnCount();
                    while (resultSet1.next()) {
                        for (int j = 1; j <= columnCount1; j++) {
                            System.out.print(resultSet1.getString(j)+", ");
                        }
                    }
                }
            }

            // ЗАДАНИЕ 2
            System.out.println("\n\nЗадание 2. Вывести зачётку полностью и БЕЗ ID.");
            System.out.println("Оценки из всей зачётки:");
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM `record`;");
            while (resultSet.next()) {
                Statement statement1 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery("SELECT surname,name,lastname FROM student WHERE id_student="+resultSet.getInt(2)+";");
                resultSet1.next();
                System.out.print(resultSet1.getString(1)+" "+resultSet1.getString(2)+" "+resultSet1.getString(3) + " - ");

                statement1 = connection.createStatement();
                resultSet1 = statement1.executeQuery("SELECT grades FROM `grades` WHERE id_grades="+resultSet.getInt(4)+";");
                while (resultSet1.next()) {
                    System.out.print(resultSet1.getString(1) + ".");
                }

                statement1 = connection.createStatement();
                resultSet1 = statement1.executeQuery("SELECT discipline_name FROM discipline WHERE id_discipline="+resultSet.getInt(3)+";");
                while (resultSet1.next()) {
                    System.out.print(" Дисциплина: " + resultSet1.getString(1) + ".");
                }

                statement1 = connection.createStatement();
                resultSet1 = statement1.executeQuery("SELECT name,lastname FROM teacher WHERE id_teacher="+resultSet.getInt(6)+";");
                while (resultSet1.next()) {
                    System.out.print(" Преподаватель: " + resultSet1.getString(1) + " " + resultSet1.getString(2) + ".");
                }
                System.out.println();
            }

            // ЗАДАНИЕ 3
            System.out.println("\n\nЗадание 3. Запрос по фамилии преподавателя, какие предметы у него.");
            System.out.println("Введите фамилию преподавателя:");
            surname = scanner.nextLine();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name,lastname,id_teacher FROM teacher WHERE surname='"+surname+"';");
            Statement statement1 = connection.createStatement();
            resultSet.next();
            ResultSet resultSet1 = statement1.executeQuery("SELECT discipline_name FROM discipline WHERE id_teacher=" + resultSet.getInt(3) + ";");
            while (resultSet1.next()) {
                System.out.println(surname + " " + resultSet.getString(1) + " " + resultSet.getString(2) + " преподаёт дисциплину - " + resultSet1.getString(1));
            }

            // ЗАДАНИЕ 4
            System.out.println("\n\nЗадание 4. Запрос по фамилии преподавателя, какие выставил оценки и по каким предметам.");
            System.out.println("Введите фамилию преподавателя:");
            surname = scanner.nextLine();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT name,lastname,id_teacher FROM teacher WHERE surname='"+surname+"';");
            resultSet.next();
            String teacher = surname + " " + resultSet.getString(1) + " " + resultSet.getString(2);
            statement1 = connection.createStatement();
            resultSet1 = statement1.executeQuery("SELECT id_discipline,id_grades,id_student FROM record WHERE id_teacher=" + resultSet.getInt(3) + ";");
            while (resultSet1.next()) {
                Statement statement2 = connection.createStatement();
                ResultSet resultSet2 = statement2.executeQuery("SELECT discipline_name FROM discipline WHERE id_discipline="+resultSet1.getInt(1)+";");
                resultSet2.next();
                String discipline_name = resultSet2.getString(1);

                statement1 = connection.createStatement();
                resultSet2 = statement1.executeQuery("SELECT grades FROM `grades` WHERE id_grades="+resultSet1.getInt(2)+";");
                resultSet2.next();
                String grades = resultSet2.getString(1);

                statement1 = connection.createStatement();
                resultSet2 = statement1.executeQuery("SELECT surname,name,lastname FROM student WHERE id_student="+resultSet1.getInt(3)+";");
                resultSet2.next();
                String student = resultSet2.getString(1) + " " + resultSet2.getString(2) + " " + resultSet2.getString(3);

                System.out.println(teacher + " поставил(а) оценку студенту(-ке) " + student + " по дисциплине " + discipline_name + " - " + grades);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}