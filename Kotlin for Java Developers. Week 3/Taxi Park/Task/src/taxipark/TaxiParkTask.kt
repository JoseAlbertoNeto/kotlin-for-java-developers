package taxipark

import java.lang.Integer.max
import kotlin.math.absoluteValue

/*
 * Task #1. Find all the drivers who performed no trips.
 */
fun TaxiPark.findFakeDrivers(): Set<Driver> = allDrivers.minus(trips.let{
    val drivers = mutableSetOf<Driver>()
    it.forEach { trip -> drivers.add(trip.driver)}
    drivers
})

/*
 * Task #2. Find all the clients who completed at least the given number of trips.
 */
fun TaxiPark.findFaithfulPassengers(minTrips: Int): Set<Passenger> = trips.let{
    if (minTrips == 0)
        allPassengers
    else{
        val result = mutableMapOf<Passenger, Int>()
        it.forEach{trip -> trip.passengers.forEach{passenger ->
            if(passenger !in result){
                result[passenger] = 0
            }
            result[passenger] = result[passenger]!!.plus(1)}
        }
        result.filter{passenger -> passenger.value >= minTrips}.keys
    }
}


/*
 * Task #3. Find all the passengers, who were taken by a given driver more than once.
 */
fun TaxiPark.findFrequentPassengers(driver: Driver): Set<Passenger> = trips.let{
    val passengers = mutableMapOf<Passenger, Int>()
    it.forEach {trip ->
        if(driver == trip.driver){
            trip.passengers.forEach{passenger -> passengers[passenger] = if(passenger !in passengers) 1 else passengers[passenger]!!.plus(1)}
        }
    }
    passengers.filter{passenger -> passenger.value > 1}.keys
}


/*
 * Task #4. Find the passengers who had a discount for majority of their trips.
 */
fun TaxiPark.findSmartPassengers(): Set<Passenger> =trips.let{
    val passengers = mutableMapOf<Passenger, Int>()
    it.forEach {trip ->
        trip.passengers.forEach{passenger ->
            if(passenger !in passengers)
                passengers[passenger] =  0
            if(trip.discount != null && trip.discount > 0){
                passengers[passenger] = passengers[passenger]!!.plus(1)
            } else{
                passengers[passenger] = passengers[passenger]!!.minus(1)
            }
        }
    }
    passengers.filter{passenger -> passenger.value > 0}.keys
}

/*
 * Task #5. Find the most frequent trip duration among minute periods 0..9, 10..19, 20..29, and so on.
 * Return any period if many are the most frequent, return `null` if there're no trips.
 */
fun TaxiPark.findTheMostFrequentTripDurationPeriod(): IntRange? {
    val durationMap = mutableMapOf<Int, Int>()
    var maxDuration = 0
    this.trips.forEach {
        val lowerNumberInRange = if(it.duration > 9) it.duration - it.duration%10 else 0
        durationMap[lowerNumberInRange] = durationMap[lowerNumberInRange]?.plus(1) ?: 1
        maxDuration = max(maxDuration, durationMap[lowerNumberInRange]!!)
    }
    val sortedDuration = durationMap.toList().filter { it.second >= maxDuration}
    return sortedDuration.firstOrNull().let { it?.first?.rangeTo(it.first+9)}
}

/*
 * Task #6.
 * Check whether 20% of the drivers contribute 80% of the income.
 */
fun TaxiPark.checkParetoPrinciple(): Boolean {
    //total income
    //ordered list by drivers who make more income
    val (drivers, totalCost) = let{
        var totalIncome = 0.0
        val driversWithCost = mutableMapOf<Driver, Double?>()
        it.trips.forEach{ trip->
            if(trip.driver !in driversWithCost)
                driversWithCost[trip.driver] = trip.cost
            else
                driversWithCost[trip.driver] = driversWithCost[trip.driver]?.plus(trip.cost)
            totalIncome += trip.cost
        }
        it.allDrivers.forEach{missingDriver -> driversWithCost.putIfAbsent(missingDriver, 0.0)}
        Pair(driversWithCost, totalIncome)
    }

    val drivers20percent = drivers.toList().sortedByDescending { (_, v) -> v }.subList(0, (drivers.size * 0.2).toInt())
    val totalIncome80 = 0.8 * totalCost

    return totalCost != 0.0 && drivers20percent.sumByDouble{it.second ?: 0.0} >= totalIncome80
}