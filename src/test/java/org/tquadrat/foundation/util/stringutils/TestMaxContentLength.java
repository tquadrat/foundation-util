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

package org.tquadrat.foundation.util.stringutils;

import static java.lang.System.out;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.format;
import static org.tquadrat.foundation.util.StringUtils.maxContentLength;

import java.util.Collection;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the methods
 *  {@link StringUtils#maxContentLength(CharSequence...)},
 *  {@link StringUtils#maxContentLength(Collection)},
 *  {@link StringUtils#maxContentLength(Iterable)}
 *  and
 *  {@link StringUtils#maxContentLength(Stream)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestMaxContentLength.java 883 2021-03-02 18:39:20Z tquadrat $
 */
@SuppressWarnings( {"MisorderedAssertEqualsArguments", "removal"} )
@ClassVersion( sourceVersion = "$Id: TestMaxContentLength.java 883 2021-03-02 18:39:20Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestMaxContentLength" )
public class TestMaxContentLength extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#maxContentLength(CharSequence[])}
     *  {@link StringUtils#maxContentLength(Collection)},
     *  and
     *  {@link StringUtils#maxContentLength(Stream)}.
     */
    @Test
    final void testMaxContentLength()
    {
        skipThreadTest();

        final var EMPTY_CharSequence_ARRAY = new CharSequence [0];

        var expected = Integer.MIN_VALUE;
        assertEquals( expected, maxContentLength( EMPTY_CharSequence_ARRAY ) );
        assertEquals( expected, maxContentLength( asList( EMPTY_CharSequence_ARRAY ) ) );
        assertEquals( expected, maxContentLength( Stream.of( EMPTY_CharSequence_ARRAY ) ) );

        var candidate = new String [] {null, null, null};
        expected = -1;
        assertEquals( expected, maxContentLength( candidate ) );
        assertEquals( expected, maxContentLength( asList( candidate ) ) );
        assertEquals( expected, maxContentLength( Stream.of( candidate ) ) );

        candidate = new String [] {"eins", "zwei", "drei"};
        expected = 4;
        assertEquals( expected, maxContentLength( candidate ) );
        assertEquals( expected, maxContentLength( asList( candidate ) ) );
        assertEquals( expected, maxContentLength( Stream.of( candidate ) ) );

        candidate = new String [] {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
        expected = 4;
        assertEquals( expected, maxContentLength( candidate ) );
        assertEquals( expected, maxContentLength( asList( candidate ) ) );
        assertEquals( expected, maxContentLength( Stream.of( candidate ) ) );
    }   //  testMaxContentLengthNull()

    /**
     *  Tests for
     *  {@link StringUtils#maxContentLength(CharSequence[])},
     *  {@link StringUtils#maxContentLength(Collection)},
     *  and
     *  {@link StringUtils#maxContentLength(Stream)}.
     */
    @Test
    final void testMaxContentLengthWithNullArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            maxContentLength( (Stream<CharSequence>) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            maxContentLength( (Collection<CharSequence>) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
        try
        {
            maxContentLength( (CharSequence []) null );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testMaxContentLengthWithNullArgument()
}
//  class TestMaxContentLength

/*
 *  End of File
 */