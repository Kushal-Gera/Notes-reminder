package com.example.reminder.SQLDatabase;

import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = NoteUser.class, version = 1)
public abstract class NoteDatabase extends RoomDatabase {

    public abstract MyDAO mydao();

}
