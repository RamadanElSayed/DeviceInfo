package com.example.ramadan.deviceinfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.google.*;
import com.google.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
interface	GoogleApiClient.ConnectionCallbacks	Provides callbacks that are called when the client is connected or disconnected from the service.
*/
/*
* interface	GoogleApiClient.OnConnectionFailedListener	Provides callbacks for scenarios that result in a failed attempt to connect the client to the service.
* */
public class MainActivity extends AppCompatActivity implements View.OnClickListener ,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, com.google.android.gms.location.LocationListener {
    private static final int PERMISSION_REQUEST_CODE = 1;
    Context context;
    String emailid,serial = null,deviceId,devicename,AndroidVersion,RAMDevice = "",InternalMemory,BuildNumber,simID,telNumber,
            IMEI,currentBatteryStatus="Battery Info",strDate,Time;
    float battery;
    IntentFilter intentfilter;
    int deviceStatus;
    int batteryLevel;
    String Address = "";

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;


    /*

LocationRequest

public final class LocationRequest extends Object
implements Parcelable Parcelable
A data object that contains quality of service parameters for requests to the FusedLocationProviderApi.

LocationRequest objects are used to request a quality of service for location updates from the FusedLocationProviderApi.

For example,
if your application wants high accuracy location it should create a location request with setPriority(int) set to PRIORITY_HIGH_ACCURACY and setInterval(long) to 5 seconds.
This would be appropriate for mapping applications that are showing your location in real-time.

At the other extreme, if you want negligible power impact, but to still receive location updates when available,
 then create a location request with setPriority(int) set to PRIORITY_NO_POWER.
 With this request your application will not trigger (and therefore will not receive any power blame) any location updates,
  but will receive locations triggered by other applications.
  This would be appropriate for applications that have no firm requirement for location, but can take advantage when available.
     */






    //Abbreviation Get Location when update happen
    private LocationRequest mLocationRequest;

    /*

GoogleApiClient

public abstract class GoogleApiClient extends Object
The main entry point for Google Play services integration.

GoogleApiClient is used with a variety of static methods. Some of these methods require that GoogleApiClient be connected, some will queue up calls before GoogleApiClient is connected; check the specific API documentation to determine whether you need to be connected.

Before any operation is executed, the GoogleApiClient must be connected. The simplest way to manage the connection is to use enableAutoManage(FragmentActivity, GoogleApiClient.OnConnectionFailedListener). See Accessing Google APIs.

GoogleApiClient instances are not thread-safe. To access Google APIs from multiple threads simultaneously, create a GoogleApiClient on each thread. GoogleApiClient service connections are cached internally, so creating multiple instances is fast.
     */
    // connect to google play services
    private GoogleApiClient googleApiClient;



    double lat,lon;


    public static final int LOCATION_PERMISSION = 1001;
    private String mProviderName;   /* GPS */
    // get GPS Location
    private LocationManager mLocationManager;   /* GPS */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        ((Button) findViewById(R.id.button1)).setOnClickListener(this);
        ((Button) findViewById(R.id.button2)).setOnClickListener(this);
        ((Button) findViewById(R.id.button3)).setOnClickListener(this);
        ((Button) findViewById(R.id.button4)).setOnClickListener(this);
        ((Button) findViewById(R.id.button5)).setOnClickListener(this);
        ((Button) findViewById(R.id.button6)).setOnClickListener(this);
        ((Button) findViewById(R.id.button7)).setOnClickListener(this);
        ((Button) findViewById(R.id.button8)).setOnClickListener(this);
        ((Button) findViewById(R.id.button9)).setOnClickListener(this);
        ((Button) findViewById(R.id.button10)).setOnClickListener(this);
        ((Button) findViewById(R.id.button11)).setOnClickListener(this);
        ((Button) findViewById(R.id.button12)).setOnClickListener(this);
        ((Button) findViewById(R.id.button13)).setOnClickListener(this);
        ((Button) findViewById(R.id.button14)).setOnClickListener(this);
        ((Button) findViewById(R.id.button15)).setOnClickListener(this);
        ((Button) findViewById(R.id.button16)).setOnClickListener(this);
        ((Button) findViewById(R.id.button17)).setOnClickListener(this);
        ((Button) findViewById(R.id.button18)).setOnClickListener(this);

        if (hasPermission(MainActivity.LOCATION_PERMISSION)) {
        }
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        /*
        A class indicating the application criteria for selecting a location provider. Providers maybe ordered according to accuracy,
        power usage, ability to report altitude, speed, and bearing, and monetary cost.
         */
        Criteria criteria = new Criteria();
        mProviderName = mLocationManager.getBestProvider(criteria, true);
        // ATTENTION: This "addApi(AppIndex.API)"was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        googleApiClient = new GoogleApiClient.Builder(this, this, this).addApi(LocationServices.API).build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds


        try {
            Class<?> c = Class.forName("android.os.SystemProperties");                   // Serial number
            Method get = c.getMethod("get", String.class);
            serial = (String) get.invoke(c, "ro.serialno");
            Toast.makeText(getApplicationContext(), "Serial Number" + serial, Toast.LENGTH_LONG).show();
        } catch (Exception ignored) {
        }

        getTotalRAM();                          //ram

    }

    // Batter Level
    public float getBatteryLevel() {
        Intent batteryIntent = registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
        int level = batteryIntent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
        int scale = batteryIntent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
        if(level == -1 || scale == -1) {
            return 50.0f;
        }
        return ((float)level / (float)scale) * 100.0f;
    }

    // Batter Status
    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS,-1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            int batteryLevel=(int)(((float)level / (float)scale) * 100.0f);
            if(deviceStatus == BatteryManager.BATTERY_STATUS_CHARGING){
                Toast.makeText(getApplicationContext(), "Charging at"+batteryLevel+" %", Toast.LENGTH_LONG).show();
            }
            if(deviceStatus == BatteryManager.BATTERY_STATUS_DISCHARGING){
                Toast.makeText(getApplicationContext(), "Discharging at"+batteryLevel+" %", Toast.LENGTH_LONG).show();
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_FULL){
                Toast.makeText(getApplicationContext(), "Battery Full at"+batteryLevel+" %", Toast.LENGTH_LONG).show();
            }
            if(deviceStatus == BatteryManager.BATTERY_STATUS_UNKNOWN){
                Toast.makeText(getApplicationContext(), "Unknown at"+batteryLevel+" %", Toast.LENGTH_LONG).show();
            }
            if (deviceStatus == BatteryManager.BATTERY_STATUS_NOT_CHARGING){
                Toast.makeText(getApplicationContext(), "Not Charging at"+batteryLevel+" %", Toast.LENGTH_LONG).show();
            }
        }
    };

    // RAM
    public String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);

            while (m.find()) {
                RAMDevice = m.group(1);
                System.out.println("Ram : " + RAMDevice);
                Log.v("", "");
            }
            reader.close();
            totRam = Double.parseDouble(RAMDevice);
            // totRam = totRam / 1024;
            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;
            if (tb > 1) {
                lastValue = twoDecimalForm.format(gb).concat(" GB");
            } else if (gb > 1) {
                lastValue = twoDecimalForm.format(tb).concat(" TB");
            } else if (mb > 1) {
                lastValue = twoDecimalForm.format(mb).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format(totRam).concat(" MB");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
        }

        return lastValue;
    }


    // Account Manager

    // Email Id
    public String EmailId() {
        String possibleEmail = "";
        try {
            possibleEmail += "************* Get Registered Gmail Account *************nn";
            Account[] accounts = AccountManager.get(this).getAccountsByType("com.google");
            for (Account account : accounts) {
                possibleEmail = account.name;    //+" : "+account.type+" , n";
                //possibleEmail += " nn";
            }
        } catch (Exception e) {
            Log.i("Exception", "Exception:" + e);
        }
        return possibleEmail;
    }

    // Device Name or Dvice id
    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }


    // Android Version
    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }


    // Build Number
    public static String getOSBuildNumber() {
        String osBuildNumber = Build.FINGERPRINT;
        final String forwardSlash = "/";
        String osReleaseVersion = Build.VERSION.RELEASE + forwardSlash;
        try {
            osBuildNumber = osBuildNumber.substring(osBuildNumber.indexOf(osReleaseVersion));
            osBuildNumber = osBuildNumber.replace(osReleaseVersion, "");
            osBuildNumber = osBuildNumber.substring(0, osBuildNumber.indexOf(forwardSlash));
        } catch (Exception e) {
            Log.e("getOSBuildNumber", "Exception while parsing - " + e.getMessage());
        }
        return osBuildNumber;
    }





    // Get Address
    private String getCompleteAddressString() {


        // Geocoder take your lat and log then return path
        //(context ,lahuage of writing)
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                Address = strReturnedAddress.toString();
                Toast.makeText(getApplicationContext(), "Address" + Address, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "No Address returned!", Toast.LENGTH_LONG).show();

            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Canont get Address!", Toast.LENGTH_LONG).show();
        }
        return Address;
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsga()) {
                    devicename =  getDeviceName();
                    Toast.makeText(getApplicationContext(), "devicename" + devicename, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsga();
                }
            } else {
                devicename = getDeviceName();
                Toast.makeText(getApplicationContext(), "devicename" + devicename, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button2) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsga()) {
                    devicename =  getDeviceName();
                    Toast.makeText(getApplicationContext(), "devicename" + devicename, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsga();
                }
            } else {
                devicename = getDeviceName();
                Toast.makeText(getApplicationContext(), "devicename" + devicename, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button3) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsga()) {
                    AndroidVersion = getAndroidVersion();
                    Toast.makeText(getApplicationContext(), "devicename" + AndroidVersion, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsga();
                }
            } else {
                AndroidVersion = getAndroidVersion();
                Toast.makeText(getApplicationContext(), "devicename" + AndroidVersion, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button4) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsga()) {
                    deviceId = Settings.System.getString(getContentResolver(),Settings.System.ANDROID_ID);
                    Toast.makeText(getApplicationContext(), "Device ID" + deviceId, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsga();
                }
            } else {
                deviceId = Settings.System.getString(getContentResolver(),Settings.System.ANDROID_ID);
                Toast.makeText(getApplicationContext(), "Device ID" + deviceId, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button5) {
            Toast.makeText(getApplicationContext(), "Device RAM" + RAMDevice, Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.button6) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsxx()) {
                    Toast.makeText(getApplicationContext(), "Internal Memory" + InternalMemory, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsxx();
                }
            } else {
                deviceId = Settings.System.getString(getContentResolver(),Settings.System.ANDROID_ID);
                Toast.makeText(getApplicationContext(), "Internal Memory" + InternalMemory, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button7) {
            BuildNumber =  getOSBuildNumber();
            Toast.makeText(getApplicationContext(), "build number" + BuildNumber, Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.button8) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsrp()) {
                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    simID = tm.getSimSerialNumber();
                    if (simID != null)
                        Toast.makeText(this, "SIM card ID: " + simID, Toast.LENGTH_LONG).show();
                    telNumber = tm.getLine1Number();
                    if (telNumber != null)
                        Toast.makeText(this, "Phone number: " + telNumber, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsrp();
                }
            } else {
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                simID = tm.getSimSerialNumber();
                if (simID != null)
                    Toast.makeText(this, "SIM card ID: " + simID, Toast.LENGTH_LONG).show();
                telNumber = tm.getLine1Number();
                if (telNumber != null)
                    Toast.makeText(this, "Phone number: " + telNumber, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button9) {
            MainActivity.this.registerReceiver(broadcastreceiver,intentfilter);

        } else if (v.getId() == R.id.button10) {
            battery =  getBatteryLevel();
            Toast.makeText(getApplicationContext(), "battery" + battery+ "%", Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.button11) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsrp()) {
                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    IMEI = tm.getDeviceId();
                    if (IMEI != null)
                        Toast.makeText(this, "IMEI number: " + IMEI, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsrp();
                }
            } else {
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                IMEI = tm.getDeviceId();
                if (IMEI != null)
                    Toast.makeText(this, "IMEI number: " + IMEI, Toast.LENGTH_LONG).show();
            }

        } else if (v.getId() == R.id.button12) {
            Toast.makeText(getApplicationContext(), "Serial Number" + serial, Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.button13) {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdfs = new SimpleDateFormat("HH:mm:ss");
            Time = sdfs.format(c.getTime());
            strDate = sdf.format(c.getTime());
            Toast.makeText(getApplicationContext(), "Date or Time" +" "+ strDate +" "+Time , Toast.LENGTH_LONG).show();

        } else if (v.getId() == R.id.button14) {
            if (Build.VERSION.SDK_INT >= 23) {
                Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {

                    /*
                    public abstract Location getLastLocation (GoogleApiClient client)

                   Returns the best most recent location currently available.

                   If a location is not available, which should happen very rarely, null will be returned.
                   The best accuracy available while respecting the location permissions will be returned.

                    This method provides a simplified way to get location. It is particularly well suited for
 applications that do not require an accurate location and that do not want to maintain extra logic for location updates.
                     */
                    Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                    lat = lastLocation.getLatitude();
                    lon = lastLocation.getLongitude();
                    Toast.makeText(this, lat + " WORKS " + lon + "", Toast.LENGTH_LONG).show();
                }
            }else{
                Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                if (location == null) {
                    LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
                } else {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                    Toast.makeText(this, lat + " WORKS " + lon + "", Toast.LENGTH_LONG).show();
                }
            }

        } else if (v.getId() == R.id.button15) {
            getCompleteAddressString();
        } else if (v.getId() == R.id.button16) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsga()) {
                    emailid = EmailId();
                    Toast.makeText(getApplicationContext(), "Email ID" + emailid, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsga();
                }
            } else {
                File path = Environment.getDataDirectory();
                StatFs stat2 = new StatFs(path.getPath());
                long blockSize = stat2.getBlockSize();
                long availableBlocks = stat2.getAvailableBlocks();
                InternalMemory =  Formatter.formatFileSize(this, availableBlocks * blockSize);
                Log.e("","Format : "+InternalMemory);
            }

        } else if (v.getId() == R.id.button17) {


            AccountManager manager = AccountManager.get(this);
            Account[] accounts = manager.getAccountsByType("com.google");
            List<String> possibleEmails = new LinkedList<String>();

            for (Account account : accounts) {
                // TODO: Check possibleEmail against an email regex or treat
                // account.name as an email address only for certain account.type values.
                possibleEmails.add(account.name);
            }
            String email="";
            if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
                 email = possibleEmails.get(0);
                String[] parts = email.split("@");
            }
            Toast.makeText(MainActivity.this, email, Toast.LENGTH_SHORT).show();

//                if (parts.length > 1)
//                    return parts[0];
//            }



        } else if (v.getId() == R.id.button18) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermissionsrds()) {
                    TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                    telNumber = tm.getLine1Number();
                    if (telNumber != null)
                        Toast.makeText(this, "Phone number: " + telNumber, Toast.LENGTH_LONG).show();
                } else {
                    requestPermissionsrds();
                }
            } else {
                TelephonyManager tm = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
                telNumber = tm.getLine1Number();
                if (telNumber != null)
                    Toast.makeText(this, "Phone number: " + telNumber, Toast.LENGTH_LONG).show();
            }
        }
    }


    // GET_ACCOUNTS
    private boolean checkPermissionsga() {
        /*
        If the app has the permission, the method returns PackageManager.PERMISSION_GRANTED,
        and the app can proceed with the operation. If the app does not have the permission,
         the method returns PERMISSION_DENIED, and the app has to explicitly ask the user for permission
         */
        int result2 = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.GET_ACCOUNTS);
        if (result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionsga() {
        /*
        shouldShowRequestPermissionRationale().
        This method returns true if the app has requested this permission previously and the user denied the request.

       Note: If the user turned down the permission request in the past and chose the Don't ask
       again option in the permission request system dialog, this method returns false.
       The method also returns false if a device policy prohibits the app from having that permission
         */
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.GET_ACCOUNTS)) {
            // Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {

            /*
            If your app doesn't already have the permission it needs,
             the app must call one of the requestPermissions() methods to request the appropriate permissions.
             Your app passes the permissions it wants, and also an integer request code that you specify to identify this permission request.
             This method functions asynchronously: it returns right away, and after the user responds to the dialog box,
              the system calls the app's callback method with the results,
            passing the same request code that the app passed to requestPermissions().
             */
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.GET_ACCOUNTS}, PERMISSION_REQUEST_CODE);

            // PERMISSION_REQUEST_CODE is an
            // app-defined int constant. The callback method gets the
            // result of the request.
        }
    }

    // READ_EXTERNAL_STORAGE
    private boolean checkPermissionsxx() {
        int result2 = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        if (result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionsxx() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
        }
    }


    // READ_PHONE_STATE
    private boolean checkPermissionsrp() {
        int result2 = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE);
        if (result2 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionsrp() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_PHONE_STATE)) {
            //Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_PHONE_STATE}, PERMISSION_REQUEST_CODE);
        }
    }


    // READ_CONTACTS
    private boolean checkPermissionsrds() {
        int result243 = ContextCompat.checkSelfPermission(MainActivity.this, android.Manifest.permission.READ_CONTACTS);
        if (result243 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestPermissionsrds() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, android.Manifest.permission.READ_CONTACTS)) {
            // Toast.makeText(MainActivity.this, "Write External Storage permission allows us to do store images. Please allow this permission in App Settings.", Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSION_REQUEST_CODE);
        }
    }


    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    // Internal Memory
                    File path = Environment.getDataDirectory();
                    StatFs stat2 = new StatFs(path.getPath());
                    long blockSize = stat2.getBlockSize();
                    long availableBlocks = stat2.getAvailableBlocks();
                    InternalMemory =  Formatter.formatFileSize(this, availableBlocks * blockSize);
                    Toast.makeText(getApplicationContext(), "Internal Memory" + InternalMemory, Toast.LENGTH_LONG).show();
                    Log.e("","Format : "+InternalMemory);

                    // Email Id
                    emailid = EmailId();
                    Toast.makeText(getApplicationContext(), "Email ID" + emailid, Toast.LENGTH_LONG).show();

                    // Device Id
                    deviceId = Settings.System.getString(getContentResolver(),Settings.System.ANDROID_ID);
                    Toast.makeText(getApplicationContext(), "Device ID" + deviceId, Toast.LENGTH_LONG).show();

                    //Device Name
                    devicename =  getDeviceName();
                    Toast.makeText(getApplicationContext(), "devicename" + devicename, Toast.LENGTH_LONG).show();

                    //Android Version
                    AndroidVersion = getAndroidVersion();
                    Toast.makeText(getApplicationContext(), "Android Version" + AndroidVersion, Toast.LENGTH_LONG).show();

                    Log.e("value", "Permission Granted, Now you can use local drive .");
                }else {
                    Log.e("value", "Permission Denied, You cannot use local drive .");
                }
            case LOCATION_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {

                    Toast.makeText(this, "Need your location!", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }




    // Lat or long or address

    public boolean hasPermission(int permissionType) {
        if (Build.VERSION.SDK_INT >= 23) {
            switch (permissionType) {
                case LOCATION_PERMISSION:
                    int hasLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasLocationPCoarseermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);

                    if (hasLocationPermission != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                LOCATION_PERMISSION);
                        return false;
                    } else {
                        return true;
                    }
            }
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (googleApiClient != null) {
            googleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle bundle) {
        if (Build.VERSION.SDK_INT >= 23) {
            Log.i(MainActivity.class.getSimpleName(), "Connected to Google Play Services!");
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
                lat = lastLocation.getLatitude();
                lon = lastLocation.getLongitude();
                Toast.makeText(getApplicationContext(), lat + "", Toast.LENGTH_LONG).show();
            }
        }else{
            Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (location == null) {
                LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, mLocationRequest, this);
            } else {
                lat = location.getLatitude();
                lon = location.getLongitude();
                Toast.makeText(this, lat + " WORKS " + lon + "", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(MainActivity.class.getSimpleName(), "Can't connect to Google Play Services!");
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        } else {
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        googleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");
        //Disconnect from API onPause()
        if (googleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient, this);
            googleApiClient.disconnect();
        }

    }

    /*
    	Used for receiving notifications from the FusedLocationProviderApi when the location has changed.
    	com.google.android.gms.location
     */
    public void onLocationChanged(Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();
        Toast.makeText(this, lat + " WORKS " + lon + "", Toast.LENGTH_LONG).show();
    }


}