package com.example.blinkit_adminside.authentication

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.blinkit_adminside.R
import com.example.blinkit_adminside.object_class.Utils
import com.example.blinkit_adminside.databinding.FragmentSigninBinding


class SignInFragment : Fragment() {
    private lateinit var binding:FragmentSigninBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentSigninBinding.inflate(layoutInflater)
        setStatusBarColor()
        getUserName()
        onContinueButtonClick()
        return binding.root
    }

    private  fun onContinueButtonClick(){
        binding.btnContinue.setOnClickListener {
            val number = binding.edtUserName.text.toString()
            if (number.isEmpty() || number.length != 10) {
                Utils.showToast(requireContext(), "Please Enter a Valid 10-Digit Phone Number")
            } else {
                val bundle = Bundle()
                bundle.putString("number", number)
                findNavController().navigate(R.id.action_signinFragment_to_o_T_PFragment, bundle)
            }
        }
    }

    private  fun getUserName(){
        binding.edtUserName.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(number: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            // check if number is 10 then colour is green otherwise gray
            override fun onTextChanged(number: CharSequence?, start: Int, before: Int, count: Int) {
                val length = number?.length
                if (length == 10) {
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        R.color.green
                    ))
                } else {
                    val bundle =Bundle()
                    bundle.putString("number", number.toString())
                    binding.btnContinue.setBackgroundColor(ContextCompat.getColor(requireContext(),
                        R.color.grayeshBule
                    ))
                }
            }



            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }


    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity?.window?.apply {
                statusBarColor = ContextCompat.getColor(requireContext(), R.color.yellow)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                }
            }
        }
    }
}