import javax.xml.parsers.FactoryConfigurationError;
// Transaction.java
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity; // Or androidx.fragment.app.FragmentActivity
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import java.util.concurrent.Executor;

public class Transaction {
    public String account_number;
    public String bank;
    public int amount;
    public String purpose;

    private static final String TAG = "TransactionMFA";
    public static final int REQUEST_CODE_BIOMETRIC_ENROLLMENT = 1001; // For guiding user to settings

    // Callback interface for authentication result
    public interface MfaAuthCallback {
        void onAuthSuccess();
        void onAuthError(String errorMessage);
        void onAuthFailed(); // Specific to biometric failure (e.g., too many attempts)
    }

    public void Offline_Transaction_System(String acc_number, String bnk, int amt, String purpose,
                                           AppCompatActivity activity, MfaAuthCallback callback) {
        this.account_number = acc_number;
        this.bank = bnk;
        this.amount = amt;
        this.purpose = purpose;

        // ... your existing transaction logic ...
        Log.d(TAG, "Offline transaction details processed for account: " + this.account_number);

        // At the end, call MFA
        multi_factor_authentication(activity, callback);
    }


    private void multi_factor_authentication(AppCompatActivity activity, MfaAuthCallback callback) {
        Executor executor = ContextCompat.getMainExecutor(activity);

        BiometricManager biometricManager = BiometricManager.from(activity);
        int authenticators = BiometricManager.Authenticators.BIOMETRIC_STRONG
                | BiometricManager.Authenticators.DEVICE_CREDENTIAL;

        switch (biometricManager.canAuthenticate(authenticators)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Log.d(TAG, "App can authenticate using biometrics or device credentials.");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                Log.e(TAG, "No biometric features available on this device.");
                callback.onAuthError("No biometric features available on this device.");
                return;
            case BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE:
                Log.e(TAG, "Biometric features are currently unavailable.");
                callback.onAuthError("Biometric features are currently unavailable.");
                return;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Log.w(TAG, "No biometrics enrolled. User might need to set up screen lock or fingerprint/face.");
                // Optionally, prompt the user to enroll biometrics.
                // For this example, we'll proceed assuming they might use device credentials.
                // However, BiometricPrompt will guide them if DEVICE_CREDENTIAL is set
                // and no screen lock is set up.
                // You can also prompt them to set up a screen lock:
                // final Intent enrollIntent = new Intent(Settings.ACTION_BIOMETRIC_ENROLL);
                // enrollIntent.putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                //        authenticators);
                // activity.startActivityForResult(enrollIntent, REQUEST_CODE_BIOMETRIC_ENROLLMENT);
                // callback.onAuthError("No biometrics or screen lock enrolled. Please set one up.");
                // return;
                break;
            case BiometricManager.BIOMETRIC_ERROR_SECURITY_UPDATE_REQUIRED:
                Log.e(TAG, "Biometric security update required.");
                callback.onAuthError("Biometric security update required.");
                return;
            case BiometricManager.BIOMETRIC_ERROR_UNSUPPORTED:
                Log.e(TAG, "Biometrics kind unsupported.");
                callback.onAuthError("Biometrics kind unsupported.");
                return;
            case BiometricManager.BIOMETRIC_STATUS_UNKNOWN:
                Log.e(TAG, "Biometric status unknown.");
                callback.onAuthError("Biometric status unknown.");
                return;
        }

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Confirm Transaction")
                .setSubtitle("Authenticate to proceed with the offline transaction")
                .setAllowedAuthenticators(authenticators) // Use BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                //.setNegativeButtonText("Cancel") // Handled by onAuthError usually or you can customize
                .build();

        BiometricPrompt biometricPrompt = new BiometricPrompt(activity, executor,
                new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Log.e(TAG, "Authentication error: " + errorCode + " - " + errString);
                        // User cancelled, or too many attempts, or other errors
                        // Don't treat "Cancel" (ERROR_USER_CANCELED / ERROR_NEGATIVE_BUTTON) as a critical error
                        // if you just want to allow them to back out.
                        if (errorCode == BiometricPrompt.ERROR_USER_CANCELED ||
                                errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            callback.onAuthError("Authentication cancelled: " + errString);
                        } else {
                            callback.onAuthError("Authentication error: " + errString);
                        }
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Log.d(TAG, "Authentication succeeded!");
                        // IMPORTANT: Authentication was successful.
                        // You can now consider the transaction authorized locally.
                        // Proceed with finalizing the offline transaction if any further local steps are needed.
                        callback.onAuthSuccess();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Log.w(TAG, "Authentication failed (e.g., fingerprint not recognized).");
                        // BiometricPrompt UI handles showing "try again". This is called when a biometric
                        // is not recognized, but the prompt is still active.
                        callback.onAuthFailed();
                    }
                });

        biometricPrompt.authenticate(promptInfo);
    }
}