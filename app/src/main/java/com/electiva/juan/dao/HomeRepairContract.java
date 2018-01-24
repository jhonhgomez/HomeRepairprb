package com.electiva.juan.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

/**
 * Created by Juan on 01/11/2015.
 */
public final class HomeRepairContract {

    // To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.

    private HomeRepairDbHelper openHelper;
    private SQLiteDatabase database;

    public HomeRepairContract(Context context) {
        openHelper = new HomeRepairDbHelper(context);
        database = openHelper.getWritableDatabase();
    }

    public static final String TEXT_TYPE = " TEXT";
    public static final String INTEGER_TYPE = " INTEGER";
    public static final String DATE_TYPE = " DATE";
    public static final String COMMA_SEP = ", ";
    public static final String NOT_NULL = " NOT NULL";

    public static final String SQL_CREATE_ECONOMIC =
            "CREATE TABLE " + HomeRepairEconomic.TABLE_NAME + " (" +
                    HomeRepairEconomic.COLUMN_NAME_ECONOMIC_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairEconomic.COLUMN_NAME_ACTIVITY + TEXT_TYPE + NOT_NULL  +
            " )";

    public static final String SQL_CREATE_ROLE =
            "CREATE TABLE " + HomeRepairRole.TABLE_NAME + " (" +
                    HomeRepairRole.COLUMN_NAME_ROLE_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairRole.COLUMN_NAME_ROLE + TEXT_TYPE + NOT_NULL +
                    " )";

    public static final String SQL_CREATE_USER =
            "CREATE TABLE " + HomeRepairUser.TABLE_NAME + " (" +
                    HomeRepairUser.COLUMN_NAME_USER_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairUser.COLUMN_NAME_USER + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairUser.COLUMN_NAME_PASSWORD + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairUser.COLUMN_NAME_ROLE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    " FOREIGN KEY (" + HomeRepairUser.COLUMN_NAME_ROLE + ")" +
                        "REFERENCES " + HomeRepairRole.TABLE_NAME + "(" + HomeRepairRole.COLUMN_NAME_ROLE_ID + ")" +
                    " )";

    public static final String SQL_CREATE_COMPANY =
            "CREATE TABLE " + HomeRepairCompany.TABLE_NAME + " (" +
                    HomeRepairCompany.COLUMN_NAME_COMPANY_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairCompany.COLUMN_NAME_NAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairCompany.COLUMN_NAME_NIT + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairCompany.COLUMN_NAME_TELEPHONE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairCompany.COLUMN_NAME_ACTIVITY + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairCompany.COLUMN_NAME_SERVICE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairCompany.COLUMN_NAME_USER + TEXT_TYPE + NOT_NULL +
                    " )";

    public static final String SQL_CREATE_CLIENT =
            "CREATE TABLE " + HomeRepairClient.TABLE_NAME + " (" +
                    HomeRepairClient.COLUMN_NAME_CLIENT_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairClient.COLUMN_NAME_FIRSTNAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairClient.COLUMN_NAME_LASTNAME + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairClient.COLUMN_NAME_EMAIL + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairClient.COLUMN_NAME_TELEPHONE + INTEGER_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairClient.COLUMN_NAME_BIRTH + DATE_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairClient.COLUMN_NAME_USER + TEXT_TYPE + NOT_NULL +
                    " )";

    public static final String SQL_CREATE_REQUEST =
            "CREATE TABLE " + HomeRepairRequest.TABLE_NAME + " (" +
                    HomeRepairRequest.COLUMN_NAME_REQUEST_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairRequest.COLUMN_NAME_SERVICE + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairRequest.COLUMN_NAME_DETAIL + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairRequest.COLUMN_NAME_USER + TEXT_TYPE + NOT_NULL +
                    " )";

    public static final String SQL_CREATE_QUOTE =
            "CREATE TABLE " + HomeRepairQuote.TABLE_NAME + " (" +
                    HomeRepairQuote.COLUMN_NAME_QUOTE_ID + INTEGER_TYPE + " PRIMARY KEY AUTOINCREMENT"+ COMMA_SEP +
                    HomeRepairQuote.COLUMN_NAME_USER_CLIENT + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairQuote.COLUMN_NAME_USER_COMPANY + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairQuote.COLUMN_NAME_ID_REQUEST + TEXT_TYPE + NOT_NULL + COMMA_SEP +
                    HomeRepairQuote.COLUMN_NAME_COST + TEXT_TYPE + NOT_NULL +
                    " )";

    public static final String SQL_DELETE_ECONOMIC =
            "DROP TABLE IF EXISTS " + HomeRepairEconomic.TABLE_NAME;

    public static final String SQL_DELETE_USER =
            "DROP TABLE IF EXISTS " + HomeRepairUser.TABLE_NAME;

    public static final String SQL_DELETE_ROLE =
            "DROP TABLE IF EXISTS " + HomeRepairRole.TABLE_NAME;

    public static final String SQL_DELETE_COMPANY =
            "DROP TABLE IF EXISTS " + HomeRepairCompany.TABLE_NAME;

    public static final String SQL_DELETE_CLIENT =
            "DROP TABLE IF EXISTS " + HomeRepairClient.TABLE_NAME;

    public static final String SQL_DELETE_REQUEST =
            "DROP TABLE IF EXISTS " + HomeRepairRequest.TABLE_NAME;

    public static final String SQL_DELETE_QUOTE =
            "DROP TABLE IF EXISTS " + HomeRepairQuote.TABLE_NAME;

    public static final String SQL_INSERT_ECONOMIC =
            "INSERT INTO "+HomeRepairEconomic.TABLE_NAME +" values (" +
                    "null," + "\"Agricultura, ganadería, caza, silvicultura y pesca\"),"+
                    "(null," + "\"Explotación de minas y canteras\"),"+
                    "(null," + "\"Industrias manufacturera\"),"+
                    "(null," + "\"Suministro de electricidad, gas, vapor y aire acondicionado\"),"+
                    "(null," + "\"Distribución de agua; evacuación y tratamiento de aguas residuales\"),"+
                    "(null," + "\"Construcción\"),"+
                    "(null," + "\"Comercio al por mayor y al por menor; reparación de vehículos automotores y motocicletas\"),"+
                    "(null," + "\"Transporte y almacenamiento\"),"+
                    "(null," + "\"Alojamiento y servicios de comida\"),"+
                    "(null," + "\"Información y comunicaciones\"),"+
                    "(null," + "\"Actividades financieras y de seguros\"),"+
                    "(null," + "\"Actividades inmobiliarias\"),"+
                    "(null," + "\"Actividades profesionales, científicas y técnicas\"),"+
                    "(null," + "\"Actividades de servicios administrativos y de apoyo\"),"+
                    "(null," + "\"Administración pública y defensa; planes de seguridad social de afiliación obligatoria \"),"+
                    "(null," + "\"Educación\"),"+
                    "(null," + "\"Actividades de atención de la salud humana y de asistencia social\"),"+
                    "(null," + "\"Actividades artísticas, de entretenimiento y recreación\"),"+
                    "(null," + "\"Otras actividades de servicios\"),"+
                    "(null," + "\"Actividades de los hogares individuales en calidad de empleadores\"),"+
                    "(null," + "\"Actividades de organizaciones y entidades extraterritoriales\")";

    public static final String SQL_INSERT_ROLE =
            "INSERT INTO "+HomeRepairRole.TABLE_NAME +" values (" +
                    "null," + "\"EMPRESA\")," +
                    "(null," + "\"PROFESIONAL\")," +
                    "(null," + "\"CLIENTE\")";

    /* Inner class that defines the table contents */
    public static abstract class HomeRepairEconomic implements BaseColumns {
        public static final String TABLE_NAME = "economic";
        public static final String COLUMN_NAME_ECONOMIC_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_ACTIVITY = "activity";
    }

    public static abstract class HomeRepairRole implements BaseColumns {
        public static final String TABLE_NAME = "role";
        public static final String COLUMN_NAME_ROLE_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_ROLE = "role";
    }

    public static abstract class HomeRepairUser implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_USER_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_USER = "user";
        public static final String COLUMN_NAME_PASSWORD = "password";
        public static final String COLUMN_NAME_ROLE = "role";
    }

    public static abstract class HomeRepairCompany implements BaseColumns {
        public static final String TABLE_NAME = "company";
        public static final String COLUMN_NAME_COMPANY_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_NIT = "nit";
        public static final String COLUMN_NAME_TELEPHONE = "tel";
        public static final String COLUMN_NAME_ACTIVITY = "activity";
        public static final String COLUMN_NAME_SERVICE = "service";
        public static final String COLUMN_NAME_USER = "user";
    }

    public static abstract class HomeRepairClient implements BaseColumns {
        public static final String TABLE_NAME = "client";
        public static final String COLUMN_NAME_CLIENT_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_FIRSTNAME = "firstname";
        public static final String COLUMN_NAME_LASTNAME = "lastname";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_TELEPHONE = "telephone";
        public static final String COLUMN_NAME_BIRTH = "birthday";
        public static final String COLUMN_NAME_USER = "user";
    }

    public static abstract class HomeRepairRequest implements BaseColumns {
        public static final String TABLE_NAME = "request";
        public static final String COLUMN_NAME_REQUEST_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_SERVICE = "service";
        public static final String COLUMN_NAME_DETAIL = "detail";
        public static final String COLUMN_NAME_USER = "user";
    }

    public static abstract class HomeRepairQuote implements BaseColumns {
        public static final String TABLE_NAME = "quote";
        public static final String COLUMN_NAME_QUOTE_ID = BaseColumns._ID;
        public static final String COLUMN_NAME_USER_CLIENT = "client";
        public static final String COLUMN_NAME_USER_COMPANY = "company";
        public static final String COLUMN_NAME_ID_REQUEST = "request";
        public static final String COLUMN_NAME_COST = "cost";
    }
}
