package com.example.reminder.SQLDatabase;


import androidx.annotation.RequiresPermission;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MyDAO {

    @Insert
    public void insert(NoteUser user);


    @Update
    public void update(NoteUser user);


    @Delete()
    public void delete(NoteUser user);


    @Query("delete from note_table")
    public void deleteAll();

    @Query("select * from note_table")
    public List<NoteUser> readData();

}
