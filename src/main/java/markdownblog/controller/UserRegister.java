package markdownblog.controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "UserRegister", value = "/UserRegister")
public class UserRegister extends HttpServlet {
    private PreparedStatement preparedStatement;
    PrintWriter out;
    public void init() throws ServletException{
        initializeJDBC();
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String userPassword = request.getParameter("userPassword");
//        System.out.println(userId+userName+userPassword);
        storeUserData(userId,userName,userPassword);
        out = response.getWriter();
        out.println("<html><body>");
        out.println("Data below had been stored:"+userId+" "+userName);
        out.println("<a href=\"index.jsp\">Go back to landing page</a>");
        out.println("</body></html>");
        out.close();
//        request.getRequestDispatcher("index.jsp").forward(request, response);
        //notworking...
    }

    private void initializeJDBC(){
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            System.out.println("Driver loaded...");

            Connection conn = DriverManager.getConnection("jdbc:sqlserver://127.0.0.1:1433;databaseName=MarkDown;encrypt=true;trustServerCertificate=true;","tsai","tsai1999");
            System.out.println("Database connected...");
            preparedStatement = conn.prepareStatement(" INSERT INTO dbo.[User] ( userId , userName , userPassword , modifyTime) VALUES  ( ? , ? , ? , GETDATE() ) ");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void storeUserData(String userId, String userName, String userPassword) {
        try {
            preparedStatement.setString(1, userId);
            preparedStatement.setString(2, userName);
            preparedStatement.setString(3, userPassword);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
