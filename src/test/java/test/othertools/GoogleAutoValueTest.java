package test.othertools;

import junit.framework.TestCase;
import org.jdraft.*;
import org.jdraft.macro.*;

/**
 * Emulate what is going on in AutoValue with jdraft
 * https://github.com/google/auto/blob/master/value/userguide/index.md#in-your-value-class
 */
public class GoogleAutoValueTest extends TestCase {

    /**
     * Here, instead of creating a public Animal class and then synthesizing
     * the AutoValue_Animal
     *
     * we just apply the @_dto macro annotation
     * we need to use the @_final annotation to specify name and numberOfLegs are intended on being final
     * @dto will create a constructor and add all final fields
     */
    public void testAutoValue(){
        @_dto class Animal{
            private @_final String name;
            private @_final int numberOfLegs;
        }
        _class _c = _class.of(Animal.class);

        System.out.println(_c);
    }


    /* Heres what the output will be:
     *
     * (this is the comparative code
     * https://github.com/google/auto/blob/master/value/userguide/generated-example.md
     */

    public class Animal {

        private final String name;

        private final int numberOfLegs;

        public String getName() {
            return name;
        }

        public int getNumberOfLegs() {
            return numberOfLegs;
        }

        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (this == o) {
                return true;
            }
            if (getClass() != o.getClass()) {
                return false;
            }
            Animal test = (Animal) o;
            boolean eq = true;
            eq = eq && java.util.Objects.equals(this.name, test.name);
            eq = eq && this.numberOfLegs == test.numberOfLegs;
            return eq;
        }

        public int hashCode() {
            int hash = 239;
            int prime = 109;
            hash = hash * prime + java.util.Objects.hashCode(name);
            hash = hash * prime + numberOfLegs;
            return hash;
        }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Animal").append("{");
            sb.append(System.lineSeparator());
            sb.append(" name: ").append(name).append(System.lineSeparator());
            sb.append(" numberOfLegs: ").append(numberOfLegs).append(System.lineSeparator());
            sb.append("}");
            return sb.toString();
        }

        public Animal(String name, int numberOfLegs) {
            this.name = name;
            this.numberOfLegs = numberOfLegs;
        }
    }

}

