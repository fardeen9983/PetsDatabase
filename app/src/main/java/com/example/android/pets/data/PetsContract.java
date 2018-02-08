package com.example.android.pets.data;

import android.provider.BaseColumns;

/**
 * Created by LUCIFER on 09-02-2018.
 */

public final class PetsContract {
    private PetsContract() {}

    public static class petsTable implements BaseColumns {

        public static final String TABLE_NAME = "pets";
        public static final String COLUMN_PET_ID = BaseColumns._ID;
        public static final String COLUMN_PET_NAME = "name";
        public static final String COLUMN_PET_BREED = "breed";
        public static final String COLUMN_PET_GENDER = "gender";
        public static final String COLUMN_PET_WEIGHT = "weight";

        public static final int PET_GENDER_UNKNOWN = 0;
        public static final int PET_GENDER_MALE = 1;
        public static final int PET_GENDER_FEMALE = 2;

    }

}
