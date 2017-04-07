using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class HardConstraints : Constraint
    {
        private bool[,] arr;

        public HardConstraints() : base(){}

        public HardConstraints(bool[,] p)
        {
            this.arr = p;
            this.constraint1();
            this.constraint2();
            this.constraint3();
            this.constraint4();
            this.constraint5();
            this.constraint6();
            this.constraint7();
            this.constraint8();
            this.constraint9();
            this.constraint10();
        }

        /* The number of consecutive shifts (workdays) is at most 6 */
        private void constraint10()
        {
            this.failed++;
        }

        /* The number of consecutive night shifts is at most 3 */
        private void constraint9()
        {
            this.failed++;
        }

        /* A night shift has to be followed by at least 14 hours rest. An exception is that once in a 
        period of 21 days for 24 consecutive hours, the resting time may be reduced to 8 hours */
        private void constraint8()
        {
            this.failed++;
        }

        /* During any period of 24 consecutive hours, at least 11 hours of rest is required. */
        private void constraint7()
        {
            this.failed++;
        }

        /* Following a series of at least 2 consecutive night shifts a 42 hours rest is required. */
        private void constraint6()
        {
            this.failed++;
        }

        /* A nurse must receive at least 2 weekends off duty per 5 week period. A weekend off duty 
        lasts 60 hours including Saturday 00:00 to Monday 04:00 */
        private void constraint5()
        {
            this.failed++;
        }

        /* The maximum number of night shifts is 3 per period of 5 consecutive weeks. */
        private void constraint4()
        {
            this.failed++;
        }

        /* Within a scheduling period a	nurse is allowed to exceed the number of hours for which	
        they are available for their department	by at most 4 hours.	*/
        private void constraint3()
        {
            this.failed++;
        }

        /* For each day a nurse may start only one shift. */	
        private void constraint2()
        {
            this.failed++;
        }

        /* Cover needs to be fulfilled (i.e. no shifts must be left	unassigned). */
        private void constraint1()
        {
            this.failed++;
        }

        public int Failed { get; set; }
    }
}
