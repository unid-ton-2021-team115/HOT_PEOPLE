package ins.hands.unid

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.format.DateFormat.is24HourFormat
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.EditText
import android.widget.TimePicker
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import ins.hands.unid.databinding.ActivityMatchWaitBinding
import okhttp3.internal.notify
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.DateFormat
import java.util.*





class MatchWaitActivity : BaseActivity() {
    val viewModel : MatchWaitViewModel by viewModel()
    val bind by binding<ActivityMatchWaitBinding>(R.layout.activity_match_wait)
    var message = ""
    var placeId = ""
    lateinit var adapter: MatchFindAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_match_wait)

        adapter = MatchFindAdapter(
            {image, url ->
                viewModel.getProfileImage(image,url)
            }
        ,{bind, id ->
                viewModel.getPlaceDataById(bind,id)
            },{
                id ->
                viewModel.joinMatch(id)
            }
        )
        placeId=intent.getStringExtra("placeId")!!
        viewModel.getPlaceDataById(bind,placeId)

        viewModel.getMatchByPlace(placeId)
        bind.apply{
            adapter=this@MatchWaitActivity.adapter
        }
        viewModel.matchingList.observe(this,{
            adapter.dataList = it
            adapter.notifyDataSetChanged()
        })
        bind.btAddMatch.setOnClickListener {
            AlertDialog.Builder(this).apply{
                setTitle("새로운 매칭 만들기")
                setMessage("하고싶은 메세지를 입력하세요")
                val input = EditText(this@MatchWaitActivity)
                setView(input)
                setPositiveButton("완료") { dialog, which ->
                    message = input.text.toString()

                    DatePickerFragment ({
                        viewModel.createMatch(placeId, message, it)
                    },supportFragmentManager,this@MatchWaitActivity).show(supportFragmentManager,"날짜")
                }
                setNegativeButton("취소",DialogInterface.OnClickListener { dialog, which ->  })
                
                show()
            }
        }
    }

}

class TimePickerFragment(val onEnd:(timeData : MyTimeData)->Unit, val timeData: MyTimeData) : DialogFragment(), TimePickerDialog.OnTimeSetListener {


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current time as the default values for the picker
        val c = Calendar.getInstance()
        val hour = c.get(Calendar.HOUR_OF_DAY)
        val minute = c.get(Calendar.MINUTE)

        // Create a new instance of TimePickerDialog and return it
        return TimePickerDialog(activity, this, hour, minute, android.text.format.DateFormat.is24HourFormat(activity))
    }

    override fun onTimeSet(view: TimePicker, hourOfDay: Int, minute: Int) {
        // Do something with the time chosen by the user
        timeData.hour= hourOfDay
        timeData.minute = minute
        onEnd(timeData)
    }
}

class DatePickerFragment(val onEnd: (timeData: MyTimeData) -> Unit, val supportFragmentManager: FragmentManager,val mycontext: Context) : DialogFragment(), DatePickerDialog.OnDateSetListener {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // Use the current date as the default date in the picker
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        // Create a new instance of DatePickerDialog and return it
        return DatePickerDialog(mycontext, this, year, month, day)
    }

    override fun onDateSet(view: DatePicker, year: Int, month: Int, day: Int) {
        // Do something with the date chosen by the user
        TimePickerFragment(onEnd, MyTimeData(year,month,day,0,0)).apply{
            show(supportFragmentManager,"시간")
        }
    }
}
data class MyTimeData(var year:Int, var month : Int, var day : Int, var hour : Int, var minute : Int)