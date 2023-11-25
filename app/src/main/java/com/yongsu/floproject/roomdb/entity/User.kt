package com.yongsu.floproject.roomdb.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "UserTable")
data class User(

    //Json으로 보낼때는 SerializedName을 써야함. value는 서버에서 지정한 것과 같아야 함
    @SerializedName(value = "email")
    var email : String,

    @SerializedName(value = "password")
    var password : String,

    @SerializedName(value = "name")
    var name: String
){
    @PrimaryKey(autoGenerate = true) var id: Int = 0
}
