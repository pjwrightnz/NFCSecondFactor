package com.example.paul.nfcsecondfactor0.nfcSecondFactorClient;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.io.File;

import com.example.paul.nfcsecondfactor0.R;
import com.example.paul.nfcsecondfactor0.nfcSecondFactorServer.MockServer;
import com.example.paul.nfcsecondfactor0.nfcSecondFactorServer.UserDataPersistence;

public class MainActivity extends AppCompatActivity {

    public static String userID, nfcCardID;
    public static File file;
    ImageView btLogo, yorkLogo, nfcLogo, loginIcon, pwIcon;
    TextView registerTextView;
    Boolean loginAccepted = false;
    EditText userIDInput, passwordInput;
    Button loginButton, registerButton;
    MockServer mockServer = new MockServer();
    UserDataPersistence udp = new UserDataPersistence();
    private NfcAdapter loginNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        file = getFilesDir();

        // set Icon
        ActionBar menu = getSupportActionBar();
        menu.setDisplayShowHomeEnabled(true);
        menu.setLogo(R.drawable.nfcsign);
        menu.setDisplayUseLogoEnabled(true);

        // assign logos to image views
        btLogo = (ImageView) findViewById(R.id.btLogo);
        btLogo.setImageResource(R.drawable.bt_logo);

        yorkLogo = (ImageView) findViewById(R.id.uniLogo);
        yorkLogo.setImageResource(R.drawable.unilogo);

        //assign logos for login and password
        loginIcon = (ImageView) findViewById(R.id.userIDIcon);
        loginIcon.setImageResource(R.drawable.ic_perm_identity_black_24dp);

        pwIcon = (ImageView) findViewById(R.id.passwordIcon);
        pwIcon.setImageResource(R.drawable.ic_lock_black_24dp);

        //asign NFC logo
        nfcLogo = (ImageView) findViewById(R.id.nfcLogo);
        nfcLogo.setImageResource(R.drawable.nfclogo);

        //assign UserID and Password edit texts
        userIDInput = (EditText) findViewById(R.id.userIDInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);

        //assign Buttons
        //login button
        loginButton = (Button) findViewById(R.id.loginButton);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                authUser();
            }
        });

        //set softkeyboard action listener for logging in
        passwordInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                authUser();
                return true;
            }
        });

        //register button, starts register activity and resets nfc logo
        registerButton = (Button) findViewById(R.id.registerButton);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nfcLogo.setImageResource(R.drawable.nfclogo);
                getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
                Intent newRegisterIntent = new Intent(MainActivity.this, RegistrationActivity.class);
                startActivityForResult(newRegisterIntent, 0);
            }
        });
        //populate hashmap with a test user
        mockServer.seedUserData();
        //  nfc code
        loginNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //  check if there is an NFC capability of the phone
        if (loginNfcAdapter == null) {
            Toast.makeText(this, "This device doesn't support NFC. Second Factor Authentication unavailable", Toast.LENGTH_LONG).show();
        }
        // check NFC is enabled
        if (!loginNfcAdapter.isEnabled()) {
            Toast.makeText(getApplicationContext(), "Please activate NFC to allow second factor authentication and press Back to return to the application.", Toast.LENGTH_LONG).show();
            startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
        }
    }

    private void authUser() {
        int auth = mockServer.authenticateUser(userIDInput.getText().toString(), passwordInput.getText().toString(), nfcCardID);
        switch (auth) {
            case 0:
                showToast("Login successful.");
                Intent newBTWebIntent = new Intent(MainActivity.this, BTWebSiteActivity.class);
                startActivityForResult(newBTWebIntent, 0);
                break;
            case 1:
                showToast("Your NFC Card did not match the card registered for your account, please try again.");
                break;
            case 2:
                showToast("Your password did not match the password for your account, please try again.");
                break;
            default:
                showToast("Login failed, please try again.");
        }
    }

    private void showToast(String string) {
        Toast.makeText(getApplicationContext(), string, Toast.LENGTH_LONG).show();
    }

    //    method is called when a new intent is thrown to the main activity. The logic then handles the
//     NFC data and captures the Ndef information and parses it. The card ID is then added to the userID
//     and password and sent to the server for authentication
    @Override
    public void onNewIntent(Intent intent) {
        Tag myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.i("tag ID", bytesToHexString(myTag.getId()));
        mockServer.seedUserData();

        if (myTag != null) {
            nfcLogo.setImageResource(R.drawable.tick);
            getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            nfcCardID = bytesToHexString(myTag.getId());
            Log.i("tag ID", nfcCardID);
            Log.i("User", udp.getUser("Test").toString());
        }
        //super.onNewIntent(intent);
    }

    // this method takes in a Byte Array and returns a String, utilising a Stroing Builder.
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    //if app is paused, the NFC Adaptor is paused. On resume the adaptor is started again.
    @Override
    protected void onResume() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags((intent.FLAG_RECEIVER_REPLACE_PENDING));
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter[] intentFilter = new IntentFilter[]{};
        loginNfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFilter, null);

        super.onResume();
    }

    //if app is paused, the NFC Adaptor is paused. This is to ensure the app does not interefer with other NFC apps.
    @Override
    protected void onPause() {
        loginNfcAdapter.disableForegroundDispatch(this);
        super.onPause();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == 1) {
            userIDInput.setText(data.getStringExtra(MainActivity.userID));
        }
    }
}
