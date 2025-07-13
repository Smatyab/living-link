package com.nothing.secad.simple

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast
import com.nothing.secad.R
import com.nothing.secad.databinding.ActivityBuildingFormBinding
import com.skydoves.powerspinner.PowerSpinnerView

class BuildingForm : AppCompatActivity() {
    private lateinit var binding: ActivityBuildingFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuildingFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(intent.getStringExtra("check").toString() == "abc"){
            binding.hideElevator.visibility = View.GONE
            binding.hideWaterTank.visibility = View.GONE
        }

        setupPowerSpinner(binding.noOfParkingBuilding)
        setupPowerSpinner(binding.noOfWatchmanBuilding)
        setupPowerSpinner(binding.noOfGardenBuilding)
        setupPowerSpinner(binding.noOfTempleBuilding)
        setupPowerSpinner(binding.noOfWaterTankBuilding)
        setupPowerSpinner(binding.noOfElevatorBuilding)

        binding.btnBuildingSignUp.setOnClickListener {
            val selectedParking = getSelectedItem(binding.noOfParkingBuilding)
            val selectedElevator = getSelectedItem(binding.noOfElevatorBuilding)
            val selectedWatchman = getSelectedItem(binding.noOfWatchmanBuilding)
            val selectedGarden = getSelectedItem(binding.noOfGardenBuilding)
            val selectedTemple = getSelectedItem(binding.noOfTempleBuilding)
            val selectedWaterTank = getSelectedItem(binding.noOfWaterTankBuilding)
            val selectedTotalHome = binding.noOfTotalHomeBuildingEdt.text.toString()


            if(intent.getStringExtra("check").toString() == "abc"){

                if (selectedParking  == "--select no of--" || selectedWatchman  == "--select no of--" || selectedGarden  == "--select no of--"
                    || selectedTemple  == "--select no of--" || selectedTotalHome  == "--select no of--") {
                    showToast("Please fill all field")
                    // You can also prevent further actions here or show an error message
                } else {
                    // Valid item selected, you can proceed with your logic here
                    val intent = Intent(this, SignupActivity::class.java).apply {
                        putExtra("selectedParking", selectedParking)
                        putExtra("selectedWatchman", selectedWatchman)
                        putExtra("selectedGarden", selectedGarden)
                        putExtra("selectedTemple", selectedTemple)
                        putExtra("selectedTotalHome", selectedTotalHome)
                        putExtra("selectedElevator", "0")
                        putExtra("selectedWaterTank", "0")
                        val totalAmount2:Int=calculator(selectedParking,selectedWatchman,selectedGarden,selectedTemple,"0","0",selectedTotalHome)
                        val totalAmount:String = totalAmount2.toString()
                        putExtra("totalAmount",totalAmount)

                    }
                    startActivity(intent)
                }
            }else{
                if (selectedParking  == "--select no of--" || selectedElevator  == "--select no of--" || selectedWatchman  == "--select no of--"
                    || selectedGarden  == "--select no of--" || selectedTemple  == "--select no of--" || selectedWaterTank  == "--select no of--"
                    || selectedTotalHome  == "") {
                    showToast("Please fill all field")
                    // You can also prevent further actions here or show an error message
                } else {
                    // Valid item selected, you can proceed with your logic here
                    val intent = Intent(this, SignupActivity::class.java).apply {
                        putExtra("selectedParking", selectedParking)
                        putExtra("selectedElevator", selectedElevator)
                        putExtra("selectedWatchman", selectedWatchman)
                        putExtra("selectedGarden", selectedGarden)
                        putExtra("selectedTemple", selectedTemple)
                        putExtra("selectedWaterTank", selectedWaterTank)
                        putExtra("selectedTotalHome", selectedTotalHome)
                        val totalAmount2:Int=calculator(selectedParking,selectedWatchman,selectedGarden,selectedTemple,selectedWaterTank,selectedElevator,selectedTotalHome)
                        val totalAmount:String = totalAmount2.toString()
                        putExtra("totalAmount",totalAmount)
                    }

                    startActivity(intent)
                }
            }
        }
    }

    private fun setupPowerSpinner(spinner: PowerSpinnerView) {
        val itemsArray = resources.getStringArray(R.array.spinner_form)
        val items = itemsArray.toList()

        spinner.setItems(items)
        spinner.selectItemByIndex(0)

        spinner.setOnSpinnerItemSelectedListener<String> { _, _, _, item ->
//            showToast("Selected item: $item")
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this@BuildingForm, message, Toast.LENGTH_LONG).show()
    }

    private fun getSelectedItem(spinner: PowerSpinnerView): String {
        val selectedIndex = spinner.selectedIndex
        val itemsArray = resources.getStringArray(R.array.spinner_form)
        return itemsArray.getOrNull(selectedIndex) ?: ""
    }

    private fun calculator(parking:String,whtchman:String,garden:String,temple:String,waterTank:String,elevator:String,totalHome:String):Int{

        var parkingInt = parking.toInt()
        var whtchmanInt = whtchman.toInt()
        var gardenInt = garden.toInt()
        var templeInt = temple.toInt()
        var waterTankInt = waterTank.toInt()
        var elevatorInt = elevator.toInt()
        var totalHomeInt = totalHome.toInt()


        var parking_rs:Int = parkingInt * 2000;
        var whtchman_rs:Int = whtchmanInt * 10000;
        var elevator_rs:Int = elevatorInt * 5000;
        var garden_rs:Int = gardenInt * 3000;
        var temple_rs:Int = templeInt * 1000;
        var waterTank_rs:Int = waterTankInt * 1000;
        var total_rs:Int = parking_rs + whtchman_rs + elevator_rs + garden_rs + temple_rs + waterTank_rs;
        Log.e("$$$$$$$$$",""+total_rs)
        return total_rs;
    }

}
