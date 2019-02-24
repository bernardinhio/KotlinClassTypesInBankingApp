package bernardo.bernardinhio.kotlinclasstypesInbankingapp.logic

abstract class Account(
        val id : Long = System.currentTimeMillis() - "account".length,
        val dateCreated : String = getFormattedTime(System.currentTimeMillis()),
        var type : AccountType = AccountType.UNDEFINED,
        var owner : Owner = Owner(),
        var totalBalance : Double = 0.toDouble(),
        var savingsBalance : Double = 0.toDouble(),
        var yearlyInterestRate : Double = 3.32,
        var checkingBalance : Double = totalBalance,
        var overdraftLimit : Double = 1000.0
) {

    companion object {

        private fun getFormattedTime(timeStamp: Long): String {
            return ""
        }
    }

    // to force 2 subclasses to have it
    // SavingsAccount returns savingBalance
    // CheckingAccount returns checkingBalance
    // getTotalBalance() is auto-generated by Kotlin
    abstract fun getBalance() : Double

    // SavingsAccount adds to savingBalance
    // CheckingAccount adds to checkingBalance
    // totalBalance is updated in subclass
    abstract fun addMoney(amount : Double)

    // SavingsAccount withdraw from savingBalance
    // CheckingAccount withdraw from checkingBalance
    // totalBalance is updated in subclass
    abstract fun withdrawMoney(amount : Double)

}


enum class AccountType(val type : String){
    SAVINGS("savings"),
    CHECKING("checking"),
    SAVINGS_AND_CHECKING("savings_and_checking"),
    UNDEFINED("")
}