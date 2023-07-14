package br.com.writeaway.screen.host

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.writeaway.R
import br.com.writeaway.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.bind(findViewById(R.id.navHostContainer))
    }
}