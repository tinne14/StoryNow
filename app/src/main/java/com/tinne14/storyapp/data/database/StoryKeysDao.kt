package com.tinne14.storyapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.tinne14.storyapp.data.StoryKeys

@Dao
interface StoryKeysDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(remoteKey: List<StoryKeys>)

    @Query("SELECT * FROM story_keys WHERE id = :id")
    suspend fun getStoryKeysId(id: String): StoryKeys?

    @Query("DELETE FROM story_keys")
    suspend fun deleteStoryKeys()
}