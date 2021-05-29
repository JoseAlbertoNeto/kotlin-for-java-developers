package nicestring

fun String.isNice(): Boolean {
    var vowelsCount = 0
    var substring: CharSequence = ""
    var condition2Failed= false
    var condition3Success= false

    for(i in 0..this.length-1){
        substring = this.subSequence(i, if(i == this.length-1) i+1 else i+2)
        if(this[i] == 'a' || this[i] == 'e' || this[i] == 'i' || this[i] == 'o' || this[i] == 'u')
            vowelsCount++
        if(substring ==  "ba" || substring ==  "be" || substring ==  "bu")
            condition2Failed = true
        if(substring == "${substring[0]}${substring[0]}")
            condition3Success = true
    }

    val success = (if(vowelsCount > 2) 1 else 0) + (if(condition2Failed) 0 else 1) + (if(condition3Success) 1 else 0)

    return success >= 2
}