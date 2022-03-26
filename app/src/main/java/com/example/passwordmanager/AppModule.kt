package com.example.passwordmanager

import android.content.ClipboardManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val singleModules = module {
    // Databaseインスタンスをシングルトンで生成
    single { Room.databaseBuilder(androidContext(), AppDatabase::class.java, "app.db").build() }
    single { androidContext().getSystemService<InputMethodManager>()!! }
    single { androidContext().getSystemService<ClipboardManager>()!! }
    //single { androidContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager }
    //single { androidContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }
}

val factoryModules = module {
    factory { get<AppDatabase>().passInfoDao() }
    // AndroidViewModelを継承している場合、コンストラクタでApplicationが必要になるので「get()」を使う
    //viewModel { HomeViewModel(get()) }
    //viewModel { EditPasswordViewModel(get()) }
}