package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTask = mutableListOf<String>()
    lateinit var adapter: TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClicked(position: Int) {
              // 1. remove the item from the list
               listOfTask.removeAt(position)
              // 2. Notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()

                saveItems()
            }

        }

        //1.Let's detect when the user clicks on the add button
//        findViewById<Button>(R.id.button).setOnClickListener {
//            // code in here is going to be executed when the user clicks on a button
//            Log.i("Jennie","User clicked on button")
//        }

        loadItems()

        // Look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTask,onLongClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set up the button and input field, so that the user can enter a task and add it to the list
            val inputTextField = findViewById<EditText>(R.id.addTaskField)
        // get a reference to the button
        // and then set an onclicklistener
        findViewById<Button>(R.id.button).setOnClickListener {
            // 1. Grab the text the user has inputted into @id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            // 2. Add the string to our list of tasks: listOftasks
            listOfTask.add(userInputtedTask)

            // Notify the adapter that our data has been uptated
            adapter.notifyItemInserted(listOfTask.size - 1)

            // 3. reser text field
            inputTextField.setText("")

            saveItems()
        }

    }

    // save the data that the user has inputted
    // Save data by writing and reading from a file

    // Get the file we need
    fun getDataFile() : File{

        // Every line is going to represent a specific task in our list of tasks
        return File(filesDir,"data.txt")
    }

    // Load the items by reading every line in the data
    fun loadItems() {
        try {
            listOfTask = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
    // Save items by writing them into our data file
    fun saveItems() {
        try {
            FileUtils.writeLines(getDataFile(),listOfTask)
        }catch (ioException: IOException){
            ioException.printStackTrace()
        }

    }
}