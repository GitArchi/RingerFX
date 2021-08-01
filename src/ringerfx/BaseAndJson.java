package ringerfx;

import javafx.scene.control.Alert;
import lombok.Data;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;

@Data
public class BaseAndJson {

    //Create logger standart util Java
    private static Logger log = Logger.getLogger("MYLOG");
    FileHandler fh;

    private String action = "xxxx";
    private String subString = "Не удалось найти запись";
    private String subStringUnreachable = "BusinessConnection Queue Is Full. Try Again Later";
    private String emplId = "";
    private String resultEncode = "";
    private String inputlineRes;
    private Boolean subStrCheck;
    private Boolean subStrCheckUnrch;
    private String userDir = "";
    private String resUserDir = "";
    private int i = 0;
    private byte[] out;
    private String s = "";

    public String baseEncode() {
        try {
            resultEncode = Base64.getEncoder().encodeToString(emplId.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            log.log(Level.ALL, "Exception: ", ex);
        }
        return resultEncode;
    }
    public void sendJson() {
        try {
            //Build string for requsting to post
            URL url = new URL("http://spkh-webax04/WebAX?Request");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Accept", "application/json");
            http.setRequestProperty("Content-Type", "application/json");
            String data = "{\"action\":\"xxxx\",\"emplId\":" + "\"" + resultEncode + "\"}";

            //Array
            out = data.getBytes();

            //Push to stream
            OutputStream stream = http.getOutputStream();
            stream.write(out);

            //Read on stream
            BufferedReader in = new BufferedReader(new InputStreamReader(http.getInputStream(), StandardCharsets.UTF_8));
            while ((s = in.readLine()) != null) {
                inputlineRes = new String(s.getBytes());
            }
            in.close();
            http.disconnect();
        } catch (Exception ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Информация");
            alert.setHeaderText(null);
            alert.setContentText("Проверьте сетевое подключение!");
            alert.showAndWait();
        }
    }
    public boolean checkResultSubString() {
        subStrCheck = inputlineRes.contains(subString);
        return subStrCheck;
    }
    public boolean checkResultSubStringUnrl() {
        subStrCheckUnrch = inputlineRes.contains(subStringUnreachable);
        System.out.println(subStrCheckUnrch);
        return subStrCheckUnrch;
    }
    public HashMap<String, String> addListView(String json) throws IOException {
        HashMap<String, String> personInfo = new HashMap<>();
        String patternName = "([А-ЯЁ][А-ЯЁ]+[\\s]?){3,}";
        String patternMobile = "[+][0-9]+";
        String patternInNumber = "\\b\\d{4}\\b";
        String patternUserDir = "\\w+";

        Pattern pUserDir = Pattern.compile(patternUserDir);
        Pattern pName = Pattern.compile(patternName);
        Pattern pMobile = Pattern.compile(patternMobile);
        Pattern pInNumber = Pattern.compile(patternInNumber);

        Matcher mName = pName.matcher(json);
        Matcher mInNumber = pInNumber.matcher(json);

        if (mName.find()) {
            personInfo.put("Name", mName.group(0));
        }
        for (Matcher mMobile = pMobile.matcher(json); mMobile.find(); i++) {
            personInfo.put("Main" + i, mMobile.group());
        }
        i = 0;
        if (mInNumber.find()) {
            personInfo.put("InNumber", mInNumber.group(0));
        }

        userDir = System.getProperty("user.dir");
        Matcher mUserDir = pUserDir.matcher(userDir);
        int i = 0;
        while (mUserDir.find() && i < 3) {
            resUserDir = mUserDir.group();
            i++;
        }


        fh = new FileHandler("C:/Users/" + resUserDir + "/RingerNK.log", true);
        log.addHandler(fh);
        log.setLevel(Level.ALL);
        log.info("Element array Person --->" + personInfo.get("Name"));
        log.info("String JSON --->" + inputlineRes);
        log.info("Directory home user --->" + inputlineRes);
        log.info("Regexp math --->" + resUserDir);
        return personInfo;
    }
}
