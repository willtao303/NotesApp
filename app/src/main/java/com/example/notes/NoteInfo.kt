package com.example.notes

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class NoteInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "creation_time") val noteTime: Int,
    @ColumnInfo(name = "creation_user") val noteUser: String,
    @ColumnInfo(name = "note_starred") val noteStarred: Boolean = false,
    @ColumnInfo(name = "note_name") val noteName: String?,
    @ColumnInfo(name = "note_type") val noteType: String?,
    //filepath
    @ColumnInfo(name = "primary_file") val primaryFile: String?,
    @ColumnInfo(name = "secondary_file") val secondaryFile: String?
){

}
/* sample table info
id creation_time creation_user note_starred note_name
0 07,25,10:13 joe false classA
1 07,27,01:46 joe true plans
2 07,27,01:47 joe false plans2
3 06,13,05:31 joe false classA
4 03,20,03:00 joe true classA
5 07,25,07:55 bob false classA
6 07,25,10:13 bob true classA
 */