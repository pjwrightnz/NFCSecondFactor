package com.example.paul.nfcsecondfactor0.nfcSecondFactorClient;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.paul.nfcsecondfactor0.R;
import com.example.paul.nfcsecondfactor0.nfcSecondFactorServer.MockServer;
import com.example.paul.nfcsecondfactor0.nfcSecondFactorServer.UserDataPersistence;

public class RegistrationActivity extends AppCompatActivity {

    //registration activity for creating a new account
    Button createAccountButton;
    EditText userIDEditText, passwordEditText, reenterPasswordEditText, emailEditText;
    ImageView nfcLogo, bTLogo, yorkLogo, loginIcon, pwIcon, emailIcon, repwIcon;
    String nfcCardID = "";
    MockServer mockServer = new MockServer();
    private NfcAdapter registerNfcAdapter;

    //pending intent is utilised to for nfc tag activty. It is enabled in onResume and intents are
    //caught by the on New Intent Activity.
    PendingIntent pendingIntent;

    public static Boolean checkNull(String input) {
        if (input.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registerstration);

        //set logos at top of activity
        bTLogo = (ImageView) findViewById(R.id.btLogo);
        bTLogo.setImageResource(R.drawable.bt_logo);

        yorkLogo = (ImageView) findViewById(R.id.uniLogo);
        yorkLogo.setImageResource(R.drawable.unilogo);

        //set NFC logo
        nfcLogo = (ImageView) findViewById(R.id.nfcLogo);
        nfcLogo.setImageResource(R.drawable.nfclogo);

        //set up icons
        loginIcon = (ImageView) findViewById(R.id.userIDIcon);
        loginIcon.setImageResource(R.drawable.ic_perm_identity_black_24dp);

        pwIcon = (ImageView) findViewById(R.id.passwordIcon);
        pwIcon.setImageResource(R.drawable.ic_lock_black_24dp);

        repwIcon = (ImageView) findViewById(R.id.reenterPasswordIcon);
        repwIcon.setImageResource(R.drawable.ic_lock_black_24dp);

        emailIcon = (ImageView) findViewById(R.id.emailIcon);
        emailIcon.setImageResource(R.drawable.ic_email_black_24dp);

        //setup editTexts
        userIDEditText = (EditText) findViewById(R.id.userIDInput);
        passwordEditText = (EditText) findViewById(R.id.passwordInput);
        reenterPasswordEditText = (EditText) findViewById(R.id.reenterPasswordEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);

        pendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),  0);

         //create account button
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
        //set up listener for when button is clicked
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check null entries, method below for returning boolean. If any are null, throw Toast
                if (checkNull(userIDEditText.getText().toString()) || checkNull(passwordEditText.getText().toString()) || checkNull(reenterPasswordEditText.getText().toString()) || checkNull(emailEditText.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "Please complete all fields.", Toast.LENGTH_LONG).show();
                    //check that password and reentry match, if wrong, throw Toast
                } else if (!passwordEditText.getText().toString().equals(reenterPasswordEditText.getText().toString())) {
                    passwordEditText.setText("");
                    reenterPasswordEditText.setText("");
                    Toast.makeText(getApplicationContext(), "Your passwords do not match, please reenter.", Toast.LENGTH_LONG).show();
                    //check if userID is already in use, if so throw Toast.
                } else if (UserDataPersistence.userData.containsKey(userIDEditText.getText().toString())) {
                    passwordEditText.setText("");
                    reenterPasswordEditText.setText("");
                    Toast.makeText(getApplicationContext(), "This UserID is already in use, please select a different UserID.", Toast.LENGTH_LONG).show();
                    //if all the above checks are passed, register user, save their details and return them back to main activity. UserID is passed back.
                } else if (nfcCardID == null) {
                    Toast.makeText(getApplicationContext(), "Please tap NFC Card to the mobile device.", Toast.LENGTH_LONG).show();
                } else {
                    //create new user note empty string, send request to server
                    mockServer.registerUser(userIDEditText.getText().toString(), passwordEditText.getText().toString(), emailEditText.getText().toString(), nfcCardID);
                    //add to hashmap and return to main intent
                    Intent returnToMainIntent = new Intent(RegistrationActivity.this, MainActivity.class);
                    returnToMainIntent.putExtra(MainActivity.userID, userIDEditText.getText().toString());
                    setResult(1, returnToMainIntent);
                    finish();
                    Toast.makeText(getApplicationContext(), "Your account has been created.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    // handles new intents from NFC Cards
    @Override
    public void onNewIntent(Intent intent) {
        Tag myTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

        if (myTag != null) {
            nfcLogo.setImageResource(R.drawable.tick);
            getWindow().getDecorView().findViewById(android.R.id.content).invalidate();
            nfcCardID = bytesToHexString(myTag.getId());
        }
    }
    //converts tags from Hex to String
    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }
        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }
        return stringBuilder.toString();
    }

    //enables forground NFC card read
    @Override
    protected void onResume() {
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
        super.onResume();
    }
}
