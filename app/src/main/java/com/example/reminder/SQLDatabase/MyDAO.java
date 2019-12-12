package com.example.reminder.SQLDatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MyDAO {

    @Insert(onConflict = REPLACE)
    void insert(NoteUser user);


    @Query("update note_table set note = :note, `desc` = :desc where id = :id")
    void updateNote(String note, String desc, int id);


    @Delete()
    void delete(NoteUser user);


    @Query("delete from note_table")
    void deleteAll();


    @Query("select * from note_table")
    LiveData<List<NoteUser>> readData();

}
