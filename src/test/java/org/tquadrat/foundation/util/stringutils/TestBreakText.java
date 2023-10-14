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

import static java.lang.String.format;
import static java.lang.System.out;
import static java.util.stream.Collectors.joining;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.tquadrat.foundation.util.StringUtils.breakText;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.tquadrat.foundation.annotation.ClassVersion;
import org.tquadrat.foundation.exception.NullArgumentException;
import org.tquadrat.foundation.exception.ValidationException;
import org.tquadrat.foundation.testutil.TestBaseClass;
import org.tquadrat.foundation.util.StringUtils;

/**
 *  Some tests for the method
 *  {@link StringUtils#breakText(CharSequence, int)}
 *  from class
 *  {@link StringUtils}
 *
 *  @extauthor Thomas Thrien - thomas.thrien@tquadrat.org
 *  @version $Id: TestBreakText.java 1076 2023-10-03 18:36:07Z tquadrat $
 */
@SuppressWarnings( "MisorderedAssertEqualsArguments" )
@ClassVersion( sourceVersion = "$Id: TestBreakText.java 1076 2023-10-03 18:36:07Z tquadrat $" )
@DisplayName( "org.tquadrat.foundation.util.stringutils.TestBreakText" )
public class TestBreakText extends TestBaseClass
{
        /*---------*\
    ====** Methods **==========================================================
        \*---------*/
    /**
     *  Tests for
     *  {@link StringUtils#breakText(CharSequence,int)}.
     */
    @Test
    final void testBreakText()
    {
        skipThreadTest();

        final var text = """
            This is a text that we use for this test. It has a certain length and, of course, it is multi-line.
            Don't be surprised, just for the test, it contains also   \
            some  German  words, like                                 \
            'Donaudampfschifffahrtskapitänsmützenbandknöpfe' or       \
            'Obersteigerlampenputzlappen'. Unfortunately, the English \
            language does not provide such long words, although the   \
            Welsh have names for their villages that are comparably   \
            long.                                                     \
            """;

        final var lineLength = 20;
        final var actual = breakText( text, lineLength ).collect( joining( "\n" ) );
        assertNotNull( actual );
        final var expected = """
            This is a text that
            we use for this
            test. It has a
            certain length and,
            of course, it is
            multi-line.
            Don't be surprised,
            just for the test,
            it contains also
            some German words,
            like
            'Donaudampfschifffahrtskapitänsmützenbandknöpfe'
            or
            'Obersteigerlampenputzlappen'.
            Unfortunately, the
            English language
            does not provide
            such long words,
            although the Welsh
            have names for
            their villages that
            are comparably
            long.""";
        assertEquals( expected, actual );
        assertFalse( breakText( text, lineLength )
            .filter( l -> l.length() > lineLength )
            .anyMatch( l -> l.contains( " " ) ) );
    }   //  testBreakText()

    /**
     *  Tests for
     *  {@link StringUtils#breakText(CharSequence,int)}.
     */
    @Test
    final void testBreakTextWithInvalidLineLengthArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = ValidationException.class;
        try
        {
            breakText( "String", -1 );
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
            breakText( "String", 0 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testBreakTextWithInvalidLineLengthArgument()

    /**
     *  Tests for
     *  {@link StringUtils#breakText(CharSequence,int)}.
     */
    @Test
    final void testBreakTextWithNullTextArgument()
    {
        skipThreadTest();

        final Class<? extends Throwable> expectedException = NullArgumentException.class;
        try
        {
            breakText( null, 10 );
            fail( () -> format( MSG_ExceptionNotThrown, expectedException.getName() ) );
        }
        catch( final AssertionError e ) { throw e; }
        catch( final Throwable t )
        {
            final var isExpectedException = expectedException.isInstance( t );
            if( !isExpectedException ) t.printStackTrace( out );
            assertTrue( isExpectedException, () -> format( MSG_WrongExceptionThrown, expectedException.getName(), t.getClass().getName() ) );
        }
    }   //  testBreakTextWithNullTextArgument()
}
//  class TestBreakText

/*
 *  End of File
 */