package pagingjsp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    private static Student getStudentFrom(ResultSet resultSet) throws SQLException {
        Student student = new Student();
        student.setId(resultSet.getInt("id"));
        student.setStudentNumber(resultSet.getString("studentNumber"));
        student.setName(resultSet.getString("name"));
        student.setDepartmentId(resultSet.getInt("departmentId"));
        student.setYear(resultSet.getInt("year"));
        student.setDepartmentName(resultSet.getString("departmentName"));
        return student;
    }

    public static List<Student> findAll() throws Exception {
        String sql = "SELECT s.*, d.departmentName" +
                     " FROM student s LEFT JOIN department d ON s.departmentId = d.id";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            ArrayList<Student> list = new ArrayList<Student>();
            while (resultSet.next())
                list.add(getStudentFrom(resultSet));
            return list;
        }
    }

    public static List<Student> findAll(int currentPage, int pageSize) throws Exception {
        String sql = "SELECT s.*, d.departmentName" +
                     " FROM student s LEFT JOIN department d ON s.departmentId = d.id" +
                     " LIMIT ?, ?";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, (currentPage - 1) * pageSize);
            statement.setInt(2, pageSize);
            try (ResultSet resultSet = statement.executeQuery()) {
                ArrayList<Student> list = new ArrayList<Student>();
                while (resultSet.next())
                    list.add(getStudentFrom(resultSet));
                return list;
            }
        }
    }

    public static int count() throws Exception {
        String sql = "SELECT COUNT(*) FROM student";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return resultSet.getInt(1);
        }
        return 0;
    }

    public static Student findByStudentNumber(String studentNumber) throws Exception {
        String sql = "SELECT s.*, d.departmentName" +
                     " FROM student s LEFT JOIN department d ON s.departmentId = d.id" +
                     " WHERE s.studentNumber = ?";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, studentNumber);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return getStudentFrom(resultSet);
                else
                    return null;
            }
        }
    }

    public static Student findById(int id) throws Exception {
        String sql = "SELECT s.*, d.departmentName " +
                     " FROM student s LEFT JOIN department d ON s.departmentId = d.id" +
                     " WHERE s.id = ?";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next())
                    return getStudentFrom(resultSet);
                else
                    return null;
            }
        }
    }

    public static void insert(Student student) throws Exception {
        String sql = "INSERT student (studentNumber, name, departmentId, year)" +
                     " VALUES (?, ?, ?, ?)";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getStudentNumber());
            statement.setString(2, student.getName());
            statement.setInt(3, student.getDepartmentId());
            statement.setInt(4, student.getYear());
            statement.executeUpdate();
        }
    }


    public static void update(Student student) throws Exception {
        String sql = "UPDATE student SET studentNumber=?, name=?, departmentId=?, year=? " +
                     " WHERE id = ?";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, student.getStudentNumber());
            statement.setString(2, student.getName());
            statement.setInt(3, student.getDepartmentId());
            statement.setInt(4, student.getYear());
            statement.setInt(5, student.getId());
            statement.executeUpdate();
        }
    }

    public static void delete(int id) throws Exception {
        String sql = "DELETE FROM student WHERE id = ?";
        try (Connection connection = DB.getConnection("student1");
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
   }

}
