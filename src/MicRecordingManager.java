import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MicRecordingManager {

    public static final int recordDuration = 15;

    public static final int authDuration = 10;
    
    public static void main(String[] args) {
        Mixer.Info desiredMicrophone = findDesiredMicrophone();

        if (desiredMicrophone != null) {
            System.out.println("사용할 마이크 정보: " + desiredMicrophone.getName());

            // 마이크 정보를 기반으로 녹음 설정 및 시작
            startRecording(desiredMicrophone, recordDuration,"seojeong","registration");
        } else {
            System.out.println("사용 가능한 마이크를 찾을 수 없습니다.");
        }
    }


    // 마이크 정보 찾기
    public static Mixer.Info findDesiredMicrophone() {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();

        for(Mixer.Info info: mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);

            if(mixer.isLineSupported(new TargetDataLine.Info(TargetDataLine.class,null))) {
                System.out.println("마이크 정보: " + info.getName());
                System.out.println("마이크 설명: " + info.getDescription());

                return info;
            }
        }
        return null;
    }

    public static void startRecording(Mixer.Info mixerInfo, int maxDuration, String userId, String reqType) {

        String audioFileType = reqType == "registration" ? "reg" : "auth";
        AudioFormat format = new AudioFormat(44100,16,1,true,false);
        DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

        Mixer mixer = AudioSystem.getMixer(mixerInfo);

        try{
//            Mixer mixer = AudioSystem.getMixer(mixerInfo);
//            TargetDataLine line = (TargetDataLine) mixer.getLine(new TargetDataLine.Info(TargetDataLine.class,null));
            TargetDataLine line = (TargetDataLine) mixer.getLine(info);

//            AudioFormat format = new AudioFormat(44100,16,1,true,false);
            line.open(format); // 마이크 라인 열기
            System.out.println("녹음을 시작합니다. " + maxDuration +"초 동안 녹음됩니다.");
            line.start(); // 마이크 시작


            //String localPath = "/Users/seojeong/Downloads/demo/wav/" + userId + "_reg_audio.wav"; // 녹음 음성 저장 경로
            String localPath = "/Users/seojeong/Desktop/mic_test/wav/" + userId + "_" + audioFileType +"_audio.wav";


           // Thread.sleep(maxDuration * 1000);



            // 녹음된 데이터를 파일로 저장
            File audioFile = new File(localPath);
            AudioInputStream audioInputStream = new AudioInputStream(line);
            AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE,audioFile);
            audioInputStream.close();
            System.out.println("음성 파일이 성공적으로 저장되었습니다.");

            line.stop();
            line.close();

        } catch ( LineUnavailableException  | IOException e) {
            e.printStackTrace();
        }
    }
}
