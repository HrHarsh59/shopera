package project.demo.shopera

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.firestore.FirebaseFirestore
import project.demo.shopera.databinding.ActivitySignInBinding
import java.util.concurrent.TimeUnit

class SignInActivity : AppCompatActivity() {

    private var _binding: ActivitySignInBinding? = null
    private val binding get() = _binding!!

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private var storeVerificationId: String? = ""
    private lateinit var resendingToken: PhoneAuthProvider.ForceResendingToken

    private lateinit var phoneNum: String

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = binding.progressBar

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                signWithCredential(p0)
                progressBar.visibility = ProgressBar.INVISIBLE
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                progressBar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this@SignInActivity, "Failed Verification", Toast.LENGTH_SHORT)
                    .show()
            }

            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                storeVerificationId = p0
                resendingToken = p1
                progressBar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this@SignInActivity, "Otp Send...", Toast.LENGTH_SHORT).show()
            }
        }

        binding.sendOtpButton.setOnClickListener {
            phoneNum = binding.userNumber.text.toString().trim()
            if (phoneNum.isNullOrEmpty()) {
                binding.userNumber.error = "Enter Phone Number"
                return@setOnClickListener
            }
            sendVerificationCode(phoneNum)
        }

        binding.verifyButton.setOnClickListener {
            val otp = binding.userOtp.text.toString().trim()
            if (otp.isNullOrEmpty()) {
                binding.userOtp.error = "Enter OTP"
                return@setOnClickListener
            }
            verifyPhoneNumberWithCode(storeVerificationId, otp)
        }

    }

    override fun onStart() {
        super.onStart()
        val firebaseUser = auth.currentUser
        updateUi(firebaseUser)
    }

    private fun updateUi(firebaseUser: FirebaseUser?) {
        if (firebaseUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signWithCredential(credential: PhoneAuthCredential) {
        progressBar.visibility = ProgressBar.VISIBLE
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = ProgressBar.INVISIBLE
                    val phone: String = auth.currentUser?.phoneNumber!!
                    Toast.makeText(this@SignInActivity, "Logged in as : $phone", Toast.LENGTH_SHORT)
                        .show()
                    val userId = auth.currentUser!!.uid
                    val docRef = db.collection("users").document(userId)
                    docRef.get()
                        .addOnSuccessListener { document ->
                        if (document != null) {
                            if (document.data == null) {
                                val intent = Intent(this, ProfileCreateActivity::class.java)
                                startActivity(intent)
                            }
                            else {
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        }
                        .addOnFailureListener {
                            progressBar.visibility = ProgressBar.INVISIBLE
                            Toast.makeText(
                                this@SignInActivity,
                                "Sign In Fail (upload data)",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    progressBar.visibility = ProgressBar.INVISIBLE
                    Toast.makeText(
                        this@SignInActivity,
                        "Sign In Fail (signInWithCredential)",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun sendVerificationCode(number: String) {

        progressBar.visibility = ProgressBar.VISIBLE

        val option = PhoneAuthOptions.newBuilder(auth)
            .setPhoneNumber("+91$number")
            .setTimeout(60L, TimeUnit.SECONDS)
            .setActivity(this)
            .setCallbacks(callbacks)
            .build()
        PhoneAuthProvider.verifyPhoneNumber(option)
    }

    private fun verifyPhoneNumberWithCode(verificationId: String?, code: String) {

        val credential = PhoneAuthProvider.getCredential(verificationId!!, code)
        signWithCredential(credential)
    }
}