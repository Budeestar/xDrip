package com.eveningoutpost.dexdrip.utilitymodels;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.eveningoutpost.dexdrip.utils.QRcodeUtils;
import com.eveningoutpost.dexdrip.xdrip;

import java.util.Map;

import lombok.val;

/**
 * MyCureMate first-run defaults.
 * <p>
 * Applies minimal configuration automatically so end users do not need to
 * scan QR codes or adjust settings manually:
 * <p>
 * 1. Installs the G7/One+/Stelo authentication certificates (keks parts)
 *    which are normally applied by scanning the xDrip Auto Configure QR code.
 * 2. Selects the Dex (OB1 G5/G6/G7/Stelo) hardware data source.
 * 3. Enables recommended collector settings.
 * <p>
 * The user then only needs to enter the 4-digit sensor pairing code and
 * the MyCureMate backend URL.
 */
public class MyCureMateDefaults {

    private static final String TAG = MyCureMateDefaults.class.getSimpleName();
    private static final String MARKER_PREF = "mycuremate_defaults_applied_version";
    private static final int DEFAULTS_VERSION = 1;

    // Contents of the official xDrip "Auto Configure" QR code for
    // G7 / One+ / Stelo collection (keks certificate parts).
    // Source: https://navid200.github.io/xDrip/docs/G6-Recommended-Settings.html
    private static final String KEKS_AUTO_CONFIG =
            "xdp2:H4sIAAAAAAAAAGNgZuBKio/PTs0uji8wZugyaGxnYmQwEGZj12rzOGfLxMjGAWYwM7Kz5BpkMzEy" +
            "sigw1Py+Ou3bc5fyHQdefvu+sEQ9TkDu2/5aiVMfBBsvqm+tXOjC7MTAEihxOG6e4/MHqf7vGmSCVh69" +
            "f1TgPWdkbcyplsQn6+ekpYs0+2ufZP6Yv/qPa2k0Y/PHBdNdz/4/7adfOS9g381lW3uQ3GXEeNGgifEs" +
            "EJcsYGZiZGISkWTVPyMezP0r7MncU39unW+1ebqr2IAL6lYWZiYDYUNBA3425lAWZh4OF9cIA+MAd0MD" +
            "OXFeI2MDE0MTQwMjC0OTKCDXFMg1BnONowysDC0MzCCa9A0MdQyAQMfA2MDAJ9DV2bnK0cmx3NFRJzkz" +
            "PzPVOCwp0CgjJ8o3KyU017Qo3dEgEmuYUSMgFtcb1BrwAJ0lK8zI+J+FyYDBQB7EU2aRMBBrEOn4Jte4" +
            "x1v8Q0y29KOZsgkcNefuVhrIghSosogZiLBxaLMxsrKyMzPBWYwQaT4WMRaRy5zPY9hVHXWrKpUerzq/" +
            "qb5ub/YDAz6QND/IMmBQsi5ADlhmdwYDFyYFjyttPZ8n32RxZNz2of5abz2bU0N8662SR//mvrhryv5B" +
            "nUlB5uL+mpy6u5HOcUaeqn8+vN586pEgb6VDTfkq68nb97CcRopeQ8Z3wKh9BcT90OjVtwna9pqjQMDt" +
            "SmzFucYKn7n/ghzwRq8BLHoNDIERZmhqamkAil5jU2DUmkO52NME7nj7LXVK8ca6N7P8tsq3GngG3yxf" +
            "uDZs+kxV/h/JM+qdFp+dv+R/uNzWjD05lknlh+9slX277kWo8AmuGBfT4C7JryuTPRY3HjBo3Au2ERJz" +
            "rAbMQAol8ubxy5l9/lS2nPFfX4dVntoy071ZfwyiQArkWYINAg38F5gsMGozyCgpKbDS108uytFLKdAr" +
            "Tkws1isoysxNzU6t1EvOzwVJ6MOCQQ/IWSS+RBR7GCFiHmu6QYl5xjaUmPdkMHBjUmRYlXm2s+5Y8vr4" +
            "eaKN7fczAv+zh33gcRBTCfNvXDg/ir0VqOTW651/NyZvN3zNlvjxwMLJGoVL+SxnXTz2VfLVXp951QuE" +
            "AcTP0rp1BAAA";

    public static void applyIfNeeded() {
        try {
            if (Pref.getInt(MARKER_PREF, 0) >= DEFAULTS_VERSION) {
                return;
            }
            Log.d(TAG, "Applying MyCureMate defaults version: " + DEFAULTS_VERSION);

            final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(xdrip.getAppContext());
            final SharedPreferences.Editor editor = prefs.edit();

            // 1. Install G7/Stelo keks certificate parts from embedded auto-config QR data.
            int applied = 0;
            final Map<String, String> map = QRcodeUtils.decodeString(KEKS_AUTO_CONFIG);
            if (map != null) {
                for (val entry : map.entrySet()) {
                    val key = entry.getKey();
                    val value = entry.getValue();
                    if ("true".equals(value) || "false".equals(value)) {
                        editor.putBoolean(key, Boolean.parseBoolean(value));
                    } else if (!"null".equals(value)) {
                        editor.putString(key, value);
                    }
                    applied++;
                }
            } else {
                Log.e(TAG, "Failed to decode embedded keks auto-config data");
            }

            // 2. Default to the Dex (OB1 G5/G6/G7/Stelo) collector unless the user
            // has already chosen a data source.
            if (!prefs.contains("dex_collection_method")) {
                editor.putString("dex_collection_method", "DexcomG5");
            }

            // 3. Recommended collector settings for reliable collection.
            editor.putBoolean("aggressive_service_restart", true);

            editor.putInt(MARKER_PREF, DEFAULTS_VERSION);
            editor.apply();
            Log.d(TAG, "MyCureMate defaults applied. Config entries: " + applied);
        } catch (Exception e) {
            Log.e(TAG, "Failed to apply MyCureMate defaults: " + e);
        }
    }
}
