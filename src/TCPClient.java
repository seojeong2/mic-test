import jdk.internal.util.xml.impl.Input;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.Socket;


public class TCPClient {

    public static void main(String[] args) {

        //String serverHost = "43.202.10.41"; // 원격 sv 서버의 호스트 주소
        String serverHost = "43.202.161.88"; // 원격 sc 서버의 호스트 주소
        int serverPort = 9101; // 원격 서버의 포트 번호

        try (Socket socket = new Socket(serverHost, serverPort);
             OutputStream out = socket.getOutputStream();

             ) {

            JSONObject jsonObj = new JSONObject();
            //jsonObj.put("wavPath","/home/ec2-user/sample/69_female_po2113_441.wav");


            // 20대 여성
            //jsonObj.put("wavPath","/home/ec2-user/sample/20_female.wav");

            // 60대이상 여성
            //jsonObj.put("wavPath","/home/ec2-user/sample/69_female_po2113_441.wav");


            // 60대이상 남성
//            jsonObj.put("wavPath","/home/ec2-user/sample/75_male_08231_441.wav");

            // 20대 이하 여자
            jsonObj.put("wavPath","/home/ec2-user/wav_dir/park_classifi_audio.wav");


            String jsonString = jsonObj.toString();

            System.out.println("json body string: " + jsonString);
            int jsonLen = jsonString.length(); // json의 길이


            // RequestHeader
            //String apiCode = "1000"; // sv 등록요청
            String apiCode = "2000"; // 화자분류 남녀노소 결과 추출

            String resultCode = "0000";
            String bodySize = String.format("%08d", jsonString.length());


            String header = apiCode + resultCode + bodySize;

            // 전체 요청 메시지 생성
            String requestMessage = header + jsonString;
            System.out.println("최종요청 스트링: " + requestMessage);

            // 요청 메시지 전송
            out.write(requestMessage.getBytes());


            InputStream in = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String response = br.readLine();
            System.out.println("response: " + response);

            if (response != null) {
                String responseApiCode = response.substring(0,4);
                String responseResultCode = response.substring(4,8);
                String responseBodySize = response.substring(8,16);

                String responseBody = response.substring(16);

                System.out.println("responseApiCode: " + responseApiCode);
                System.out.println("responseResultCode: " + responseResultCode);
                System.out.println("responseBodySize: " + responseBodySize);

                System.out.println("responseBody: " + responseBody);
            } else {
                System.out.println("response is null");
            }

            socket.close();
            out.close();
            in.close();
            jsonObj.clear();
            jsonObj = null;


        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}

