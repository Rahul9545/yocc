package com.bigv.app.yocc.utils;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.bigv.app.yocc.R;
import com.google.gson.Gson;
import com.mithsoft.lib.componants.Toasty;
import com.mithsoft.lib.utils.MsUtils;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by mithun on 12/8/17.
 */

public class AUtils extends MsUtils {


    private static final String TAG = "AUtils";

    //    Local URL
//    public static final String SERVER_URL = "http://192.168.200.3:8089/api/";

    //    Staging URL
//    public static final String SERVER_URL = "https://way2voice.co.in:444/api/";

    //    Production URL
    public static final String SERVER_URL = "https://way2voice.in:444/api/";

    // SAVE DATA CONSTANTS
    public static final String IS_USER_LOGIN = "UserLoginStatus";
    public static final String USER_DATA = "UserDetails";

    public static final String USER_ID = "UserLoginId";

    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_EXPIRED = "expired";
    public static final String KEY = "AccessKey";
    public static final String HOME_SCREEN_POJO = "HomeScreenPojoObject";
    public static final String CALL_DETAILS_POJO_LIST = "CallDetailsPojoList";
    public static final String WEEK_WISE_CALL_DURATION_LIST = "WeekWiseCallDurationList";
    public static final String WEEK_WISE_CALL_LIST = "WeekWiseCallList";
    public static final String MONTH_WISE_CALL_LIST = "MonthWiseCallList";
    public static final String CALL_DETAILS_SUMMERY_POJO = "CallDetailsSummeryPojo";
    public static final String USER_LOGIN_ID = "UserLoginId";
    public static final String USER_LOGIN_PASSWORD = "UserLoginPassword";
    public static final String CALL_PRIORITY_LIST = "CallPriorityList";
    public static final String AGENT_MASTER_LIST = "AgentMasterPojoList";
    public static final String AGENT_MASTER_POJO = "AgentMasterPojoSingleObject";
    public static final String IS_EDIT = "IsActionEdit";
    public static final String AGENT_REPLACER_LIST = "AgentReplacerPojoList";
    public static final String AGENT_REPLACER_POJO = "AgentReplacerPojo";
    public static final String MENU_MASTER_LIST = "MenuMasterPojoList";
    public static final String MENU_MASTER_POJO = "MenuMasterPojo";
    public static final String ROUTING_PATTERN_LIST = "RoutingPatternPojoList";
    public static final String ROUTING_TYPE_LIST = "RoutingTypePojoList";
    public static final String LANGUAGE_LIST = "LanguageList";
    public static final String CALL_DETAILS_POJO = "CallDetailsPojo";
    public static final String SERVER_DATE_TIME_FORMATE = "MM-dd-yyyy HH:mm:ss";
    public static final String SERVER_DATE_time = "dd-MM-yyyy HH:mm";
    public static final String MOBILE_DATE_TIME_FORMATE = "dd/MM/yyyy HH:mm";
    public static final String MOBILE_DATE_time = "dd-MMM-yyyy hh:mm a";
    public static final String MOBILE_DATE_FORMATE = "dd/MM/yyyy";
    public static final String SERVER_DATE_FORMATE = "MM-dd-yyyy";
    public static final String LIVE_CALL_LIST = "LiveCallList";
    public static final String CALL_TRANSCRIPTION_LIST = "CallTranscriptionList";
    public static final String ADDRESS_BOOK_LIST = "AddressBookList";
    public static final String ADDRESS_BOOK_POJO = "AddressBookPojo";
    public static final String CLL_REMARK_LIST = "CallRemarkList";
    public static final String UPDATE_CALL_LIST = "UpdateCallListToTodayCalls";
    public static final String DAY_WISE_REPORT_CALL_DETAILS = "DayWiseReportCallDetails";
    public static final String WEEK_WISE_REPORT_CALL_DETAILS = "WeekWiseReportCallDetails";
    public static final String GROUP_ADDRESS_BOOK_LIST = "GroupAddressBookList";
    public static final String GROUP_ADDRESS_BOOK_POJO = "GroupAddressBookPojo";
    public static final String IS_LOGIN_BY_AGENT = "TheLoginIsAgent";
    public static final String AGENT_ID = "AgentId";
    public static final String USER_TYPE_ID = "UserTypeId";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String SERVER_DATE_TIME_FORMATE_LOCAL = "yyyy-MM-dd HH:mm:ss.SSS";


    private AUtils() {

    }


    public static <S> S createService(Class<S> serviceClass, Gson gson, String url) {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create(gson));

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }


    public static <S> S createService(Class<S> serviceClass, String url) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient httpClient = new OkHttpClient.Builder().connectTimeout(3600, TimeUnit.SECONDS)
                .writeTimeout(3600, TimeUnit.SECONDS)
                .readTimeout(3600, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        Retrofit.Builder builder =
                new Retrofit.Builder()
                        .baseUrl(url)
                        .addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public static String getTodaysDate(String dateFormat) {

        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(new Date());
    }

    public static String getMonthShortName(String monthName) {
        switch (monthName) {
            case "January":
                return "Jan";
            case "February":
                return "Feb";
            case "March":
                return "Mar";
            case "April":
                return "Apr";
            case "May":
                return "May";
            case "June":
                return "Jun";
            case "July":
                return "Jul";
            case "August":
                return "Aug";
            case "September":
                return "Sep";
            case "October":
                return "Oct";
            case "November":
                return "Nov";
            case "December":
                return "Dec";
        }
        return "UN";
    }

    public static void getDeviceDrawable(Context context) {
        String s = "";
        Log.e(TAG, "densityDpi = " + context.getResources().getDisplayMetrics().density);
        switch (context.getResources().getDisplayMetrics().densityDpi) {

            case DisplayMetrics.DENSITY_LOW:
                s = "LDPI";
                break;
            case DisplayMetrics.DENSITY_MEDIUM:
                s = "MDPI";
                break;
            case DisplayMetrics.DENSITY_HIGH:
                s = "HDPI";
                break;
            case DisplayMetrics.DENSITY_XHIGH:
                s = "XHDPI";
                break;
            case DisplayMetrics.DENSITY_XXHIGH:
                s = "XXHDPI";
                break;
            case DisplayMetrics.DENSITY_XXXHIGH:
                s = "XXXHDPI";
                break;
        }
        Toast.makeText(context, s + " - " + context.getResources().getDisplayMetrics().density, Toast.LENGTH_SHORT).show();
    }


    public static void downloadFile(Context context, String fileUrl, String fileFormate) {

        File dir = new File(Environment.getExternalStorageDirectory().toString()
                + "/YOCC");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (MsUtils.isNetWorkAvailable(context)) {

            try {

//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse("http://naasongsdownload.com/Telugu/2009-Naasongs.Audio/01%20File/Kick%20(2009)/Dil%20Kalaase%20-Naasongs.Audio.mp3"));
                String filename = fileUrl.substring(fileUrl.lastIndexOf('/') + 1);
                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(fileUrl));
                request.setDescription("Downloading....");
                request.setTitle(filename + " file");
                request.allowScanningByMediaScanner();
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
//                request.setDestinationInExternalPublicDir("YOCC", "yocc." + fileFormate);
                request.setDestinationInExternalPublicDir("YOCC", filename);
                // get download service and enqueue file
                DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                manager.enqueue(request);
            } catch (Exception e) {

                e.printStackTrace();
                Toasty.error(context, "" + context.getString(R.string.serverError), Toast.LENGTH_SHORT).show();
            }
        } else {

            Toasty.warning(context, "" + context.getString(R.string.noInternet), Toast.LENGTH_SHORT).
                    show();
        }
    }

    public static void downloadMediaFileType(Context context, String fileUrl, String fileFormate) {
         File toMedia = null;
         long downloadId = 0;

        String link = fileUrl;
        String s = link.substring(link.lastIndexOf("/")+1);

        Log.d(TAG, "downloadMedia: " + s);
        Log.i("RahulCheck", "downloadFileUrl: "+s );
        if (MsUtils.isNetWorkAvailable(context)) {

            String destination = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/";

            toMedia = new File(destination, s);

            destination += s;
            Uri uri = Uri.parse("file://" + destination);

            Log.d(TAG, "downloadMedia: " + uri);

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(link));
            request.setDescription("Downloading new media.....");
            request.setTitle("Yocc fileView");
            request.setTitle(s + " fileView");
            request.setDestinationUri(uri);
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

            DownloadManager manager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            downloadId = manager.enqueue(request);
            Toasty.success(context, "" +context.getString(R.string.fileViewTypeDownloaded), Toast.LENGTH_SHORT).show();

            Uri muri = manager.getUriForDownloadedFile(downloadId);
            Log.d(TAG, "downloadMediaRahul: " + toMedia);
        }else {

            Toasty.warning(context, "" + context.getString(R.string.noInternet), Toast.LENGTH_SHORT).
                    show();
        }
    }

    public static Calendar getCurrentTime() {
        Calendar now = Calendar.getInstance();
        return now;
    }

    public static Calendar getLogOutEndTime() {
        Calendar cl = Calendar.getInstance();
        /*cl.set(Calendar.HOUR_OF_DAY, 23);
        cl.set(Calendar.MINUTE, 50);
        cl.set(Calendar.SECOND, 0);*/

        cl.set(Calendar.HOUR_OF_DAY, 3);
        cl.set(Calendar.MINUTE, 47);
        cl.set(Calendar.SECOND, 0);

        return cl;
    }

    public static Calendar getSplashLogOutEndTime() {
        Calendar cl = Calendar.getInstance();

        /*cl.set(Calendar.HOUR_OF_DAY, 23);
        cl.set(Calendar.MINUTE, 50);
        cl.set(Calendar.SECOND, 0);*/

        cl.set(Calendar.HOUR_OF_DAY, 11);
        cl.set(Calendar.MINUTE, 58);
        cl.set(Calendar.SECOND, 0);

        return cl;
    }


    public static String getCurrentDateDutyOffTime() {

        DateFormat dateFormat = new SimpleDateFormat(AUtils.SERVER_DATE_TIME_FORMATE_LOCAL, Locale.ENGLISH);

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 50);
        cal.set(Calendar.SECOND, 0);

        return dateFormat.format(cal.getTime());
    }

    public static String getApiSideDateLocal(String date) {

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMMM-yyyy", Locale.US);
            Date datee = inputFormat.parse(date);
            return outputFormat.format(datee);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }}
    public static String getApiSideFollowUpDateLocal(String date) {

        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.US);
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MMMM-yyyy hh:mm aa", Locale.US);
            Date datee = inputFormat.parse(date);
            return outputFormat.format(datee);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }}

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    public static String truncateString(String str, int len) {
        if (str.length() > len) {
            return str.substring(0, len)+"";
        } else {
            return str;
        }}

    public static String stripNonValidXMLCharacters(String in) {
        StringBuffer out = new StringBuffer(); // Used to hold the output.
        char current; // Used to reference the current character.

        if (in == null || ("".equals(in))) return ""; // vacancy test.
        for (int i = 0; i < in.length(); i++) {
            current = in.charAt(i); // NOTE: No IndexOutOfBoundsException caught here; it should not happen.
            if ((current == 0x9) ||
                    (current == 0xA) ||
                    (current == 0xD) ||
                    ((current >= 0x20) && (current <= 0xD7FF)) ||
                    ((current >= 0xE000) && (current <= 0xFFFD)) ||
                    ((current >= 0x10000) && (current <= 0x10FFFF)))
                out.append(current);
        }
        return out.toString();
    }
}
