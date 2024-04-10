package ru.volkov.rediexpress

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.volkov.meditation.adapters.login
import ru.volkov.meditation.net.Api
import ru.volkov.meditation.net.MyRetrofit

class LoginActivity : AppCompatActivity() {
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText

    lateinit var tvLoginError: TextView
    lateinit var tvPasswordError: TextView

    lateinit var btnLogin: Button

    var isLoginValid: Boolean = false;
    var isPasswordValid: Boolean = false;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etLogin = findViewById(R.id.et_login)
        etPassword = findViewById(R.id.et_password)
        tvLoginError = findViewById(R.id.tv_login_error)
        tvPasswordError = findViewById(R.id.tv_password_error)
        btnLogin = findViewById(R.id.btn_login)
        etLogin.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isLoginValid = (s.toString().length !== 0);
                btnLogin.isEnabled = isLoginValid && isPasswordValid;
                when (isLoginValid) {
                    true -> {
                        setEtLoginValid(true)
                        tvLoginError.visibility = View.GONE
                    }
                    false -> {
                        setEtLoginValid(false)
                        tvLoginError.visibility = View.VISIBLE
                    }
                }
            }
        })
        etPassword.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                isPasswordValid = (s.toString().length !== 0);
                btnLogin.isEnabled = isLoginValid && isPasswordValid;
                when (isPasswordValid) {
                    true -> {
                        setEtPasswordValid(true)
                        tvPasswordError.visibility = View.GONE
                    }
                    false -> {
                        setEtPasswordValid(false)
                        tvPasswordError.visibility = View.VISIBLE
                    }
                }
            }
        })
    }
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("login", etLogin.text.toString())
        outState.putString("password", etPassword.text.toString())
        super.onSaveInstanceState(outState)
    }
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        etLogin.setText(savedInstanceState.getString("login"))
        etPassword.setText(savedInstanceState.getString("password"))
    }
    fun goToProfile(view: View) {
        val login = etLogin.text.toString()
        val password = etPassword.text.toString()
        if(etLogin.text.isNotEmpty() && etPassword.text.isNotEmpty()) {

            val log = MyRetrofit().get()
            val getApi = log.create(Api::class.java)

            val hashMap: HashMap<String, String> = HashMap()

            hashMap.put("login", login)
            hashMap.put("password", password)

            val logCall: Call<login> = getApi.getAuth(hashMap)

            val contextThemeWrapper = ContextThemeWrapper(this, androidx.appcompat.R.style.AlertDialog_AppCompat)

            logCall.enqueue(object : Callback<login> {
                override fun onResponse(call: Call<login>, response: Response<login>) {
                    if (response.isSuccessful) {
                        val intent = Intent(this@LoginActivity, ProfileActivity::class.java)
                        intent.putExtra("username", response.body()?.username)
                        startActivity(intent);
                        finish();
                    } else {
                        setEtLoginValid(false);
                        setEtPasswordValid(false);
                        var str = response.errorBody()!!.string()
                        val alert = AlertDialog.Builder(contextThemeWrapper)
                            .setTitle("Ошибка входа")
                            .setMessage(when(str){
                                "" -> response.raw().code().toString() + ": " + response.raw().message()
                                else -> str
                            })
                            .setPositiveButton("ОК",null)
                            .create()
                            .show()
                    }
                }

                override fun onFailure(call: Call<login>, t: Throwable) {
                    val alert = AlertDialog.Builder(contextThemeWrapper)
                        .setTitle("Ошибка")
                        .setMessage(t.message.toString())
                        .setPositiveButton("ОК",null)
                        .create()
                        .show()
                }

            })
        } else {
            val alert = AlertDialog.Builder(ContextThemeWrapper(this, androidx.appcompat.R.style.AlertDialog_AppCompat))
                .setTitle("Ошибка")
                .setMessage("У вас есть незаполненные поля")
                .setPositiveButton("ОК",null)
                .create()
                .show()
        }
    }
    fun setEtLoginValid(isValid: Boolean) {
        return when(isValid) {
            true -> {
                etLogin.setTextAppearance(R.style.editTextDefault)
                etLogin.background = ContextCompat.getDrawable(applicationContext, R.drawable.bordered_et_default)
            }
            false -> {
                etLogin.setTextAppearance(R.style.editTextError)
                etLogin.background = ContextCompat.getDrawable(applicationContext, R.drawable.bordered_et_error)
            }
        }
    }
    fun setEtPasswordValid(isValid: Boolean) {
        return when(isValid) {
            true -> {
                etPassword.setTextAppearance(R.style.editTextDefault)
                etPassword.background = ContextCompat.getDrawable(applicationContext, R.drawable.bordered_et_default)
            }
            false -> {
                etPassword.setTextAppearance(R.style.editTextError)
                etPassword.background = ContextCompat.getDrawable(applicationContext, R.drawable.bordered_et_error)
            }
        }
    }
}