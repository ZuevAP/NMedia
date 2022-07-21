package ru.netology.nmedia.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.activity.result.contract.ActivityResultContract
import ru.netology.nmedia.activity.Constance.EDIT_TEXT


import ru.netology.nmedia.databinding.ActivityEditPostBinding



class EditPostActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityEditPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val editText = intent.getStringExtra(EDIT_TEXT)


        binding.edit.text = Editable.Factory.getInstance().newEditable(editText)

        binding.edit.requestFocus()

        binding.ok.setOnClickListener {
            val intent = Intent()
            if (binding.edit.text.isNullOrBlank()){
                setResult(Activity.RESULT_CANCELED, intent)
            } else {
                intent.putExtra(EDIT_TEXT, binding.edit.text.toString())
                setResult(Activity.RESULT_OK, intent)
            }
            finish()
        }

        }


    }


class EditPostResultContract :  ActivityResultContract<String, String?>() {

    override fun createIntent(
        context: Context, input: String
    ) = Intent(context, EditPostActivity::class.java).apply {
        putExtra(EDIT_TEXT, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (resultCode != Activity.RESULT_OK) return null
        return intent?.getStringExtra(EDIT_TEXT)
    }


}


