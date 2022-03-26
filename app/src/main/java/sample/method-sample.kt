package sample

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.passwordmanager.R
import com.example.passwordmanager.base.dataBinding
import com.example.passwordmanager.base.viewBinding
import com.example.passwordmanager.databinding.FragmentEditPasswordBinding
import com.example.passwordmanager.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class HomeFragment: Fragment(R.layout.fragment_home)/* レイアウトIDをいれる */ {
    private val _binding: FragmentHomeBinding? by viewBinding(FragmentHomeBinding::bind) // 引数はこれをいれる
    private val binding get() = checkNotNull(_binding) // これでNonNullに扱える
    /*
    ここは使わない
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding?.root!!
    }*/

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // continued...
    }
}

class EditPasswordFragment: BottomSheetDialogFragment() /* レイアウトIDをいれられない */ {
    private val _binding: FragmentEditPasswordBinding? by dataBinding() // 引数いらない
    private val binding get() = checkNotNull(_binding) // これでNonNullに扱える

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        /* ViewBindingのinflateをつかわないで、inflateしている。これはOKだが、もしbindingを使っていたらエラーになる */
        return inflater.inflate(R.layout.fragment_edit_password, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // continued...
    }
}