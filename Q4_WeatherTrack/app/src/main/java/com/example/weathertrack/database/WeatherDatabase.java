package com.example.weathertrack.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {WeatherEntity.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class WeatherDatabase extends RoomDatabase {
    private static volatile WeatherDatabase INSTANCE;

    public static WeatherDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (WeatherDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    WeatherDatabase.class, "weatherDatabase")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    public abstract WeatherDao weatherDao();
}