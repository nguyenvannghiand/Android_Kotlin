package com.example.flowkotlinexample

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL

class MainActivity : AppCompatActivity() {
	val TAG: String = "Demo"
	val URL_COLLECTION =
		"https://sg.tpserverproxy.xyz/ringtonegz/rest/categories?lang=vi_VN&mobileid=454573a0e603fec4_sdk30_tg30&token=928509030628e8bad97c8c6375&appid=fr2019tkv2secv54"
	private var recyclerView: RecyclerView? = null


	private var listPhoto = mutableListOf<Photo>()

	@SuppressLint("NewApi")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		recyclerView = findViewById(R.id.photo_list)
		CoroutineScope(Dispatchers.IO).launch {
			getListPhoto()
			Log.d(TAG, "get>>>>>>>>>> $listPhoto")
			runBlocking(Dispatchers.Main) {
				Log.d(TAG, "get>>>>>>>>>> $listPhoto")
				loadCategory(listPhoto)
			}
		}
		findViewById<Button>(R.id.sort_id).setOnClickListener {
			val listSort = listPhoto
			listSort.sortWith(compareBy { it.id })
			Log.d(TAG, "get sort id $listSort")
			loadCategory(listSort)
			//Short Id
		}
		findViewById<Button>(R.id.sort_name).setOnClickListener {
			//Short Name
			val listSort = listPhoto
			listSort.sortWith(compareBy { it.name })
			Log.d(TAG, "get sort name $listSort")
			loadCategory(listSort)
		}
		findViewById<Button>(R.id.reset_sort).setOnClickListener {
			//Short Name
			Log.d(TAG, "get reset name $listPhoto")
			loadCategory(listPhoto)
		}
	}

	private fun loadCategory(list: List<Photo>) {
		val adapter = CategoryAdapter()
		adapter.setCategoryList(list)
		recyclerView!!.adapter = adapter
	}

	suspend fun foo(): Flow<String> = flow {
		emit(getJSON(URL_COLLECTION, 1000))
	}

	fun getListPhoto() = runBlocking {
		foo().collect { value ->
			val rootJson: ObjectJson =
				Gson().fromJson(value, ObjectJson::class.java)
			Log.d(TAG, "rootJson ${rootJson.data}}")
			listPhoto = rootJson.data as MutableList<Photo>
		}
	}

	private fun getJSON(url: String, timeout: Int): String {
		var c: HttpURLConnection? = null
		try {
			val u = URL(url)
			c = u.openConnection() as HttpURLConnection
			c.requestMethod = "GET"
			c.setRequestProperty("Content-length", "0")
			c.useCaches = false
			c.allowUserInteraction = false
			c.connectTimeout = timeout
			c.readTimeout = timeout
			c.connect()
			when (c.responseCode) {
				200, 201 -> {
					val br = BufferedReader(InputStreamReader(c.inputStream))
					val sb = StringBuilder()
					var line: String?
					while (br.readLine().also { line = it } != null) {
						sb.append("""    $line    """.trimIndent())
					}
					br.close()
					return sb.toString()
				}
			}
		} catch (ex: MalformedURLException) {
			Log.e(TAG, "1$ex")
		} catch (ex: IOException) {
			Log.e(TAG, "2$ex")
		} finally {
			if (c != null) {
				try {
					c.disconnect()
				} catch (ex: Exception) {
					Log.e(TAG, "3$ex")
				}
			}
		}
		return ""
	}

}