import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.util.Scanner;

public class Main {

    private static final String url="jdbc:mysql://localhost:3306/Hotel_db";
    private static final String username ="root";
    private static final String password="root";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Connecting to driver...");
        }catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        }

        try{
            Connection con = DriverManager.getConnection(url,username,password);
            while(true){
                System.out.println();
                System.out.println("HOTEL MANAGEMENT SYSTEM");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room: ");
                System.out.println("2. View reservations: ");
                System.out.println("3. Get room number: ");
                System.out.println("4. Update Reservation: ");
                System.out.println("5. Delete Reservation: ");
                System.out.println("6. Exit: ");
                System.out.println("Enter your choice: ");
                int choice = sc.nextInt();
                switch(choice){
                    case 1:
                        reserveRoom(con,sc);
                        break;
                    case 2:
                        viewReservations(con);
                        break;
                    case 3:
                        getRoomNumber(con,sc);
                        break;
                    case 4:
                        updateReservation(con,sc);
                        break;
                    case 5:
                        deleteReservation(con,sc);
                        break;
                    case 6:
                        exit();
                        sc.close();
                        return;

                    default:
                        System.out.println("Invalid choice Try again");
                }
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        catch(InterruptedException e){
            throw new RuntimeException(e);
        }

    }

    private static void reserveRoom(Connection con, Scanner sc){
        try{
            System.out.println("Enter guest name: ");
            String guestName= sc.next();
            sc.nextLine();
            System.out.println("Enter room number: ");
            int roomNumber = sc.nextInt();
            System.out.println("Enter contact number: ");
            String contactNumber= sc.next();

            String sql = "INSERT INTO reservations (guest_name, room_no, contact_no) " +
                    "VALUES('" + guestName + "' , " + roomNumber + " , '" + contactNumber + "')";

            try(Statement stmt= con.createStatement()) {
                int rowsaffected = stmt.executeUpdate(sql);

                if (rowsaffected > 0) {
                    System.out.println("Reservation successfully");
                } else {
                    System.out.println("Reservation failed");
                }
            }
        } catch(SQLException e){
                e.printStackTrace();
              }



    }

    private static void viewReservations(Connection con) throws SQLException{
       String sql = "SELECT reservation_id, guest_name, room_no, contact_no, reservation_date FROM reservations";

       try (Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql)){

           System.out.println();
           System.out.println("+--------------------+----------------+-------------+------------------+------------------------+");
           System.out.println(" Reservation ID      | Guest          |Room Number  | Contact Number   |     Reservation Date    ");
           System.out.println("+--------------------+----------------+-------------+------------------+------------------------+");

           while(rs.next()) {
               int reservationId = rs.getInt("reservation_id");
               String guestName = rs.getString("guest_name");
               int roomNumber = rs.getInt("room_no");
               String contactNumber = rs.getString("contact_no");
               String reservationDate = rs.getTimestamp("reservation_date").toString();

               System.out.printf("| %-16d   |%-15s  | %-9d  | %-15s  | %-20s |\n",
                       reservationId, guestName, roomNumber, contactNumber, reservationDate);

           }

           System.out.println("+--------------------+----------------+-------------+------------------+------------------------+");

       }
       catch(SQLException e){
           System.out.println(e.getMessage());
       }
    }

    private static void getRoomNumber(Connection con, Scanner sc){
      try{
          System.out.println("Enter reservation Id: ");
          int reservationId = sc.nextInt();
          System.out.println("Enter guest Name");
          String guestName = sc.next();

          String sql ="SELECT room_no FROM reservations " +
                  "WHERE reservation_id = " + reservationId +
                  " AND guest_name = '" + guestName + "'";

          try(Statement stmt = con.createStatement();
              ResultSet rs = stmt.executeQuery(sql)) {

              if(rs.next()){
                  int roomNo = rs.getInt("room_no");
                  System.out.println("Room number for reservation Id" + reservationId  +
                          " and guest " + guestName + " is: " +roomNo );

              } else{
                  System.out.println("Reservation not found for the given Id and guest name");
              }

          }
      }
      catch(SQLException e) {
          e.printStackTrace();
      }
    }

    private static void updateReservation(Connection con, Scanner sc){
      try{
          System.out.println("Enter reservation Id to update");
          int reservationId = sc.nextInt();
          sc.nextLine();

          if (!reservationExists(con, reservationId)){
              System.out.println("Reservation not found for the given ID");
              return;
          }
          System.out.println("Enter new guest name: ");
          String newGuestName = sc.nextLine();
          System.out.println("Enter new room number");
          int newRoomNumber = sc.nextInt();
          System.out.println("Enter new contact number");
          String newContactNumber = sc.next();

          String sql="UPDATE reservations SET guest_name = '" + newGuestName + "', "+
                     "room_no = " +newRoomNumber+ ", " +
                    "contact_no ='" +newContactNumber+ "' " +
                  "WHERE reservation_id = " +reservationId;

          try(Statement stmt = con.createStatement()) {
              int rowsaffected = stmt.executeUpdate(sql);

              if (rowsaffected > 0) {
                  System.out.println("Reservation Updated successfully");

              }else {
                  System.out.println("Reservation update Failed");
              }
          }


      } catch (SQLException e){
          e.printStackTrace();
      }

    }

    private static void deleteReservation(Connection con, Scanner sc) {
        try {
            System.out.print("Enter Reservation Id to delete");
            int reservationId = sc.nextInt();

            if (!reservationExists(con, reservationId)) {
                System.out.println("Reservation not found for the given ID");
                return;

            }

            String sql = "DELETE FROM reservations WHERE reservation_id = " + reservationId;

            try (Statement stmt = con.createStatement()) {
                int rowsaffected = stmt.executeUpdate(sql);

                if (rowsaffected > 0) {
                    System.out.println("Reservation Deleted successfully");
                } else {
                    System.out.println("Reservation delete Failed");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private static boolean reservationExists(Connection con, int reservationId) {
        try{
            String sql ="SELECT reservation_id FROM reservations where reservation_id = " +reservationId;

            try(Statement stmt = con.createStatement();
            ResultSet rs= stmt.executeQuery(sql)) {
                return rs.next();
            }
        }catch(SQLException e){
            e.printStackTrace();
            return false;

        }
    }

    public static void exit() throws InterruptedException {
        System.out.print("Exiting System");
        int i= 5;
        while (i!=0) {
            System.out.print(".");
            Thread.sleep(450);
            i--;
        }
        System.out.println();
        System.out.println("Thank you for using HOTEL MANAGEMENT SYSTEM!!!");
    }
}
