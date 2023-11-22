import javax.sound.sampled.*;
import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

public class FindMic {

    public static int maxDuration = 10;
    private static TargetDataLine line;

    public static void main(String[] args) {

        // Get available mixers
        Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();


        //Mixer.Info[] mixerInfo = AudioSystem.getMixerInfo();

        System.out.println("Available Audio Devices:");
        for (Mixer.Info info : mixerInfo) {
            System.out.println("Name: " + info.getName());
            System.out.println("Description: " + info.getDescription());
            System.out.println("Vendor: " + info.getVendor());
            System.out.println("Version: " + info.getVersion());
            System.out.println("-----");
        }

        // Select the desired mixer (you might need to modify this based on your actual mixer names)
        Mixer mixer = null;
        for (Mixer.Info info : mixerInfo) {
            if (info.getName().contains("Usb Audio Device")) {
                mixer = AudioSystem.getMixer(info);
                break;
            }
        }

        if (mixer != null) {
            // Use the USB microphone
            System.out.println("Using USB Microphone: " + mixer.getMixerInfo().getName()); // usb마이크 정보, 이름으로 가져오기
            captureAudio(mixer);
        } else {
            // Use the default (internal) microphone
            System.out.println("Using Internal Microphone");
            captureAudio(null);
        }
    }
    private static void captureAudio(Mixer mixer) {
        try {
            //AudioFormat format = new AudioFormat(44100, 16, 2, true, true);
            //AudioFormat format = new AudioFormat(16000,16,1,true,false);
            AudioFormat format = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED,44100,16,1,2*1,16000,true);

            DataLine.Info targetInfo = new DataLine.Info(TargetDataLine.class, format);
            TargetDataLine targetLine;

            if (mixer != null) {
                // Use the selected mixer
                line = (TargetDataLine) mixer.getLine(targetInfo);
            } else {
                // Use the default mixer
                line = (TargetDataLine) AudioSystem.getLine(targetInfo);
            }

            line.open(format);
            line.start();



            System.out.println("음성 녹음 시작...");


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
            File audioFile = new File("/Users/seojeong/Desktop/mic_test/wav/seojeong_1_441.wav");
            AudioSystem.write(audioInputStream,AudioFileFormat.Type.WAVE, audioFile);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void stopRecording() {
        line.stop();
        line.close();
    }
}
