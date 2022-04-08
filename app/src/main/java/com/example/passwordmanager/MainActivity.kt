package com.example.passwordmanager

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.ActivityMainBinding

/**
 * 既存のものから何か変更を加えるのであれば、
 * manifests/AndroidManifest.xml
 * res/themes/themes.xml
 * も気にかけておくこと。
 *
 * ActionBarとToolBarの違いは、ActionBarがデフォルトで表示されるものに対し、ToolBarはレイアウトに追加しないと表示されない点
 * ・ToolBarのほうが後発で、柔軟なカスタマイズ可能で上位互換
 * ・ToolBarを表示する際は、ActionBarは隠すようにしないと上部にバーが２つ表示されてしまう
 * ・ActionBarはTheme.xmlでNoActionBarなどの設定を加えて非表示にするなどの方法がある
 * */
class MainActivity: AppCompatActivity(R.layout.activity_main) {
    companion object {
        val ITEM_ID = "item_id"
    }

    private val binding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /*
        ・ナビゲーションコントローラはどういう風にフラグメントを遷移させるかを操作するもの。
        　　activityのlayoutにnavHostFragmentを追加する必要あり
        　　（navHostFragmentはフラグメントを表示するためのコンテナのようなもの）
        ・BottomNavigationView等、フラグメント切り替えのためのメニューバーみたいなものも必要（とりあえずその2つだけあれば画面遷移は可能になる）
        */
        val navController = findNavController(R.id.menu_nav_host)
        binding.run {
            menuNavView.setupWithNavController(navController)
            settingToolbar.setOnMenuItemClickListener { item ->
                if (item == null) false
                else {
                    val intent = Intent(applicationContext, OptionActivity::class.java)
                    intent.putExtra(ITEM_ID, item.itemId)
                    startActivity(intent)
                    true
                }
            }
        }
    }
}