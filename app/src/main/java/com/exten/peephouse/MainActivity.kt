package com.exten.peephouse

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.exten.peephouse.utils.ToolbarWorker

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel:MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.toolbar_main))
        viewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)
        val tb = ToolbarWorker(this)
        viewModel.toolbarMode.observe(this, Observer {
            tb.switchBox(it)
        })
        if(viewModel.currentUser == null){
            //remain on registration
        }else{
            //check if premium y-> check questions   n->check questions
            viewModel.init()
        }
    }
}