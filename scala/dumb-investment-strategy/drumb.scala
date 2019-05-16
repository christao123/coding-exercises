

val blchip_portfolio = List("GOOG", "AAPL", "MSFT", "IBM", "FB", "AMZN", "BIDU")
val rstate_portfolio = List("PLD", "PSA", "AMT", "AIV", "AVB", "BXP", "CCI", 
                            "DLR", "EQIX", "EQR", "ESS", "EXR", "FRT", "HCP") 

import io.Source
import scala.util._


def return_line(line: String, month : String, year: String) : Boolean = {
   if(line.split("-").length > 1){
        if(line.split("-")(1) == month && line.split("-")(0) == year)
            true
        else
            false
    }
    else false
}

def get_january_data(symbol: String, year: Int) : List[String] = {

   Try(
    Some(
            Source.fromFile(symbol ++ ".csv")
                .getLines.toList
                    .filter(
                        return_line(_ ,"01", year.toString)
                    )
                
            )
        )
        .getOrElse(None)
        .toList
        .flatten

}
def get_first_price(symbol: String, year: Int) : Option[Double] = {
    Try(Some(get_january_data(symbol, year).head.split(",")(1).toDouble)).getOrElse(None)
}


def get_prices(portfolio: List[String], years: Range) : List[List[Option[Double]]] = {
    
        for( year <- years.toList) yield{
                for(stock <- portfolio)  yield{
                    get_first_price(stock , year)
                    }
        }
}


def get_delta(price_old: Option[Double], price_new: Option[Double]) : Option[Double] = (price_old, price_new) match  {
    case  (Some(po), Some(pn)) => Some((pn - po) / po)
    case _ => None
}


def get_deltas(data: List[List[Option[Double]]]) :  List[List[Option[Double]]] = {
val data_no_head = data.tail
    data_no_head.map( year => {
        year.map( company_share_price => {
                val index_previous_year =  data.indexOf(year) - 1
                val index_company = year.indexOf(company_share_price)
                val price_old = data(index_previous_year)(index_company)
               
                get_delta( price_old, company_share_price)
            })
        })
    }

def get_sum_of_year(year: List[Double]) : Long =
    year match{
        case x :: tail => (x.toLong+ get_sum_of_year(tail))
        case Nil => 0
    }

def yearly_yield(data: List[List[Option[Double]]], balance: Long, index: Int) : Long = {
    val year = data(index).flatten
    val balance_per_company = balance/year.length
    val evaluated_year =  year.map( company_factor => {
        company_factor * balance_per_company
    })
    get_sum_of_year(evaluated_year) + balance
}
 
//yearly_yield( get_deltas( get_prices (List("GOOG", "AAPL"), (2010 to 2012))), 100, 0)
// should be 125
//yearly_yield( get_deltas( get_prices (rstate_portfolio, ( 1978 to 2018))), 100, 0)
//CW6b.yearly_yield( CW6b.get_deltas( CW6b.get_prices (rstate_portfolio, ( 1978 to 2018))), 100, 0)
//96
//yearly_yield( get_deltas( get_prices (blchip_portfolio, ( 1978 to 2018))), 100, 0)
//CW6b.yearly_yield( CW6b.get_deltas( CW6b.get_prices (blchip_portfolio, ( 1978 to 2018))), 100, 0)
//117


def compound_yield(data: List[List[Option[Double]]], balance: Long, index: Int) : Long = {
   if(index >= data.length){
        balance
   }
   else{
        val new_balance = yearly_yield(data, balance, index)
        compound_yield(data, new_balance, index+1)
   }
}
    
def investment(portfolio: List[String], years: Range, start_balance: Long) : Long = {
    compound_yield(get_deltas(get_prices(portfolio, years)), start_balance, 0)
}




//Test cases for the two portfolios given above

//println("Real data: " + investment(rstate_portfolio, 1978 to 2018, 100))
//101589
//println("Blue data: " + investment(blchip_portfolio, 1978 to 2018, 100))
//1587528
//scala -cp drumb.jar

//println("Real data: " + CW6b.investment(rstate_portfolio, 1978 to 2018, 100))
//println("Real data: " + CW6b.investment(blchip_portfolio, 1978 to 2018, 100))

//get_deltas(get_prices(rstate_portfolio, 1978 to 2018)) //40