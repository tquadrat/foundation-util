/*
 * ============================================================================
 *  Copyright © 2002-2020 by Thomas Thrien.
 *  All Rights Reserved.
 * ============================================================================
 *  Licensed to the public under the agreements of the GNU Lesser General Public
 *  License, version 3.0 (the "License"). You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/lgpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 *  WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 *  License for the specific language governing permissions and limitations
 *  under the License.
 */

package org.tquadrat.foundation.util.arrayutils;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.ArrayUtils.isEqualIn;
import static org.tquadrat.foundation.util.StringUtils.format;

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
 *  @version $Id: TestIsEqualIn.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "StringOperationCanBeSimplified" )
@ClassVersion( sourceVersion = "$Id: TestIsEqualIn.java 820 2020-12-29 20:34:22Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.arrayutils.TestIsEqualIn" )
public class TestIsEqualIn extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link ArrayUtils#isEqualIn(Object, Object...)}
     */
    @Test
    final void testIsEqualIn()
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
        assertTrue( isEqualIn( s1, s1, s2, s3, s4 ) );
        assertTrue( isEqualIn( s1, s4, s3, s2, s1 ) );
        assertTrue( isEqualIn( s1, a ) );
        assertFalse( isEqualIn( "not in", s1, s2, s3, s4 ) );
        assertFalse( isEqualIn( "not in", s4, s3, s2, s1 ) );
        assertFalse( isEqualIn( "not in", a ) );
        assertTrue( isEqualIn( s1, s1n, s2n, s3n, s4n ) );
        assertTrue( isEqualIn( s1, s4n, s3n, s2n, s1n ) );
        assertTrue( isEqualIn( s1, an ) );
        assertFalse( isEqualIn( "not in", s1n, s2n, s3n, s4n ) );
        assertFalse( isEqualIn( "not in", s4n, s3n, s2n, s1n ) );
        assertFalse( isEqualIn( "not in", an ) );

        //---* Test failures *-------------------------------------------------
        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            isEqualIn( null, a );
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

        try
        {
            isEqualIn( s1, (String []) null );
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

        assertFalse( isEqualIn( s1, null, null ) );

        try
        {
            isEqualIn( null, (String []) null );
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

        try
        {
            isEqualIn( null, null, null );
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
    }   //  testIsEqualIn()
}
//  class TestIsEqualIn

/*
 *  End of File
 */