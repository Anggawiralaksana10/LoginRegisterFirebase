package com.example.anggaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.example.anggaapp.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var binding: ActivityRegisterBinding
    lateinit var DbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        auth = FirebaseAuth.getInstance()
        binding.tvToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.btnRegister.setOnClickListener {
            val email = binding.edtEmailRegister.text.toString()
            val password = binding.edtPasswordRegister.text.toString()
            val name = binding.edtNameRegister.text.toString()
            val number_phone = binding.edtNoHpRegister.text.toString()

        if (email.isEmpty()) {
            binding.edtEmailRegister.error = "Email Harus Diisi"
            binding.edtEmailRegister.requestFocus()
            return@setOnClickListener
        }

        //Validasi email tidak sesuai
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edtEmailRegister.error = "Email Tidak Valid"
            binding.edtEmailRegister.requestFocus()
            return@setOnClickListener
        }

        //Validasi password
        if (password.isEmpty()) {
            binding.edtPasswordRegister.error = "Password Harus Diisi"
            binding.edtPasswordRegister.requestFocus()
            return@setOnClickListener
        }

        //Validasi panjang password
        if (password.length < 6) {
            binding.edtPasswordRegister.error = "Password Minimal 6 Karakter"
            binding.edtPasswordRegister.requestFocus()
            return@setOnClickListener
        }
            if (name.isEmpty()) {
                binding.edtNameRegister.error = "Name Harus Diisi"
                binding.edtNameRegister.requestFocus()
                return@setOnClickListener
            }
            if (number_phone.isEmpty()) {
                binding.edtNoHpRegister.error = "Number Phone Harus Diisi"
                binding.edtNoHpRegister.requestFocus()
                return@setOnClickListener
            }
        RegisterFirebase(name, email, password)
    }
}

private fun RegisterFirebase(name: String, email: String, password: String) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(this) {
            if (it.isSuccessful) {
                addUserToDatabase(name,email,auth.currentUser?.uid!!)
                Toast.makeText(this, "Register Berhasil", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                finish()
                startActivity(intent)
            } else {
                Toast.makeText(this, "${it.exception?.message}", Toast.LENGTH_SHORT).show()
            }
        }
}
    private fun addUserToDatabase(name:String, email:String, uid:String){
        DbRef = FirebaseDatabase.getInstance().getReference("users")
        DbRef.child("users").child(uid).setValue(User(name,email,uid))
    }
}
