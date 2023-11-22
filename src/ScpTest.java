import com.jcraft.jsch.*;

import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;


public class ScpTest {

    public static void main(String[] args) {

//        String remoteHost = "43.202.10.41";
//        String username = "ec2-user";
        String pemKeyPath = "/Users/seojeong/Desktop/sv_aws/ation.pem";
        //int port = 22; // ssh 포트(기본 22)

        String localFilePath = "/Users/seojeong/Desktop/mic_test/wav/recorded_audio_test.wav";
        String remoteFilePath = "ec2-user@43.202.10.41:/home/ec2-user/test/recorded_audio_test.wav";

        try {
            // scp 명령 실행
            String scpCommand = "scp -i " + pemKeyPath + " " + localFilePath + " " + remoteFilePath;

            // scp 명령 실행
            ProcessBuilder processBuilder = new ProcessBuilder("/bin/bash", "-c", scpCommand);
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("파일이 성공적으로 전송되었습니다.");
            } else {
                System.out.println("파일 전송 실패");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


        // jsch 라이브러리 이용
//
//        try {
//            JSch jsch = new JSch();
//
//            jsch.addIdentity(pemKeyPath);
//
//            Session session = jsch.getSession(username, remoteHost,port);
//            session.setConfig("StrictHostKeyChecking","no");
//
//            session.connect();
//
//            // Scp 송신 채널 생성
//            Channel channel = session.openChannel("exec");
//            ((ChannelExec) channel).setCommand("scp -i" + remoteFilePath);
//
//            // 출력 및 입력 스트림 설정
//            ChannelExec channelExec = (ChannelExec) channel;
//            channelExec.setErrStream(System.err);
//            channelExec.setInputStream(System.in);
//            channelExec.setOutputStream(System.out);
//
//            channel.connect();
//
//            // 원격 서버로 파일 전송
//            byte[] buf = new byte[1024];
//            try (FileInputStream fis = new FileInputStream(localFilePath)) {
//                BufferedOutputStream bos = new BufferedOutputStream(channelExec.getOutputStream(), 1024);
//                int len;
//                while ((len = fis.read(buf, 0, 1024)) > 0) {
//                    bos.write(buf, 0, len);
//                }
//                bos.flush();
//                bos.close();
//            }
//
//
////            ChannelSftp channel = (ChannelSftp) session.openChannel("sftp");
////            channel.connect();
////
////            // 로컬 파일 -> 원격 파일로 복사
////            channel.put(localFilePath,remoteFilePath);
//            channel.disconnect();
//            session.disconnect();
//
//            System.out.println("파일이 성공적으로 전송되었습니다.");
//
//        } catch (JSchException | IOException e) {
//            e.printStackTrace();
//        }
    }
}
