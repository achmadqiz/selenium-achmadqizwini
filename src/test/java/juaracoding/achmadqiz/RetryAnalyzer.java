package juaracoding.achmadqiz;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

/**
 * RetryAnalyzer — otomatis retry test jika gagal di percobaan pertama.
 * Berguna untuk menangani flaky error seperti websocket connection timeout
 * saat Chrome belum sepenuhnya siap.
 *
 * Max retry: 2x (jadi total percobaan = 3x)
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private int retryCount = 0;
    private static final int MAX_RETRY = 2;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount < MAX_RETRY) {
            retryCount++;
            System.out.println("[RETRY] Percobaan ke-" + retryCount + " untuk test: "
                    + result.getName());
            return true;
        }
        return false;
    }
}
