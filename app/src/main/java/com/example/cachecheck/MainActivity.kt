package com.example.cachecheck

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.cachecheck.data.remote.PostsService
import com.example.cachecheck.data.remote.db.DatabaseHelper
import com.example.cachecheck.data.remote.db.FileHelper
import com.example.cachecheck.data.remote.dto.PostResponse
import com.example.cachecheck.ui.theme.CacheCheckTheme
import org.json.JSONObject

class MainActivity : ComponentActivity() {
    private val client = PostsService.create()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val dbHelper = DatabaseHelper(this)
        val fileHelper = FileHelper(this)

        setContent {
            val posts = produceState<List<PostResponse>>(
                initialValue = emptyList(),
                producer = {
                    value = client.getPosts()
                })
//            Log.d("Testing", "onCreate: $posts")
//            val s = dbHelper.addData("key", "value")
//            val st = dbHelper.addData("key2", "value2")
//            Log.d("Testing", "onCreate: addData $st")
//            val ss = dbHelper.getAllData()
//            Log.d("Testing", "onCreate: getAllData $ss")
//
//
//            val f = fileHelper.addData("filekey", "filevalue")
//            Log.d("Testing", "onCreate: getDataByKey ${fileHelper.getDataByKey("filekey")}")
//            Log.d("Testing", "onCreate: getAllData ${fileHelper.getAllData(this)}")
//            Log.d("Testing", "onCreate: getAllData ${dbHelper.addData("this", "snjsb")}")
//            Log.d("Testing", "onCreate: measureTimeToAddAndRetrieveDataToFile ${fileHelper.measureTimeToAddAndRetrieveDataToFile(this)}")
//            dbHelper.measureTimeToAddAndRetrieveData(this)
            CacheCheckTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Log.d("jndnjsd", "onCreate: ${posts.value}")
                    fileHelper.measureTimeToAddAndRetrieveDataToApiFile(this, posts.value)
                    LazyColumn {
//                        Log.d("Checking", "T: ${posts.value}")
                        items(posts.value) {
//                            Log.d("Checking", "onCreate: $it")
                            Text(text = it.title)
                        }
                    }
                }
            }

        }
    }

    fun jsonObjectToString(jsonObject: PostResponse): String {
        return jsonObject.toString()
    }

//    fun stringToJsonObject(stringValue: String): JSONObject {
//        return PostResponse(stringValue)
//    }
}