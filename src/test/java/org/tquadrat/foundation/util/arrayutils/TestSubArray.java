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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_String_ARRAY;
import static org.tquadrat.foundation.util.ArrayUtils.subArray;

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
 *  @version $Id: TestSubArray.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "StringOperationCanBeSimplified" )
@ClassVersion( sourceVersion = "$Id: TestSubArray.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.arrayutils.TestSubArray" )
public class TestSubArray extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Test method for
     *  {@link ArrayUtils#subArray(Object[], int, int)}
     *  and
     *  {@link ArrayUtils#subArray(Object[], int)}.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testSubArray() throws Exception
    {
        skipThreadTest();

        String [] actual;
        String [] expected;
        String [] source;
        int start, len;

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;
            source = null;
            start = 0;
            len = 0;
            try
            {
                actual = subArray( source, start );
                assertNotNull( actual );
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
                actual = subArray( source, start, len );
                assertNotNull( actual );
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
            final Class<? extends Throwable> expectedException = IndexOutOfBoundsException.class;
            source = EMPTY_String_ARRAY;
            start = 1;
            len = 0;
            try
            {
                actual = subArray( source, start );
                assertNotNull( actual );
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
                actual = subArray( source, start, len );
                assertNotNull( actual );
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

        source = EMPTY_String_ARRAY;
        expected = EMPTY_String_ARRAY;
        start = 0;
        len = 0;
        actual = subArray( source, start );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );
        actual = subArray( source, start, len );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );

        source = new String [] { "eins", "zwei", "drei", "vier" };
        expected = new String [] { "eins", "zwei", "drei", "vier" };
        start = 0;
        len = 4;
        actual = subArray( source, start );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );
        actual = subArray( source, start, len );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );

        source = new String [] { "eins", "zwei", "drei", "vier" };
        expected = EMPTY_String_ARRAY;
        start = 0;
        len = 0;
        actual = subArray( source, start, len );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );

        source = new String [] { "eins", "zwei", "drei", "vier" };
        expected = new String [] { "zwei", "drei", "vier" };
        start = 1;
        len = 4;
        actual = subArray( source, start );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );
        actual = subArray( source, start, len );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );

        source = new String [] { "eins", "zwei", "drei", "vier" };
        expected = new String [] { "zwei", "drei" };
        start = 1;
        len = 2;
        actual = subArray( source, start, len );
        assertNotNull( actual );
        assertArrayEquals( expected, actual );
    }   //  testSubArray()
}
//  class TestSubArray

/*
 *  End of File
 */