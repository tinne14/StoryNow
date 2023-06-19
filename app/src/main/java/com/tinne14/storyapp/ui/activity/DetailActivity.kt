package com.tinne14.storyapp.ui.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.tinne14.storyapp.R
import com.tinne14.storyapp.data.remote.response.Story
import com.tinne14.storyapp.databinding.ActivityDetailBinding
import com.tinne14.storyapp.ui.ViewModelFactory
import com.tinne14.storyapp.ui.viewmodel.DetailViewModel
import java.text.SimpleDateFormat
import java.util.Date

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class DetailActivity : AppCompatActivity() {



    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModels {
        ViewModelFactory.getInstance(this, dataStore)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        var id = intent.getStringExtra(EXTRA_ID)

        viewModel.isLoading.observe(this, Observer {
            showLoading(it)
        })

        viewModel.getToken().observe(this, Observer {
            viewModel.getStoriesDetail("Bearer $it", id.toString())
        })

        viewModel.listStoriesDetail.observe(this, Observer {
            setItems(it)
        })

        binding.btnBack.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    fun setItems(item: Story?) {
        binding.apply {
            Glide.with(this@DetailActivity)
                .load(item?.photoUrl)
                .into(photo)
            name.text = "${item?.name}"
            idstorie.text = resources.getString(R.string.detail_desc, item?.id)
            val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val datee: Date = isoFormat.parse(item?.createdAt)
            date.text = resources.getString(R.string.detail_desc, datee)
            description.text = resources.getString(R.string.detail_desc, item?.description)
            lat.text = resources.getString(R.string.detail_desc, item?.lat)
            lon.text = resources.getString(R.string.detail_desc, item?.lon)
        }

    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    companion object {
        const val EXTRA_ID = "extra_detail"
    }
}