package bernardo.bernardinhio.kotlinclasstypesInbankingapp.logic

abstract class Account(
        val id : Long = System.currentTimeMillis() - "account".length,
        val dateCreated : String = Account.getFormattedTime(System.currentTimeMillis()),
        var type : AccountType = AccountType.UNDEFINED,
        var owner : Owner = Owner(),
        var savingsBalance : Double = 0.toDouble(),
        var yearlyInterestRate : Double = Account.yearlyInterestRate,
        var yearlyBenefit : Double = 0.toDouble(),
        var checkingBalance : Double = 0.toDouble(),
        var overdraftLimit : Double = OverdraftLimitType.LIMIT_1000.limit,
        var remainingOverdraft : Double = overdraftLimit
) {

    companion object {

        var yearlyInterestRate : Double = 3.32

        private fun getFormattedTime(timeStamp: Long): String {
            return timeStamp.toString() // TODO conversion later
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
    CHECKING("checking"),
    SAVINGS("savings"),
    CHECKING_AND_SAVINGS("checking_and_savings"),
    UNDEFINED("")
}

// enum class allowing choosing overdraft limit according to salary range
enum class OverdraftLimitType(val limit : Double, val salaryLess : Double){
    LIMIT_1000(1000.0, 1000.0),
    LIMIT_1600(1600.0, 2350.0),
    LIMIT_2000(2000.0, 3500.0),
    LIMIT_3000(3000.0, 4800.0),
    LIMIT_4500(4500.0, 6300.0),
    LIMIT_6000(6000.0, 8000.0),
    UNDEFINED(0.0, 0.0)
}