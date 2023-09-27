import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class AudioSpeaker {

    public static void main(String[] args) {
        try {
            // 재생할 WAV 파일 경로 설정
            File wavFile = new File("/Users/seojeong/Desktop/mic_test/wav/recorded_audio.wav"); // WAV 파일 경로를 수정하세요

            // 오디오 입력 스트림 열기
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(wavFile);

            // 오디오 포맷 가져오기
            AudioFormat audioFormat = audioInputStream.getFormat();

            // 소리 출력 라인 정보 설정
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);

            // 소리 출력 라인 열기
            SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
            line.open(audioFormat);
            line.start();

            System.out.println("재생 중...");

            // 오디오 데이터를 스피커로 전송
            int bufferSize = 1024;
            byte[] buffer = new byte[bufferSize];
            int bytesRead;

            while ((bytesRead = audioInputStream.read(buffer, 0, bufferSize)) != -1) {
                line.write(buffer, 0, bytesRead);
            }

            // 재생 종료
            line.drain();
            line.close();
            audioInputStream.close();

            System.out.println("재생 완료.");
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }
}
