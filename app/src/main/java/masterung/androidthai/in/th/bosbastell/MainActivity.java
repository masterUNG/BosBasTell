package masterung.androidthai.in.th.bosbastell;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.anastr.speedviewlib.SpeedView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TextView tempTextView, humidTextView;
    private String currentTempString = "20", currentHumidString = "30",
            alertTempString, alertHumidString;
    private int alertTempAnInt = 30, alertHumidAnInt = 60;
    int greenInt = 0xFF00FF00;
    int redInt = 0xFFFF0000;
    int yellowInt = 0xFFFFFF00;
    private boolean statusABoolean = true;
    private boolean tempABoolean = false, humidABoolean = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tempTextView = findViewById(R.id.txtTemp);
        humidTextView = findViewById(R.id.txtHumid);
        tempTextView.setTextColor(greenInt);
        humidTextView.setTextColor(greenInt);

//        RealTime Service
        realTimeService();

//        Stop Alert
        stopAlert();

//        Test Speed
        SpeedView speedView = findViewById(R.id.speedViewTest);
        speedView.speedTo(findInt(currentTempString), 4000);


    }   // Main Method

    private void stopAlert() {
        Button button = findViewById(R.id.btnStopAlert);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (statusABoolean) {
                    Toast.makeText(MainActivity.this, "Click When Alert",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "Stop Buzzer Alert",
                            Toast.LENGTH_SHORT).show();

                    if (tempABoolean) {
                        tempTextView.setTextColor(yellowInt);
                    }

                    if (humidABoolean) {
                        humidTextView.setTextColor(yellowInt);
                    }





                    stopBuzzer();

                }



            }   // onClick
        });


    }

    private void stopBuzzer() {

        String[] strings = new String[]{"MyTemp", "MyHumid"};
        String[] strings1 = new String[]{"alertTemp", "alertHumid"};

        for (int i = 0; i < strings.length; i++) {

            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child(strings[i]);

            Map<String, Object> objectMap = new HashMap<>();
            objectMap.put(strings1[i], 0);
            databaseReference.updateChildren(objectMap);

        }   // for


    }

    private void realTimeService() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference();

        try {

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Map map = (Map) dataSnapshot.getValue();
                    currentTempString = String.valueOf(map.get("Temp"));
                    currentHumidString = String.valueOf(map.get("Humidity"));

                    tempTextView.setText(currentTempString);
                    humidTextView.setText(currentHumidString);


                    if (statusABoolean) {

                        //                Check Temp
                        if (findInt(currentTempString) > alertTempAnInt) {
                            tempABoolean = true;
                            setupAlert(true);
                        }

                        //                Check Humid
                        if (findInt(currentHumidString) > alertHumidAnInt) {
                            humidABoolean = true;
                            setupAlert(false);
                        }

                    }else {

                        //                Check Temp
                        if ((findInt(currentTempString) <= alertTempAnInt) && tempABoolean) {
                            tempTextView.setTextColor(greenInt);
                            statusABoolean = true;
                            tempABoolean = false;
                            stopBuzzer();
                        }

                        //                Check Humid
                        if ((findInt(currentHumidString) <= alertHumidAnInt) && humidABoolean) {
                            humidTextView.setTextColor(greenInt);
                            statusABoolean = true;
                            humidABoolean = false;
                            stopBuzzer();
                        }
                    }






                }   // Data change

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }


    }   // realTime

    private int findInt(String currentString) {

        try {

            return Integer.valueOf(currentString);

        } catch (Exception e) {
            return 0;
        }



    }

    private void setupAlert(boolean status) {

        statusABoolean = false;
        Map<String, Object> objectMap = new HashMap<>();

        if (status) {
//            Alert Temp
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("MyTemp");
            objectMap.put("alertTemp", 1);
            databaseReference.setValue(objectMap);
            tempTextView.setTextColor(redInt);

        } else {
//            Alert Humid
            FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
            DatabaseReference databaseReference = firebaseDatabase.getReference().child("MyHumid");
            objectMap.put("alertHumid", 1);
            databaseReference.setValue(objectMap);
            humidTextView.setTextColor(redInt);
        }   // if








    }   // setup

}   // Main Class