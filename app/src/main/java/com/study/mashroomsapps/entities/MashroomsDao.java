package com.study.mashroomsapps.entities;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "MASHROOMS".
*/
public class MashroomsDao extends AbstractDao<Mashrooms, Long> {

    public static final String TABLENAME = "MASHROOMS";

    /**
     * Properties of entity Mashrooms.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "Id", true, "ID");
        public final static Property Latidute = new Property(1, String.class, "Latidute", false, "LATIDUTE");
        public final static Property Longtidute = new Property(2, String.class, "Longtidute", false, "LONGTIDUTE");
        public final static Property Description = new Property(3, String.class, "Description", false, "DESCRIPTION");
    }


    public MashroomsDao(DaoConfig config) {
        super(config);
    }
    
    public MashroomsDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"MASHROOMS\" (" + //
                "\"ID\" INTEGER PRIMARY KEY ," + // 0: Id
                "\"LATIDUTE\" TEXT NOT NULL ," + // 1: Latidute
                "\"LONGTIDUTE\" TEXT NOT NULL ," + // 2: Longtidute
                "\"DESCRIPTION\" TEXT NOT NULL );"); // 3: Description
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_MASHROOMS_LATIDUTE ON MASHROOMS" +
                " (\"LATIDUTE\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_MASHROOMS_LONGTIDUTE ON MASHROOMS" +
                " (\"LONGTIDUTE\");");
        db.execSQL("CREATE INDEX " + constraint + "IDX_MASHROOMS_DESCRIPTION ON MASHROOMS" +
                " (\"DESCRIPTION\");");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"MASHROOMS\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, Mashrooms entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
        stmt.bindString(2, entity.getLatidute());
        stmt.bindString(3, entity.getLongtidute());
        stmt.bindString(4, entity.getDescription());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, Mashrooms entity) {
        stmt.clearBindings();
 
        Long Id = entity.getId();
        if (Id != null) {
            stmt.bindLong(1, Id);
        }
        stmt.bindString(2, entity.getLatidute());
        stmt.bindString(3, entity.getLongtidute());
        stmt.bindString(4, entity.getDescription());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public Mashrooms readEntity(Cursor cursor, int offset) {
        Mashrooms entity = new Mashrooms( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // Id
            cursor.getString(offset + 1), // Latidute
            cursor.getString(offset + 2), // Longtidute
            cursor.getString(offset + 3) // Description
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, Mashrooms entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setLatidute(cursor.getString(offset + 1));
        entity.setLongtidute(cursor.getString(offset + 2));
        entity.setDescription(cursor.getString(offset + 3));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(Mashrooms entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(Mashrooms entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(Mashrooms entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
