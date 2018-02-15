package com.study.mashroomsapps;

import android.support.v7.app.AppCompatActivity;

import com.study.mashroomsapps.entities.DaoMaster;
import com.study.mashroomsapps.entities.DaoSession;

import org.greenrobot.greendao.database.Database;

/**
 * Created by nvv on 16.01.2018.
 */

public class BaseActivity extends AppCompatActivity {

    protected DaoSession mDaoSession;

    private static final boolean ENCRYPTED = false;

    public DaoSession getDaoSession() {
        if (mDaoSession == null) {
            DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "notes-db-encrypted" : "notes-db");
            Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
            mDaoSession = new DaoMaster(db).newSession();
        }
        return mDaoSession;
    }
}
