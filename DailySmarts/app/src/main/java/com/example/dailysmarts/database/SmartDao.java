package com.example.dailysmarts.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface SmartDao {

    @Query("SELECT * FROM smartentity WHERE id NOT LIKE 1  ORDER BY id DESC")
    List<SmartEntity> getAll();

    @Query("SELECT * FROM smartentity WHERE text LIKE :text ORDER BY id ASC")
    List<SmartEntity> getSmartiesWithText(String text);

    @Query("SELECT * FROM smartentity ORDER BY id DESC LIMIT 1")
    List<SmartEntity> getTheLastAdded();

    @Query("SELECT * FROM smartentity WHERE date LIKE  :date LIMIT 1")
    SmartEntity findTodaysEntity(String date);

    @Query("SELECT * FROM smartentity WHERE id LIKE  :id")
    SmartEntity findEntityById(int id);

    @Query("UPDATE smartentity SET text= :text , author = :author, date = :date WHERE id = 1")
    void updateTodaysSmarty (String text, String author, String date);

    @Insert
    void insertSingle(SmartEntity... smartEntities);

    @Delete
    void deleteEntity(SmartEntity smartEntity);

    @Query("DELETE FROM smartentity WHERE id = :userId")
   void deleteByEntityId(int userId);

}
