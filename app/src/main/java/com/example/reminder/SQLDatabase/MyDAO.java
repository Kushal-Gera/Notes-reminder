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
    public void insert(NoteUser user);


//    @Query("updateNote note_table set note = :note, `desc` = :desc where id = :id")
//    public void updateNote(String note, String desc, int id);

    @Query("update note_table set note = :note, `desc` = :desc where id = :id")
    public void updateNote(String note, String desc, int id);


    @Delete()
    public void delete(NoteUser user);


    @Query("delete from note_table")
    public void deleteAll();


    @Query("select * from note_table")
    public LiveData<List<NoteUser>> readData();

}
