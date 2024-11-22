package com.example.localstorage;

import android.provider.BaseColumns;

public class SchoolControlContract {
    private SchoolControlContract() {} // Esterilizar clase
    public static class Student implements BaseColumns {
        public static final String TABLE_NAME = "Students";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_GRADE = "grade";
        public static final String COLUMN_NAME_GROUP = "group_name";
        public static final String COLUMN_NAME_AVERAGE = "average";
        public static final String COLUMN_NAME_CAREER = "career";
    }
}