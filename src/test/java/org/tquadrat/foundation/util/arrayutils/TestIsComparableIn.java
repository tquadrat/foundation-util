/*
 * ============================================================================
 * Copyright © 2002-2020 by Thomas Thrien.
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

package org.tquadrat.foundation.util.arrayutils;

import static java.lang.String.format;
import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.ArrayUtils.isComparableIn;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.ArrayUtils;

/**
 *  This class tests the methods in
 *  {@link ArrayUtils}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestIsComparableIn.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "StringOperationCanBeSimplified" )
@ClassVersion( sourceVersion = "$Id: TestIsComparableIn.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.arrayutils.TestIsComparableIn" )
public class TestIsComparableIn extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link ArrayUtils#isComparableIn(Comparable, Object...)}
     */
    @Test
    final void testIsComparableIn()
    {
        skipThreadTest();

        final var s1 = "eins";
        final var s2 = "zwei";
        final var s3 = "drei";
        final var s4 = "vier";
        final String [] a = { s1, s2, s3, s4 };
        final var s1n = new String( s1 );
        final var s2n = new String( s2 );
        final var s3n = new String( s3 );
        final var s4n = new String( s4 );
        final String [] an = { s1n, s2n, s3n, s4n };

        //---* Test *----------------------------------------------------------
        assertTrue( isComparableIn( s1, s1, s2, s3, s4 ) );
        assertTrue( isComparableIn( s1, s4, s3, s2, s1 ) );
        assertTrue( isComparableIn( s1, a ) );
        assertFalse( isComparableIn( "not in", s1, s2, s3, s4 ) );
        assertFalse( isComparableIn( "not in", s4, s3, s2, s1 ) );
        assertFalse( isComparableIn( "not in", a ) );
        assertTrue( isComparableIn( s1, s1n, s2n, s3n, s4n ) );
        assertTrue( isComparableIn( s1, s4n, s3n, s2n, s1n ) );
        assertTrue( isComparableIn( s1, an ) );
        assertFalse( isComparableIn( "not in", s1n, s2n, s3n, s4n ) );
        assertFalse( isComparableIn( "not in", s4n, s3n, s2n, s1n ) );
        assertFalse( isComparableIn( "not in", an ) );

        //---* Test failures *-------------------------------------------------
        String [] array;
        String value;

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;
            value = null;
            array = new String [] { "eins", "zwei", "drei" };
            try
            {
                isComparableIn( value, array );
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

            value = "value";
            array = null;
            try
            {
                isComparableIn( value, array );
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
        }

        {
            final Class<? extends Throwable> expectedException = NullPointerException.class;
            value = "value";
            array = new String [] { "eins", null, "drei" };
            try
            {
                isComparableIn( value, array );
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

            value = "value";
            try
            {
                isComparableIn( value, null, "eins", "zwei" );
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
        }
    }   //  testIsComparableIn()
}
//  class TestIsComparableIn

/*
 *  End of File
 */