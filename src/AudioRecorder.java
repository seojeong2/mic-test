import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Target;
import java.util.Timer;
import java.util.TimerTask;
import javax.sound.sampled.TargetDataLine;

public class AudioRecorder {

    private static TargetDataLine line;
    public static void main(String[] args) {

            // 모든 믹서 정보 가져오기
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
            Mixer.Info desiredMixerInfo = null;

            // 마이크를 찾기 위한 루프
            for (Mixer.Info info: mixerInfos) {


                Mixer mixer = AudioSystem.getMixer(info);
                // 마이크로 사용 가능한 라인인지 확인
                if(mixer.isLineSupported(new TargetDataLine.Info(TargetDataLine.class, null))) {
                    System.out.println("마이크 정보: "+ info.getName());
                    System.out.println("마이크 설명: " + info.getDescription());

                    desiredMixerInfo = info;

                    break;
                }
            }


            if (desiredMixerInfo != null) {
                System.out.println("사용할 마이크 믹서: " + desiredMixerInfo.getName());

                try{
                    Mixer mixer = AudioSystem.getMixer(desiredMixerInfo);
                    TargetDataLine line = (TargetDataLine) mixer.getLine(new TargetDataLine.Info(TargetDataLine.class,null));

                    // 오디오 포맷 설정
                    AudioFormat format = new AudioFormat(16000,16,1,true,false);

                    // 마이크 라인 열기
                    line.open(format);
                    line.start();

                    System.out.println("마이크 입력 시작...");

                    // 오디오 입력을 저장할 파일 설정(wav 형식)
                    File audioFile = new File("/Users/seojeong/Desktop/mic_test/wav/decibel_test3.wav");

                    AudioInputStream audioInputStream = new AudioInputStream(line);

                    // 파일에 저장
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);

                    System.out.println("음성 데이터를 파일로 저장했습니다.");

                    // 마이크 입력 닫기
                    line.stop();
                    line.close();


                } catch(LineUnavailableException | IOException e) {
                    e.printStackTrace();
                }
            } else{
                System.out.println("사용 가능한 마이크를 찾지 못했습니다.");
            }


    }

}
