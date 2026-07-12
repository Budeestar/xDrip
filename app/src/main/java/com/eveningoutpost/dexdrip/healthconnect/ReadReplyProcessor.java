package com.eveningoutpost.dexdrip.healthconnect;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.health.connect.client.records.BloodGlucoseRecord;
import androidx.health.connect.client.units.BloodGlucose;

import com.eveningoutpost.dexdrip.models.BgReading;
import com.eveningoutpost.dexdrip.models.HeartRate;
import com.eveningoutpost.dexdrip.models.JoH;
import com.eveningoutpost.dexdrip.models.Sensor;
import com.eveningoutpost.dexdrip.models.StepCounter;
import com.eveningoutpost.dexdrip.models.UserError;

import lombok.val;

// jamorham

@RequiresApi(api = Build.VERSION_CODES.O)
public class ReadReplyProcessor {

    private static final String TAG = ReadReplyProcessor.class.getSimpleName();

    public static void process(final DataReply dataReply) {
        if (dataReply == null) {
            UserError.Log.d(TAG, "Null reply");
            return;
        }

        if (dataReply.stepsRecords != null) {
            for (val item : dataReply.stepsRecords) {
                StepCounter.createUniqueRecord(item.getStartTime().toEpochMilli() + ((item.getEndTime().toEpochMilli() - item.getStartTime().toEpochMilli()) / 2),
                        (int) item.getCount(),
                        true);
            }
        }

        if (dataReply.heartRateRecords != null) {
            for (val item : dataReply.heartRateRecords) {
                for (val i : item.getSamples()) {
                    UserError.Log.d(TAG, "heart rate: " + JoH.dateTimeText(i.getTime().toEpochMilli()) + " bpm:" + i.getBeatsPerMinute());
                    HeartRate.create(i.getTime().toEpochMilli(), (int) i.getBeatsPerMinute(), 1);
                }
            }
        }

        if (dataReply.bloodGlucoseRecords != null) {
            for (val item : dataReply.bloodGlucoseRecords) {
                BloodGlucose level = item.getLevel();
                double mgDl = level.getMilligramsPerDeciliter();
                long timestamp = item.getTime().toEpochMilli();
                UserError.Log.d(TAG, "blood glucose: " + JoH.dateTimeText(timestamp) + " mg/dL:" + mgDl);
                ensureSensorActive();
                BgReading.bgReadingInsertFromG5(mgDl, timestamp, "HealthConnect");
            }
        }
    }

    private static void ensureSensorActive() {
        Sensor.createDefaultIfMissing();
    }
}
