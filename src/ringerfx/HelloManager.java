package ringerfx;

import java.io.IOException;

import javafx.scene.control.Alert;
import lombok.Data;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;

@Data
public class HelloManager {
    private ManagerConnection managerConnection;
    static String number;
    static String chanNumb = "";

    public void setNumb(String number) {
        this.number = number;
    }

    public HelloManager() throws IOException {
        ManagerConnectionFactory factory = new ManagerConnectionFactory("0.0.0.0", "0000", "000000");
        this.managerConnection = factory.createManagerConnection();
    }

    public void run() {
        try {
            System.out.println("numb"+number);
            OriginateAction originateAction;
            ManagerResponse originateResponse;
            originateAction = new OriginateAction();
            originateAction.setChannel("SIP/"+chanNumb);
            originateAction.setContext("new_bd_context");
            originateAction.setExten(number);
            originateAction.setPriority(new Integer(1));
            originateAction.setTimeout(new Integer(30000));
            managerConnection.login();
            originateResponse = managerConnection.sendAction(originateAction, 30000);
            System.out.println("Enter Response=" + originateResponse.getResponse());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Звонок успешно переведен на ваш телефон");
            alert.showAndWait();
            managerConnection.logoff();
        }catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Подключение к сервису Телефонии отсутствует! Проверьте сетевое подключение или обратитесь в службу технической поддержки");
            alert.showAndWait();
            System.out.println(e);
        }
    }
} 