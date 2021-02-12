package com.oktydeniz.movielist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.oktydeniz.movielist.adapters.SQLAdapter

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = Navigation.findNavController(this, R.id.fragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.delete_all_table) {
            val builder = AlertDialog.Builder(this)
            builder.setTitle(getString(R.string.alert_title))
            builder.setMessage(getString(R.string.alert_message))
            builder.setCancelable(false)
            builder.setIcon(R.drawable.delete_sweep_drawable)
            builder.setPositiveButton(getString(R.string.delete)) { _, _ ->
                val deleteAll = SQLAdapter(this)
                deleteAll.deleteAllTable()
                startActivity(Intent(this, MainActivity::class.java))
            }
            builder.setNegativeButton(getString(R.string.cancel)) { _, _ ->
            }
            val alertDialog = builder.create()
            alertDialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.fragment)
        return navController.navigateUp()
    }
}