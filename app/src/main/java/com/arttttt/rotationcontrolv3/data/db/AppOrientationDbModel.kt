package com.arttttt.rotationcontrolv3.data.db

import androidx.room.Entity

@Entity(
    tableName = "app_orientation_table",
    primaryKeys = [
        "pkg",
    ]
)
data class AppOrientationDbModel(
    val pkg: String,
    val orientation: Int,
)