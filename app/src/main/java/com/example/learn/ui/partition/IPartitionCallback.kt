package com.example.learn.ui.partition

interface IPartitionCallback {
    fun clickOne(position: Int, text: String?)
    fun clickTwo(position: Int, text: String?)
    fun clickThree(position: Int, text: String?)
}