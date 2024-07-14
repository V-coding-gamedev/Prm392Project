package com.example.signuploginfirebase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public DBHelper(@Nullable Context context) {
        super(context, "FashionShop.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create Table Role(" +
                "role_id integer primary key autoincrement, " +
                "role_name text not null)");

        db.execSQL("create Table Users (user_id integer primary key autoincrement, " +
                "username text not null, " +
                "password text not null, " +
                "email text not null, " +
                "phone text not null, " +
                "address text not null, " +
                "role_id integer, " +
                "foreign key (role_id) references Role(role_id))");

        db.execSQL("create Table Categories (category_id integer primary key autoincrement, " +
                "name text not null," +
                "descripteion text not null)");

        db.execSQL("create Table Product (product_id integer primary key autoincrement, " +
                "name text not null, " +
                "description text not null, " +
                "size text not null, " +
                "unitPrice real not null, " +
                "unitsInStock integer, " +
                "unitsOnOrder integer," +
                "category_id integer, " +
                "foreign key (category_id) references Category(category_id))");

        db.execSQL("create table Orders (order_id integer primary key autoincrement, " +
                "orderDate text not null, " +
                "status text not null, " +
                "totalAmount real not null, " +
                "startDate text not null, " +
                "endDate text not null, " +
                "shipAddress text not null, " +
                "user_id integer, " +
                "foreign key (user_id) references Users(user_id))");

        db.execSQL("CREATE TABLE Order_Details (" +
                "order_id INTEGER, " +
                "product_id INTEGER, " +
                "quantity INTEGER NOT NULL, " +
                "unit_price REAL NOT NULL, " +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id), " +
                "FOREIGN KEY (product_id) REFERENCES Product(product_id), " +
                "PRIMARY KEY (order_id, product_id))");

        db.execSQL("CREATE TABLE Payment_Methods (" +
                "payment_method_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "description TEXT)");

        db.execSQL("CREATE TABLE Payments (" +
                "payment_id INTEGER PRIMARY KEY AUTOINCREMENT, " +  // Added a primary key for Payments table
                "order_id INTEGER, " +
                "payment_method_id INTEGER, " +
                "payment_date TEXT NOT NULL, " +
                "qr_url TEXT, " +
                "FOREIGN KEY (order_id) REFERENCES Orders(order_id), " +
                "FOREIGN KEY (payment_method_id) REFERENCES Payment_Methods(payment_method_id))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists Users");
        db.execSQL("drop table if exists Role");
        db.execSQL("drop table if exists Categories");
        db.execSQL("drop table if exists Product");
        db.execSQL("drop table if exists Orders");
        db.execSQL("drop table if exists Order_Details");
        db.execSQL("drop table if exists Payment_Methods");
        db.execSQL("drop table if exists Payments");
        onCreate(db);
    }
//---------------------------ORDERS--------------------------- //

    // Create Order
    public long createOrder(String orderDate, String status, float totalAmount, String startDate, String endDate, String shipAddress, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("orderDate", orderDate);
        values.put("status", status);
        values.put("totalAmount", totalAmount);
        values.put("startDate", startDate);
        values.put("endDate", endDate);
        values.put("shipAddress", shipAddress);
        values.put("user_id", userId);
        return db.insert("Orders", null, values);
    }

    // Read Order
    public Cursor readOrder(int orderId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Orders WHERE order_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderId)});
    }

    // Update Order
    public int updateOrder(int orderId, String orderDate, String status, float totalAmount, String startDate, String endDate, String shipAddress, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("orderDate", orderDate);
        values.put("status", status);
        values.put("totalAmount", totalAmount);
        values.put("startDate", startDate);
        values.put("endDate", endDate);
        values.put("shipAddress", shipAddress);
        values.put("user_id", userId);
        return db.update("Orders", values, "order_id = ?", new String[]{String.valueOf(orderId)});
    }

    // Delete Order
    // Delete Order and associated Order Details
    public int deleteOrder(int orderId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try {
            // Delete order details associated with the order
            db.delete("Order_Details", "order_id = ?", new String[]{String.valueOf(orderId)});
            // Delete the order
            int rowsDeleted = db.delete("Orders", "order_id = ?", new String[]{String.valueOf(orderId)});
            db.setTransactionSuccessful();
            return rowsDeleted;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            db.endTransaction();
        }
    }


    //--------------------------ORDER DETAILS--------------------------- //
    // Create Order Detail
    public long createOrderDetail(int orderId, int productId, int quantity, float unitPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("order_id", orderId);
        values.put("product_id", productId);
        values.put("quantity", quantity);
        values.put("unit_price", unitPrice);
        return db.insert("Order_Details", null, values);
    }

    // Read Order Detail
    public Cursor readOrderDetail(int orderId, int productId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM Order_Details WHERE order_id = ? AND product_id = ?";
        return db.rawQuery(query, new String[]{String.valueOf(orderId), String.valueOf(productId)});
    }

    // Update Order Detail
    public int updateOrderDetail(int orderId, int productId, int quantity, float unitPrice) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("quantity", quantity);
        values.put("unit_price", unitPrice);
        return db.update("Order_Details", values, "order_id = ? AND product_id = ?", new String[]{String.valueOf(orderId), String.valueOf(productId)});
    }

    // Delete Order Detail
    public int deleteOrderDetail(int orderId, int productId) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("Order_Details", "order_id = ? AND product_id = ?", new String[]{String.valueOf(orderId), String.valueOf(productId)});
    }

//------------------------------------------------------ //

    public Cursor getData(){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users", null);
        return cursor;
    }

    public Cursor getUserByUsername(String username){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where username = ? ", new String[]{username});
        return cursor;
    }

    public Cursor getUserByEmailAddress(String emailAddress){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where email = ? ", new String[]{emailAddress});
        return cursor;
    }

    public Cursor getUserByPhoneNumber(String phone){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Users where phone = ?", new String[]{phone});
        return cursor;
    }

    public Cursor getUserByEmailAndPassword(String email, String password){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("select * from Users where email = ? and password = ?"
                , new String[]{email, password});
        return cursor;
    }


    public Boolean insertuserdata(String usernameTXT, String emailTXT, String phoneTXT,
                                  String passwordTXT, String addressTXT) {

        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", usernameTXT);
        contentValues.put("email", emailTXT);
        contentValues.put("phone", phoneTXT);
        contentValues.put("password", passwordTXT);
        contentValues.put("address", addressTXT);

        long result = DB.insert("Users", null, contentValues);

        if (result == -1){
            return false;
        } else {
            return true;
        }
    }

    public boolean resetUserPassword(String emailAddress, String newPassword){
        SQLiteDatabase DB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("password", newPassword);
        Cursor cursor = DB.rawQuery("Select * from Users where email=?", new String [] {emailAddress});

        if (cursor.getCount() > 0){
            long result = DB.update("Users", contentValues, "email=?", new String[]{emailAddress});

            if (result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    public boolean deletedata(String username){
        SQLiteDatabase DB = this.getWritableDatabase();
        Cursor cursor = DB.rawQuery("Select * from Users where username = ? ", new String[]{username});

        if (cursor.getCount() > 0){
            long result = DB.delete("Users", "username=?", new String[]{username});

            if (result == -1){
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
