package com.lollykrown.coronavirusstats;

import android.app.Application;
import android.os.AsyncTask;

import com.lollykrown.coronavirusstats.model.Corona;
import com.lollykrown.coronavirusstats.model.CoronaDao;
import com.lollykrown.coronavirusstats.model.CoronaDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class CoronaRepository {
    private CoronaDao mCoronaDao;
    private LiveData<List<Corona>> mAllCases;
    private List<Corona> mAllCa;


    CoronaRepository(Application application) {
        CoronaDatabase db = CoronaDatabase.getDatabase(application);
        mCoronaDao = db.coronaDao();
        mAllCases = mCoronaDao.getAllDataLiveData();
//        mAllCa = mCoronaDao.getAllData();
    }
    LiveData<List<Corona>> getAllCases() {
        return mAllCases;
    }

//    List<Corona> getAllCa() {
//        return mAllCa;
//    }

    public void insertAll(List<Corona> c) {
        new InsertAllCasesAsyncTask(mCoronaDao).execute(c);
    }

    public void deleteAll() {
        new DeleteAllCasesAsyncTask(mCoronaDao).execute();
    }

    static class InsertAllCasesAsyncTask extends AsyncTask<List<Corona>, Void, Void> {
        private CoronaDao coronaDao;

        private InsertAllCasesAsyncTask(CoronaDao coronaDao) {
            this.coronaDao = coronaDao;
        }

        @Override
        protected Void doInBackground(List<Corona>... movies) {
            coronaDao.addAll(movies[0]);
            return null;
        }
    }

    private static class DeleteAllCasesAsyncTask extends AsyncTask<Void, Void, Void> {
        private CoronaDao coronaDao;

        private DeleteAllCasesAsyncTask(CoronaDao coronaDao) {
            this.coronaDao = coronaDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            coronaDao.deleteAll();
            return null;
        }
    }

}
