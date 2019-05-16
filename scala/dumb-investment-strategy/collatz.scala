

def collatz(n: Long) : Long =  if(n <=1) 0 else if(n%2 == 0) (1 + collatz(n/2) ) else (1 + collatz(3*n+1))

def collatz_max(bnd: Long) : (Long, Long) = {
    ((bnd-bnd+1 to bnd).toList.map(n => collatz(n)).max, 
    (bnd-bnd+1 to bnd).toList.map(n => collatz(n)).indexOf(
    (bnd-bnd+1 to bnd).toList.map(n => collatz(n)).max)+1)
}



