package com.ersubhadip.blueconnect_offlinechatapp.core

import android.content.Context
import com.ersubhadip.blueconnect_offlinechatapp.bluetooth.AndroidBluetoothControllerImpl
import com.ersubhadip.blueconnect_offlinechatapp.bluetooth.BluetoothController
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideBluetoothController(@ApplicationContext context: Context): BluetoothController =
        AndroidBluetoothControllerImpl(context)
}