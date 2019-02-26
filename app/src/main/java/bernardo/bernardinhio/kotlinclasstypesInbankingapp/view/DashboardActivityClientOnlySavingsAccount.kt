package bernardo.bernardinhio.kotlinclasstypesInbankingapp.view

import android.os.Bundle
import android.view.View
import android.view.WindowManager

import bernardo.bernardinhio.kotlinclasstypesInbankingapp.R
import bernardo.bernardinhio.kotlinclasstypesInbankingapp.data.SystemData
import bernardo.bernardinhio.kotlinclasstypesInbankingapp.logic.Account

class DashboardActivityClientOnlySavingsAccount : DashboardActivity() {

    private val accountOnlySavings : Account = SystemData.accountOnlySavings!!
    private val accountBothCheckingAndSavings : Account? = SystemData.accountBothCheckingAndSavings
    private val yearlyInterestRate : Double = accountOnlySavings.yearlyInterestRate

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_client_only_savings_account)
        setActivityDimensions()
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
    }

    // abstract
    fun getBalance(view : View){}

    // abstract // add to checking balance
    fun addMoney(view : View){}

    // superclass
    // according to type if only Checking then
    // withdraw from checking, if type is CheckingAndSavings
    // then offer possibility if not exist to withdraw from Savings
    fun withdrawMoney(view : View){}

    // both yearly or monthly (radio buttons)
    fun getInterestPerYear(view : View){}
    fun getInterestPerMonth(view : View){}

    // to only to other account Savings type
    fun transferToSomeone(view : View){}

}