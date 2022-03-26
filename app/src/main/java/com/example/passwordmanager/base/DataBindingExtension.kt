package com.example.passwordmanager.base

import android.view.View
import android.view.ViewGroup
import androidx.annotation.MainThread
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * [AppCompatActivity]などアクティビティのコンストラクタ引数に`R.layout.activity_main` など、**レイアウトのID**を入れる必要あり
 *
 * なぜなら`onCreate(savedInstanceState: Bundle?)`内で`setContentView(binding.root)`を行うとエラーになるから
 * */
@MainThread
fun <T : ViewDataBinding> FragmentActivity.dataBinding(): ReadOnlyProperty<FragmentActivity, T> {
    return object : ReadOnlyProperty<FragmentActivity, T> {
        override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
            val view = thisRef.findViewById<ViewGroup>(android.R.id.content)[0]
            return checkNotNull(DataBindingUtil.bind<T>(view)).apply { lifecycleOwner = thisRef }
        }
    }
}

/**
 * [Fragment]のコンストラクタ引数に `R.layout.fragment_home` など、レイアウトのIDを入れて、
 * `binding`には`onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)`
 * より後に（つまり`onViewCreated(view: View, savedInstanceState: Bundle?)`から）アクセスするようにしないといけない
 *
 * （レイアウトIDを入れるのはそうすることでviewのrootを設定する必要がなくなるからであり、bindingをつかわずにinflateするのであれば問題ない<br>
 * **実際、[BottomSheetDialogFragment]にはレイアウトIDを入れられないので、そのようにするしかない**）
 *
 * @sample sample.EditPasswordFragment
 * */
@MainThread
fun <T : ViewDataBinding> Fragment.dataBinding(): ReadOnlyProperty<Fragment, T?> {
    return object : ReadOnlyProperty<Fragment, T?> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
            val view = thisRef.view ?: return null
            return checkNotNull(DataBindingUtil.bind<T>(view)).apply { lifecycleOwner = thisRef.viewLifecycleOwner }
        }
    }
}

/**
 * [AppCompatActivity]などアクティビティのコンストラクタ引数に`R.layout.activity_main` など、**レイアウトのID**を入れる必要あり
 *
 * なぜなら`onCreate(savedInstanceState: Bundle?)`内で`setContentView(binding.root)`を行うとエラーになるから
 * */
@MainThread
inline fun <T : ViewBinding> FragmentActivity.viewBinding(crossinline block: View.() -> T): ReadOnlyProperty<FragmentActivity, T> {
    return object : ReadOnlyProperty<FragmentActivity, T> {
        private var binding: T? = null
        override fun getValue(thisRef: FragmentActivity, property: KProperty<*>): T {
            val view = thisRef.findViewById<ViewGroup>(android.R.id.content)[0]
            return binding ?: block.invoke(view).apply { binding = this }
        }
    }
}

/**
 * [Fragment]のコンストラクタ引数に `R.layout.fragment_home` など、レイアウトのIDを入れて、
 * `binding`には`onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)`
 * より後に（つまり`onViewCreated(view: View, savedInstanceState: Bundle?)`から）アクセスするようにしないといけない
 *
 * （レイアウトIDを入れるのはそうすることでviewのrootを設定する必要がなくなるからであり、bindingをつかわずにinflateするのであれば問題ない<br>
 * **実際、[BottomSheetDialogFragment]にはレイアウトIDを入れられないので、そのようにするしかない**）
 *
 * @sample sample.HomeFragment
 * */
@MainThread
inline fun <reified T : ViewBinding> Fragment.viewBinding(crossinline block: View.() -> T): ReadOnlyProperty<Fragment, T?> {
    return object : ReadOnlyProperty<Fragment, T?> {
        override fun getValue(thisRef: Fragment, property: KProperty<*>): T? {
            val view = thisRef.view ?: return null
            val key = T::class.java.name.hashCode()
            @Suppress("UNCHECKED_CAST")
            return view.getTag(key) as? T ?: block.invoke(view).apply { view.setTag(key, this) }
        }
    }
}