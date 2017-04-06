using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace GeneticAlgorithmSolutionToNSP
{
    class SoftConstraints
    {
        public SoftConstraints() : base(){}

        public SoftConstraints(Unit unit)
        {
            Constraint1(unit);
            Constraint2(unit);
            Constraint3(unit);
            Constraint4(unit);
            Constraint5(unit);
            Constraint6(unit);
        }

        private void Constraint6(Unit unit)
        {
            throw new NotImplementedException();
        }

        private void Constraint5(Unit unit)
        {
            throw new NotImplementedException();
        }

        private void Constraint4(Unit unit)
        {
            throw new NotImplementedException();
        }

        private void Constraint3(Unit unit)
        {
            throw new NotImplementedException();
        }

        private void Constraint2(Unit unit)
        {
            throw new NotImplementedException();
        }

        private void Constraint1(Unit unit)
        {
            throw new NotImplementedException();
        }

        public int Failed { get; set; }
    }
}
