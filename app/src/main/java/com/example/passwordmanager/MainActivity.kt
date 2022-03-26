package com.example.passwordmanager

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.ActivityMainBinding

/**
 * 既存のものから何か変更を加えるのであれば、
 * manifests/AndroidManifest.xml
 * res/themes/themes.xml
 * も気にかけておくこと。
 * どちらもActionBarと呼ばれる、画面上部のツールバー的なところのスタイル定義に関連するもの。
 * （どういう風になっているかは学習中...)
 * */
class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //_binding = ActivityMainBinding.inflate(layoutInflater)
        //setContentView(binding.root)

        /*
        ・ナビゲーションコントローラはどういう風にフラグメントを遷移させるかを操作するもの。
        　　activityのlayoutにnavHostFragmentを追加する必要あり
        　　（navHostFragmentはフラグメントを表示するためのコンテナのようなもの）
        ・BottomNavigationView等、フラグメント切り替えのためのメニューバーみたいなものも必要（とりあえずその2つだけあれば画面遷移は可能になる）
        */
        val navController = findNavController(R.id.menu_nav_host)
        //setupActionBarWithNavController(navController) // これをつけるとツールバーにタイトル表示（と戻るボタンが追加）される。いらないので削除
        binding.menuNavView.setupWithNavController(navController)
    }
}