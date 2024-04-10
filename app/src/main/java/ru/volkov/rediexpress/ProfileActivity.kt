package ru.volkov.rediexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat

class ProfileActivity : AppCompatActivity() {
    private lateinit var btnHideBalance: ImageView;
    private lateinit var tvBalanceValue: TextView;
    private lateinit var tvWelcomeName: TextView;
    private var isBalanceVisible = true;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        btnHideBalance = findViewById(R.id.img_hide)
        tvBalanceValue = findViewById(R.id.tv_balance_value)
        tvWelcomeName = findViewById(R.id.tv_welcome_name)
        tvWelcomeName.text =
            intent.extras?.getString("username").toString().split(" ").toTypedArray()[0];
        btnHideBalance.setOnClickListener {
            when(isBalanceVisible) {
                true -> {
                    isBalanceVisible = false;
                    tvBalanceValue.text = getString(R.string.profilePage_balance_hidden)
                    btnHideBalance.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_eye_open))
                }
                false -> {
                    isBalanceVisible = true;
                    tvBalanceValue.text = getString(R.string.profilePage_balance_value)
                    btnHideBalance.setImageDrawable(ContextCompat.getDrawable(applicationContext, R.drawable.ic_eye_slash))
                }
            }
        }
    }
    fun onLogoutClick(view: View) {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
        finish();
    }
}