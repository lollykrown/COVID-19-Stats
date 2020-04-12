package com.lollykrown.coronavirusstats.model;

import android.content.Context;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Corona.class}, version = 1, exportSchema = false)
public abstract class CoronaDatabase extends RoomDatabase {

    public abstract CoronaDao coronaDao();

    public static final String DATABASE_NAME = "CoronaDb";

    //SINGLETON
    private static volatile CoronaDatabase sINSTANCE;

    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static CoronaDatabase getDatabase(final Context context) {
        if (sINSTANCE == null) {
            synchronized (CoronaDatabase.class) {
                if (sINSTANCE == null) {
                    sINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CoronaDatabase.class, CoronaDatabase.DATABASE_NAME)
                            // .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return sINSTANCE;
    }
}