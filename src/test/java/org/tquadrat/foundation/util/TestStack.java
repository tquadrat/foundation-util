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

package org.tquadrat.foundation.util;

import static java.lang.System.out;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.lang.CommonConstants.EMPTY_STRING;
import static org.tquadrat.foundation.util.ArrayUtils.revertCopy;
import static org.tquadrat.foundation.util.StringUtils.format;

import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;

/**
 *  Some tests for the class
 *  {@link Stack}.
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestStack.java 820 2020-12-29 20:34:22Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestStack.java 820 2020-12-29 20:34:22Z tquadrat $" )
public class TestStack extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests the constructor.
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testConstructor() throws Exception
    {
        skipThreadTest();

        final var candidate = new Stack<String>();
        assertNotNull( candidate );
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );
    }   //  testConstructor()

    /**
     *  Test method for the basic methods in the class
     *  {@link Stack}
     *
     *  @throws Exception   Something unexpected went wrong.
     */
    @Test
    final void testMethod() throws Exception
    {
        skipThreadTest();

        final var candidate = new Stack<String>();
        assertNotNull( candidate );
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );

        String value;
        String [] values;

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;
            value = null;
            try
            {
                candidate.push( value );
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
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );

        value = EMPTY_STRING;
        candidate.push( value );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.hasMore() );
        assertEquals( 1, candidate.size() );

        assertNotNull( candidate.peek() );
        assertTrue( candidate.peek().isPresent() );
        assertEquals( value, candidate.peek().get() );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.hasMore() );
        assertEquals( 1, candidate.size() );

        final var popResult = candidate.pop();
        assertNotNull( popResult );
        assertTrue( popResult.isPresent() );
        assertEquals( value, popResult.get() );
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );

        {
            final Class<? extends Throwable> expectedException = NullArgumentException.class;
            values = null;
            try
            {
                candidate.push( values );
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

        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );

        {
            final Class<? extends Throwable> expectedException = IllegalArgumentException.class;
            values = new String [] {"one", "two", "three", null, "five", "six", "seven"};
            try
            {
                candidate.push( values );
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
            assertTrue( candidate.isEmpty() );
            assertFalse( candidate.hasMore() );
            assertEquals( 0, candidate.size() );
        }

        values = new String [] {"one", "two", "three", "four", "five", "six", "seven"};
        candidate.push( values );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.hasMore() );
        assertEquals( values.length, candidate.size() );

        final var current = new String [values.length];
        var index = 0;
        while( candidate.hasMore() )
        {
            current [index++] = candidate.pop().orElseThrow();
        }
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );

        assertArrayEquals( current, revertCopy( values ) );

        candidate.push( values );
        assertFalse( candidate.isEmpty() );
        assertTrue( candidate.hasMore() );
        assertEquals( values.length, candidate.size() );
        candidate.clear();
        assertTrue( candidate.isEmpty() );
        assertFalse( candidate.hasMore() );
        assertEquals( 0, candidate.size() );
    }   //  testMethod()
}
//  class TestFrame

/*
 *  End of File
 */