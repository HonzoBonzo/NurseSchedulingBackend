using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace GeneticAlgorithmSolutionToNSP
{
    class HardConstraints : Constraint
    {
        public HardConstraints() : base(){}

        public HardConstraints(Unit unit)
        {
            Constraint1(unit);
            Constraint2(unit);
            Constraint3(unit);
            Constraint4(unit);
            Constraint5(unit);
            Constraint6(unit);
            Constraint7(unit);
            Constraint8(unit);
            Constraint9(unit);
            Constraint10(unit);
        }

        /* The number of consecutive shifts (workdays) is at most 6 */
        private void Constraint10(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* The number of consecutive night shifts is at most 3 */
        private void Constraint9(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* A night shift has to be followed by at least 14 hours rest. An exception is that once in a 
        period of 21 days for 24 consecutive hours, the resting time may be reduced to 8 hours */
        private void Constraint8(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* During any period of 24 consecutive hours, at least 11 hours of rest is required. */
        private void Constraint7(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* Following a series of at least 2 consecutive night shifts a 42 hours rest is required. */
        private void Constraint6(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* A nurse must receive at least 2 weekends off duty per 5 week period. A weekend off duty 
        lasts 60 hours including Saturday 00:00 to Monday 04:00 */
        private void Constraint5(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* The maximum number of night shifts is 3 per period of 5 consecutive weeks. */
        private void Constraint4(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* Within a scheduling period a	nurse is allowed to exceed the number of hours for which	
        they are available for their department	by at most 4 hours.	*/
        private void Constraint3(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* For each day a nurse may start only one shift. */	
        private void Constraint2(Unit unit)
        {
            throw new NotImplementedException();
        }

        /* Cover needs to be fulfilled (i.e. no shifts must be left	unassigned). */
        private void Constraint1(Unit unit)
        {
            throw new NotImplementedException();
        }

        public int Failed { get; set; }
    }
}
