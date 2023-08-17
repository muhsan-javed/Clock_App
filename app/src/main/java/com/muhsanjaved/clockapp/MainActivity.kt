package com.muhsanjaved.clockapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.ArrayAdapter
import android.widget.NumberPicker
import com.google.android.material.button.MaterialButton
import com.muhsanjaved.clockapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    var isRunning = false
    private var minutes:String?= "00.00.00"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var lapsList = ArrayList<String>()
        var arrayAdapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,lapsList)
        binding.listView.adapter = arrayAdapter
        binding.lapMaterialButton.setOnClickListener {
            if (isRunning){
                lapsList.add(binding.chronometerTextView.text.toString())
                arrayAdapter.notifyDataSetChanged()
            }
        }

        binding.clockImageView.setOnClickListener {
            var dialog = Dialog(this)
            dialog.setContentView(R.layout.dialog)
            var numberPicker = dialog.findViewById<NumberPicker>(R.id.numberPicker)
            var setTimeMaterialButton = dialog.findViewById<MaterialButton>(R.id.setTimeMaterialButton)
            numberPicker.minValue=0
            numberPicker.maxValue=5

            setTimeMaterialButton.setOnClickListener {
                minutes = numberPicker.value.toString()
                binding.clockTimerTextView.text = numberPicker.value.toString() +" minutes"
                dialog.dismiss()
            }
            dialog.show()
        }

        binding.runButton.setOnClickListener {
            if (isRunning){
                isRunning = false
                if (!minutes.equals("00.00.00")){
                    var totalMin = minutes!!.toInt()*60*1000L
                    var countDonw = 1000L
                    binding.chronometerTextView.base =SystemClock.elapsedRealtime()+totalMin
                    binding.chronometerTextView.format = "%S %S"
                    binding.chronometerTextView.setOnChronometerTickListener {
                        var lapSetTime = SystemClock.elapsedRealtime()- binding.chronometerTextView.base
                        if (lapSetTime >= totalMin){
                            binding.chronometerTextView.stop()
                            isRunning = false
                            binding.runButton.text = "Run"
                        }
                    }
                }
                else{
                    isRunning = true
                    binding.chronometerTextView.base = SystemClock.elapsedRealtime()
                    binding.runButton.text = "Run"
                }
            }
            else{
                isRunning = true
                binding.chronometerTextView.base = SystemClock.elapsedRealtime()
                binding.runButton.text = "Stop"
            }
            binding.chronometerTextView.start()
        }
    }
}