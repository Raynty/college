import java.sql.*;
import java.util.Random;

public class MY_SQL {
    private String url = "jdbc:mysql://localhost:3306/college2?useSSL=false&serverTimezone=Europe/Moscow";
    private String user = "root";
    private String pass = "12345678";
    private Connection connection = null;

    public boolean openConnection() {
        try {
            this.connection = DriverManager.getConnection(url, user, pass);
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void closeConnection() {
        try {
            Statement statement = this.connection.createStatement();
            statement.execute("DELETE FROM `variants`;");
            statement.close();
            this.connection.close();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }
    public Connection getConnection() {
        return this.connection;
    }

    public boolean sendVariants() {
        try {
            this.connection.createStatement().execute("DELETE FROM `variants`;");
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT id_student,surname,name,lastname FROM `students`;");
            while(resultSet.next()) {
                int variant = (new Random().nextInt(10)+1);
                PreparedStatement stmt_variants = connection.prepareStatement("INSERT INTO `variants` (id_student, variant_number) VALUES (?, ?);");
                stmt_variants.setInt(1, resultSet.getInt(1));
                stmt_variants.setInt(2, variant);
                stmt_variants.executeUpdate();
                System.out.println("Студенту " + resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4) + " выпал вариант №" + variant);
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getSortedStudents() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT surname,name,lastname FROM `students` ORDER BY surname,name,lastname;");
            System.out.println("Отсортированный список студентов:");
            while (resultSet.next()) {
                System.out.println(resultSet.getString(1) + " " + resultSet.getString(2) + " " + resultSet.getString(3));
            }
            return true;
        }catch(SQLException e){
            e.printStackTrace();
            return false;
        }
    }
    public boolean getGroupedStudents() {
        try {
            Statement statement = this.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM `variants` ORDER BY `variant_number`;");
            System.out.println("Список студентов, сгруппированных по варианту:");
            int current_variant = 0;
            while (resultSet.next()) {
                int id = resultSet.getInt(1);
                int variant = resultSet.getInt(2);
                if (current_variant != variant) {
                    current_variant = variant;
                    System.out.println("\nСтуденты с вариантом №" + variant + ":");
                }
                PreparedStatement stmt_student = this.connection.prepareStatement("SELECT surname,name,lastname FROM `students` WHERE id_student = ?;");
                stmt_student.setInt(1, id);
                ResultSet resultSet_student = stmt_student.executeQuery();
                resultSet_student.next();
                System.out.println(resultSet_student.getString(1) + " " + resultSet_student.getString(2) + " " + resultSet_student.getString(3));
            }
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean getVariantBySurname(String surname) {
        try {
            PreparedStatement statement = this.connection.prepareStatement("SELECT id_student,surname,name,lastname FROM `students` WHERE surname = ?;");
            statement.setString(1, surname);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            int id_student = resultSet.getInt(1);
            String student = resultSet.getString(2) + " " + resultSet.getString(3) + " " + resultSet.getString(4);

            PreparedStatement stmt_variant = this.connection.prepareStatement("SELECT variant_number FROM `variants` WHERE id_student = ?;");
            stmt_variant.setInt(1, id_student);
            ResultSet resultSet_variant = stmt_variant.executeQuery();
            resultSet_variant.next();
            System.out.println("Варианта студента (" + student + ") - №" + resultSet_variant.getInt(1));
            return true;
        }catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public boolean getStudentsByVariantNumber(int variantNumber) {
        try {
            System.out.println("Список студентов с вариантом №" + variantNumber + ":");
            PreparedStatement statement = this.connection.prepareStatement("SELECT id_student FROM `variants` WHERE variant_number = ?;");
            statement.setInt(1, variantNumber);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                PreparedStatement stmt_student = this.connection.prepareStatement("SELECT surname,name,lastname FROM `students` WHERE id_student = ?;");
                stmt_student.setInt(1, resultSet.getInt(1));
                ResultSet resultSet_student = stmt_student.executeQuery();
                resultSet_student.next();
                System.out.println(resultSet_student.getString(1) + " " + resultSet_student.getString(2) + " " + resultSet_student.getString(3));
            }
            return true;
        }catch(SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}