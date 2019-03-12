package com.example.titi

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class MainActivity : AppCompatActivity() {

    val LISP: Int = 0
    lateinit var uri: Uri
    lateinit var mStorage: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lspButton = findViewById<View>(R.id.lspButton) as Button

        mStorage = FirebaseStorage.getInstance().getReference("Uploads")

        lspButton.setOnClickListener(View.OnClickListener {
            view: View? -> val intent = Intent()
            intent.setType("lsp/*")
            intent.setAction(Intent.ACTION_GET_CONTENT)
            startActivityForResult(Intent.createChooser(intent, "Seleccionar archivo .lsp"), LISP)
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val uriTxt = findViewById<View>(R.id.uriTxt) as TextView
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == LISP) {
                uri = data!!.data
                uriTxt.text = uri.toString()
                upload()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun upload() {
        var mReference = mStorage.child(uri.lastPathSegment)
        try {
            mReference.putFile(uri).addOnSuccessListener{
                    taskSnapshot: UploadTask.TaskSnapshot? -> var url = taskSnapshot!!.downloadUrl
                val dwnTxt = findViewById<View>(R.id.dwnTxt) as TextView
                dwnTxt.text = url.toString()
                Toast.makeText(this, "Upload exitosa", Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show()
        }

    }
}
