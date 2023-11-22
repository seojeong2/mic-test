import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class MicEndTest {

    private static TargetDataLine line;
    private static ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    private static AtomicBoolean recording = new AtomicBoolean(true);



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
            AudioFormat format = new AudioFormat(16000,16,1,true,false);

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

            File audioFile = new File("/Users/seojeong/Desktop/mic_test/wav/seojeong_test.wav");

            // 녹음 스레드 시작
            new Thread(()->{
                try{
                    AudioInputStream audioInputStream = new AudioInputStream(line);
                    while (recording.get()) {
                        AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                    }

                    System.out.println("음성 데이터를 파일로 저장완료");

                    line.stop();
                    line.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }).start();


            // 일정 시간 후에 녹음 중지
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    recording.set(false);
                    timer.cancel();
                }
            },5000);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void saveRecording(String filePath) {

        try {
            byte[] audioData = byteArrayOutputStream.toByteArray();
            if (audioData.length > 0) {  // 오디오 데이터가 존재하는 경우에만 저장
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(audioData);
                AudioInputStream audioInputStream = new AudioInputStream(byteArrayInputStream, line.getFormat(), audioData.length / line.getFormat().getFrameSize());

                File audioFile = new File(filePath);
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, audioFile);
                System.out.println("Recording saved to: " + audioFile.getAbsolutePath());

                byteArrayInputStream.close();
            } else {
                System.out.println("No audio data recorded.");
            }

            byteArrayOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
