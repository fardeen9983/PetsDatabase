package com.example.android.pets.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.print.PrintManager;

import com.example.android.pets.data.PetsContract.petsTable;

/**
 * Created by LUCIFER on 09-02-2018.
 */

public class PetsDBManager extends SQLiteOpenHelper {

    private final static String DATABASE_NAME = "pets";
    private final static int DATABASE_VERSION = 1;
    private final static String TEXT_TYPE = " TEXT ";
    private final static String INTEGER_TYPE = " INTEGER ";
    private final static String NULL_TYPE = " NULL ";
    private final static String CONSTRAINT_NOT_NULL = " NOT NULL ";
    private final static String CONSTRAINT_PRIMARY_KEY = " PRIMARY KEY ";
    private final static String CONSTRAINT_AUTO_INCREMENT = " AUTOINCREMENT ";
    private final static String CONSTRAINT_DEFAULT_VALUE = " DEFAULT " + petsTable.PET_GENDER_UNKNOWN + " ";

    private final static String CREATE_TABLE =
            "CREATE TABLE " + petsTable.TABLE_NAME + " ( "
                    + petsTable.COLUMN_PET_ID + INTEGER_TYPE + CONSTRAINT_PRIMARY_KEY + CONSTRAINT_AUTO_INCREMENT + ", "
                    + petsTable.COLUMN_PET_NAME + TEXT_TYPE + CONSTRAINT_NOT_NULL + ", "
                    + petsTable.COLUMN_PET_BREED + TEXT_TYPE + CONSTRAINT_NOT_NULL + ", "
                    + petsTable.COLUMN_PET_GENDER + INTEGER_TYPE + CONSTRAINT_DEFAULT_VALUE + CONSTRAINT_NOT_NULL + ", "
                    + petsTable.COLUMN_PET_WEIGHT + CONSTRAINT_NOT_NULL + " );";

    private static final String DELETE_TABLE =
            "DELETE FROM IF EXISTS " + petsTable.TABLE_NAME;
    public PetsDBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLE);
        onCreate(db);
    }
}
