package project.demo.shopera

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import project.demo.shopera.databinding.ActivityProfileCreateBinding
import project.demo.shopera.model.UserData

class ProfileCreateActivity : AppCompatActivity() {

    private var _binding: ActivityProfileCreateBinding? = null
    private val binding get() = _binding!!

    private lateinit var filePathUri: Uri

    //Firebase
    private lateinit var userCollection: CollectionReference
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private val auth = Firebase.auth

    private lateinit var userName: String
    private lateinit var userLocation: String
    private lateinit var userEmail: String

    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityProfileCreateBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()

        progressBar = binding.progressBarProfile

        binding.skipButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.createProfileButton.setOnClickListener {
            userName = binding.userName.text.toString().trim()
            userLocation = binding.userLocation.text.toString().trim()
            userEmail = binding.userEmail.text.toString().trim()
            userCollection = db.collection("users")

            if (userName.isEmpty()) {
                binding.userName.error = "Enter Name"
                return@setOnClickListener
            }

            if (userLocation.isEmpty()) {
                binding.userLocation.error = "Enter Location"
                return@setOnClickListener
            }

            if (userEmail.isEmpty()) {
                binding.userEmail.error = "Enter Email"
                return@setOnClickListener
            }
            val userId = auth.currentUser!!.uid
            val userData = UserData(userId, userName, userLocation, userEmail)
            createProfile(userId, userData)

        }
    }

    private fun createProfile(id: String, data: UserData) {
        progressBar.visibility = ProgressBar.VISIBLE
        userCollection.document(id).set(data).addOnCompleteListener {
            progressBar.visibility = ProgressBar.INVISIBLE
            Toast.makeText(this, "Profile Created", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
            .addOnFailureListener {
                progressBar.visibility = ProgressBar.INVISIBLE
                Toast.makeText(this, "Failing...", Toast.LENGTH_SHORT).show()
            }
    }
}