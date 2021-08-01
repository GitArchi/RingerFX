package ringerfx;

import lombok.Data;

@Data
public class DataTransferObject {
    static String loginTransfer = "";
    static String passwordTransfer = "";
    static String titleTransfer = "";
    static String idRequestTransfer = "";
    static String phoneTransfer = "";

    public void print() {
        System.out.println(loginTransfer + passwordTransfer + titleTransfer + idRequestTransfer);
    }

}
