package com.example.cachecheck.data.remote.db

import android.content.Context
import android.util.Log
import com.example.cachecheck.data.remote.dto.PostResponse
import java.io.*
import kotlin.system.measureTimeMillis

class FileHelper(private val context: Context) {
    private val cacheFileName = "file_cache.txt"

    fun addData(key: String, value: String) {
        val dataMap = getAllData(context).toMutableMap()
        dataMap[key] = value
        writeDataToFile(dataMap)
    }

    fun getDataByKey(key: String): String? {
        val dataMap = getAllData(context)
        Log.d("Checking", "getDataByKey: ${dataMap[key]}")
        return dataMap[key]
    }

    fun getAllData(context: Context): Map<String, String> {
        val dataMap = mutableMapOf<String, String>()
        try {
            val fileInputStream = context.openFileInput(cacheFileName)
            val inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)

            var line: String? = bufferedReader.readLine()
            while (line != null) {
                val parts = line.split(":")
                if (parts.size == 2) {
                    dataMap[parts[0]] = parts[1]
                }
                line = bufferedReader.readLine()
            }

            bufferedReader.close()
            inputStreamReader.close()
            fileInputStream.close()
        } catch (e: FileNotFoundException) {
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return dataMap
    }

    private fun writeDataToFile(dataMap: Map<String, String>) {
        try {
            val fileOutputStream = context.openFileOutput(cacheFileName, Context.MODE_PRIVATE)
            val outputStreamWriter = OutputStreamWriter(fileOutputStream)
            val bufferedWriter = BufferedWriter(outputStreamWriter)

            for ((key, value) in dataMap) {
                bufferedWriter.write("$key:$value\n")
            }

            bufferedWriter.close()
            outputStreamWriter.close()
            fileOutputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun measureTimeToAddAndRetrieveDataToFile(context: Context) {
        val keyValueCache = FileHelper(context)

        val addTime = measureTimeMillis {
            for (i in 1..1000) {
                keyValueCache.addData("key$i", "value$i")
            }
        }
        Log.d("Testing", "Time taken to add data to file cache: $addTime milliseconds")

        val retrieveTime = measureTimeMillis {
            keyValueCache.getAllData(context)
        }
        Log.d("Testing", "Time taken to retrieve data from file cache: $retrieveTime milliseconds")

        val getKeyTime = measureTimeMillis {
            for (i in 1..1000) {
                keyValueCache.getDataByKey("key$i")
            }
        }
        Log.d("Testing", "Time taken to getDataByKey: $getKeyTime milliseconds")
    }

    fun measureTimeToAddAndRetrieveDataToApiFile(context: Context, res: List<PostResponse>) {
        val keyValueCache = FileHelper(context)

        val addTime = measureTimeMillis {
            for (i in 1..1000) {
                keyValueCache.addData("key$i", "value${res.toString()}")
            }
        }
        Log.d("Testing", "Time taken to add data to file cache: $addTime milliseconds")

        val retrieveTime = measureTimeMillis {
            keyValueCache.getAllData(context)
        }
        Log.d("Testing", "Time taken to retrieve data from file cache: $retrieveTime milliseconds")

        val getKeyTime = measureTimeMillis {
            for (i in 1..1000) {
                keyValueCache.getDataByKey("key$i")
            }
        }
        Log.d("Testing", "Time taken to getDataByKey: $getKeyTime milliseconds")
    }
}

//D  Time taken to add data to file cache: 1020 milliseconds
//D  Time taken to retrieve data from file cache: 1 milliseconds
//D  Time taken to getDataByKey: 692 milliseconds
