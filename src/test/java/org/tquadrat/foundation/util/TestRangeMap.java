/*
 * ============================================================================
 * Copyright © 2002-2014 by Thomas Thrien.
 * All Rights Reserved.
 * ============================================================================
 * Licensed to the public under the agreements of the GNU Lesser General Public
 * License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *      http://www.gnu.org/licenses/lgpl.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.tquadrat.foundation.util;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  This class performs the tests for the class
 *  {@link RangeMap}.
 *
 *  @author Thomas Thrien - thomas.thrien@tquadrat.org
 */
@ClassVersion( sourceVersion = "$Id: TestRangeMap.java 574 2019-04-16 18:33:24Z tquadrat $" )
public class TestRangeMap extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests a range map for integer values that is created from scratch.
     */
    @Test @SuppressWarnings( "static-method" )
    public final void testIntRangeMapFromScratch()
    {
        double [] inputs;
        int [] expecteds;
        int [] actuals;
        RangeMap<Integer> candidate;
        double [] ranges;
        int [] values;

        //---* Create the map with border excluded *---------------------------
        candidate = RangeMap.of( Integer.MAX_VALUE, false );

        //---* Check the empty map *-------------------------------------------
        assertFalse( candidate.isEmpty() );

        //---* Add some values *-----------------------------------------------
        ranges = new double [] {1.0, 3.0, 2.0, Math.E, Math.PI, -345.2234, 0.0, 2.1};
        values = new int []    {3,   7,   4,   6,      8,       1,         2,   5};
        for( var i = 0; i < ranges.length; ++i )
        {
            candidate.addRange( ranges [i], values[i] );
        }
        assertFalse( candidate.isEmpty() );

        //---* Test some Values *----------------------------------------------
        inputs = new double [] {Double.MIN_VALUE, -345.2234, -99.23, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5,               Double.MAX_VALUE };
        expecteds = new int [] {3,                2,         2,      3,   3,   4,   4,   5,   6,   8,   Integer.MAX_VALUE, Integer.MAX_VALUE };
        assertEquals( expecteds.length, inputs.length );
        actuals = new int [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );

        /*
         * A new default is set.
         */
        candidate.setDefault( 99 );
        expecteds = new int [] {3, 2, 2, 3, 3, 4, 4, 5, 6, 8, 99, 99};
        assertEquals( expecteds.length, inputs.length );
        actuals = new int [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );

        //---* Create the map with border included *---------------------------
        candidate = RangeMap.of( Integer.MAX_VALUE, true );

        //---* Check the empty map *-------------------------------------------
        assertFalse( candidate.isEmpty() );

        //---* Add some values *-----------------------------------------------
        ranges = new double [] {1.0, 3.0, 2.0, Math.E, Math.PI, -345.2234, 0.0, 2.1};
        values = new int []    {3,   7,   4,   6,      8,       1,         2,   5};
        for( var i = 0; i < ranges.length; ++i )
        {
            candidate.addRange( ranges [i], values[i] );
        }
        assertFalse( candidate.isEmpty() );

        //---* Test some Values *----------------------------------------------
        inputs = new double [] {Double.MIN_VALUE, -345.2234, -99.23, 0.0, 0.5, 1.0, 1.5, 2.0, 2.5, 3.0, 3.5,               Double.MAX_VALUE };
        expecteds = new int [] {3,                1,         2,      2,   3,   3,   4,   4,   6,   7,   Integer.MAX_VALUE, Integer.MAX_VALUE};
        assertEquals( expecteds.length, inputs.length );
        actuals = new int [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );

        /*
         * A new default is set.
         */
        candidate.setDefault( 99 );
        expecteds = new int [] {3, 1, 2, 2, 3, 3, 4, 4, 6, 7, 99, 99};
        assertEquals( expecteds.length, inputs.length );
        actuals = new int [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );
    }   //  testIntRangeMapFromScratch()

    /**
     *  Test the method
     *  {@link RangeMap#of(Class, boolean)}
     */
    @Test
    final void testOf()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            RangeMap.of( null, true );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException )
            {
                t.printStackTrace( out );
            }
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testOf()

    /**
     *  Tests a Range map for String values that is created from scratch.
     */
    @Test @SuppressWarnings( "static-method" )
    public final void testStringRangeMapFromScratch()
    {
        double [] inputs;
        String [] expecteds;
        String [] actuals;
        RangeMap<String> candidate;
        double [] ranges;
        String [] values;

        //---* Create the map with border excluded *---------------------------
        candidate = RangeMap.of( "infinity", false );

        //---* Check the empty map *-------------------------------------------
        assertFalse( candidate.isEmpty() );

        //---* Add some values *-----------------------------------------------
        ranges = new double [] {1.0,    3.0,      2.0,    Math.E,  Math.PI, -345.2234, 0.0,    2.1};
        values = new String [] {"drei", "sieben", "vier", "sechs", "acht",  "eins",    "zwei", "fünf"};
        for( var i = 0; i < ranges.length; ++i )
        {
            candidate.addRange( ranges [i], values[i] );
        }
        assertFalse( candidate.isEmpty() );

        //---* Test some Values *----------------------------------------------
        inputs = new double [] {   Double.MIN_VALUE, -345.2234, -99.23, 0.0,    0.5,    1.0,    1.5,    2.0,    2.5,     3.0,    3.5,        Double.MAX_VALUE };
        expecteds = new String [] {"drei",           "zwei",    "zwei", "drei", "drei", "vier", "vier", "fünf", "sechs", "acht", "infinity", "infinity"};
        assertEquals( expecteds.length, inputs.length );
        actuals = new String [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );

        /*
         * A new default is set.
         */
        candidate.setDefault( "ganz groß" );
        expecteds = new String [] {"drei", "zwei", "zwei", "drei", "drei", "vier", "vier", "fünf", "sechs", "acht", "ganz groß", "ganz groß"};
        assertEquals( expecteds.length, inputs.length );
        actuals = new String [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );

        //---* Create the map with border included *---------------------------
        candidate = RangeMap.of( "infinity", true );

        //---* Check the empty map *-------------------------------------------
        assertFalse( candidate.isEmpty() );

        //---* Add some values *-----------------------------------------------
        ranges = new double [] {1.0,    3.0,      2.0,    Math.E,  Math.PI, -345.2234, 0.0,    2.1};
        values = new String [] {"drei", "sieben", "vier", "sechs", "acht",  "eins",    "zwei", "fünf"};
        for( var i = 0; i < ranges.length; ++i )
        {
            candidate.addRange( ranges [i], values[i] );
        }
        assertFalse( candidate.isEmpty() );

        //---* Test some Values *----------------------------------------------
        inputs = new double [] {   Double.MIN_VALUE, -345.2234, -99.23, 0.0,    0.5,    1.0,    1.5,    2.0,    2.5,     3.0,      3.5,        Double.MAX_VALUE };
        expecteds = new String [] {"drei",           "eins",    "zwei", "zwei", "drei", "drei", "vier", "vier", "sechs", "sieben", "infinity", "infinity"};
        assertEquals( expecteds.length, inputs.length );
        actuals = new String [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );

        /*
         * A new default is set.
         */
        candidate.setDefault( "ganz groß" );
        expecteds = new String [] {"drei", "eins", "zwei", "zwei", "drei", "drei", "vier", "vier", "sechs", "sieben", "ganz groß", "ganz groß"};
        assertEquals( expecteds.length, inputs.length );
        actuals = new String [expecteds.length];
        for( var i = 0; i < inputs.length; ++i )
        {
            actuals [i] = candidate.get( inputs [i] );
        }
        assertArrayEquals( expecteds, actuals );
    }   //  testStringRangeMapFromScratch()
}
//  class TestRangeMap

/*
 *  End of File
 */