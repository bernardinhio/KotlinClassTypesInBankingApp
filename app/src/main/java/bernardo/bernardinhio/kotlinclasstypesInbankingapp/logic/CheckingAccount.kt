package bernardo.bernardinhio.kotlinclasstypesInbankingapp.logic

/**
 * By using a primary constructor in Kotlin with parameters
 * initialized by default values, then we don't need to
 * overload constructors so we can call all these:
 *
 * val checkingAccount = CheckingAccount()
 * val checkingAccount = CheckingAccount(123.5)
 * val checkingAccount = CheckingAccount(123.5, 4355.9)
 * val checkingAccount = CheckingAccount(123.5, 4355.9, 3339.6)
 * val checkingAccount = CheckingAccount(123.5, 4355.9, 3339.6, AccountType.UNDEFINED)
 * val checkingAccount = CheckingAccount(123.5, 4355.9, 3339.6, AccountType.UNDEFINED, SavingsAccount())
 */
class CheckingAccount(
        var checkingBalance: Double = 0.toDouble(),
        var overdraftLimit: Double = 0.toDouble(), // when openeing an account, it's 0.0
        var remainingOverdraft: Double = 0.toDouble(),
        override var type: AccountType = AccountType.CHECKING, // used for constructor when creating an account having 2 types
        var savingsAccount : SavingsAccount? = null
) : Account() {
    /**
     * we can create from the beginning an account that is
     * of type CHECKING_AND_SAVINGS, or just CHECKING
     * that later we can upgrade to CHECKING_AND_SAVINGS
     *
     * example:
     * val checkingAccount = CheckingAccount(AccountType.CHECKING_AND_SAVINGS)
     */
    constructor(type: AccountType) : this(){
        this.type = type
    }

    /**
     * All combination of parameters in the constructor
     * are allowed if we use the "named parameters"
     * technique from Kotlin. Such as:
     *
     * val checkingAccount = CheckingAccount(checkingBalance =  23415.7, type = AccountType.CHECKING)
     */

    override fun getBalance() : Double{
        return when(type){
            AccountType.CHECKING -> checkingBalance
            AccountType.CHECKING_AND_SAVINGS -> checkingBalance + savingsAccount?.savingsBalance!!
            else -> -1.toDouble()
        }
    }

    override fun addMoney(money : Double, toTypeIfSecondAccount : AccountType) : Boolean{
        var isAdded = false
        if (type.equals(AccountType.CHECKING)) {
            if (toTypeIfSecondAccount.equals(AccountType.CHECKING)) {
                checkingBalance += money
                isAdded = true
            }
            else if (toTypeIfSecondAccount.equals(AccountType.SAVINGS))
                println("You account is Checking can't add to Savings")
        }
        else if (type.equals(AccountType.CHECKING_AND_SAVINGS)){
            if (toTypeIfSecondAccount.equals(AccountType.CHECKING)) {
                checkingBalance += money
                isAdded = true
            }
            else if (toTypeIfSecondAccount.equals(AccountType.SAVINGS)) {
                savingsAccount?.savingsBalance = money + savingsAccount?.savingsBalance!!
                isAdded = true
            }
        }
        return isAdded
    }

    override fun withdrawMoney(money : Double, fromTypeIfSecondAccount : AccountType): Boolean {
        var canWithdraw = false
        if (type.equals(AccountType.CHECKING)){
            if (fromTypeIfSecondAccount.equals(AccountType.CHECKING)){
                canWithdraw = withdrawFromChecking(money)
            } else if (fromTypeIfSecondAccount.equals(AccountType.SAVINGS)){
                canWithdraw = false
                println("Your account is checking, you can't withdraw from savings")
            }
        } else if (type.equals(AccountType.CHECKING_AND_SAVINGS)){
            if (fromTypeIfSecondAccount.equals(AccountType.CHECKING))
                canWithdraw = withdrawFromChecking(money)
            else if (fromTypeIfSecondAccount.equals(AccountType.SAVINGS))
                canWithdraw = withdrawFromSavings(money)
        }
        return canWithdraw
    }

    private fun withdrawFromChecking(money: Double) : Boolean{
        var canWithdraw = false
        if (money <= checkingBalance){
            checkingBalance -= money
            canWithdraw = true
        } else if (money > checkingBalance && (money - checkingBalance) <= remainingOverdraft){
            remainingOverdraft = remainingOverdraft - (money - checkingBalance)
            checkingBalance = 0.toDouble()
            canWithdraw = true
        } else if(money > checkingBalance + remainingOverdraft){
            canWithdraw = false
            println("cannot withdraw money > checkingBalance + remainingOverdraft")
        }
        return canWithdraw
    }

    private fun withdrawFromSavings(money: Double): Boolean {
        var canWithdraw = false
        if (money <= savingsAccount?.savingsBalance!!){
            savingsAccount?.savingsBalance = savingsAccount?.savingsBalance!! - money
            canWithdraw = true
        } else if (money > savingsAccount?.savingsBalance!!){
            canWithdraw = false
            println("cannot withdraw money > savingsBalance")
        }
        return canWithdraw
    }

    override fun transferMoneyToSomeone(money : Double, fromTypeIfSecondAccount : AccountType, receiverAccount : Account): Boolean {
        var canTransfer = false
        if (type.equals(AccountType.CHECKING)){
            if (fromTypeIfSecondAccount.equals(AccountType.CHECKING)){
                canTransfer = transferFromCheckingToReceiverAccount(money, receiverAccount)
            } else if (fromTypeIfSecondAccount.equals(AccountType.SAVINGS))
                println("Your account is Checking, you can't transfer from Savings")
        } else if (type.equals(AccountType.CHECKING_AND_SAVINGS)){
            if (fromTypeIfSecondAccount.equals(AccountType.CHECKING))
                canTransfer = transferFromCheckingToReceiverAccount(money, receiverAccount)
            else if (fromTypeIfSecondAccount.equals(AccountType.SAVINGS))
                canTransfer = transferFromSavingsToReceiverAccount(money, receiverAccount)
        }
        return canTransfer
    }

    private fun transferFromCheckingToReceiverAccount(money: Double, receiverAccount: Account): Boolean {
        var canTransfer = false
        if (money <= checkingBalance){
            canTransfer = transferFromCheckingBalanceToReceiverAccount(money, receiverAccount)
        } else if (money > checkingBalance && (money - checkingBalance) <= remainingOverdraft){
            canTransfer = transferFromCheckingBalanceAndRemainingOverdraftToReceiverAccount(money, receiverAccount)
        } else if(money > checkingBalance + remainingOverdraft){
            canTransfer = false
            println("can't withdraw money > checkingBalance + remainingOverdraft")
        }
        return canTransfer
    }

    private fun transferFromCheckingBalanceToReceiverAccount(money: Double, receiverAccount: Account): Boolean {
        var canTransfer = false
        when (receiverAccount.type){
            AccountType.CHECKING -> {
                (receiverAccount as CheckingAccount).checkingBalance += money
                checkingBalance -= money
                canTransfer = true
            }
            AccountType.SAVINGS -> {
                (receiverAccount as SavingsAccount).savingsBalance += money
                checkingBalance -= money
                canTransfer = true
            }
            // receiving to CHECKING_AND_SAVINGS, receive on CHECKING
            AccountType.CHECKING_AND_SAVINGS -> {
                (receiverAccount as CheckingAccount).checkingBalance += money
                checkingBalance -= money
                canTransfer = true
            }
            AccountType.UNDEFINED -> {}
        }
        return canTransfer
    }

    private fun transferFromCheckingBalanceAndRemainingOverdraftToReceiverAccount(money: Double, receiverAccount: Account): Boolean {
        var canTransfer = false
        when (receiverAccount.type){
            AccountType.CHECKING -> {
                (receiverAccount as CheckingAccount).checkingBalance += money
                remainingOverdraft = remainingOverdraft - (money - checkingBalance)
                checkingBalance = 0.toDouble()
                canTransfer = true
            }
            AccountType.SAVINGS -> {
                (receiverAccount as SavingsAccount).savingsBalance += money
                remainingOverdraft = remainingOverdraft - (money - checkingBalance)
                checkingBalance = 0.toDouble()
                canTransfer = true
            }
            // receiving to CHECKING_AND_SAVINGS, receive on CHECKING
            AccountType.CHECKING_AND_SAVINGS -> {
                (receiverAccount as CheckingAccount).checkingBalance += money
                remainingOverdraft = remainingOverdraft - (money - checkingBalance)
                checkingBalance = 0.toDouble()
                canTransfer = true
            }
            AccountType.UNDEFINED -> {}
        }
        return canTransfer
    }

    private fun transferFromSavingsToReceiverAccount(money: Double, receiverAccount: Account): Boolean {
        var canTransfer = false
        if (money <= savingsAccount?.savingsBalance!!){
            when (receiverAccount.type){
                AccountType.CHECKING -> {
                    (receiverAccount as CheckingAccount).checkingBalance =+ money
                    savingsAccount?.savingsBalance = savingsAccount?.savingsBalance!! - money
                    canTransfer = true
                }
                AccountType.SAVINGS -> {
                    (receiverAccount as SavingsAccount).savingsBalance += money
                    savingsAccount?.savingsBalance = savingsAccount?.savingsBalance!! - money
                    canTransfer = true
                }
                // receiving to CHECKING_AND_SAVINGS, receive on CHECKING
                AccountType.CHECKING_AND_SAVINGS -> {
                    (receiverAccount as CheckingAccount).checkingBalance += money
                    savingsAccount?.savingsBalance = savingsAccount?.savingsBalance!! - money
                    canTransfer = true
                }
                AccountType.UNDEFINED -> {}
            }
        } else if (money > savingsAccount?.savingsBalance!!){
            canTransfer = false
            println("cannot transfer money > savingsBalance")
        }
        return canTransfer
    }

    override fun convertMoneyFromMyCheckingToMySavings(money : Double) : Boolean {
        var isConverted = false
        if (type.equals(AccountType.CHECKING_AND_SAVINGS)){
            if (money <= checkingBalance){
                isConverted = convertFromCheckingBalanceToSavingsBalance(money)
            } else println("can't convert money more than you have in checkingBalance")
        } else println("you don't even have a Savings Account")
        return isConverted
    }

    private fun convertFromCheckingBalanceToSavingsBalance(money: Double): Boolean {
        savingsAccount?.savingsBalance = money + savingsAccount?.savingsBalance!!
        checkingBalance -= money
        return true
    }

    override fun convertMoneyFromMySavingsToMyChecking(money : Double) : Boolean {
        return savingsAccount?.convertMoneyFromMySavingsToMyChecking(money)!!
    }

    fun getOverdraftStatus() : String {
        val overdraftStatus = "You still can withdraw $remainingOverdraft from your checking, your limit was $overdraftLimit"
        println(overdraftStatus)
        return overdraftStatus
    }
}
