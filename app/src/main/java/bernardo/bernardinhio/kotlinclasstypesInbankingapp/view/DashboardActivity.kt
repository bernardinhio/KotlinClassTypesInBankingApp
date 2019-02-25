package bernardo.bernardinhio.kotlinclasstypesInbankingapp.view

import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Point
import android.os.Bundle
import android.os.PersistableBundle
import android.support.constraint.ConstraintLayout
import android.support.v4.app.FragmentActivity
import android.view.View
import android.view.WindowManager
import bernardo.bernardinhio.kotlinclasstypesInbankingapp.R

open class DashboardActivity : FragmentActivity(){ // should be FragmentActivity to allow floating popop effect

    // comon to all dashboard activities
    protected fun setActivityDimensions(){
        val display = windowManager.defaultDisplay
        val point = Point()
        display.getSize(point)
        val screenWidth = point.x
        val screenHeight = point.y

        val distanceToDeduce = (screenWidth * 0.17).toInt()
        val activityContainerLayoutParams = findViewById<ConstraintLayout>(R.id.cl_container_activity_dashboard).layoutParams
        activityContainerLayoutParams.width = screenWidth - distanceToDeduce
        activityContainerLayoutParams.height = screenHeight - distanceToDeduce
    }

    // comon to all dashboard activities button close same id
    fun closeActivityBank(view : View){
        confirmLeavingActivity()
    }

    // comon to all dashboard activities
    private fun confirmLeavingActivity() : Boolean{
        var shouldAllowBack = false
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Sure you want to stop?")
        builder.setMessage("If you stop it, all the information recorded will be lost")
        builder.setPositiveButton("YES") { dialog, which ->
            this.finish()
            animateAffectedClient()
            shouldAllowBack = true
        }
        builder.setNeutralButton("CANCEL") {
            dialog, which ->
            builder.setCancelable(true)
        }
        builder.show()
        return shouldAllowBack
    }

    protected fun chooseBetweenGlobalClients(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick a Client")
                .setItems(R.array.three_clients,
                        DialogInterface.OnClickListener { dialog, which ->
                            // The 'which' argument contains the index position
                            // of the selected item
                        })
        builder.create()
    }

    protected fun animateAffectedClient(){
        // to develop later maybe shaking container one of the 4
        // in MainActivity
    }

    // comon to all dashboard activities
    override fun onBackPressed() {
        confirmLeavingActivity()
    }



}