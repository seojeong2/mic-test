import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class AudioRecordTest {

    public static int maxDuration = 10;

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
                line = (TargetDataLine) mixer.getLine(new TargetDataLine.Info(TargetDataLine.class,null));

                // 오디오 포맷 설정
//                AudioFormat format = new AudioFormat(44100,16,1,true,false);
                AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,16000,16,1,2*1,16000,true);

                // 마이크 라인 열기
                line.open(format);
                line.start();

                System.out.println("마이크 입력 시작... " + maxDuration +" 초 동안 녹음됩니다.");

                // 마이크 입력 시작
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        // 녹음 중지

                        stopRecording();
                        timer.cancel();
                    }
                }, maxDuration * 1000);

                //Thread.sleep(maxDuration * 1000);
                // 녹음 데이터 파일에 저장
                AudioInputStream audioInputStream = new AudioInputStream(line);
                File audioFile = new File("/Users/seojeong/Desktop/mic_test/wav/time_test.wav");
                AudioSystem.write(audioInputStream,AudioFileFormat.Type.WAVE, audioFile);


            } catch(LineUnavailableException | IOException e) {
                e.printStackTrace();
            }
        } else{
            System.out.println("사용 가능한 마이크를 찾지 못했습니다.");
        }
    }

    public static void stopRecording() {
        line.stop();
        line.close();
    }


}
