using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GeneticAlgorithmSolutionToNSP
{
    class SoftConstraints : Constraint
    {
        private bool[,] arr;

        public SoftConstraints() : base(){}

        public SoftConstraints(Unit unit)
        {
            Constraint1();
            Constraint3();
            Constraint6();
            Constraint8();
            Constraint11();
            Constraint12();
        }

        public SoftConstraints(bool[,] p)
        {
            this.arr = p;
        }

        /* 
        An early shift after a day shift should be avoided.
         */
        private void Constraint12()
        {
            this.failed++;
        }

        /* 
        For all employees the length of a series of  late    
        shifts should be within the range of  2-3. It  could   be  
        within  another series.
         */
        private void Constraint11()
        {
            this.failed++;
        }

        /* 
        For employees   with    availability    of  30-48   hours   per 
        week,   the length  of  a   series  of  shifts  should  be  within  
        the range   of  4-6.
         */
        private void Constraint8()
        {
            this.failed++;
        }

        /* 
        For employees   with    availability    of  30-48   hours   per 
        week,   within  one week    the number  of  shifts  is  within  
        the range   4- 5.
         */
        private void Constraint6()
        {
            this.failed++;
        }

        /* 
        For employees   with    availability    of  30-48   hours   per 
        week,   the length  of  a   series  of  night   shifts  should  be  
        within  the range   2-3.    It  could   be  before  another series.
         */
        private void Constraint3()
        {
            this.failed++;
        }

        /* 
        For the period  of  Friday  22:00   to  Monday  0:00    a   
        nurse   should  have    either  no  shifts  or  at  least   2   shifts  
        (‘Complete  Weekend’).
         */
        private void Constraint1()
        {
            this.failed++;
        }

        public int Failed { get; set; }
    }
}
