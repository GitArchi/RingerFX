package ringerfx;

import javafx.scene.control.Alert;
import lombok.Data;
import java.util.logging.*;
import java.sql.*;
import java.util.ArrayList;

@Data
public class RequestDb {

    private Connection con;
    private Statement stmtFetch;
    private Statement stmtCheck;
    private Statement stmtInsert;
    private String[] array = new String[5];
    private int i = 0;

    //Get Logger
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

    public void connect() {
        LOGGER.log(Level.INFO,"Inint connect to DB");
        con = null;
        try {
            Class.forName("org.postgresql.Driver");
            con = DriverManager.getConnection("jdbc:postgresql://0.0.0.0:5432/0000", "0000", "0000");
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("DB");
            alert.setHeaderText(null);
            alert.setContentText("Подключение к базе данных отсутсвует! Проверьте сетевое подключение или обратитесь в службу технической поддержки");
            alert.showAndWait();
            System.out.println(e);
            LOGGER.log(Level.WARNING,"ERROR! Connect to DB not success"+e);
        }
    }

    public ArrayList<String> fetchFavorite(String id) throws SQLException {
        stmtFetch = con.createStatement();
        ResultSet resultSetFetch = stmtFetch.executeQuery("select data_name from ringer_setting where id_setting =" + "'" + id + "'");
        ArrayList<String> arr = new ArrayList<>();
        while (resultSetFetch.next()) {
            arr.add(resultSetFetch.getString("data_name"));
        }
        con.close();
        return arr;
    }

    public String[] checkUsers(String name) throws SQLException {
        stmtCheck = con.createStatement();
        ResultSet resultSetCheck = stmtCheck.executeQuery("select namenk,passwordnk,title,setting_id,phone from ringer_user where namenk = " + "'" + name + "'");

        while (resultSetCheck.next()) {
            array[0] = resultSetCheck.getString("namenk");
            array[1] = resultSetCheck.getString("passwordnk");
            array[2] = resultSetCheck.getString("title");
            array[3] = resultSetCheck.getString("setting_id");
            array[4] = resultSetCheck.getString("phone");
        }
        con.close();
        return array;
    }

    public void insertDataToDb(String sourceData, String idRequest) throws SQLException {
        stmtInsert = con.createStatement();
        try {
            System.out.println("INSERT INTO ringer_setting (id_setting,data_name) VALUES (" + idRequest + "," + "'" + sourceData + "')");
            stmtInsert.execute("INSERT INTO ringer_setting (id_setting,data_name) VALUES (" + idRequest + "," + "'" + sourceData + "')");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        con.close();
    }

    public void deleteDataToDb(String sourceData, String id) throws SQLException {
        stmtInsert = con.createStatement();
        try {
            System.out.println(sourceData);
            stmtInsert.execute("DELETE FROM ringer_setting WHERE data_name = " + "'" + sourceData + "'and id_setting =" + "'" + id + "'");
        } catch (Exception e) {
        }
        con.close();
    }
}
